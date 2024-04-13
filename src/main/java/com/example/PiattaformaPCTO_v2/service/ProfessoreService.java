package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.Professore;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProfessoreService {
    String save(Professore professore);
    String upload();

    void uploadConFile(MultipartFile file);
    String stampa();

    List<Professore> getAllProf();
    Sheet fileOpenerHelper(MultipartFile file);
}
