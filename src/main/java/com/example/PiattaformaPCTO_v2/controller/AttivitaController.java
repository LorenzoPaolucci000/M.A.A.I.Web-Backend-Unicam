package com.example.PiattaformaPCTO_v2.controller;

import com.example.PiattaformaPCTO_v2.collection.Attivita;
import com.example.PiattaformaPCTO_v2.dto.ActivityViewDTOPair;
import com.example.PiattaformaPCTO_v2.service.AttivitaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/attivita")
@CrossOrigin(origins = "http://localhost:4200",allowedHeaders = "*")
public class AttivitaController {

    @Autowired
    private AttivitaService attivitaService;

    @PostMapping
    public String save(@RequestBody Attivita attivita) {
        return attivitaService.save(attivita);
    }


    @RequestMapping(value = "/uploadConAnno", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadConAnno(@RequestParam("file") MultipartFile file,@RequestParam("anno") int anno,@RequestParam("nome")String nome){attivitaService.uploadConAnno(file,anno,nome);}

    @PostMapping("/uploadConAnno1/{nome}")
    public void uploadConAnno1(@RequestParam("file") MultipartFile file,@PathVariable("nome")String nome){
        int anno=Integer.parseInt(nome.substring(0,4));
        String nome1= nome.substring(4);
        attivitaService.uploadConAnno(file,anno,nome1);
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


    @GetMapping("/prova")
    void prova(){attivitaService.prova();}


}
