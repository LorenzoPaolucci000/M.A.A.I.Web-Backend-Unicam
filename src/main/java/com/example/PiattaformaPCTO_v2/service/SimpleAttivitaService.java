package com.example.PiattaformaPCTO_v2.service;

import com.example.PiattaformaPCTO_v2.collection.*;
import com.example.PiattaformaPCTO_v2.dto.ActivityViewDTOPair;
import com.example.PiattaformaPCTO_v2.enumeration.Sede;
import com.example.PiattaformaPCTO_v2.repository.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class SimpleAttivitaService implements AttivitaService {

    @Autowired
    private AttivitaRepository attivitaRepository;
    /**
     * Universitario repository instance.
     */
    @Autowired
    private UniversitarioRepository universitarioRepository;
    @Autowired
    private ScuolaRepository scuolaRepository;
    @Autowired
    private StringFinderHelper stringFinderHelper;
    @Autowired
    private ScuolaService scuolaService;
    @Autowired
    private UniversitarioService universitarioService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private RisultatiAttRepository risAttRepository;
    @Autowired
    private ImmatricolazioniRepository immatricolazioniRepository;
    @Autowired
    private StudenteRepository studenteRepository;
    @Autowired
    private RisultatiAttRepository risultatiAttRepository;
    @Autowired
    private RisultatiRepository risultatiRepository;
    @Override
    public String save(Attivita attivita) {
        return attivitaRepository.save(attivita).getNome();
    }

    public void uploadConAnno(MultipartFile file,int anno,String name){


        List<Attivita> att=attivitaRepository.findByNomeAnno(name+anno,anno);
        Attivita attivita = new Attivita(name+anno,name, anno, new ArrayList<>());
        Sheet dataSheet = this.fileOpenerHelper(file);
        List<String> citta = this.scuolaService.getCitta();
        Iterator<Row> iterator = dataSheet.rowIterator();
         iterator.next();
        while(iterator.hasNext()){
            Row row = iterator.next();
            String c = row.getCell(3).getStringCellValue().toUpperCase();
            String trovata = this.stringFinderHelper.findMostSimilarString(c,citta);
            List<String> nomi = this.scuolaService.getNomi(trovata);
            String s =row.getCell(2).getStringCellValue();
            String sT= this.stringFinderHelper.findMostSimilarString(s,nomi);
            System.out.println("Data: "+s+" Trovata: "+sT);
            Scuola scuola = scuolaRepository.getScuolaByCittaAndNome(trovata,sT);
            String nome = row.getCell(0).getStringCellValue();
            String cognome = row.getCell(1).getStringCellValue();
            String email="ciao";

            Studente stud = new Studente(nome, cognome,email,scuola);
            attivita.getStudPartecipanti().add(stud);
            studenteRepository.save(stud);//aggiunto

        }

        if(!att.isEmpty()) {
            this.addElement(attivita, att.get(0));
            this.updateRisulAtt(attivita,anno);
        }
        else {
            System.out.println(this.save(attivita));
            this.createRisulAtt(attivita,anno);
        }
    }

    private void updateRisulAtt(Attivita attivita, int anno) {
        List<RisultatiAtt> risatt=risAttRepository.findByNomeAndAnno(attivita.getNome(),anno);
System.out.println(attivita.getNome());
        List<RisultatiAtt> elencoris=risAttRepository.findAll();
        elencoris.remove(risatt.get(0));
       risAttRepository.deleteAll();
    risatt.get(0).addUniversitari(this.checkUniversitario(attivita.getStudPartecipanti(),anno));
    elencoris.add(risatt.get(0));
    this.risAttRepository.saveAll(elencoris);

    }

    /**
     * metodo che crea il risultato att
     * @param attivita
     */
    private void createRisulAtt(Attivita attivita,int anno){

        RisultatiAtt risultatiAtt=new RisultatiAtt();
        risultatiAtt.setAttivita(attivita.getNome());
        risultatiAtt.setAnnoAcc(attivita.getAnnoAcc());
        risultatiAtt.addUniversitari(this.checkUniversitario(attivita.getStudPartecipanti(),anno));
        this.risAttRepository.save(risultatiAtt);
    }


    /**
     *
     * metodo che data una lista di studenti partecipanti a quell'attività ritorna la lista degli studenti che sono
     * diventati studenti universitari
     * @param stud
     * @return
     */

    private List<Universitario> checkUniversitario(List<Studente> stud,int anno) {
        List<Universitario> universitari=new ArrayList<>();

//Query query=new Query();
//query.addCriteria(Criteria.where("universitari.nome").is("ALBERTO").and("universitari.cognome").is("POL"));
//System.out.println( mongoTemplate.find(query, Iscrizione.class));
    for(int i=0;i<stud.size();i++){

        universitari.add(universitarioRepository.findByNomeAndCognome(stud.get(i).getNome(),stud.get(i).getCognome()));
    }



        return universitari;
    }

    /**
     *  metodo che aggiunge gli elementi inseriti nell'attività presente
     * @param a1
     * @param a2
     */

    private void addElement(Attivita a1,Attivita a2){
        List<Attivita> attivitaList=attivitaRepository.findAll();
        attivitaList.remove(a2);
        this.attivitaRepository.deleteAll();
        for(int i=0;i<a1.getStudPartecipanti().size();i++){
            a2.getStudPartecipanti().add(a1.getStudPartecipanti().get(i));
        }
        attivitaList.add(a2);
        this.attivitaRepository.saveAll(attivitaList);
    }




    /**
     * Find information about students that chose UNICAM and their high school, given an activity.
     *
     * @return list of activity view pairs
     */

    @Override
    public List<ActivityViewDTOPair> findStudentsFromActivity(String activityName) {
        List<ActivityViewDTOPair> result = new ArrayList<>();
        Attivita activity = this.attivitaRepository.findByNome(activityName);
        System.out.println(activity.getNome());
        System.out.println("prova");
        if(activity.getNome().equals("CONTEST_INFORMATICA_X_GIOCO_4043")){
            System.out.println("qua");
            activity.getStudPartecipanti().forEach(s -> {
                List<Immatricolazioni> i = this.universitarioService.getIscrizioniAnno(4047);
                System.out.println(i.size());
                for (Universitario un : i.get(0).getUniversitari()){
                    if(un.getNome().equals(s.getNome().toUpperCase())){
                        if (un.getCognome().equals(s.getCognome().toUpperCase())){
                            result.add(new ActivityViewDTOPair(un,this.findScuola(un.getComuneScuola(),un.getScuolaProv())));
                        }
                    }
                }

            });
        }else {
            activity.getStudPartecipanti().forEach(s -> {
                List<Immatricolazioni> i = this.universitarioService.getIscrizioniAnno(4047);
                for (Universitario un : i.get(0).getUniversitari()){
                    if(un.getNome().equals(s.getNome().toUpperCase())){
                        if (un.getCognome().equals(s.getCognome().toUpperCase())){
                            if (un.getComuneScuola().equals(s.getScuola().getCitta().toUpperCase())){
                                result.add(new ActivityViewDTOPair(un, this.scuolaRepository.getScuolaByNomeContainingAndAndCitta(
                                        s.getScuola().getNome(), un.getComuneScuola())));
                            }
                        }
                    }
                }

            });
        }
        return result;
    }

    @Override
    public List<Attivita> getAttivita(int anno) {
        int min= anno-6;
        Query query = new Query();
        query.addCriteria(Criteria.where("annoAcc").gte(min).lt(anno));
        return mongoTemplate.find(query,Attivita.class);
    }

    @Override
    public void prova() {
        List<Attivita> a = this.getAttivita(4045);
        System.out.println(a.size());
    }

    @Override
    public void uploadSingleActivity(String nome, String tipo, String scuola, int anno, Sede sede, LocalDateTime dataInizio, LocalDateTime dataFine, String descrizione, List<ProfessoreUnicam> prof, Professore profReferente, MultipartFile file) {
       System.out.println(scuola);
        Sheet dataSheet = this.fileOpenerHelper(file);
        Iterator<Row> iterator = dataSheet.rowIterator();
        iterator.next();
        Scuola scuolaP=scuolaRepository.getScuolaByNome(scuola);
        List<Studente> studPartecipanti=new ArrayList<>();

        while(iterator.hasNext()){
            Row row = iterator.next();
            String nomeStud = row.getCell(0).getStringCellValue();
            String cognome = row.getCell(1).getStringCellValue();
            String email=row.getCell(2).getStringCellValue();
            Studente stud = new Studente(nomeStud, cognome,email,scuolaP);

          studPartecipanti.add(stud);
            if(studenteRepository.findByEmail(stud.getEmail())==null) {
                studenteRepository.save(stud);
            }
        }

        if(attivitaRepository.findByNomeAndAnno(nome,anno)==null){
            Attivita attivita=new Attivita(nome,tipo,anno,studPartecipanti,sede,dataInizio,dataFine,descrizione,prof,profReferente,false);
            attivita.setScuola(scuola);
            attivitaRepository.save(attivita);
            createRisulataiAtt(attivita);
            attivita.setScuola(scuola);
            System.out.println(scuolaP.getIdScuola());

            if(!scuola.isEmpty()) {
                attivita.setScuola(scuola);
                createRisultati(attivita);
            }
        }

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
                System.out.println(risultatiRepository.findAll().size());
            }
            else{
                List<Universitario> universitario=risultatiRepository.findByScuolaId(scuola.getIdScuola()).get(0).getIscritti();
                List<Presenza> presenze=risultatiRepository.findByScuolaId(scuola.getIdScuola()).get(0).getAttivita();
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












    private Scuola findScuola(String citta, String scuola){
        List<Scuola> scuole = scuolaRepository.getScuolaByCitta(citta);
        List<String> nomi = new ArrayList<>();
        for (Scuola s : scuole){
            nomi.add(s.getNome());
        }
        return  scuolaRepository.getScuolaByNome(findMostSimilarString(scuola,nomi));
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



    private  String findMostSimilarString(String input, @org.jetbrains.annotations.NotNull List<String> strings) {
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





}
