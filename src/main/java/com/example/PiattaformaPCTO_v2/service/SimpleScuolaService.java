package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.Scuola;
import com.example.PiattaformaPCTO_v2.repository.ScuolaRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    public void downloadAllSchhool() {
        // Crea un nuovo workbook Excel
        Workbook workbook = new XSSFWorkbook();
        // Crea un foglio di lavoro
        Sheet sheet = workbook.createSheet("Sheet1");
        // Percorso della cartella delle risorse
        String resourcesPath = "src/main/resources/";
        // Percorso completo della cartella "activity" nelle risorse
        String activityFolderPath = resourcesPath + "activity/";
        // Nome del file Excel
        String filename = "scuole.xlsx";
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


}
