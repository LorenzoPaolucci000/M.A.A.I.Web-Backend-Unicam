package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.Professore;
import com.example.PiattaformaPCTO_v2.collection.ProfessoreUnicam;
import com.example.PiattaformaPCTO_v2.collection.Scuola;
import com.example.PiattaformaPCTO_v2.enumeration.Sede;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public interface ProfessoreService {
    String save(Professore professore);
    /*
    String upload();
*/

    /**
     * metodo che crea un attività vuota
     * @param nome
     * @param tipo
     * @param scuola
     * @param anno
     * @param sede
     * @param dataInizio
     * @param dataFine
     * @param descrizione
     * @param profUnicam
     * @param profReferente
     */

    void createEmptyActivity(String nome, String tipo, String scuola, int anno,Sede sede, LocalDateTime dataInizio, LocalDateTime dataFine
            , String descrizione, List<ProfessoreUnicam> profUnicam, Professore profReferente);

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
     * metodo che ritorna tutti i nomi delle attività a cui è ancora possibile iscriversi
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

    /**
     * metodo che permette di fare upload di un singolo professore
     */
    void uploadSingleProf(String email, String nome, String cognome, String scuola,String citta,String attività);
    /**
     * metodo che data una string con 3 parametri nome,cognome,email, restituisce il professore associato
     * @param prof
     * @return
     */
    Professore getProfByString(String prof);
}
