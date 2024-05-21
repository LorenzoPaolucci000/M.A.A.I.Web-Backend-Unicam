package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.Immatricolazioni;
import com.example.PiattaformaPCTO_v2.collection.Universitario;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UniversitarioService {
    String save(Universitario universitario);

    String upload(MultipartFile file);
    /**
     metodo che inserisce tutti quei studenti inseriti nell'exel in quell'anno
     */
    String uploadConAnno(MultipartFile file,int anno);


    List<Universitario> getUniversitari();

    void salva();

    List<Immatricolazioni> getIscrizioni();

    List<Immatricolazioni> getIscrizioniAnno(int anno);


    Sheet fileOpenerHelper(MultipartFile file);
}
