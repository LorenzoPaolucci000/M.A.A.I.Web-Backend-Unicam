package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.Scuola;
import com.example.PiattaformaPCTO_v2.repository.ScuolaRepository;
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


import java.io.*;

import java.util.*;

@Service
public class SimpleScuolaService implements ScuolaService{

    @Autowired
    private ScuolaRepository scuolaRepository;


    @Override
    public String save(Scuola scuola) {
        return scuolaRepository.save(scuola).getIdScuola();
    }



    @Override
    public String upload() {
        String filePath="src/main/resources/scuole-statali.xlsx";
        try {
            FileInputStream excel = new FileInputStream(new File(filePath));
            System.out.println(filePath);
            Workbook workbook = new XSSFWorkbook(excel);
            Sheet dataSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = dataSheet.rowIterator();
            iterator.next();
            iterator.next();
            iterator.next();
            while(iterator.hasNext()){
                Row row = iterator.next();
                if (!row.getCell(7).getStringCellValue().equals("SCUOLA PRIMO GRADO")){
                    if (!row.getCell(7).getStringCellValue().equals("SCUOLA PRIMARIA")){
                        if (!row.getCell(7).getStringCellValue().equals("SCUOLA INFANZIA")){
                            String id = row.getCell(2).getStringCellValue();
                            String nome = row.getCell(3).getStringCellValue();
                            String regione = row.getCell(0).getStringCellValue();
                            String provincia = row.getCell(1).getStringCellValue();
                            String citta = row.getCell(6).getStringCellValue();
                            String tipo =row.getCell(7).getStringCellValue();
                            Scuola scuola = new Scuola(id,nome,regione,provincia,citta,tipo);
                            this.save(scuola);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    @Override
    public void stampa() {
        Scuola scuola = scuolaRepository.getScuolaByNomeContainingAndAndCitta("RICCI","FERMO");
        String s= scuola.getNome();

        System.out.println(s.replace('"','$'));
    }

    @Override
    public List<Scuola> getScuole() {
        return this.scuolaRepository.findAll();
    }

    @Override
    public List<String> getCitta() {
        List<Scuola> scuole = this.scuolaRepository.findAll();
        List<String> citta = new ArrayList<>();
        for (Scuola s : scuole){
            citta.add(s.getCitta());
        }
        return citta;
    }

    @Override
    public List<String> getNomi(String c) {
        List<Scuola> scuole = this.scuolaRepository.getScuolaByCitta(c);
        List<String> nomi = new ArrayList<>();
        for (Scuola s : scuole){
            nomi.add(s.getNome());
        }
        return nomi;
    }

    @Override
    public void downloadAllSchhool(String filename) {
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
        List<Scuola> scuole =scuolaRepository.findAll();
        for (int i = 0; i< scuole.size(); i++) {
            // Creazione della prima riga
            Row row = sheet.createRow(i);
            Cell cellId = row.createCell(0);
            Cell cellNome = row.createCell(1);
            Cell cellRegione = row.createCell(2);
            Cell cellProvincia = row.createCell(3);
            Cell cellCitta = row.createCell(4);
            Cell cellTipo = row.createCell(5);
            // Impostazione dei valori delle celle
            cellId.setCellValue(scuole.get(i).getIdScuola());
            cellNome.setCellValue(scuole.get(i).getNome());
            cellRegione.setCellValue(scuole.get(i).getRegione());
            cellProvincia.setCellValue(scuole.get(i).getProvincia());
            cellCitta.setCellValue(scuole.get(i).getCitta());
            cellTipo.setCellValue(scuole.get(i).getTipo());
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

    public ResponseEntity<Object> downloadFile(String name) throws FileNotFoundException {
        // Percorso del file sul tuo sistema
        String filePath = "C:/Users/user/IdeaProjects/PiattaformaPCTO-master-master/"+name;

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


}
