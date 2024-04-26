package com.example.PiattaformaPCTO_v2.controller;

import com.example.PiattaformaPCTO_v2.collection.Studente;
import com.example.PiattaformaPCTO_v2.service.StudenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/studente")
public class StudenteController {

    @Autowired
    private StudenteService studenteService;

    @RequestMapping(value="/addIscrizione")
    public void addIscrizione(@RequestParam("nome") String nome ,@RequestParam("cognome") String cognome,
                                    @RequestParam("filepath")String filepath)
    {studenteService.addIscrizione(nome,cognome,filepath);}
    @PostMapping
    public String save(@RequestBody Studente studente){
        return studenteService.save(studente);
    }
}
