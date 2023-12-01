package esprit.tn.springdemo.repositories;

import esprit.tn.springdemo.entities.Universite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniversiteRepo extends JpaRepository<Universite, Long> {
    @Query("select u from Universite u where u.adresse like %:adresse% ")
List<Universite> findUniversitesByAdresseIsLike(@Param("adresse") String adresse );
    @Query("select u from Universite u where u.nom like %:nom% ")
List<Universite> findUniversitesByNomIsLike(@Param("nom")String nom);
    @Query("select u from Universite u where u.foyer.nom like %:foyer% ")
    List<Universite> findUniversitesByFoyer(@Param("foyer")String nom);
    @Query("select u from Universite u where u.adresse= :add and (u.foyer.nom like %:query% or u.nom like %:query% ) ")
    List<Universite> findUniversitesSearch(@Param("query")String nom,@Param("add")String add);



}
