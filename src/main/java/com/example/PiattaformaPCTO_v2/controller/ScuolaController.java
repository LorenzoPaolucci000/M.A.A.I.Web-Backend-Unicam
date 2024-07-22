package com.example.PiattaformaPCTO_v2.controller;

import com.example.PiattaformaPCTO_v2.Request.DeleteFileRequest;
import com.example.PiattaformaPCTO_v2.collection.Scuola;
import com.example.PiattaformaPCTO_v2.service.ScuolaService;
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
@RequestMapping("/scuola")
@CrossOrigin(origins = "http://localhost:4200",allowedHeaders = "*")
public class ScuolaController {

    @Autowired
    private ScuolaService scuolaService;


    @PostMapping
    public String save(@RequestBody Scuola scuola){
        return scuolaService.save(scuola);
    }


    @GetMapping("/upload")
    public String save(){
        return scuolaService.upload();
    }




    /**
     * chiamato al metodo che invoca il download delle scuole in formatao excel
     */

    @PostMapping("/download")
    public ResponseEntity<Object> downloadFile(@RequestBody DeleteFileRequest filerequest) throws FileNotFoundException {
        scuolaService.downloadAllSchhool(filerequest.getName(), filerequest.getAnno());
      return scuolaService.downloadFile(filerequest.getName());
    }
    @GetMapping("/scuoleCitta/{citta}")
   public  ResponseEntity<List<String>> getScuoleByCitta(@PathVariable("citta") String citta){
List<String> scuole=scuolaService.getNomeAndCittaAnnoByScuola(citta);

        return new ResponseEntity<>(scuole, HttpStatus.OK);
    }




    @GetMapping("/visua")
    public ResponseEntity<List<Scuola>> visualizza(){
        List<Scuola> s = this.scuolaService.getScuole();
        return new ResponseEntity<>(s, HttpStatus.OK);
    }


    @GetMapping("/c")
    public void citta(){
        this.scuolaService.getCitta();
    }
}
