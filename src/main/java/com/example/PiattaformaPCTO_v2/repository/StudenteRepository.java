package com.example.PiattaformaPCTO_v2.repository;

import com.example.PiattaformaPCTO_v2.collection.Studente;
import com.example.PiattaformaPCTO_v2.collection.Universitario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StudenteRepository extends MongoRepository<Studente,String> {
    @Query("{'nome': ?0,'cognome': ?1}")
    Studente findByNomeAndCognome(String nome, String cognome);
    @Query("{'nome': ?0,'cognome': ?1,'email':?2}")
    Studente findByNomeAndCognomeAndEmail(String nome,String cognome,String email);
    @Query("{'email':?0}")
    Studente findByEmail(String email);
}
