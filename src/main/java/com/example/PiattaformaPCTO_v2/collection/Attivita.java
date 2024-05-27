package com.example.PiattaformaPCTO_v2.collection;

import com.example.PiattaformaPCTO_v2.enumeration.Sede;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@Document(collection = "Attivita")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Attivita {


    private String nome;

    private String tipo;


    private int annoAcc;



    private List<Studente> studPartecipanti;



    private Sede sede;
//dd-MM-yyyy HH:mm
    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;
    private String descrizione;
    /**
     * professori unicam
     */
    private List<ProfessoreUnicam> profUnicam;
    /**
     * proffessore della scuola
     */
    private Professore profReferente;

  private boolean iscrizionePossibile;
  //la scuola potrebbe non esserci
private String scuola;
    public Attivita(String nome,String tipo, int annoAcc, List<Studente> studPartecipanti) {
        this.nome = nome;
        this.tipo= tipo;
        this.annoAcc = annoAcc;
        this.studPartecipanti = studPartecipanti;
    }

    public Attivita(String nome, String tipo, int annoAcc, List<Studente> studPartecipanti, Sede sede,
                    LocalDateTime dataInizio, LocalDateTime dataFine, String descrizione,
                    List<ProfessoreUnicam> profUnicam, Professore profReferente,boolean iscrizionePossibile) {
        this.nome = nome;
        this.tipo = tipo;
        this.annoAcc = annoAcc;
        this.studPartecipanti = studPartecipanti;
        this.sede = sede;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.descrizione = descrizione;
        this.profUnicam = profUnicam;
        this.profReferente = profReferente;
        this.iscrizionePossibile=iscrizionePossibile;
    }
    public void addStudente(Studente studente){
        this.studPartecipanti.add(studente);
    }
    public void addScuola(String scuola){
        this.scuola=scuola;
    }
    public void addProfUnicam(ProfessoreUnicam prof){
        this.profUnicam.add(prof);
    }
    public String getNome() {
        return nome;
    }

    public String getTipo() {
        return tipo;
    }

    public int getAnnoAcc() {
        return annoAcc;
    }

    public List<Studente> getStudPartecipanti() {
        return studPartecipanti;
    }

    public Sede getSede() {
        return sede;
    }

    public LocalDateTime getDataInizio() {
        return dataInizio;
    }

    public LocalDateTime getDataFine() {
        return dataFine;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public List<ProfessoreUnicam> getProfUnicam() {
        return profUnicam;
    }

    public Professore getProfReferente() {
        return profReferente;
    }

    public String getScuola() {
        return scuola;
    }

    public void setIscrizione(boolean iscrizione) {
        this.iscrizionePossibile = iscrizione;
    }


    @Override
    public boolean equals(Object obj) {
        if(this == obj ) return true;
        if(!(obj instanceof Attivita)) return false;
       Attivita other = (Attivita) obj;
        if(this.nome.equals(other.nome) && this.annoAcc==other.annoAcc) return true;
        else return false;
    }

    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + Objects.hashCode(this.nome);
        hash = 31 * hash + Objects.hashCode(this.annoAcc);
        return hash;
    }

    public boolean iscrizionePossibile() {
        return iscrizionePossibile;
    }

    public void setScuola(String scuola) {
        this.scuola = scuola;
    }
}
