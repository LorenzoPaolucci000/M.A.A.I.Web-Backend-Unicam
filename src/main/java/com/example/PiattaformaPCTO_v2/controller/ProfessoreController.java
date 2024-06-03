package com.example.PiattaformaPCTO_v2.controller;


import com.example.PiattaformaPCTO_v2.Request.ActivityRequest;
import com.example.PiattaformaPCTO_v2.Request.DeleteFileRequest;
import com.example.PiattaformaPCTO_v2.Request.UploadDefinitively;
import com.example.PiattaformaPCTO_v2.Request.UploadProfRequest;
import com.example.PiattaformaPCTO_v2.collection.Professore;
import com.example.PiattaformaPCTO_v2.collection.ProfessoreUnicam;
import com.example.PiattaformaPCTO_v2.service.ProfessoreService;
import com.example.PiattaformaPCTO_v2.service.ProfessoreUnicamService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@RestController
@RequestMapping("/professori")

@CrossOrigin(origins = "http://localhost:4200",allowedHeaders = "*")
//@CrossOrigin(origins = "*",allowedHeaders = "*")
public class ProfessoreController {


    @Autowired
    private ProfessoreService professoreService;
    @Autowired
    private ProfessoreUnicamService professoreUnicamService;

    @PostMapping
    public String save(@RequestBody Professore professore){
        return professoreService.save(professore);
    }





    @PostMapping("/createEmptyActivity1")
    public void createEmptyActivity1(@RequestBody ActivityRequest create)
    {

List<ProfessoreUnicam> prof=professoreUnicamService.getProfByString(create.getProfUnicam());
Professore profReferente=professoreService.getProfByString(create.getProfReferente());

        professoreService.createEmptyActivity(create.getNome(), create.getTipo(), create.getScuola(),create.getAnno(),
               create.getSede(), create.getDataInizio(),create.getDataFine(),create.getDescrizione(),prof,profReferente);}


    /**
     * chiamato al metodo che invoca il download dei professori in formatao excel
     */

    @PostMapping("/download")
    public ResponseEntity<Object> downloadFile(@RequestBody DeleteFileRequest filerequest) throws FileNotFoundException {
        professoreService.downloadAllProfOnFile(filerequest.getName());
       return professoreService.downloadFile(filerequest.getName());
    }


    @GetMapping("/getPendingActivities")
        public ResponseEntity<List<String>> getActivities(){
        List<String> activities=professoreService.getAllPendingActivities();

        return new ResponseEntity<>(activities, HttpStatus.OK);
    }





    @PostMapping("/uploadActivityDefinitively")
    public void uploadActivityDefinitively(@RequestBody UploadDefinitively uploadDefinitively) throws IOException {
        professoreService.uploadActivityDefinitively(uploadDefinitively.getNome());
    }

    @PostMapping("/uploadConFile1")
    public void uploadConAnno1(@RequestParam("file") MultipartFile file){
        professoreService.uploadConFile(file);
    }
    @GetMapping("/stampa")
    public String stampa(){
        return professoreService.stampa();
    }

    @GetMapping("/get")
    public ResponseEntity<List<Professore>> getProf() {

        List<Professore> p = this.professoreService.getAllProf();
        return new ResponseEntity<>(p, HttpStatus.OK);
    }
    @GetMapping("/getReferenti")
    public ResponseEntity<List<String>> getProfVista() {
        List<Professore> p = this.professoreService.getAllProf();
        List<String> vista=new ArrayList<>();
        for(int i=0;i<p.size();i++){
            vista.add(p.get(i).getNome()+" "+p.get(i).getCognome()+" "+p.get(i).getEmail());
        }
        return new ResponseEntity<>(vista, HttpStatus.OK);
    }


    @PostMapping("/uploadSingleProf")
    public void uploadSingleProf(@RequestBody UploadProfRequest prof ){

        professoreService.uploadSingleProf(prof.getNome(),prof.getCognome(),prof.getEmail(),prof.getScuola(),prof.getCitta(),prof.getAttivita());
    }

}
