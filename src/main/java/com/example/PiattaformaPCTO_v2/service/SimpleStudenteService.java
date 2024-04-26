package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.Studente;
import com.example.PiattaformaPCTO_v2.repository.StudenteRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class SimpleStudenteService implements StudenteService{

    @Autowired
    private StudenteRepository studenteRepository;

    @Override
    public void addIscrizione(String nome, String cognome,String filePath) {
        String filePathac="src/main/resources/activity/"+filePath+".xlsx";
        try (FileInputStream fileInputStream = new FileInputStream(filePathac)) {
            // Carica il file Excel in un workbook
            Workbook workbook = new XSSFWorkbook(fileInputStream);


            // Ottieni il foglio di lavoro (o crealo se non esiste)
            Sheet sheet = workbook.getSheetAt(0);
            if(sheet == null) {
                sheet = workbook.createSheet("Sheet1");
            }
            // Cerca la prima riga vuota disponibile
            int rowNum = 0;
            while(sheet.getRow(rowNum) != null) {
                rowNum++;
            }

            Row row = sheet.createRow(rowNum);
            row.createCell(0).setCellValue(nome);
            row.createCell(1).setCellValue(cognome);
            Row rowStudente = sheet.getRow(0);
            String scuola=rowStudente.getCell(0).getStringCellValue();
            String cittaScuola=rowStudente.getCell(1).getStringCellValue();
            row.createCell(2).setCellValue(scuola);
            row.createCell(3).setCellValue(cittaScuola);


            try (FileOutputStream fileOut = new FileOutputStream(filePathac)) {
                workbook.write(fileOut);
            } catch (IOException e) {
                System.out.println("Si è verificato un errore durante la scrittura del file: " + e.getMessage());
            } finally {
                try {
                    workbook.close();
                } catch (IOException e) {
                    System.out.println("Si è verificato un errore durante la chiusura del workbook: " + e.getMessage());
                }
            }


            System.out.println("Foglio di lavoro caricato con successo.");
        } catch (IOException e) {
            System.out.println("Si è verificato un errore durante la lettura del file Excel: " + e.getMessage());
        }








    }

    @Override
    public String save(Studente studente) {
        return studenteRepository.save(studente).getScuola().getNome();
    }
}
