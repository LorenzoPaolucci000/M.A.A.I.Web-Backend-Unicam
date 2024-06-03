package com.example.PiattaformaPCTO_v2.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "Risultati")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Risultati {

    private int annoAcc;

    private Scuola  scuola;

    private List<Presenza> attivita;

    private List<Universitario> iscritti;

    public Risultati(int annoAcc, Scuola scuola) {
        this.annoAcc = annoAcc;
        this.scuola = scuola;
        this.attivita=new ArrayList<>(10000);
  this.iscritti=new ArrayList<>();
    }


    public List<Universitario> getIscritti() {
        return iscritti;
    }

    public void addIscritti(List<Universitario> iscritti) {
        this.iscritti.addAll(iscritti);
    }
public void addIscritto(Universitario u){
        this.iscritti.add(u);
}
    public int getAnnoAcc() {
        return annoAcc;
    }

    public Scuola getScuola() {
        return scuola;
    }

    public List<Presenza> getAttivita() {
        return attivita;
    }

       public void addAttivita(Presenza presenza){
        this.attivita.add(presenza);
}
}
