package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.*;
import com.example.PiattaformaPCTO_v2.dto.ActivityViewDTOPair;
import com.example.PiattaformaPCTO_v2.repository.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.io.*;
import java.util.*;

@Service
public class SimpleRisultatiService implements RisultatiService {

    @Autowired
    private RisultatiRepository risultatiRepository;
    @Autowired
    private RisultatiAttRepository risultatiAttRepository;
    @Autowired
    private AttivitaRepository attivitaRepository;
    @Autowired
    private UniversitarioRepository universitarioRepository;
    @Autowired
    private ScuolaRepository scuolaRepository;
    @Autowired
    private AttivitaService attivitaService;
    @Autowired
    private UniversitarioService universitarioService;
    @Autowired
    private MongoTemplate mongoTemplate;



    @Override
    public void createStudentsFromActivities() {
        Map<Attivita, List<ActivityViewDTOPair>> result = new HashMap<>();
        List<Attivita> activities = this.attivitaService.getAttivita(4047);

        List<RisultatiAtt> res = new ArrayList<>();
        activities.forEach(a -> result.put(a, attivitaService.findStudentsFromActivity(a.getNome())));
        result.entrySet().forEach(e -> {
            RisultatiAtt r = new RisultatiAtt();
            r.setAttivita(e.getKey().getNome());
            r.setAnnoAcc(4047);
            e.getValue().forEach(v ->{
                List<Universitario> u = new ArrayList<>();
                u.add(v.universityStudent());
               r.addUniversitari(v.universityStudent());
            });
            res.add(r);
        });
        risultatiAttRepository.saveAll(res);
    }

    @Override
    public List<Risultati> getRisultati() {
        return this.risultatiRepository.findAll();
    }

    @Override
    public List<RisultatiAtt> getRisultatiAtt() {


        return this.risultatiAttRepository.findAll();
    }


    @Override
    public Risultati stampa() {
        List<Risultati> r = this.risultatiRepository.findAll();
        return r.get(0);
    }

    @Override
    public List<Risultati> getRisultatiAnno(int anno) {
        Query query = new Query();
        query.addCriteria(Criteria.where("annoAcc").is(anno));

        return this.mongoTemplate.find(query,Risultati.class);
    }

    @Override
    public void donloadResOnFile(String filename,int anno) {
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
        List<RisultatiAtt> risultati;
        if(anno==0){
            risultati = risultatiAttRepository.findAll();}
        else{
             risultati = risultatiAttRepository.findbyAnno(anno);
        }
        int j=0;

        for (int i = 0; i< risultati.size(); i++) {
            Row row0 = sheet.createRow(j);
            Cell cellAtt0 = row0.createCell(0);
            row0.createCell(0).setCellValue("Anno");
            j++;

            Row row = sheet.createRow(j);
            Cell cellAnno = row.createCell(0);
            // Impostazione dei valori delle celle
            cellAnno.setCellValue(risultati.get(i).getAnnoAcc());j++;
            // Creazione della prima riga
            Row row01 = sheet.createRow(j);
            row01.createCell(0).setCellValue("Matricola");
            row01.createCell(1).setCellValue("Nome");
            row01.createCell(2).setCellValue("Cognome");
            row01.createCell(3).setCellValue("AnnoImm");
            row01.createCell(4).setCellValue("Corso");
            row01.createCell(5).setCellValue("ComuneScuola");
            row01.createCell(5).setCellValue("ProvinciaScuola");
            for(int p=0;p<risultati.get(i).getUniversitarii().size();p++) {
                Universitario universitario = risultati.get(i).getUniversitarii().get(p);


                j++;
                Row row1 = sheet.createRow(j);
                Cell cellMatricola = row1.createCell(0);
                Cell cellNome = row1.createCell(1);
                Cell cellCognome = row1.createCell(2);
                Cell cellannoImm = row1.createCell(3);
                Cell cellCorso = row1.createCell(4);
                Cell cellcomuneScuola = row1.createCell(5);
                Cell cellscuolaProv = row1.createCell(6);

                cellMatricola.setCellValue(universitario.getMatricola());
                cellNome.setCellValue(universitario.getNome());
                cellCognome.setCellValue(universitario.getCognome());
                cellannoImm.setCellValue(universitario.getAnnoImm());
                cellCorso.setCellValue(universitario.getCorso());
                cellcomuneScuola.setCellValue(universitario.getComuneScuola());
                cellscuolaProv.setCellValue(universitario.getScuolaProv());
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

    public ResponseEntity<Object>  downloadFile(String name) throws FileNotFoundException {
        // Percorso del file sul tuo sistema
        String filePath = "C:/Users/user/IdeaProjects/PiattaformaPCTO-master-master/"+name; // Modifica il percorso del file

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
     * metodo che si occupa di eliminare il file precedentemenete creato nel filesystemdopo 3 secondi dal download
     * @param filePath nome del file
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


    private Scuola findScuola(String citta, String scuola){
        List<Scuola> scuole = scuolaRepository.getScuolaByCitta(citta);
        List<String> nomi = new ArrayList<>();
        for (Scuola s : scuole){
            nomi.add(s.getNome());
        }
        return  scuolaRepository.getScuolaByNome(findMostSimilarString(scuola,nomi));
    }



    public  String findMostSimilarString(String input, @org.jetbrains.annotations.NotNull List<String> strings) {
        String mostSimilarString = "";
        int minDistance = Integer.MAX_VALUE;
        for (String str : strings) {
            int distance = levenshteinDistance(input, str);
            if (distance < minDistance) {
                minDistance = distance;
                mostSimilarString = str;
            }
        }
        return mostSimilarString;
    }

    private  int levenshteinDistance(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();
        int[][] dp = new int[m+1][n+1];
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i-1) == s2.charAt(j-1)) {
                    dp[i][j] = dp[i-1][j-1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i-1][j-1], Math.min(dp[i-1][j], dp[i][j-1]));
                }
            }
        }
        return dp[m][n];
    }
    /**
     * Metodo che controlla se una scuola è presente all'interno della lista dei risultati
     *
     * @param res la lista dei risultati
     * @param id  l'id della scuola
     * @return l'indice a cui si trova nel caso sia presente, -1 nel caso non sia presente
     */
    private int scuoleHelperd(List<Risultati> res, String id) {
        if (res.size() == 0) {
            return -1;
        } else {
            for (int i = 0; i < res.size(); i++) {
                if (res.get(i).getScuola().getIdScuola().equals(id)) {
                    return i;
                }
            }
        }
        return -1;
    }


    /**
     * Metodo che controlla se una scuola è presente all'interno della lista dei risultati
     *
     * @param pres
     * @param nome
     * @return
     */
    private int attivitaHelper(List<Presenza> pres, String nome) {
        for (int i = 0; i < pres.size(); i++) {
            if (Objects.equals(pres.get(i).getNomeAttivita(), nome)) {
                
                return i;
            }
        }
        return -1;
    }


}
