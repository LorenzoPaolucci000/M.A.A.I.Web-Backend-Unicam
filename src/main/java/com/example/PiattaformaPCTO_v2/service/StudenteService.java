package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.Studente;

public interface StudenteService {

    /**
     * metodo che permette allo studente di inscriversi ad una attività
     * @param nome dello studente
     * @param cognome dello studente
     * @param filePath sarebbe il nome dell'attività con anno
     */
    void addIscrizione(String nome,String cognome,String filePath);
    String save(Studente studente);
}
