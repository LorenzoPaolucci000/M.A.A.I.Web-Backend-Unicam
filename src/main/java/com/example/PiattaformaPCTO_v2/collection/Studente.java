package com.example.PiattaformaPCTO_v2.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "Studenti")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Studente {


    private String nome;
    private String cognome;
    private String email;
    private Scuola scuola;

    public Studente( String nome, String cognome,String email, Scuola scuola) {

        this.nome = nome;
        this.cognome = cognome;
        this.scuola = scuola;
        this.email=email;
    }



    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public Scuola getScuola() {
        return scuola;
    }

    public void setScuola(Scuola scuola) {
        this.scuola = scuola;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
