package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.Attivita;
import com.example.PiattaformaPCTO_v2.collection.Risultati;
import com.example.PiattaformaPCTO_v2.collection.RisultatiAtt;
import org.springframework.http.ResponseEntity;

import java.io.FileNotFoundException;
import java.util.List;

public interface RisultatiService {






    void createStudentsFromActivities();

    List<Risultati> getRisultati();

    List<RisultatiAtt> getRisultatiAtt();
    Risultati stampa();

    List<Risultati> getRisultatiAnno(int anno);

    /**
     * metodo che mette tutti i risultati delle attività sul file
     */
    void donloadResOnFile(String name,int anno);

    /**
     * metodo che si occupa di  scaricare  il file precedentemente creato
     */
    ResponseEntity<Object> downloadFile(String name)throws FileNotFoundException;
}
