package com.example.PiattaformaPCTO_v2.controller;


import com.example.PiattaformaPCTO_v2.collection.Professore;
import com.example.PiattaformaPCTO_v2.service.ProfessoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/professori")
@CrossOrigin(origins = "http://localhost:4200",allowedHeaders = "*")
public class ProfessoreController {


    @Autowired
    private ProfessoreService professoreService;

    @PostMapping
    public String save(@RequestBody Professore professore){
        return professoreService.save(professore);
    }
    @GetMapping("/upload")
    public String save(){
        return professoreService.upload();
    }
/*
    @RequestMapping(value = "/uploadConFile", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadConAnno(@RequestParam("file") MultipartFile file){
        professoreService.uploadConFile(file);
    }*/
    @RequestMapping(value="/createEmptyActivity")
public void createEmptyActivity(@RequestParam("nome") String nome ,@RequestParam("anno") int anno,
                          @RequestParam("scuola")String scuola,@RequestParam("cittaScuola")String cittaScuola)
    {professoreService.createEmptyActivity(nome, anno, scuola, cittaScuola);}
    @PostMapping("/uploadConFile1")
    public void uploadConAnno1(@RequestParam("file") MultipartFile file){
        professoreService.uploadConFile(file);
    }
    @GetMapping("/stampa")
    public String stampa(){
        return professoreService.stampa();
    }

    @GetMapping("/get")
    public ResponseEntity<List<Professore>> getProf(){
        List<Professore> p = this.professoreService.getAllProf();
        return new ResponseEntity<>(p, HttpStatus.OK);
    }
}
