package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.Professore;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
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

    /**
     * il docente carica definitivamente l'attività con i suoi iscritti nel database
     * @param nome dell'attività
     */
    void uploadActivityDefinitively(String nome) throws IOException;

    /**
     * metodo che carica i professori nel database tramite un file passato
     * @param file
     */
    void uploadConFile(MultipartFile file);

    /**
     * metodo che ritorna tutti i nomi dei file nel package activity
     * @return
     */
    List<String> getAllPendingActivities();

    /**
     * metodo che scarica un file excel in cui mette tutti i prof presenti
     */
    void downloadAllProfOnFile(String filename);


    String stampa();

    List<Professore> getAllProf();
    Sheet fileOpenerHelper(MultipartFile file);

    /**
     * metodo che si occupa di  scaricare  il file precedentemente creato
     */
    ResponseEntity<Object> downloadFile(String name)throws FileNotFoundException;
}
