package com.example.PiattaformaPCTO_v2;

import com.example.PiattaformaPCTO_v2.collection.Immatricolazioni;
import com.example.PiattaformaPCTO_v2.collection.Universitario;
import com.example.PiattaformaPCTO_v2.repository.ImmatricolazioniRepository;
import com.example.PiattaformaPCTO_v2.repository.UniversitarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication

public class PiattaformaPctoV2Application {

	public static void main(String[] args) {

		SpringApplication.run(PiattaformaPctoV2Application.class, args);

	}



}
