package com.example.PiattaformaPCTO_v2.Request;

public class IscrizioneRequest {
    private String nome;
    private String cognome;

    private String filepath;

    public IscrizioneRequest(String nome, String cognome, String filepath) {
        this.nome = nome;
        this.cognome = cognome;
        this.filepath = filepath;
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

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
