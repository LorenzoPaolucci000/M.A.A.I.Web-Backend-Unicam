package com.example.PiattaformaPCTO_v2.repository;

import com.example.PiattaformaPCTO_v2.collection.Risultati;
import com.example.PiattaformaPCTO_v2.collection.RisultatiAtt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RisultatiRepository extends MongoRepository<Risultati,String> {
    @Query("{'annoAcc': ?0}")
    List<Risultati> findbyAnno(int anno);
    @Query("{'scuola._id': ?0}")
    List<Risultati> findByScuolaId(String scuolaId);
}
