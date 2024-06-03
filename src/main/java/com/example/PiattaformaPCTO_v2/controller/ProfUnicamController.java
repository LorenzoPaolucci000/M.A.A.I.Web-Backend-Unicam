package com.example.PiattaformaPCTO_v2.controller;

import com.example.PiattaformaPCTO_v2.Request.DeleteFileRequest;
import com.example.PiattaformaPCTO_v2.Request.UploadProfUnicamRequest;
import com.example.PiattaformaPCTO_v2.collection.Professore;
import com.example.PiattaformaPCTO_v2.collection.ProfessoreUnicam;
import com.example.PiattaformaPCTO_v2.service.ProfessoreUnicamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/professoriUnicam")

@CrossOrigin(origins = "http://localhost:4200",allowedHeaders = "*")
public class ProfUnicamController {
    @Autowired
    private ProfessoreUnicamService professoreUnicamService;
    @GetMapping("/getAllProf")
    public ResponseEntity<List<ProfessoreUnicam>> getAllProf(){
        List<ProfessoreUnicam> prof=professoreUnicamService.getAllProf();

        return new ResponseEntity<>(prof, HttpStatus.OK);
    }
    @GetMapping("/getProfUnicam")
    public ResponseEntity<List<String>> getProfVista() {
        List<ProfessoreUnicam> p = this.professoreUnicamService.getAllProf();
        List<String> vista=new ArrayList<>();
        for(int i=0;i<p.size();i++){
            vista.add(p.get(i).getNome()+" "+p.get(i).getCognome()+" "+p.get(i).getEmail());
        }
        return new ResponseEntity<>(vista, HttpStatus.OK);
    }
    @PostMapping("/uploadConFile1")
    public void uploadConAnno1(@RequestParam("file") MultipartFile file){
        professoreUnicamService.uploadConFile(file);
    }

    @PostMapping("/uploadSingleProf")
    public void uploadSingleProf(@RequestBody UploadProfUnicamRequest prof ){
        professoreUnicamService.uploadSingleProf(prof.getNome(),prof.getCognome(),prof.getEmail());
    }
    @GetMapping("/get")
    public ResponseEntity<List<ProfessoreUnicam>> getProf() {

        List<ProfessoreUnicam> p = this.professoreUnicamService.getAllProf();

        return new ResponseEntity<>(p, HttpStatus.OK);
    }

    @PostMapping("/download")
    public ResponseEntity<Object> downloadFile(@RequestBody DeleteFileRequest filerequest) throws FileNotFoundException {
        professoreUnicamService.downloadAllProfOnFile(filerequest.getName());
        return professoreUnicamService.downloadFile(filerequest.getName());
    }
}
