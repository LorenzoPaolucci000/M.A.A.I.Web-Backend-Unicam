package com.example.PiattaformaPCTO_v2.Request;

import com.example.PiattaformaPCTO_v2.collection.Professore;
import com.example.PiattaformaPCTO_v2.collection.Scuola;

public class UploadProfRequest {



    private String nome;
    private String cognome;
    private String email;
    private String scuola;
    private String citta;
    private String attivita;

    public UploadProfRequest(String nome, String cognome,String email, String scuola,String citta,String attivita){
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.scuola = scuola;
        this.citta=citta;
        this.attivita = attivita;
    }
public UploadProfRequest(){

}
    public String toString(){
        return "Nome:"+this.nome+"; Email: "+this.email+"; Scuola: "+this.scuola;
    }
    public String toJson(){
        return "{nome:"+this.nome+";email:"+this.email+";scuolaImp:"+this.scuola+"}";
    }
    public boolean equals(Professore prof){
        if(prof.getEmail().equals(this.getEmail()) ){
            return true;
        }
        return false;
    }
    /**
     * Confronta 2 stringhe, se tutte le sottostringhe di questa non sono contenute nell'altra ritorna falso
     * @param s1    Stringa che viene scomposta in sottostringhe
     * @param s2    Stringa che dovrà contenere le sottostringhe
     * @return      True se queste s2 contiene tutte le sottostringhe di s1, False altrimenti
     */
    public boolean compare(String s1,String s2){
        String[] substrings = s1.split(" ");
        for(String substring:substrings){
            if(!s2.contains(substring)){
                return false;
            }
        }
        return true;
    }


    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getScuola() {
        return scuola;
    }

    public String getAttivita() {
        return attivita;
    }

    public String getCitta() {
        return citta;
    }
}
