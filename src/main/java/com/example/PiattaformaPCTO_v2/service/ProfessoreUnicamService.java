package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.ProfessoreUnicam;
import com.example.PiattaformaPCTO_v2.collection.Scuola;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProfessoreUnicamService {


    /**
     * metodo che carica i professori nel database tramite un file passato
     * @param file
     */
    void uploadConFile(MultipartFile file);
    List<ProfessoreUnicam> getAllProf();


    /**
     * metodo che permette di fare upload di un singolo professore
     */
    void uploadSingleProf( String nome, String cognome,String email);


    Sheet fileOpenerHelper(MultipartFile file);

}
