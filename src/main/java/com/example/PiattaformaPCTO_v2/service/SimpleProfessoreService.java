package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.*;
import com.example.PiattaformaPCTO_v2.enumeration.Sede;
import com.example.PiattaformaPCTO_v2.repository.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SimpleProfessoreService implements ProfessoreService{

    @Autowired
    private ProfessoreRepository professoreRepository;
    @Autowired
    private ScuolaRepository scuolaRepository;
    @Autowired
    private AttivitaService attivitaService;
    @Autowired
    private AttivitaRepository attivitaRepository;

    @Autowired
    private UniversitarioRepository universitarioRepository;
    @Autowired
    private RisultatiAttRepository risultatiAttRepository;
    @Autowired
    private RisultatiRepository risultatiRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public String save(Professore professore) {
        return professoreRepository.save(professore).getEmail();
    }

    @Override
    public String stampa(){
        List<Professore> profs = professoreRepository.findAll();
        String message = "MESSAGGIO:<br> Lunghezza: "+ profs.size()+"<br>";
        message+="<table>";
        message+="<tr><th>Nome</th><th>Cognome</th><th>Email</th><th>Attivita</th><th>Scuola</th><th>Citta</th><th>Regione</th></tr>";
        for (Professore prof:profs) {
            Scuola scuola = scuolaRepository.getScuolaById(prof.getScuolaImp().getIdScuola());

            message += "<tr><th>"+prof.getNome()+"</th><th>"+prof.getCognome()+"</th><th>"+prof.getEmail()+"</th><th>"+prof.getAttivita()+"</th><th>"+scuola.getNome()+"</th><th>"+scuola.getCitta()+"</th><th>"+scuola.getRegione()+"</th></tr>";
        }
        message+="</table>";
        return message;
    }






    @Override
    public void createEmptyActivity(String nome, String tipo, String scuola, int anno,Sede sede, LocalDateTime dataInizio, LocalDateTime dataFine
            , String descrizione, List<ProfessoreUnicam> profUnicam, Professore profReferente) {

if(attivitaRepository.findByNomeAnno(nome,anno).isEmpty()) {
    Attivita attivita = new Attivita(nome, tipo, anno, new ArrayList<>(), sede, dataInizio, dataFine, descrizione, profUnicam, profReferente, true);

    attivita.setScuola(scuola);


    attivitaRepository.save(attivita);
}
    }





    @Override
    public void uploadActivityDefinitively(String nome) throws IOException {

        int anno =Integer.parseInt(nome.substring(nome.lastIndexOf(" ")+1,nome.length()));
        String nomeA=nome.substring(0,nome.lastIndexOf(" "));

        Attivita attivita=attivitaRepository.findByNomeAndAnno(nomeA,anno);
        Query query = new Query();
        query.addCriteria(Criteria.where("nome").is(nomeA).and("annoAcc").is(anno));
        Update update = new Update();
        update.set("iscrizionePossibile", false);
        mongoTemplate.updateFirst(query, update, Attivita.class);
       createRisulataiAtt(attivita);
       createRisultati(attivita);
    }

    /**
     * metodo che da un attività crea la vista sulle scuola
     * @param attivita
     */
    private void createRisultati(Attivita attivita){
        Presenza presenza=createPresenza(attivita);

        List<Risultati> risultati=risultatiRepository.findAll();





        //se l'attività ha una scuola in cui si è svolta
        if(!attivita.getScuola().equals("")){
            Scuola scuola=scuolaRepository.getScuolaByNome(attivita.getScuola());
            Query query = new Query();
            query.addCriteria(Criteria.where("scuola").is(scuola));
            Scuola scuola1=null;
            for(int i=0;i< risultati.size();i++){
                if(risultati.get(i).getScuola().getIdScuola().equals(scuola.getIdScuola())&&
                        risultati.get(i).getAnnoAcc()==attivita.getAnnoAcc()){
                    scuola1=scuola;
                }
            }
                //se la scuola non ha altre attività fatte creao un nuovo risultati
                if(scuola1==null) {
                    Risultati risultato = new Risultati(attivita.getAnnoAcc(), scuola);
                    risultato.addAttivita(presenza);
                    risultato.addIscritti(presenza.getIscritti());
                    risultatiRepository.save(risultato);

                }
                else{
                    List<Presenza> presenze=risultatiRepository.findByScuolaId(scuola.getIdScuola()).get(0).getAttivita();
                    List<Universitario> universitario=risultatiRepository.findByScuolaId(scuola.getIdScuola()).get(0).getIscritti();
                    universitario.addAll(presenza.getIscritti());
                    presenze.add(presenza);
                    Query query1 = new Query();
                    query1.addCriteria(Criteria.where("scuola").is(scuola));
                    Update update = new Update();
                    update.set("attivita", presenze);
                    update.set("iscritti",universitario);
                    mongoTemplate.updateFirst(query1, update, Risultati.class);
                }



        }
    }

    /**
     * metodo che crea la presenza
     * @param attivita
     */
    private Presenza createPresenza(Attivita attivita) {

      List<Universitario> universitari=risultatiAttRepository.findbyNomeAttivita(attivita.getNome()).get(0).getUniversitarii();
      Presenza presenza=new Presenza(attivita.getNome());
      presenza.setTipo(attivita.getTipo());
      presenza.addPartecipanti(attivita.getStudPartecipanti());
      presenza.addIscritti(universitari);
      return presenza;


    }


    /**
     * metodo che crea la vista dei risultati di quella attività va a vedere gli studenti che sono diventati universitari
     * @param attivita
     */
    private void  createRisulataiAtt(Attivita attivita){
        RisultatiAtt risultatiAtt=new RisultatiAtt();
        risultatiAtt.setAnnoAcc(attivita.getAnnoAcc());
        risultatiAtt.setAttivita(attivita.getNome());
        risultatiAtt.setTipo(attivita.getTipo());
        List<Universitario> universitarioList=new ArrayList<>();
for(int i=0;i<attivita.getStudPartecipanti().size();i++){
    Studente stud=attivita.getStudPartecipanti().get(i);
    Universitario universitario=universitarioRepository.findByNomeAndCognome(stud.getNome(),stud.getCognome());
    if(universitario!=null){
       risultatiAtt.addUniversitari(universitario);
    }
}
risultatiAttRepository.save(risultatiAtt);
}


    @Override
    public void uploadConFile(MultipartFile file){
        Sheet dataSheet = this.fileOpenerHelper(file);
        Iterator<Row> iterator = dataSheet.rowIterator();
        iterator.next();

        while (iterator.hasNext()){
            Row row = iterator.next();
            if(row.getCell(0)==null)break;
            String nome=row.getCell(0).getStringCellValue();
            String cognome=row.getCell(1).getStringCellValue();
            String attivita=row.getCell(2).getStringCellValue();
            String scuola=row.getCell(3).getStringCellValue();
            String cittascuola=row.getCell(4).getStringCellValue();
            String email=row.getCell(5).getStringCellValue();


            Scuola scuola1 =scuolaRepository.getScuolaByCittaAndNome(cittascuola,scuola);
            System.out.println(checkactivity(nome,cognome,attivita));
            if(scuolaRepository.getScuolaByCittaAndNome(cittascuola,scuola)!=null&&
            !checkactivity(nome,cognome,attivita)) {
                Professore prof=new Professore(nome,cognome,email,scuola1,attivita);
                professoreRepository.save(prof);
            }
        }

    }


    public List<String> getAllPendingActivities() {
        // Recupera la lista delle attività pendenti
        List<String> activity = new ArrayList<>();
        List<Attivita> activityPending=attivitaRepository.findByIscrizione(true);
        for(int i=0;i<activityPending.size();i++){
            activity.add(activityPending.get(i).getNome()+" "+activityPending.get(i).getAnnoAcc());
        }
   return activity;

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
        List<Professore> professori=professoreRepository.findAll();
        Row row0 = sheet.createRow(0);
        row0.createCell(0).setCellValue("Email");
        row0.createCell(1).setCellValue("Nome");
        row0.createCell(2).setCellValue("Cognome");
        row0.createCell(3).setCellValue("Scuola");
        row0.createCell(4).setCellValue("Attività");
        for (int i=0;i< professori.size();i++) {
            // Creazione della prima riga
            Row row = sheet.createRow(i+1);
            Cell cellEmail = row.createCell(0);
            Cell cellNome = row.createCell(1);
            Cell cellCognome = row.createCell(2);
            Cell cellScuola = row.createCell(3);
            Cell cellAttivita = row.createCell(4);
            // Impostazione dei valori delle celle
            cellEmail.setCellValue(professori.get(i).getEmail());
            cellNome.setCellValue(professori.get(i).getNome());
            cellCognome.setCellValue(professori.get(i).getCognome());
            cellScuola.setCellValue(professori.get(i).getScuolaImp().getIdScuola());
            cellAttivita.setCellValue(professori.get(i).getAttivita());
        }

        try (FileOutputStream outputStream = new FileOutputStream(filename)) {
            workbook.write(outputStream);

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

    @Override
    public void uploadSingleProf( String nome, String cognome,String email, String scuola,String cittaScuola, String attività) {
Scuola scuola1=scuolaRepository.getScuolaByCittaAndNome(cittaScuola.toUpperCase(),scuola);

        if(professoreRepository.getProfByEmail(email)==null&&scuola!=null){
            professoreRepository.save(new Professore(nome,cognome,email,scuola1,attività));
        }
    }

    @Override
    public Professore getProfByString(String prof) {
        Professore profReferente;
        List<String> parametri=separa(prof);
        return professoreRepository.getNomeCognome(parametri.get(0),parametri.get(1));
    }

    private List<String> separa(String s) {
        List<String> parole=new ArrayList<>();
        for(int i=0;i<3;i++) {
            String p=s;
            if(i<2) {
                parole.add(s.substring(0, s.indexOf(" ")));
            }
            else{
                parole.add(s);
            }
            s = s.substring(s.indexOf(" ")+1,s.length());

        }

        return parole;
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


    /**
     * metodo che controlla se lo stesso professore fa già la stessa attivitò per evitare duplicati
     * @return true se fa la stessa attività
     */
   private boolean checkactivity(String nome,String cognome,String attivita){
return professoreRepository.getProfessoreByNomeCognomeAttivita(nome,cognome,attivita)!=null;

   }

    @Override
    public List<Professore> getAllProf() {
        List<Professore> p = this.professoreRepository.findAll();
        return p;
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
