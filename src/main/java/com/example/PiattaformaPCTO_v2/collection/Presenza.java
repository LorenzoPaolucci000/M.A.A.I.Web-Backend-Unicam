package com.example.PiattaformaPCTO_v2.collection;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data

public class Presenza {

    private String nomeAttivita;

    private String tipo;

    private List<Studente> partecipanti;

    private List<Universitario> iscritti;


    public Presenza(String nomeAttivita) {
        this.nomeAttivita = nomeAttivita;
        this.partecipanti=new ArrayList<>();
        this.iscritti=new ArrayList<>();
    }


public void addPartecipanti(List<Studente> studenti){
        this.partecipanti.addAll(studenti);
}

    public void addIscritti(List<Universitario> iscritti){
        this.iscritti.addAll(iscritti);
    }
    public String getNomeAttivita() {
        return nomeAttivita;
    }

    public List<Studente> getPartecipanti() {
        return partecipanti;
    }

    public List<Universitario> getIscritti() {
        return iscritti;
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
