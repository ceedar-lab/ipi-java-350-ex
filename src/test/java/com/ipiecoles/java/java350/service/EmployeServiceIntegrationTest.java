package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class EmployeServiceIntegrationTest {

    @Autowired
    private EmployeService employeService;
    @Autowired
    private EmployeRepository employeRepository;

    @BeforeEach
    @AfterEach
    public void purge(){
        employeRepository.deleteAll();
    }


    /**********  TP  **********/

    // Test méthode calculPerformanceCommercial()
    @Test
    void testCalculPerformanceCommercialSuppMoyenne() throws EmployeException {
        // Given
        String matricule = "C00001";
        Employe employe = new Employe("Doe", "John", matricule, LocalDate.now(), 1500d, 10, 1d);
        employeRepository.save(employe);
        Long caTraite = 50000L;
        Long objectifCa = 10000L;

        // When
        employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

        // Then
        Assertions.assertThat(employeRepository.findByMatricule(matricule).getPerformance()).isEqualTo(15);
    }

    @Test
    void testCalculPerformanceCommercialInfMoyenne() throws EmployeException {
        // Given
        String matricule = "C00001";
        Employe employe = new Employe("Doe", "John", matricule, LocalDate.now(), 1500d, 10, 1d);
        Employe employe2 = new Employe("Doe", "Jane", "C00002", LocalDate.now(), 1500d, 50, 1d);
        employeRepository.save(employe);
        employeRepository.save(employe2);
        Long caTraite = 50000L;
        Long objectifCa = 10000L;

        // When
        employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

        // Then
        Assertions.assertThat(employeRepository.findByMatricule(matricule).getPerformance()).isEqualTo(14);
    }


    /**********  Cours  **********/

    @Test
    void testEmbauchePremierEmploye() throws EmployeException {
        // Given Pas d'employés en base
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;

        // When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
        
        // Then
        Employe employe = employeRepository.findByMatricule("T00001");
        Assertions.assertThat(employe).isNotNull();
        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.46);
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(1.0);
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getMatricule()).isEqualTo("T00001");
    }
}