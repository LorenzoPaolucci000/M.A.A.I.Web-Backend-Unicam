package com.example.PiattaformaPCTO_v2.Request;

import com.example.PiattaformaPCTO_v2.collection.Scuola;

public class IscrizioneRequest {
    private String nome;
    private String cognome;
   private String email;
   private String nomeAttivita;
   private int anno;
   private String scuola;

    public IscrizioneRequest(String nome, String cognome, String email, String nomeAttivita, int anno,String scuola) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.nomeAttivita = nomeAttivita;
        this.anno = anno;
        this.scuola=scuola;
    }

    public String getNomeAttivita() {
        return nomeAttivita;
    }

    public int getAnno() {
        return anno;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }



    public String getEmail() {
        return  email;
    }

    public String getScuola() {
        return scuola;
    }
}
