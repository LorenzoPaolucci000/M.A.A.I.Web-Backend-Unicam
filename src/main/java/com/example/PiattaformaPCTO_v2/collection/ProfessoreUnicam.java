package com.example.PiattaformaPCTO_v2.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "ProfessoriUniversitari")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfessoreUnicam {
    @Id
    private String email;
    private String nome;
    private String cognome;


    public ProfessoreUnicam(String nome, String cognome, String email) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }


    public String toString(){
        return "Nome:"+this.nome+"; Cognome: "+this.cognome+"; Email: "+this.email;
    }
    public String toJson(){
        return "{nome:"+this.nome+";cognome:"+this.cognome+";email:"+this.email+"}";
    }
    public boolean equals(ProfessoreUnicam prof){
        if(prof.getEmail().equals(this.getEmail()) ){
            return true;
        }
        return false;
    }
}
