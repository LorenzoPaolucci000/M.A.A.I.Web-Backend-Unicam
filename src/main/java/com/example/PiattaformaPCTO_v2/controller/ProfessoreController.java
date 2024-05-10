package com.example.PiattaformaPCTO_v2.controller;


import com.example.PiattaformaPCTO_v2.Request.ActivityRequest;
import com.example.PiattaformaPCTO_v2.Request.DeleteFileRequest;
import com.example.PiattaformaPCTO_v2.Request.UploadDefinitively;
import com.example.PiattaformaPCTO_v2.collection.Professore;
import com.example.PiattaformaPCTO_v2.service.ProfessoreService;
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

    @PostMapping
    public String save(@RequestBody Professore professore){
        return professoreService.save(professore);
    }
    @GetMapping("/upload")
    public String save(){
        return professoreService.upload();
    }

    @RequestMapping(value="/createEmptyActivity")
public void createEmptyActivity(@RequestParam("nome") String nome ,@RequestParam("anno") int anno,
                          @RequestParam("scuola")String scuola,@RequestParam("cittaScuola")String cittaScuola)
    {professoreService.createEmptyActivity(nome, anno, scuola, cittaScuola);}

    @PostMapping("/createEmptyActivity1")
    public void createEmptyActivity1(@RequestBody ActivityRequest create)
    {

        professoreService.createEmptyActivity(create.getNome(), create.getAnno(), create.getNomeScuola(), create.getCittaScuola());}


    /**
     * chiamato al metodo che invoca il download dei professori in formatao excel
     */


    @PostMapping("/download")
    public ResponseEntity<Object> downloadFile(@RequestBody DeleteFileRequest filerequest) throws FileNotFoundException {
        professoreService.downloadAllProfOnFile(filerequest.getName());
        // Percorso del file sul tuo sistema
        String filePath = "C:/Users/user/IdeaProjects/PiattaformaPCTO-master-master/"+filerequest.getName();;

        // Creazione di un oggetto File con il percorso specificato
        File file = new File(filePath);

        // Controllo se il file esiste
        if (!file.exists()) {
            return ResponseEntity.notFound().build(); // File non trovato, restituisce una risposta 404
        }

        // Creazione di un oggetto InputStreamResource per avvolgere il file
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        // Costruzione delle intestazioni della risposta HTTP
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName()); // Specifica il nome del file nel Content-Disposition
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");
        this.deleteFile(filePath);
        // Costruzione della risposta HTTP con il file scaricabile
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length()) // Imposta la lunghezza del contenuto nel corpo della risposta
                .contentType(MediaType.parseMediaType("application/octet-stream")) // Imposta il tipo MIME del contenuto
                .body(resource); // Imposta il corpo della risposta con il file
    }

    private  void deleteFile(String filePath) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                File file = new File(filePath);
                if (file.exists()) {
                    if (file.delete()) {
                        System.out.println("Il file è stato eliminato con successo: " + filePath);
                    } else {
                        System.out.println("Impossibile eliminare il file: " + filePath);
                    }
                } else {
                    System.out.println("Il file non esiste: " + filePath);
                }
            }
        }, 3000);
    }
    @GetMapping("/getPendingActivities")
        public ResponseEntity<List<String>> getActivities(){
        List<String> activities=professoreService.getAllPendingActivities();
System.out.println(activities.size());
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
    public ResponseEntity<List<Professore>> getProf(){
        List<Professore> p = this.professoreService.getAllProf();
        return new ResponseEntity<>(p, HttpStatus.OK);
    }


}
