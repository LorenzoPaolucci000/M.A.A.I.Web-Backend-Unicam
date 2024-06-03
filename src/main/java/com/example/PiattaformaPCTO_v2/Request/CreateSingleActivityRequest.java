package com.example.PiattaformaPCTO_v2.Request;

import com.example.PiattaformaPCTO_v2.enumeration.Sede;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public class CreateSingleActivityRequest {
    private String nome;
    private String tipo;
    private String scuola;

    private int anno;

    private Sede sede;
    //dd-MM-yyyy HH:mm
    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;
    private String descrizione;
    /**
     * professori unicam
     */
    private List<String> profUnicam;
    /**
     * proffessore della scuola
     */
    private String profReferente;
 private MultipartFile file;

    public CreateSingleActivityRequest(String nome, String tipo, String scuola, int anno, Sede sede,
                           LocalDateTime dataInizio, LocalDateTime dataFine, String descrizione,
                           List<String> profUnicam, String profReferente,MultipartFile file) {
        this.nome = nome;
        this.tipo = tipo;
        this.scuola = scuola;
        this.anno = anno;
        this.sede = sede;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.descrizione = descrizione;
        this.profUnicam = profUnicam;
        this.profReferente = profReferente;
        this.file=file;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getScuola() {
        return scuola;
    }

    public void setScuola(String scuola) {
        this.scuola = scuola;
    }

    public Sede getSede() {
        return sede;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }

    public LocalDateTime getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDateTime dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDateTime getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDateTime dataFine) {
        this.dataFine = dataFine;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public List<String> getProfUnicam() {
        return profUnicam;
    }

    public void setProfUnicam(List<String> profUnicam) {
        this.profUnicam = profUnicam;
    }

    public String getProfReferente() {
        return profReferente;
    }

    public void setProfReferente(String profReferente) {
        this.profReferente = profReferente;
    }

    public MultipartFile getFile() {
        return file;
    }
}
