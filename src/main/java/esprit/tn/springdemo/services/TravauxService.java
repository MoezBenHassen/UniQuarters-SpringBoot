package esprit.tn.springdemo.services;

import esprit.tn.springdemo.entities.Travaux;
import esprit.tn.springdemo.repositories.TravauxRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TravauxService implements ITravauxService {
    @Autowired
    private TravauxRepo travauxRepo;

    @Override
    public Travaux addTravaux(Travaux travaux) {
        return travauxRepo.save(travaux);
    }

    @Override
    public List<Travaux> listTravaux() {
        return (List<Travaux>) travauxRepo.findAll();
    }

    @Override
    public void deleteTravaux(int id) {
        travauxRepo.deleteById(id);
    }

    @Override
    public List<Travaux> getFilteredAppointmentsByLocation(int location) {
        return null;
    }

    @Override
    public List<Travaux> getFilteredAppointmentByRoom(String room) {
        return null;
    }
}