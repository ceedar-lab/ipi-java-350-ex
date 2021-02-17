package com.ipiecoles.java.java350.service;
import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class EmployeServiceTest {

    @InjectMocks
    private EmployeService employeService;
    @Mock
    private EmployeRepository employeRepository;

    /** TP **/

    // Tests méthode calculPerformanceCommercial()
    @ParameterizedTest(name = "CA traité : {0}, objectif : {1}, perf actuelle : {2}, perf moyenne : {3} = nouvelle perf : {4}")
    @CsvSource({
            "1000, 10000, 10, 5.0, 1", // Performance de base
            "8000, 10000, 2, 5.0, 1", // Performance minimale
            "8000, 10000, 5, 15.0, 3", // Cas 2 && <= moyenne
            "9500, 10000, 5, 15.0, 5", // Cas 3 && <= moyenne
            "12000, 10000, 5, 15.0, 6", // Cas 4 && <= moyenne
            "70000, 10000, 5, 15.0, 9", // Cas 5 && <= moyenne
            "9400, 10000, 10, 5.0, 9", // Cas 2 && > moyenne
            "10500, 10000, 10, 5.0, 11", // Cas 3 && > moyenne
            "10550, 10000, 10, 5.0, 12", // Cas 4 && > moyenne
            "50000, 10000, 10, 5.0, 15" // Cas 5 && > moyenne
    })
    void testCalculPerformanceCommercial(Long caTraite, Long objectifCa, Integer performanceActuelle, Double performanceMoyenne, Integer nouvellePerformance) throws EmployeException {
        // Given
        String matricule = "C00001";
        Mockito.when(employeRepository.findByMatricule(matricule)).thenReturn(new Employe(
                "Doe", "John", matricule, LocalDate.now(), 1500d, performanceActuelle, 1d)
        );
        Mockito.when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(performanceMoyenne);

        // When
        employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);

        // Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        Mockito.verify(employeRepository).save(employeArgumentCaptor.capture());
        Employe employe = employeArgumentCaptor.getValue();

        Assertions.assertThat(employe.getPerformance()).isEqualTo(nouvellePerformance);
    }

    @ParameterizedTest(name = "Matricule : {0}, CA traité : {1}, objectif CA : {2}")
    @CsvSource({
            "'C00001', , 10000, Le chiffre d'affaire traité ne peut être négatif ou null !", // Erreur caTraite
            "'C00001', -10000, 10000, Le chiffre d'affaire traité ne peut être négatif ou null !",
            "'C00001', 10000, , L\'objectif de chiffre d'affaire ne peut être négatif ou null !", // Erreur objectCa
            "'C00001', 10000, -10000, L\'objectif de chiffre d'affaire ne peut être négatif ou null !",
            ", 10000, 10, Le matricule ne peut être null et doit commencer par un C !", // Erreur matricule
            "T00001, 10000, 10, Le matricule ne peut être null et doit commencer par un C !",
    })
    void testCalculPerformanceCommercialFail(String matricule, Long caTraite, Long objectifCa, String errorMessage) throws EmployeException {
        // Given, When, Then
        try {
            employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
            Assertions.fail("La méthode calculPerformanceCommercial() aurait dû lancer une exception.");
        } catch (EmployeException e) {
            Assertions.assertThat(e.getMessage()).isEqualTo(errorMessage);
        }
    }



    /** Cours **/
    @Test
    void testEmbauchePremierEmploye() throws EmployeException {
        //Given Pas d'employés en base
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        //Simuler qu'aucun employé n'est présent (ou du moins aucun matricule)
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        //Simuler que la recherche par matricule ne renvoie pas de résultats
        Mockito.when(employeRepository.findByMatricule("T00001")).thenReturn(null);
        Mockito.when(employeRepository.save(Mockito.any(Employe.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
        //Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
//        Mockito.verify(employeRepository, Mockito.times(1)).save(employeArgumentCaptor.capture());
        Mockito.verify(employeRepository).save(employeArgumentCaptor.capture());
        Employe employe = employeArgumentCaptor.getValue();
        Assertions.assertThat(employe).isNotNull();
        Assertions.assertThat(employe.getNom()).isEqualTo(nom);
        Assertions.assertThat(employe.getPrenom()).isEqualTo(prenom);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(1825.46);
        Assertions.assertThat(employe.getTempsPartiel()).isEqualTo(1.0);
        Assertions.assertThat(employe.getDateEmbauche()).isEqualTo(LocalDate.now());
        Assertions.assertThat(employe.getMatricule()).isEqualTo("T00001");
    }

    @Test
    void testEmbaucheLimiteMatricule() {
        //Given Pas d'employés en base
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        //Simuler qu'il y a 99999 employés en base (ou du moins que le matricule le plus haut
        //est X99999
        Mockito.when(employeRepository.findLastMatricule()).thenReturn("99999");
        //When
        try {
            employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
            Assertions.fail("embaucheEmploye aurait dû lancer une exception");
        } catch (EmployeException e) {
            //Then
            Assertions.assertThat(e.getMessage()).isEqualTo("Limite des 100000 matricules atteinte !");
        }
    }

    @Test
    void testEmbaucheEmployeExisteDeja() throws EmployeException {
        //Given Pas d'employés en base
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        Employe employeExistant = new Employe("Doe", "Jane", "T00001", LocalDate.now(), 1500d, 1, 1.0);
        //Simuler qu'aucun employé n'est présent (ou du moins aucun matricule)
        Mockito.when(employeRepository.findLastMatricule()).thenReturn(null);
        //Simuler que la recherche par matricule renvoie un employé (un employé a été embauché entre temps)
        Mockito.when(employeRepository.findByMatricule("T00001")).thenReturn(employeExistant);
        //When
        try {
            employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);
            Assertions.fail("embaucheEmploye aurait dû lancer une exception");
        } catch (Exception e){
            //Then
            Assertions.assertThat(e).isInstanceOf(EntityExistsException.class);
            Assertions.assertThat(e.getMessage()).isEqualTo("L'employé de matricule T00001 existe déjà en BDD");
        }
    }

}