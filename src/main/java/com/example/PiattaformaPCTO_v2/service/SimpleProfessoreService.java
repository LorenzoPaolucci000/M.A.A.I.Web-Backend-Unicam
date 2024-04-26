package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.Attivita;
import com.example.PiattaformaPCTO_v2.collection.Professore;
import com.example.PiattaformaPCTO_v2.collection.Scuola;
import com.example.PiattaformaPCTO_v2.collection.Universitario;
import com.example.PiattaformaPCTO_v2.repository.ProfessoreRepository;
import com.example.PiattaformaPCTO_v2.repository.ScuolaRepository;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SimpleProfessoreService implements ProfessoreService{

    @Autowired
    private ProfessoreRepository professoreRepository;
    @Autowired
    private ScuolaRepository scuolaRepository;

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
            Scuola scuola = scuolaRepository.getScuolaById(prof.getScuolaImp());
            System.out.println(scuola.getNome());
            message += "<tr><th>"+prof.getNome()+"</th><th>"+prof.getCognome()+"</th><th>"+prof.getEmail()+"</th><th>"+prof.getAttivita()+"</th><th>"+scuola.getNome()+"</th><th>"+scuola.getCitta()+"</th><th>"+scuola.getRegione()+"</th></tr>";
        }
        message+="</table>";
        return message;
    }

    @Override
    public void createEmptyActivity(String nome, int anno, String nomeScuola, String cittaScuola) {
        // Crea un nuovo workbook Excel
        Workbook workbook = new XSSFWorkbook();
        // Crea un foglio di lavoro
        Sheet sheet = workbook.createSheet("Sheet1");
        // Percorso della cartella delle risorse
        String resourcesPath = "src/main/resources/";
        // Percorso completo della cartella "activity" nelle risorse
        String activityFolderPath = resourcesPath + "activity/";
        // Nome del file Excel
        String filename = nome+anno+".xlsx";
        // Percorso completo del file Excel
        String filePath = activityFolderPath + filename;
        // Assicurati che la cartella "activity" esista, altrimenti creala
        File activityFolder = new File(activityFolderPath);
        if (!activityFolder.exists()) {
            activityFolder.mkdirs();
        }
        try {
            // Crea un file di output
            FileOutputStream fileOut = new FileOutputStream(filePath);
            // Scrivi il workbook su file
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            insertScuolaOnFile(nomeScuola,cittaScuola,filePath);
        } catch (IOException e) {
            System.out.println("Si è verificato un errore durante la scrittura del file: " + e.getMessage());
        }

    }

    /**
     * metodo che inserisce la scuola e la città nella prima riga del file excel
     */
    private void insertScuolaOnFile(String scuola,String cittaScuola,String filePath){
        Workbook workbook = new XSSFWorkbook();
        // Crea un foglio di lavoro
        Sheet sheet = workbook.createSheet("Sheet1");
        // Crea la prima riga
        Row row = sheet.createRow(0);
        // Scrivi scuola nella prima colonna
        Cell cell1 = row.createCell(0);
        cell1.setCellValue(scuola);
        // Scrivi città scuola nella seconda colonna
        Cell cell2 = row.createCell(1);
        cell2.setCellValue(cittaScuola);
        try {
            // Crea un file di output
            FileOutputStream fileOut = new FileOutputStream(filePath);
            // Scrivi il workbook su file
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
        } catch (IOException e) {
            System.out.println("Si è verificato un errore durante la scrittura del file: " + e.getMessage());
        }

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
                Professore prof=new Professore(nome,cognome,email,scuola1.getIdScuola(),attivita);
                professoreRepository.save(prof);
            }
        }

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
