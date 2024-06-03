package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.Professore;
import com.example.PiattaformaPCTO_v2.collection.ProfessoreUnicam;
import com.example.PiattaformaPCTO_v2.collection.Scuola;
import com.example.PiattaformaPCTO_v2.repository.ProfessoreUnicamRepository;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Service
public class SimpleProfessoreUnicamService implements ProfessoreUnicamService {
    @Autowired
    ProfessoreUnicamRepository professoreUnicamRepository;


    @Override
    public void uploadConFile(MultipartFile file) {
        Sheet dataSheet = this.fileOpenerHelper(file);
        Iterator<Row> iterator = dataSheet.rowIterator();
        iterator.next();

        while (iterator.hasNext()){
            Row row = iterator.next();
            String nome=row.getCell(0).getStringCellValue();
            String cognome=row.getCell(1).getStringCellValue();
            String email=row.getCell(2).getStringCellValue();
            ProfessoreUnicam prof=new ProfessoreUnicam(nome,cognome,email);
          if(professoreUnicamRepository.getProfByEmail(email)==null){
              professoreUnicamRepository.save(prof);
          }


        }
    }
    @Override
    public List<ProfessoreUnicam> getAllProf() {
        List<ProfessoreUnicam> p = this.professoreUnicamRepository.findAll();
        return p;
    }


    @Override
    public void uploadSingleProf(String nome, String cognome, String email) {
        ProfessoreUnicam prof=new ProfessoreUnicam(nome,cognome,email);
        if(professoreUnicamRepository.getProfByEmail(email)==null){
            professoreUnicamRepository.save(prof);
        }
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

    @Override
    public List<ProfessoreUnicam> getProfByString(List<String> prof) {
        List<ProfessoreUnicam> professoreUnicam=new ArrayList<>();

        for(int i=0;i< prof.size();i++){
            List<String> s=separa(prof.get(i));
            if(professoreUnicamRepository.getByNomeCognome(s.get(0),s.get(1))!=null){

                professoreUnicam.add(professoreUnicamRepository.getByNomeCognome(s.get(0),s.get(1)));
            }
        }
        return professoreUnicam;
    }

    @Override
    public void downloadAllProfOnFile(String filename) {
        // Crea un nuovo workbook Excel
        Workbook workbook = new XSSFWorkbook();
        // Crea un foglio di lavoro
        Sheet sheet = workbook.createSheet("Sheet1");
        // Percorso della cartella delle risorse
        String resourcesPath = "src/main/resources/";
        // Percorso completo della cartella "activity" nelle risorse
        String activityFolderPath = resourcesPath + "activity/";
        // Nome del file Excel

        // Percorso completo del file Excel
        String filePath = activityFolderPath + filename;
        // Assicurati che la cartella "activity" esista, altrimenti creala
        File activityFolder = new File(activityFolderPath);
        List<ProfessoreUnicam> professori=professoreUnicamRepository.findAll();
        Row row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("Email");
        row0.createCell(1).setCellValue("Nome");
        row0.createCell(2).setCellValue("Cognome");

        for (int i=0;i< professori.size();i++) {
            // Creazione della prima riga
            Row row = sheet.createRow(i+1);
            Cell cellEmail = row.createCell(0);
            Cell cellNome = row.createCell(1);
            Cell cellCognome = row.createCell(2);

            // Impostazione dei valori delle celle
            cellEmail.setCellValue(professori.get(i).getEmail());
            cellNome.setCellValue(professori.get(i).getNome());
            cellCognome.setCellValue(professori.get(i).getCognome());

        }

        try (FileOutputStream outputStream = new FileOutputStream(filename)) {
            workbook.write(outputStream);
            System.out.println("File Excel creato con successo!");
        } catch (IOException e) {
            System.err.println("Errore durante la creazione del file Excel: " + e.getMessage());
        } finally {
            // Chiusura del workbook
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public ResponseEntity<Object> downloadFile(String name) throws FileNotFoundException {
        // Percorso del file sul tuo sistema
        String filePath = "C:/Users/user/IdeaProjects/PiattaformaPCTO-master-master/"+name;;

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
    /**
     * metodo che elimina il file scaricato dal filesystem
     * @param filePath
     */

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
    private List<String> separa(String s) {
        List<String> parole=new ArrayList<>();
        for(int i=0;i<3;i++) {
           String p=s;
           if(i<2) {
               parole.add(s.substring(0,s.indexOf(" ")));
           }
           else{
               parole.add(s);
           }
            s = s.substring(s.indexOf(" ")+1,s.length());

        }

        return parole;
    }
}
