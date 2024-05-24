package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.Professore;
import com.example.PiattaformaPCTO_v2.collection.ProfessoreUnicam;
import com.example.PiattaformaPCTO_v2.collection.Scuola;
import com.example.PiattaformaPCTO_v2.repository.ProfessoreUnicamRepository;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

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
        return professoreUnicamRepository.findAll();
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
}
