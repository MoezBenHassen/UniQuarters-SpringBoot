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
public class TravauxController {

    @Autowired
    private final ITravauxService iTravauxService;

    @Autowired
    private TravauxRepo travauxRepo;

    @RequestMapping(value="/addAudience", method= RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Travaux> addAudience(@RequestBody Travaux travaux) {
        Travaux createdAudience = iTravauxService.addTravaux(travaux);
        // System.out.println("aaaaaaaaaaaa"+createdAudience.getEndDate());
        return new ResponseEntity<>(createdAudience, HttpStatus.CREATED);
    }
    @GetMapping("/loadData")
    public ResponseEntity<List<Travaux>> loadData() {
        List<Travaux> data = iTravauxService.listTravaux();
        return ResponseEntity.ok(data);
    }

    @DeleteMapping("/deleteAudience/{id}")
    public ResponseEntity<String> deleteAudience(@PathVariable int id) {
        try {
            travauxRepo.deleteById(id);
            return ResponseEntity.ok("Audience deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PutMapping("/updateAudience/{id}")
    public ResponseEntity<Travaux> updateAudience(@PathVariable int id, @RequestBody Travaux updatedAudience) {
        Travaux existingAudience = travauxRepo.findById(id).get();

        if (existingAudience == null) {
            return ResponseEntity.notFound().build();
        }
        existingAudience.setText(updatedAudience.getText());
        existingAudience.setStartDate(updatedAudience.getStartDate());
        existingAudience.setEndDate(updatedAudience.getEndDate());
        existingAudience.setDescription(updatedAudience.getDescription());
        existingAudience.setBloc(updatedAudience.getBloc());
        existingAudience.setRooms(updatedAudience.getRooms());
        Travaux updated = iTravauxService.addTravaux(existingAudience);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/AudienceByLocation/{location}")
    public List<Travaux> getFilteredAppointmentsByLocation(@PathVariable int location) {
        return travauxRepo.findAllByBloc(location);
    }
}
