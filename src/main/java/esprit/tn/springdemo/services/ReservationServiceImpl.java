package esprit.tn.springdemo.services;

import esprit.tn.springdemo.entities.Chambre;
import esprit.tn.springdemo.entities.Etudiant;
import esprit.tn.springdemo.entities.Reservation;
import esprit.tn.springdemo.entities.TypeChambre;
import esprit.tn.springdemo.repositories.ChambreRepo;
import esprit.tn.springdemo.repositories.EtudiantRepo;
import esprit.tn.springdemo.repositories.ReservationRepo;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReservationServiceImpl implements IReservationService {
    private final ReservationRepo reservationRepo;
    private final ChambreRepo chambreRepo;
    private final EtudiantRepo etudiantRepo;
    private final EmailService emailService;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Reservation> retrieveAllReservation() {
        return reservationRepo.findAll();
    }

    @Override
    public Reservation updateReservation(Reservation updatedReservation, long idChambre, long cinEtudiant) {
        throw new RuntimeException("Not implemented yet");
    }


    @Override
    public Reservation retrieveReservation(String idReservation) {
        return reservationRepo.findById(idReservation);
    }

    @Override
    public Reservation ajouterReservation(Reservation reservation, long idChambre, long cinEtudiant) {
        System.out.println("Begin add reservation");
        Chambre chambre = chambreRepo.getById(idChambre);
        System.out.println("founded chambre: " + chambre);
        if (chambre == null) {
            throw new RuntimeException("Chambre not found");
        }

        Etudiant etudiant = etudiantRepo.getByCin(cinEtudiant);
        System.out.println("founded etudiant: " + etudiant);
        if (etudiant == null) {
            throw new RuntimeException("Etudiant not found");
        }

        System.out.println("Bloc: " + chambre.getBloc());
        if (chambre.getBloc() == null) {
            throw new RuntimeException("Bloc not found");
        }

        String id = this.generateId(idChambre, chambre.getBloc().getNom(), cinEtudiant);
        System.out.println("generated id: " + id);

        // check if reservation already exists
        Reservation existingReservation = reservationRepo.findById(id);

        if (existingReservation != null) {
            throw new RuntimeException("There is already a reservation with this chambre: " + chambre.getNumero() + " and this etudiant " + etudiant.getCin());
        }

        reservation.setId(id);

        // checking chambre free places
        /*int chambreFreePlaces = this.getChambreFreePlaces(chambre);
        System.out.println("chambre free places: " + chambreFreePlaces);
        if (chambreFreePlaces == 0) {
            throw new RuntimeException("Chambre is full");
        }*/

        //System.out.println("les etudiants de entite resa: " + reservation.getEtudiants());
        System.out.println("les reservations de entite etudiant: " + etudiant.getReservations());
        System.out.println("saving reservation: " + reservation);
        reservationRepo.save(reservation);

        System.out.println("adding reservation to chambre");

        chambre.getReservations().add(reservation);
        Chambre savedChambre = chambreRepo.save(chambre);
        System.out.println("saved chambre: " + savedChambre);

        System.out.println("adding reservation to etudiant");
        etudiant.getReservations().add(reservation);
        Etudiant savedEtudiant = etudiantRepo.save(etudiant);
        System.out.println("saved etudiant: " + savedEtudiant);

        entityManager.clear();
        Reservation savedReservation = reservationRepo.findById(id);
        System.out.println("saved reservation: " + savedReservation);
        return savedReservation;
    }

    @Override
    public Map<String, Object> getReservationDetails(Reservation reservation) {
        System.out.println("Reservation details:");

        Chambre chambre = chambreRepo.findChambreByReservations(reservation);
        System.out.println("Reservation chambre: " + chambre);

        Collection<Etudiant> etudiants = reservation.getEtudiants();
        System.out.println("Reservation etudiants: " + etudiants);

        // Convert the Collection<Etudiant> to a Set<Etudiant>
        Set<Etudiant> etudiantsSet = new HashSet<>(etudiants);

        Map<String, Object> details = new HashMap<>();
        details.put("chambre", chambre);
        details.put("etudiants", etudiantsSet); // Put the Set<Etudiant> into the map

        return details;
    }

    @Override
    public List<Map<String, Object>> countChambresReservations() {
        List<Chambre> chambres = chambreRepo.findAll();
        List<Map<String, Object>> chambresReservations = new ArrayList<>();
        chambres.forEach(chambre -> {
            Map<String, Object> chambreReservations = new HashMap<>();
            chambreReservations.put("idChambre", chambre.getId());
            chambreReservations.put("numeroChambre", chambre.getNumero());
            chambreReservations.put("type", chambre.getType());
            chambreReservations.put("maxPlaces", this.getChambreMaxPlaces(chambre.getType()));
            chambreReservations.put("reservationsCount", chambre.getReservations().size());
            chambreReservations.put("freePlaces", this.getChambreFreePlaces(chambre));
            //chambreReservations.put("reservations", chambre.getReservations());
            chambreReservations.put("reservationsIds", chambre.getReservations().stream().map(Reservation::getId).collect(Collectors.toList()));
            chambresReservations.add(chambreReservations);
        });
        return chambresReservations;
    }

    @Override
    public Set<Reservation> getReservationsByEtudiant(long idEtudiant) {
        Etudiant etudiant = etudiantRepo.getById(idEtudiant);
        System.out.println("founded etudiant: " + etudiant);
        if (etudiant == null) {
            throw new RuntimeException("Etudiant not found");
        }
        Set<Reservation> reservations = reservationRepo.findReservationsByEtudiants(etudiant.getId());
        //Set<Reservation> reservations = etudiant.getReservations();
        System.out.println("founded reservations: " + reservations);
        return reservations;
    }


    @Override
    public Reservation validerReservation(String idReservation) {
        Reservation reservation = reservationRepo.findById(idReservation);

        System.out.println("founded reservation: " + reservation);

        if (reservation == null) {
            throw new RuntimeException("Reservation not found");
        }

        if (reservation.getEstValide() == true) {
            throw new RuntimeException("Reservation is already valid");
        }


        System.out.println("Reservation details: " + this.getReservationDetails(reservation));
        Map<String, Object> reservationDetails = getReservationDetails(reservation);
        Set<Etudiant> etudiants = (Set<Etudiant>) reservationDetails.get("etudiants");
        System.out.println("etudiants x1: " + etudiants);
        Etudiant etudiant = etudiants.stream().collect(Collectors.toList()).get(0);
        System.out.println("etudiant x2: " + etudiant);
        // check if edudiant has already a valid reservation
        Set<Reservation> etudiantReservations = etudiant.getReservations();
        System.out.println("etudiant reservations x3: " + etudiantReservations);
        // check if edudiant has already a valid reservation using .some like js
        boolean hasValidReservation = etudiantReservations.stream().anyMatch(resa -> resa.getEstValide() == true);
        System.out.println("has valid reservation: " + hasValidReservation);
        if (hasValidReservation) {
            throw new RuntimeException("Etudiant has already a valid reservation");
        }
        Chambre chambre = (Chambre) reservationDetails.get("chambre");


        // checking chambre free places
        int chambreFreePlaces = this.getChambreFreePlaces(chambre);
        System.out.println("chambre free places: " + chambreFreePlaces);
        if (chambreFreePlaces == 0) {
            throw new RuntimeException("Chambre is full");
        }


        Boolean resaValidation = this.checkReservationValidation(reservation, chambre, etudiants);
        System.out.println("reservation validation: " + resaValidation);
        reservation.setEstValide(true);
        System.out.println("saving reservation: " + reservation);
        Reservation savedReservation = reservationRepo.save(reservation);

        try {
            String mailBody = this.generateMailBody(reservation, etudiant, chambre);
            System.out.println("mail body: " + mailBody);
            emailService.sendEmail("medyacine.khouini@esprit.tn", "Reservation validee", mailBody);
        } catch (MessagingException e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
        entityManager.clear();
        return reservationRepo.findById(savedReservation.getId());
    }

    private String generateMailBody(Reservation reservation, Etudiant etudiant, Chambre chambre) {
        String mailBody = "<html><head><style>";
        mailBody += "body { font-family: Arial, sans-serif; }";
        mailBody += "table { width: 100%; border-collapse: collapse; margin-top: 10px; }";
        mailBody += "th, td { border: 1px solid #dddddd; text-align: left; padding: 8px; }";
        mailBody += "th { background-color: #f2f2f2; }";
        mailBody += "</style></head><body>";

        mailBody += "<p>Bonjour " + etudiant.getPrenom() + " " + etudiant.getNom() + ",</p>";
        mailBody += "<p>Votre réservation a été validée.</p>";
        mailBody += "<p>Détails de la réservation:</p>";

        // Creating a table for reservation details
        mailBody += "<table>";
        mailBody += "<tr><th>Année universitaire</th><td>" + reservation.getAnneeUniversitaire() + "</td></tr>";
        mailBody += "<tr><th>État de la réservation</th><td>" + reservation.getEstValide() + "</td></tr>";
        mailBody += "</table>";

        mailBody += "<p>Détails de la chambre:</p>";

        // Creating a table for chambre details
        mailBody += "<table>";
        mailBody += "<tr><th>Chambre</th><td>" + chambre.getNumero() + "</td></tr>";
        mailBody += "<tr><th>Type</th><td>" + chambre.getType() + "</td></tr>";
        mailBody += "<tr><th>Bloc</th><td>" + chambre.getBloc().getNom() + "</td></tr>";
        mailBody += "</table>";

        mailBody += "<p>Détails de l'étudiant:</p>";

        // Creating a table for etudiant details
        mailBody += "<table>";
        mailBody += "<tr><th>CIN</th><td>" + etudiant.getCin() + "</td></tr>";
        mailBody += "<tr><th>Nom</th><td>" + etudiant.getNom() + "</td></tr>";
        mailBody += "<tr><th>Prénom</th><td>" + etudiant.getPrenom() + "</td></tr>";
        mailBody += "<tr><th>Date de naissance</th><td>" + etudiant.getDateNaissance() + "</td></tr>";
        mailBody += "<tr><th>École</th><td>" + etudiant.getEcole() + "</td></tr>";
        mailBody += "</table>";

        mailBody += "<p>Cordialement,<br/>Service des réservations</p>";
        mailBody += "</body></html>";

        return mailBody;
    }


    @Override
    public Reservation annulerReservation(long cinEtudiant) {
        Etudiant etudiant = etudiantRepo.getByCin(cinEtudiant);
        System.out.println("founded etudiant: " + etudiant);
        if (etudiant == null) {
            throw new RuntimeException("Etudiant not found");
        }
        System.out.println("les reservations de etudiant: " + etudiant.getReservations());
        Set<Reservation> reservations = etudiant.getReservations();
        if (reservations == null || reservations.isEmpty()) {
            throw new RuntimeException("Etudiant does not have reservations");
        }
        Reservation reservation = reservations.stream().collect(Collectors.toList()).stream().filter(resa -> resa.getEstValide() == true).collect(Collectors.toList()).get(0);
        System.out.println("reservation to cancel: " + reservation);
        if (reservation.getEstValide() == false) {
            System.out.println("Reservation is already invalid");
            throw new RuntimeException("Reservation is already invalid");
        }
        System.out.println("founded reservation: " + reservation);
        if (reservation == null) {
            throw new RuntimeException("Reservation not found");
        }

        // check if reservation is valid

        Chambre chambre = chambreRepo.findChambreByReservations(reservation);
        System.out.println("founded chambre: " + chambre);
        this.checkReservationValidation(reservation, chambre, new HashSet<>(Arrays.asList(etudiant)));

        etudiant.getReservations().remove(reservation);
        Etudiant savedEtudiant = etudiantRepo.save(etudiant);
        System.out.println("saved etudiant: " + savedEtudiant);

        // desaffecter reservation de chambre

        System.out.println("founded chambre: " + chambre);
        System.out.println("les reservations de chambre: " + chambre.getReservations());
        if (chambre == null) {
            throw new RuntimeException("Chambre not found");
        }
        chambre.getReservations().remove(reservation);
        Chambre savedChambre = chambreRepo.save(chambre);
        System.out.println("saved chambre: " + savedChambre);

        reservation.setEstValide(false);
        Reservation savedReservation = reservationRepo.save(reservation);
        entityManager.clear();

        return reservationRepo.findById(savedReservation.getId());
    }

    private String generateId(long idChambre, String nomBloc, long cinEtudiant) {
        return idChambre + "-" + nomBloc + "-" + cinEtudiant;
    }

    private Boolean checkReservationValidation(Reservation reservation, Chambre chambre, Set<Etudiant> etudiants) {
        System.out.println("checking reservation validation");
        if (reservation == null) {
            System.out.println("reservation is null");
            throw new RuntimeException("Reservation is null");
        }

        if (chambre == null) {
            System.out.println("reservation does not have a chambre");
            throw new RuntimeException("Reservation does not have a chambre");
        }
        if (etudiants == null || etudiants.isEmpty()) {
            System.out.println("reservation does not have etudiants");
            throw new RuntimeException("Reservation does not have etudiants");
        }
        System.out.println("valid reservation");
        return true;
    }

    private int getChambreMaxPlaces(TypeChambre typeChambre) {
        int maxPlaces;
        switch (typeChambre) {
            case SIMPLE:
                maxPlaces = 1;
                break;
            case DOUBLE:
                maxPlaces = 2;
                break;
            case TRIPLE:
                maxPlaces = 3;
                break;
            default:
                throw new RuntimeException("Invalid chambre type");
        }
        return maxPlaces;
    }

    private int getChambreFreePlaces(Chambre chambre) {
        int maxPlaces = this.getChambreMaxPlaces(chambre.getType());
        System.out.println("max places: " + maxPlaces);
        int reservedPlaces = chambre.getReservations().stream() // only valid reservation
                .filter(reservation -> reservation.getEstValide() == true)
                .mapToInt(reservation -> reservation.getEtudiants().size())
                .sum();
        System.out.println("reserved places: " + reservedPlaces);
        return maxPlaces - reservedPlaces;
    }
}
