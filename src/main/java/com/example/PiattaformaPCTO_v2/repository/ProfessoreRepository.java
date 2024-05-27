package com.example.PiattaformaPCTO_v2.repository;

import com.example.PiattaformaPCTO_v2.collection.Professore;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessoreRepository extends MongoRepository<Professore,String> {
    @Query("{'nome': ?0}")
    List<Professore> getProfessoresByNomeContains(String nome);
    @Query("{'nome': ?0,'cognome': ?1,'attivita': ?2}")
    Professore getProfessoreByNomeCognomeAttivita(String nome,String cognome,String attivita);
    @Query("{'id': ?0}")
    Professore getProfByEmail(String id);
    @Query("{'nome': ?0,'cognome': ?1}")
    Professore getNomeCognome(String nome,String cognome);
}
