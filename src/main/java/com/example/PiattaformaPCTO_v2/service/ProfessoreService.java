package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.Professore;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProfessoreService {
    String save(Professore professore);
    String upload();

    /**
     * metodo che permette allo studente di inserire un'attività vuota
     * @param nome dell'attività
     * @param anno in cui si svolge l'attività
     * @param nomeScuola scuola coinvolta
     * @param cittaScuola città della scuola coinvolta
     */
    void createEmptyActivity(String nome,int anno,String nomeScuola,String cittaScuola);


    void uploadConFile(MultipartFile file);
    String stampa();

    List<Professore> getAllProf();
    Sheet fileOpenerHelper(MultipartFile file);
}
