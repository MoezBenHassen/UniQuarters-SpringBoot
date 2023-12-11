package esprit.tn.springdemo.services;

import esprit.tn.springdemo.entities.Bloc;
import esprit.tn.springdemo.entities.Chambre;

import java.util.List;

public interface IBlocService {
    List<Bloc> retrieveBlocs();

    Bloc updateBloc(Bloc bloc);

    Bloc addBloc(Bloc bloc);

    Bloc retrieveBloc(long idBloc);

    void removeBloc(long idBloc);

    Bloc affectChambres(long blocId, List<Chambre> chambres);

    Bloc addBlocWithFoyer(Bloc bloc, Long foyerId);

}
