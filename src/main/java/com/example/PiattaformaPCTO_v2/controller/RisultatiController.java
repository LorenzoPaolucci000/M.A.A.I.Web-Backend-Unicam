package com.example.PiattaformaPCTO_v2.controller;

import com.example.PiattaformaPCTO_v2.Request.DeleteFileRequest;
import com.example.PiattaformaPCTO_v2.collection.Risultati;
import com.example.PiattaformaPCTO_v2.collection.RisultatiAtt;
import com.example.PiattaformaPCTO_v2.service.RisultatiService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@RestController
@RequestMapping("/risultati")
@CrossOrigin(origins = "http://localhost:4200",allowedHeaders = "*")
public class RisultatiController {
    @Autowired
    private RisultatiService risultatiService;



    @RequestMapping("/createStudentsFromActivities")
    public void createStudentsFromActivities(){
        this.risultatiService.createStudentsFromActivities();
    }

    /**
     * metodo che mette in un file i risulatati
     */


    @PostMapping("/download")
    public ResponseEntity<Object> downloadFile(@RequestBody DeleteFileRequest filerequest) throws FileNotFoundException {

        risultatiService.donloadResOnFile(filerequest.getName(), filerequest.getAnno());
       return risultatiService.downloadFile(filerequest.getName());
    }

    @GetMapping("/res")
    public ResponseEntity<List<Risultati>> getRisultati(){
        List<Risultati> res = this.risultatiService.getRisultati();
        return new ResponseEntity<>(res,HttpStatus.OK);
    }

    @GetMapping("/resa")
    public ResponseEntity<List<RisultatiAtt>> getRisulatatiAtt(){
        List<RisultatiAtt> resa = this.risultatiService.getRisultatiAtt();

        return new ResponseEntity<>(resa,HttpStatus.OK);
    }

    @GetMapping("/res/{anno}")
    public ResponseEntity<List<Risultati>> getRisultatiAnno(@PathVariable int anno){
        List<Risultati> res = this.risultatiService.getRisultatiAnno(anno);
        return new ResponseEntity<>(res,HttpStatus.OK);
    }
    @GetMapping("/s")
    public ResponseEntity<Risultati> stampa(){
        Risultati r = this.risultatiService.stampa();
        return new ResponseEntity<>(r, HttpStatus.OK);
    }
}
