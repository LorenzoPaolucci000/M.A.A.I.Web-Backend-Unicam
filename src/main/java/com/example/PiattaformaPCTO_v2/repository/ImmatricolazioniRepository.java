package com.example.PiattaformaPCTO_v2.repository;

import com.example.PiattaformaPCTO_v2.collection.Immatricolazioni;
import com.example.PiattaformaPCTO_v2.collection.Universitario;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImmatricolazioniRepository extends MongoRepository<Immatricolazioni,String> {

    @Query("{'annoAc': ?0 }")
    List<Immatricolazioni> findByAnno(int anno);

    @Query(value = "{'universitari':  { 'nome': ?0, 'cognome': ?1 } }")
    List<Universitario> findByNomeAndCognome(String nome, String cognome);
}
