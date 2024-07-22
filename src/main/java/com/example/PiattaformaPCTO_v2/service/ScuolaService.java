package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.Scuola;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.function.EntityResponse;

import javax.swing.text.html.parser.Entity;
import java.io.FileNotFoundException;
import java.util.List;

public interface ScuolaService {
    String save(Scuola scuola);

    String upload();


    List<Scuola> getScuole();


    List<String> getCitta();

    List<String> getNomi(String c);
/**
 * metodo che carica in un file tutte le scuole in cui ci sono state delle attività
 */
void downloadAllSchhool(String nome,int anno);
    /**
     * metodo che si occupa di  scaricare  il file precedentemente creato
     */
    ResponseEntity<Object> downloadFile(String name)throws FileNotFoundException;

    List<String>getNomeAndCittaAnnoByScuola(String citta);
}
