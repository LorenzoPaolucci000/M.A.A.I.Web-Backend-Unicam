package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.Scuola;
import com.example.PiattaformaPCTO_v2.collection.Studente;

public interface StudenteService {

    /**
     *
     * @param nome
     * @param cognome
     * @param email
     * @param nomeAttivita
     * @param anno
     */
    void addIscrizione(String nome, String cognome, String email, String scuola,String nomeAttivita, int anno);
    String save(Studente studente);
}
