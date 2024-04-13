package com.example.PiattaformaPCTO_v2.repository;

import com.example.PiattaformaPCTO_v2.collection.Iscrizioni;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IscrizioniRepository extends MongoRepository<Iscrizioni,String> {

    @Query("{'annoAc': ?0 }")
    List<Iscrizioni> findByAnno(int anno);


}
