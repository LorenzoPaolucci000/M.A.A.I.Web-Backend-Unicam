package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.Attivita;
import com.example.PiattaformaPCTO_v2.collection.Scuola;
import com.example.PiattaformaPCTO_v2.collection.Studente;
import com.example.PiattaformaPCTO_v2.repository.AttivitaRepository;
import com.example.PiattaformaPCTO_v2.repository.ScuolaRepository;
import com.example.PiattaformaPCTO_v2.repository.StudenteRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
    @Autowired
    private ScuolaRepository scuolaRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public void addIscrizione(String nome, String cognome, String email, String scuola, String nomeAttivita, int anno) {


        Scuola scuolaP=scuolaRepository.getScuolaByNome(scuola);
nomeAttivita=nomeAttivita.trim();

        if(attivitaRepository.findByNomeAndAnnoAndIscrizione(nomeAttivita,anno,true)!=null){
            Attivita attivita=attivitaRepository.findByNomeAnno(nomeAttivita,anno).get(0);
            List<Studente> part=attivita.getStudPartecipanti();
            Query query = new Query();
            query.addCriteria(Criteria.where("nome").is(nomeAttivita).and("annoAcc").is(anno));
            if(!attivita.getStudPartecipanti().contains(new Studente(nome, cognome, email, scuolaP))) {
                part.add(new Studente(nome, cognome, email, scuolaP));
            }
            if(studenteRepository.findByEmail(email)==null) {
                studenteRepository.save(new Studente( nome,cognome,email, scuolaP));
            }
            Update update = new Update();
            update.set("studPartecipanti", part);
            mongoTemplate.updateFirst(query, update, Attivita.class);
        }

    }

    @Override
    public String save(Studente studente) {
        return studenteRepository.save(studente).getScuola().getNome();
    }
}
