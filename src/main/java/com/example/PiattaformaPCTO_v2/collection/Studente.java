package com.example.PiattaformaPCTO_v2.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Studente studente = (Studente) o;
        return Objects.equals(email, studente.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
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
