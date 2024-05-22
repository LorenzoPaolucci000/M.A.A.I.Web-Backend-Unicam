package com.example.PiattaformaPCTO_v2.controller;

import com.example.PiattaformaPCTO_v2.Request.ActivityRequest;
import com.example.PiattaformaPCTO_v2.Request.IscrizioneRequest;
import com.example.PiattaformaPCTO_v2.collection.Studente;
import com.example.PiattaformaPCTO_v2.service.StudenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/studente")
public class StudenteController {

    @Autowired
    private StudenteService studenteService;

    /**
     * studente che si iscrive ad
     * @param create
     */


    @PostMapping("/addIscrizione1")
    public void addIscrizione1(@RequestBody IscrizioneRequest create)
    {

        studenteService.addIscrizione(create.getNome(),create.getCognome(),create.getEmail(),create.getScuola(),create.getNomeAttivita(),create.getAnno());

    }




    @PostMapping
    public String save(@RequestBody Studente studente){
        return studenteService.save(studente);
    }
}
