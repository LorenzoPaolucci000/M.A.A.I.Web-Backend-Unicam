package com.example.PiattaformaPCTO_v2.repository;

import com.example.PiattaformaPCTO_v2.collection.Professore;
import com.example.PiattaformaPCTO_v2.collection.ProfessoreUnicam;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessoreUnicamRepository extends MongoRepository<ProfessoreUnicam,String> {


    @Query("{'id': ?0}")
    ProfessoreUnicam getProfByEmail(String id);
    @Query("{'nome':?0,'cognome':?1}")
    ProfessoreUnicam getByNomeCognome(String nome,String cognome);
}
