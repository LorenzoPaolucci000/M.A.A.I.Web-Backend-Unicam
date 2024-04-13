package com.example.PiattaformaPCTO_v2.collection;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "Iscrizioni")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Iscrizioni {

    private int annoAc;

    private List<Universitario> universitari;


    public Iscrizioni(int annoAc) {
        this.annoAc = annoAc;
        this.universitari= new ArrayList<>();
    }



    public void addUniversitario(Universitario u){
        this.universitari.add(u);
    }

    public int getAnnoAc() {
        return annoAc;
    }

    public List<Universitario> getUniversitari() {
        return universitari;
    }


}
