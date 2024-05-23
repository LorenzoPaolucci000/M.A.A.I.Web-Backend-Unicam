package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.Attivita;
import com.example.PiattaformaPCTO_v2.collection.Scuola;
import com.example.PiattaformaPCTO_v2.collection.Studente;
import com.example.PiattaformaPCTO_v2.repository.AttivitaRepository;
import com.example.PiattaformaPCTO_v2.repository.StudenteRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SimpleStudenteService implements StudenteService{

    @Autowired
    private StudenteRepository studenteRepository;
    @Autowired
    private AttivitaRepository attivitaRepository;

    @Override
    public void addIscrizione(String nome, String cognome, String email, Scuola scuola, String nomeAttivita, int anno) {

        List<Attivita> list=new ArrayList<>();
        if(attivitaRepository.findByNomeAndAnno(nomeAttivita,anno)!=null){

            Attivita attivita=attivitaRepository.findByNomeAndAnno(nomeAttivita,anno);
            list.addAll(attivitaRepository.findAll());
            attivitaRepository.deleteAll();
            list.remove(attivita);
            if(!attivita.getStudPartecipanti().contains(new Studente(nome, cognome, email, scuola))) {
                attivita.addStudente(new Studente(nome, cognome, email, scuola));
            }
            if(studenteRepository.findByNomeAndCognomeAndEmail(nome,cognome,email)==null) {
                studenteRepository.save(new Studente( nome,cognome,email, scuola));
            }
            list.add(attivita);
            attivitaRepository.saveAll(list);
        }


    }

    @Override
    public String save(Studente studente) {
        return studenteRepository.save(studente).getScuola().getNome();
    }
}
