package esprit.tn.springdemo.services;

import esprit.tn.springdemo.entities.Universite;

import java.util.List;

public interface IUniversiteService {
    List<Universite> retrieveAllUniversities();

    Universite addUniversity(Universite u);

    Universite updateUniversity(Universite u,Long id);

    Universite retrieveUniversity(long idUniversity);

    Universite affectFoyer(long idUniversity, long idFoyer);


    public Universite desaffecterFoyerAUniversite(long idUniversite);
}
