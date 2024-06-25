package com.example.PiattaformaPCTO_v2.controller;

import com.example.PiattaformaPCTO_v2.collection.*;
import com.example.PiattaformaPCTO_v2.repository.*;
import com.example.PiattaformaPCTO_v2.service.UniversitarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/universitari")
@CrossOrigin(origins = "*",allowedHeaders = "*")

public class UniversitariController {

    @Autowired
    private UniversitarioService universitarioService;

    @PostMapping
    public String save(@RequestBody Universitario universitario){
        return universitarioService.save(universitario);
    }



    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file){return universitarioService.upload(file);}
    @RequestMapping(value = "/uploadConAnno", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadConAnno(@RequestParam("file") MultipartFile file,@RequestParam("anno") int anno){return universitarioService.uploadConAnno(file,anno);}

    @PostMapping("/uploadConAnno1/{anno}")
    public void uploadConAnno1(@RequestParam("file") MultipartFile file,@PathVariable("anno")String anno){

        int anno1=Integer.parseInt(anno.substring(0,8));
        universitarioService.uploadConAnno(file,anno1);
    }


    @GetMapping("/geta")
    public ResponseEntity<List<Universitario>> getUniveristari(){
        List<Universitario> u = this.universitarioService.getUniversitari();
        return new ResponseEntity<>(u, HttpStatus.OK);
    }

    @GetMapping("/salva")
    public void salva(){
        this.universitarioService.salva();
    }


    @GetMapping("/geti")
    public ResponseEntity<List<Immatricolazioni>> getIscrizioni(){
        List<Immatricolazioni> i = this.universitarioService.getIscrizioni();
        return new ResponseEntity<>(i,HttpStatus.OK);
    }

    @GetMapping("/geti/{anno}")
    public ResponseEntity<List<Immatricolazioni>> getIscrizioniAnno(@PathVariable int anno){
        List<Immatricolazioni> i = this.universitarioService.getIscrizioniAnno(anno);
        return new ResponseEntity<>(i,HttpStatus.OK);
    }



}
