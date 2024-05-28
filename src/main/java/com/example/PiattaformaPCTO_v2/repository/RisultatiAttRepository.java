package com.example.PiattaformaPCTO_v2.repository;


import com.example.PiattaformaPCTO_v2.collection.Professore;
import com.example.PiattaformaPCTO_v2.collection.RisultatiAtt;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RisultatiAttRepository extends MongoRepository <RisultatiAtt,String> {
    @Query("{'attivita': ?0,'annoAcc': ?1}")
    List<RisultatiAtt> findByNomeAndAnno(String nome,int anno);
    @Query("{'annoAcc': ?0}")
    List<RisultatiAtt> findbyAnno(int anno);
    @Query("{'attivita':?0}")
   List<RisultatiAtt>  findbyNomeAttivita(String nomeAttivita);
}
