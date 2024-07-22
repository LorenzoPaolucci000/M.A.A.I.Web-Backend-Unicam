package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.Attivita;
import com.example.PiattaformaPCTO_v2.collection.Professore;
import com.example.PiattaformaPCTO_v2.collection.ProfessoreUnicam;
import com.example.PiattaformaPCTO_v2.dto.ActivityViewDTOPair;
import com.example.PiattaformaPCTO_v2.enumeration.Sede;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

public interface AttivitaService {

    String save(Attivita attivita);



Sheet fileOpenerHelper(MultipartFile file);

    /**
     * Find information about students that chose UNICAM and their high school, given an activity.
     *
     * @return list of activity view pairs
     */
    List<ActivityViewDTOPair> findStudentsFromActivity(String activityName);


    List<Attivita> getAttivita(int anno);



    /**
     * metodo che crea l'attività singola

     */
    void uploadSingleActivity(String nome, String tipo, String scuola, int anno, Sede sede,
                              LocalDateTime dataInizio, LocalDateTime dataFine, String descrizione,
                              List<ProfessoreUnicam> prof, Professore profReferente, MultipartFile file);

}
