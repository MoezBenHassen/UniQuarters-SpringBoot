package esprit.tn.springdemo.repositories;

import esprit.tn.springdemo.entities.Travaux;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TravauxRepo extends CrudRepository<Travaux, Integer> {
    List<Travaux> findAllByBloc(int tribunal);
}