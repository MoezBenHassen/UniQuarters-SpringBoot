package esprit.tn.springdemo.controllers;


import esprit.tn.springdemo.entities.Travaux;
import esprit.tn.springdemo.repositories.TravauxRepo;
import esprit.tn.springdemo.services.ITravauxService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/schedule")
@CrossOrigin(origins = "http://localhost:4200/", allowedHeaders = "*")
public class TravauxController {

    @Autowired
    private final ITravauxService iTravauxService;

    @Autowired
    private TravauxRepo travauxRepo;

    @RequestMapping(value="/addTravaux", method= RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Travaux> addTravaux(@RequestBody Travaux travaux) {
        Travaux createdTravaux = iTravauxService.addTravaux(travaux);
        // System.out.println("aaaaaaaaaaaa"+createdTravaux.getEndDate());
        return new ResponseEntity<>(createdTravaux, HttpStatus.CREATED);
    }
    @GetMapping("/loadData")
    public ResponseEntity<List<Travaux>> loadData() {
        List<Travaux> data = iTravauxService.listTravaux();
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/deleteTravaux/{id}")
    public ResponseEntity<String> deleteTravaux(@PathVariable int id) {
        try {
            travauxRepo.deleteById(id);
            return ResponseEntity.ok("Travaux deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PutMapping("/updateTravaux/{id}")
    public ResponseEntity<Travaux> updateTravaux(@PathVariable int id, @RequestBody Travaux updatedTravaux) {
        Travaux existingTravaux = travauxRepo.findById(id).get();

        if (existingTravaux == null) {
            return ResponseEntity.notFound().build();
        }
        existingTravaux.setText(updatedTravaux.getText());
        existingTravaux.setStartDate(updatedTravaux.getStartDate());
        existingTravaux.setEndDate(updatedTravaux.getEndDate());
        existingTravaux.setDescription(updatedTravaux.getDescription());
        existingTravaux.setBloc(updatedTravaux.getBloc());
        existingTravaux.setRooms(updatedTravaux.getRooms());
        Travaux updated = iTravauxService.addTravaux(existingTravaux);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/TravauxByLocation/{location}")
    public List<Travaux> getFilteredAppointmentsByLocation(@PathVariable int location) {
        return travauxRepo.findAllByBloc(location);
    }
}
