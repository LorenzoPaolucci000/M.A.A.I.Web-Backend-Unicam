package com.example.PiattaformaPCTO_v2.controller;

import com.example.PiattaformaPCTO_v2.Request.ActivityRequest;
import com.example.PiattaformaPCTO_v2.Request.CreateSingleActivityRequest;
import com.example.PiattaformaPCTO_v2.Request.FileMultipartFile;
import com.example.PiattaformaPCTO_v2.collection.Attivita;
import com.example.PiattaformaPCTO_v2.collection.Professore;
import com.example.PiattaformaPCTO_v2.collection.ProfessoreUnicam;
import com.example.PiattaformaPCTO_v2.dto.ActivityViewDTOPair;
import com.example.PiattaformaPCTO_v2.enumeration.Sede;
import com.example.PiattaformaPCTO_v2.service.AttivitaService;
import com.example.PiattaformaPCTO_v2.service.ProfessoreService;
import com.example.PiattaformaPCTO_v2.service.ProfessoreUnicamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/attivita")
@CrossOrigin(origins = "http://localhost:4200",allowedHeaders = "*")
public class AttivitaController {

    @Autowired
    private AttivitaService attivitaService;

    @Autowired
    private ProfessoreService professoreService;
    @Autowired
    private ProfessoreUnicamService professoreUnicamService;

    @PostMapping
    public String save(@RequestBody Attivita attivita) {
        return attivitaService.save(attivita);
    }




    @PostMapping("/uploadConAnno1/{param}")
    public void uploadActivity( @PathVariable ("param" ) String create,
                                @RequestParam("file") MultipartFile file){
        String nome=create.substring(0,create.indexOf("&"));
        create=create.substring(create.indexOf("&")+1);
        String tipo=create.substring(0,create.indexOf(" "));
        create=create.substring(create.indexOf(" ")+1);
        String s=create.substring(0,create.indexOf("-"));
        create=create.substring(create.indexOf("-")+1);
        String sede;
        String scuola="";
        if(s.length()<7){
         sede=s;
        }
        else{

            scuola=s;
            sede=create.substring(0,create.indexOf("*"));
            create=create.substring(create.indexOf("*")+1);
        }

        String dataI=create.substring(0,create.indexOf(" "));

        String dataIn;
        dataIn=dataI.substring(0,dataI.indexOf("T"))+" "+dataI.substring(dataI.indexOf("T")+1,dataI.length())+":00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Effettua il parsing della stringa nella data e ora
        LocalDateTime dataInizio = LocalDateTime.parse(dataIn, formatter);


        create=create.substring(create.indexOf(" ")+1);
        String dataF=create.substring(0,create.indexOf(" "));

        String dataFn;
        dataFn=dataF.substring(0,dataF.indexOf("T"))+" "+dataF.substring(dataF.indexOf("T")+1,dataF.length())+":00";


        // Effettua il parsing della stringa nella data e ora
        LocalDateTime dataFine = LocalDateTime.parse(dataFn, formatter);
        create=create.substring(create.indexOf(" ")+1);
        String descrizione=create.substring(0,create.indexOf("+"));
        create=create.substring(create.indexOf("+")+1);
        List<String> pUnicam=new ArrayList<>();

       boolean c;

        while(create.contains(",")){
                pUnicam.add(create.substring(0,create.indexOf(",")));
                create=create.substring(create.indexOf(",")+1);

        }

String referente=create.substring(1,create.indexOf("-"));

        create=create.substring(create.indexOf("2"));

int anno=Integer.parseInt(create);
Sede sedeA=Sede.Online;
        switch (sede) {
            case "online":
                sedeA=Sede.Online;
                break;
            case "università":
                sedeA=Sede.Università;
                break;
            case "scuola":
                sedeA=Sede.Scuola;
                break;
            case "altro":
                sedeA=Sede.AltraSede;
                break;
        }






        List<ProfessoreUnicam> prof=professoreUnicamService.getProfByString(pUnicam);
        Professore profReferente=professoreService.getProfByString(referente);
        attivitaService.uploadSingleActivity(nome,tipo,scuola,anno,
                sedeA,dataInizio,dataFine,descrizione,prof,profReferente,file);

    }


    /**
     * Get information about students that chose UNICAM and their high school, given an activity.
     *
     * @return list of activity view pairs
     */
    @ResponseBody
    @GetMapping("/studentsFromActivity/{activityName}")
    @ResponseStatus(HttpStatus.OK)
    public List<ActivityViewDTOPair> getStudentsFromActivity(@PathVariable String activityName) {
        return this.attivitaService.findStudentsFromActivity(activityName);
    }





}
