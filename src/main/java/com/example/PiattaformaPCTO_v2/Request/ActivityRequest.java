package com.example.PiattaformaPCTO_v2.Request;

public class ActivityRequest {
    private String nome;
    private String nomeScuola;
    private String cittaScuola;
private int anno;

    public ActivityRequest(String nome ,int anno, String nomeScuola, String cittaScuola) {
        this.nome = nome;
        this.nomeScuola = nomeScuola;
        this.cittaScuola = cittaScuola;
        this.anno = anno;

    }



    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }






    public String getNomeScuola() {
        return nomeScuola;
    }

    public void setNomeScuola(String nomeScuola) {
        this.nomeScuola = nomeScuola;
    }

    public String getCittaScuola() {
        return cittaScuola;
    }

    public void setCittaScuola(String cittaScuola) {
        this.cittaScuola = cittaScuola;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
