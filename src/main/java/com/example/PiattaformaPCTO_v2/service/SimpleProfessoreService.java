package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.*;
import com.example.PiattaformaPCTO_v2.enumeration.Sede;
import com.example.PiattaformaPCTO_v2.repository.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
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
            System.out.println(scuola.getNome());
            message += "<tr><th>"+prof.getNome()+"</th><th>"+prof.getCognome()+"</th><th>"+prof.getEmail()+"</th><th>"+prof.getAttivita()+"</th><th>"+scuola.getNome()+"</th><th>"+scuola.getCitta()+"</th><th>"+scuola.getRegione()+"</th></tr>";
        }
        message+="</table>";
        return message;
    }






    @Override
    public void createEmptyActivity(String nome, String tipo, String scuola, int anno,Sede sede, LocalDateTime dataInizio, LocalDateTime dataFine
            , String descrizione, List<ProfessoreUnicam> profUnicam, Professore profReferente) {

      Attivita attivita=new Attivita(nome,tipo,anno,new ArrayList<>(),sede,dataInizio,dataFine,descrizione,profUnicam,profReferente,true);


      attivitaRepository.save(attivita);
    }





    @Override
    public void uploadActivityDefinitively(String nome) throws IOException {
        int anno =Integer.parseInt(nome.substring(nome.length() - 4));
        String nomeA=nome.substring(0,nome.length() - 4);
        List<Attivita> list=new ArrayList<>();
        list.addAll(attivitaRepository.findAll());
        Attivita attivita=attivitaRepository.findByNomeAndAnno(nomeA,anno);
        list.remove(attivita);
        attivitaRepository.deleteAll();
        list.add(attivita);
        attivita.setIscrizione(false);
        attivitaRepository.saveAll(list);
       createRisulataiAtt(attivita);
    }

    /**
     * metodo che crea la vista dei risultati di quella attività va a vedere gli studenti che sono diventati universitari
     * @param attivita
     */
    private void  createRisulataiAtt(Attivita attivita){
        RisultatiAtt risultatiAtt=new RisultatiAtt();
        risultatiAtt.setAnnoAcc(attivita.getAnnoAcc());
        risultatiAtt.setAttivita(attivita.getNome());
        List<Universitario> universitarioList=new ArrayList<>();
for(int i=0;i<attivita.getStudPartecipanti().size();i++){
    Studente stud=attivita.getStudPartecipanti().get(i);
    Universitario universitario=universitarioRepository.findByNomeAndCognomeAndComuneScuola(stud.getNome(),stud.getCognome(),
            stud.getScuola().getCitta(),stud.getScuola().getNome());
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
        // Recupera la lista delle attività pendenti dalla directory monitorata
        List<String> classNames = new ArrayList<>();
        try {

            Path directoryPath = Paths.get("src/main/resources/activity/");
            File directory = directoryPath.toFile();
            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isFile()) {
                            String fileName = file.getName();
                            if (fileName.endsWith(".xlsx")) {
                                String className = fileName.substring(0, fileName.lastIndexOf('.'));
                                classNames.add(className);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classNames;
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
    public void uploadSingleProf(String email, String nome, String cognome, Scuola scuola, String attività) {
        System.out.println(scuolaRepository.getScuolaById(scuola.getIdScuola())==null);
        if(professoreRepository.getProfByEmail(email)==null&&scuolaRepository.getScuolaById(scuola.getIdScuola())!=null){
            professoreRepository.save(new Professore(nome,cognome,email,scuola,attività));
        }
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
/*
    @Override
    public String upload()  {
        int counter = 0;
        XSSFSheet regioni;
        XSSFWorkbook wbRegioni = null;
        try{
            FileInputStream fisRegioni = new FileInputStream(new File("src/main/resources/comuni_regioni.xlsx"));
            wbRegioni = new XSSFWorkbook(fisRegioni);

        }catch(Exception e){}
        regioni = wbRegioni.getSheetAt(0);
        //file da dove prendo i dati
        String[] files = new String[]{"Docenti_attivita.xlsx","Progetto-NERD-2021-2022.xlsx"};
        Set<Professore> professori = new HashSet<Professore>();
        //Docenti attivita
        XSSFSheet sheet = null;
        try{
            FileInputStream fis = new FileInputStream(new File("src/main/resources/Docenti_attivita.xlsx"));
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            sheet = wb.getSheetAt(0);

        }catch(FileNotFoundException e){
            System.out.println("Errore file non trovato !!");
        } catch (IOException e) {
            System.out.println("Errore Workbook!!");
        }
        for(Row row:sheet) {
            String nome = "";
            String cognome = "";
            String email = "";
            String scuolaImp = "";
            String citta= "";
            String attivita="";
            for (int i=0; i<5;i++){
                switch (i){
                    case 0:
                        attivita = row.getCell(0).getStringCellValue();
                        break;
                    case 1:
                        try {
                            String[] tmp = row.getCell(1).getStringCellValue().split(" ",2);
                            if(tmp.length>1){
                                nome = tmp[0];
                                cognome = tmp[1];
                            }else{
                                nome = tmp[0];
                            }
                            if(Utilities.isEmail(nome))
                                nome = "";
                        }catch (NullPointerException e) {
                            continue;
                        }
                        break;
                    case 2:
                        try {
                            email = row.getCell(2).getStringCellValue();
                            email = email.toLowerCase();
                            if(!Utilities.isEmail(email)){
                                email = "";
                            }
                        }catch (NullPointerException e) {
                            continue;
                        }
                        break;
                    case 3:
                        try {
                            scuolaImp = row.getCell(3).getStringCellValue();
                            if(Utilities.isEmail(scuolaImp))
                                scuolaImp = "";
                        }catch (NullPointerException e) {
                            continue;
                        }
                        break;
                    case 4:
                        try {
                            citta = row.getCell(4).getStringCellValue();
                            if(Utilities.isEmail(citta))
                                citta = "";
                        }catch (NullPointerException e) {
                            continue;
                        }
                        break;
                }
            }

            //Se nome o email sono vuote salto la riga
            if (nome.isEmpty() || email.isEmpty())
                continue;
            //controllo presenza provincia
            if(citta.contains("(")){
                boolean open = false;

                int cat = citta.length()-1;
                int i = cat;
                do{
                    //rimuovo spazi vuoti in coda
                    if(citta.charAt(cat)==' '){
                        cat--;
                        i--;
                    }
                    if(citta.charAt(cat)==')'){
                        cat--;
                        i--;
                        open = true;
                    }
                    if(citta.charAt(cat)=='('){
                        cat--;
                        i--;
                        open=false;
                    }
                    if(open){
                        cat--;
                        i--;
                    }
                    i--;
                }while(i>=0);
                citta = citta.substring(0,cat);
                if(citta.length()>0){
                    String low = citta.substring(1);
                    char upper = citta.charAt(0);
                    low = low.toLowerCase();
                    upper = Character.toUpperCase(upper);
                    citta = upper+low;
                }
            }
            if(Utilities.isEmail(nome) && !Utilities.isEmail(email)){
                String tmp = email;
                email = nome;
                nome = tmp;
            }
            Professore prof = new Professore(nome,cognome, email, findSchoolId(scuolaImp,citta),attivita);
            //professori.add(prof);
            this.save(prof);
            counter++;
        }
        //Professore[] profs = professori.toArray(new Professore[professori.size()]);

        return "Caricati "+counter+" professori";
    }



    private String findSchoolId(String scuola,String citta){
        //converto tutto in maiuscolo
        citta = citta.toUpperCase();
        scuola = scuola.toUpperCase();
        //Prendo le scuole che si trovano nella stessa citta
        List<Scuola> scuole = scuolaRepository.getScuolaByCitta(citta);
        if(scuole.size()==0){
            scuole= scuolaRepository.findAll();
        }
            List<String> nomiScuole = new ArrayList<>();
            for (Scuola s:scuole) {
                nomiScuole.add(s.getNome());
            }
            ScuolaHelperService helper= new ScuolaHelperService();
            String mostSimilarScuola = helper.findMostSimilarString(scuola,nomiScuole);
            Iterator<Scuola> it = scuole.iterator();
            while(it.hasNext()){
                Scuola tmp = it.next();
                if(tmp.getNome().equals(mostSimilarScuola))
                    return tmp.getIdScuola();
            }


        return "";
    }


*/
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
