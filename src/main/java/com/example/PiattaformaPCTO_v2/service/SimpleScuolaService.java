package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.Risultati;
import com.example.PiattaformaPCTO_v2.collection.Scuola;
import com.example.PiattaformaPCTO_v2.repository.RisultatiRepository;
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
    @Autowired
    private RisultatiRepository risultatiRepository;


    @Override
    public String upload() {
        String filePath="src/main/resources/scuole-statali.xlsx";
        try {
            FileInputStream excel = new FileInputStream(new File(filePath));

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
    public void downloadAllSchhool(String filename,int anno) {
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
        List<Risultati> risultati;
if(anno==0) {
    risultati = risultatiRepository.findAll();
}else{
    risultati = risultatiRepository.findbyAnno(anno);
}

        int j=0;
        for (int i = 0; i< risultati.size(); i++) {

            // Creazione della prima riga
            Row row = sheet.createRow(j);
            Cell annoAc = row.createCell(0);
            annoAc.setCellValue("Anno Accademico");j++;
            Row row0 = sheet.createRow(j);
            Cell annoAc0 = row0.createCell(0);
            annoAc0.setCellValue(risultati.get(i).getAnnoAcc());
            j++;
            Row row1 = sheet.createRow(j);
            Cell ScuolaId=row1.createCell(0);
            Cell cellNome = row1.createCell(1);
            Cell cellRegione = row1.createCell(2);
            Cell cellProvincia = row1.createCell(3);
            Cell cellCitta = row1.createCell(4);
            Cell cellTipo = row1.createCell(5);
            ScuolaId.setCellValue("ID Scuola");
            cellNome.setCellValue("Nome Scuola");
            cellRegione.setCellValue("Regione ");
            cellProvincia.setCellValue("Provincia");
            cellCitta.setCellValue("Citta");
            cellTipo.setCellValue("Tipo");
            j++;
            Row row11 = sheet.createRow(j);
            Cell ScuolaId1=row11.createCell(0);
            Cell cellNome1 = row11.createCell(1);
            Cell cellRegione1 = row11.createCell(2);
            Cell cellProvincia1 = row11.createCell(3);
            Cell cellCitta1 = row11.createCell(4);
            Cell cellTipo1 = row11.createCell(5);

            ScuolaId1.setCellValue(risultati.get(i).getScuola().getIdScuola());
            cellNome1.setCellValue(risultati.get(i).getScuola().getNome());
            cellRegione1.setCellValue(risultati.get(i).getScuola().getRegione());
            cellProvincia1.setCellValue(risultati.get(i).getScuola().getProvincia());
            cellCitta1.setCellValue(risultati.get(i).getScuola().getCitta());
            cellTipo1.setCellValue(risultati.get(i).getScuola().getTipo());
            j++;
            //cicla le attività
              for(int p=0;p<risultati.get(i).getAttivita().size();p++){
                  Row row2 = sheet.createRow(j);
                  Cell cellNomeAttivita = row2.createCell(0);
                  cellNomeAttivita.setCellValue("Nome Attività");
                  j++;
                  Row row22 = sheet.createRow(j);
                  Cell cellNomeAttivita2 = row22.createCell(0);
                   cellNomeAttivita2.setCellValue(risultati.get(i).getAttivita().get(p).getNomeAttivita());
                   j++;
                   //clica i partecipanti
                    for(int v=0;v<risultati.get(i).getAttivita().get(p).getPartecipanti().size();v++){
                        Row row3 = sheet.createRow(j);

                        Cell cellNomeP = row3.createCell(0);
                        Cell cellCognomeP = row3.createCell(1);
                        Cell ScuolaIdP=row3.createCell(2);
                        Cell cellNomeSP = row3.createCell(3);
                        Cell cellRegioneP = row3.createCell(4);
                        Cell cellProvinciaP = row3.createCell(5);
                        Cell cellCittaP = row3.createCell(6);
                        Cell cellTipoP = row3.createCell(7);

                        cellNomeP.setCellValue("Nome ");
                        cellCognomeP.setCellValue("Cognome");
                        ScuolaIdP.setCellValue("ID scuola");
                        cellNomeSP.setCellValue("Nome Scuola");
                        cellRegioneP.setCellValue("Regione Scuola");
                        cellProvinciaP.setCellValue("Provincia Scuola");
                        cellCittaP.setCellValue("Città Scuola");
                        cellTipoP.setCellValue("Tipo Scuola");
                        j++;

                        Row row33 = sheet.createRow(j);

                        Cell cellNomeP1 = row33.createCell(0);
                        Cell cellCognomeP1 = row33.createCell(1);
                        Cell ScuolaIdP1=row33.createCell(2);
                        Cell cellNomeSP1 = row33.createCell(3);
                        Cell cellRegioneP1 = row33.createCell(4);
                        Cell cellProvinciaP1 = row33.createCell(5);
                        Cell cellCittaP1 = row33.createCell(6);
                        Cell cellTipoP1 = row33.createCell(7);

                        cellNomeP1.setCellValue(risultati.get(i).getAttivita().get(p).getPartecipanti().get(v).getNome());
                        cellCognomeP1.setCellValue(risultati.get(i).getAttivita().get(p).getPartecipanti().get(v).getCognome());
                        ScuolaIdP1.setCellValue(risultati.get(i).getScuola().getIdScuola());
                        cellNomeSP1.setCellValue(risultati.get(i).getScuola().getNome());
                        cellRegioneP1.setCellValue(risultati.get(i).getScuola().getRegione());
                        cellProvinciaP1.setCellValue(risultati.get(i).getScuola().getProvincia());
                        cellCittaP1.setCellValue(risultati.get(i).getScuola().getCitta());
                        cellTipoP1.setCellValue(risultati.get(i).getScuola().getTipo());
                        j++;
                    }
              }

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

    @Override
    public List<String> getNomeAndCittaAnnoByScuola(String citta) {
        List<String> scuoleNome=new ArrayList<>();


        List<Scuola> scuole=scuolaRepository.getScuolaByCitta(citta.toUpperCase());
        for (Scuola scuola : scuole) {
            scuoleNome.add(scuola.getNome());
        }

        return scuoleNome;
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
