package com.example.PiattaformaPCTO_v2.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "Universitari")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Universitario {

    @Id
    public String matricola;
    public String nome;
    public String cognome;
    public int annoImm;
    public String corso;
    public String comuneScuola;
    public String scuolaProv;

    public Universitario(String matricola, String nome, String cognome, int annoImm,String corso, String comuneScuola, String scuolaProv) {
        this.matricola = matricola;
        this.nome = nome;
        this.cognome = cognome;
        this.annoImm = annoImm;
        this.corso=corso;
        this.comuneScuola = comuneScuola;
        this.scuolaProv = scuolaProv;
    }

    public String getMatricola() {
        return matricola;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public int getAnnoImm() {
        return annoImm;
    }

    public String getCorso() {
        return corso;
    }

    public String getComuneScuola() {
        return comuneScuola;
    }

    public String getScuolaProv() {
        return scuolaProv;
    }
}
