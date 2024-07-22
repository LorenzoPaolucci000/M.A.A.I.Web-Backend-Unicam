package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.Attivita;
import com.example.PiattaformaPCTO_v2.collection.Immatricolazioni;
import com.example.PiattaformaPCTO_v2.collection.Universitario;
import com.example.PiattaformaPCTO_v2.repository.ImmatricolazioniRepository;
import com.example.PiattaformaPCTO_v2.repository.UniversitarioRepository;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

@Service
public class SimpleUniversitarioService implements UniversitarioService {

    @Autowired
    private UniversitarioRepository universitarioRepository;
    @Autowired
    private ImmatricolazioniRepository immatricolazioniRepository;
    @Autowired
    private MongoTemplate mongoTemplate;


    @Override
    public String save(Universitario universitario) {
        return universitarioRepository.save(universitario).getNome();
    }

/**
metodo che inserisce tutti quei studenti inseriti nell'exel in quell'anno


 */

@Override
    public String uploadConAnno(MultipartFile file,int anno) {

        Immatricolazioni i = new Immatricolazioni(anno);
        Sheet dataSheet = this.fileOpenerHelper(file);
        Iterator<Row> iterator = dataSheet.rowIterator();
        iterator.next();
        String Corso="";
        while (iterator.hasNext()){
            Row row = iterator.next();
            if(row.getCell(1)==null){
                break;
            }
            double matr= row.getCell(1).getNumericCellValue();
            String m = String.valueOf(matr).replaceAll("[0]*$", "").replaceAll(".$", "");
            String nome = row.getCell(2).getStringCellValue();
            String cognome = row.getCell(3).getStringCellValue();
            if (row.getCell(5)!=null){
                String comune = row.getCell(5).getStringCellValue().toUpperCase();
                String scuola = row.getCell(4).getStringCellValue();
                String corso = row.getCell(0).getStringCellValue();
                Corso=corso;
                Universitario universitario = new Universitario(m,nome,cognome,anno,corso,comune,scuola);

                if(universitarioRepository.findByMatricola( universitario.getMatricola())==null) {
                    universitarioRepository.save(universitario);
                    i.addUniversitario(universitario);
                }
            }
        }

          if(isAnno(anno,Corso)!=null)this.addElementi(i,isAnno(anno,Corso));
          else {
              this.immatricolazioniRepository.save(i);
          }
        return "caricati";
    }

    /**
     * metodo che restituisce l'iscrizione con quel determinato anno accademico e corso
     * @return null se non lo trova
     */
    private Immatricolazioni isAnno(int anno, String corso) {

        List<Immatricolazioni> immatricolazioni = immatricolazioniRepository.findByAnno(anno);
        for(int i = 0; i< immatricolazioni.size(); i++){
            if(immatricolazioni.get(i).getUniversitari().get(0).getCorso().equals(corso))return immatricolazioni.get(i);
        }

return null;
    }

    /**
     * metodo che aggiunge gli elementi inseriti nell'iscrizione presente
     * @param i1 iscrizioni da aggiungere
     * @param i2 iscrizioni su cui aggiungere i1
     */
    private void addElementi(Immatricolazioni i1, Immatricolazioni i2 ){
        List<Immatricolazioni> immatricolazioniList = immatricolazioniRepository.findAll();
        immatricolazioniList.remove(i2);
        this.immatricolazioniRepository.deleteAll();
        for(int i=0;i<i1.getUniversitari().size();i++){
            i2.getUniversitari().add(i1.getUniversitari().get(i));
        }
        immatricolazioniList.add(i2);
        this.immatricolazioniRepository.saveAll(immatricolazioniList);
}

    /**
     * metodo che controlla se gli universitari sono gia presenti come studenti tra le attività
     */
    private Map<Universitario,String> checkUniversitari(List<Universitario> universitari ){
        Map<Universitario,String> mapping=new HashMap<>();
        for(int i=0;i<universitari.size();i++){
            Query query=new Query();
query.addCriteria(Criteria.where("studPartecipanti.nome").is(universitari.get(i).getNome()).
        and("studPartecipanti.cognome").is(universitari.get(i).getCognome()));
if(!mongoTemplate.find(query, Attivita.class).isEmpty()) {
    mapping.put(universitari.get(i), mongoTemplate.find(query, Attivita.class).get(0).getNome());
}

        }
return mapping;
    }





    @Override
    public String upload(MultipartFile file) {
        LocalDate date = LocalDate.now();

        Immatricolazioni i = new Immatricolazioni((date.getYear()*2)+1);
        Sheet dataSheet = this.fileOpenerHelper(file);
        Iterator<Row> iterator = dataSheet.rowIterator();
        iterator.next();
        while (iterator.hasNext()){
            Row row = iterator.next();
            double matr= row.getCell(1).getNumericCellValue();
            String m = String.valueOf(matr).replaceAll("[0]*$", "").replaceAll(".$", "");
            String nome = row.getCell(2).getStringCellValue();
            String cognome = row.getCell(3).getStringCellValue();
            if (row.getCell(5)!=null){
                String comune = row.getCell(5).getStringCellValue().toUpperCase();
                String scuola = row.getCell(4).getStringCellValue();
                String corso = row.getCell(0).getStringCellValue();
                Universitario universitario = new Universitario(m,nome,cognome,4047,corso,comune,scuola);

                i.addUniversitario(universitario);

            }
        }

        this.immatricolazioniRepository.save(i);

        return "caricati";
    }

    @Override
    public List<Universitario> getUniversitari() {
        return this.universitarioRepository.findAll();
    }

    @Override
    public void salva() {
       /* Iscrizioni i = new Iscrizioni(2023);
        Universitario u = new Universitario("109211","Matteo","Manci","2023","info","pse","pippo");
        i.addUniversitario(u);
        this.iscrizioniRepository.save(i);*/
    }

    @Override
    public List<Immatricolazioni> getIscrizioni() {
       return this.immatricolazioniRepository.findAll();
    }

    @Override
    public List<Immatricolazioni> getIscrizioniAnno(int anno) {
        Query query = new Query();
        query.addCriteria(Criteria.where("annoAc").is(anno));
        return mongoTemplate.find(query, Immatricolazioni.class);
    }

    @Override
    public Sheet fileOpenerHelper(MultipartFile file) {
        try {
            Path tempDir = Files.createTempDirectory("");
            File tempFile = tempDir.resolve(file.getOriginalFilename()).toFile();
            file.transferTo(tempFile);
            Workbook workbook = new XSSFWorkbook(tempFile);
            Sheet dataSheet = workbook.getSheetAt(0);
            return dataSheet;
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }
}
