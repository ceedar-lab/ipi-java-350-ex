package com.ipiecoles.java.java350.model;

import com.ipiecoles.java.java350.exception.EmployeException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

public class EmployeTest {


    /**********  TP  **********/

    // Tests méthode getNbRtt()
    @ParameterizedTest(name = "A la date du {0}/{1}/{2} avec un taux de travail de {3} = {4} jours de RTT")
    @CsvSource({
            "1, 1, 2016, 1.0, 9", // 2016
            "12, 7, 2016, 1.0, 9",
            "12, 7, 2016, 0.5, 5",
            "1, 1, 2019, 1.0, 8", // 2019
            "25, 10, 2019, 1.0, 8",
            "25, 10, 2019, 0.5, 4",
            "1, 1, 2021, 1.0, 10", // 2021
            "12, 6, 2021, 1.0, 10",
            "12, 6, 2021, 0.5, 5",
            "1, 1, 2022, 1.0, 10", // 2022
            "24, 8, 2022, 1.0, 10",
            "24, 8, 2022, 0.5, 5",
            "1, 1, 2025, 1.0, 8", // 2025
            "9, 12, 2025, 1.0, 8",
            "9, 12, 2025, 0.5, 4",
            "1, 1, 2032, 1.0, 11", // 2032
            "29, 2, 2032, 1.0, 11",
            "29, 2, 2032, 0.5, 6"
    })
    void testGetNbRttCasNominaux(Integer day, Integer month, Integer year, Double tempsPartiel, Integer nbRtt) {
        Employe employe = new Employe();
        employe.setTempsPartiel(tempsPartiel);
        Assertions.assertThat(employe.getNbRtt(LocalDate.of(year, month, day))).isEqualTo(nbRtt);
    }

    // Tests méthode augmenterSalaire()
    @ParameterizedTest(name = "Salaire : {0}, augmentation : {1} -> nouveau salaire : {2}")
    @CsvSource({
            "1000.0, 0.0, 1000.0",
            "1000.0, 0.75, 1750.0",
            "1500.0, 1.0, 3000.0",
            "1500.0, 0.15, 1725.0",
            "2150.0, -0.0, 2150.0",
            "2150.0, 1.50, 5375.0"
    })
    void testAugmenterSalaireCasNominaux(Double ancienSalaire, Double augmentation, Double nouveauSalaire) throws EmployeException {
        //Given, When, Then
        Employe employe = new Employe();
        employe.setSalaire(ancienSalaire);
        employe.augmenterSalaire(augmentation);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(nouveauSalaire);
    }

    @ParameterizedTest(name = "Salaire : {0}, augmentation : {1} -> nouveau salaire : {2}")
    @CsvSource({
            "1253.25, 0.33, 1666.82",
            "2169.75, 0.66, 3601.79",
            "2546.1557, 0.31, 3335.46",
            "1234.1234, 0.02, 1258.81",
            "2046.1546, 1.0034, 4099.27",
            "1342.1564, 0.6481, 2212.01"

    })
    void testAugmenterSalaireCasLimites(Double ancienSalaire, Double augmentation, Double nouveauSalaire) throws EmployeException {
        //Given, When, Then
        Employe employe = new Employe();
        employe.setSalaire(ancienSalaire);
        employe.augmenterSalaire(augmentation);
        Assertions.assertThat(employe.getSalaire()).isEqualTo(nouveauSalaire);
    }

    @Test
    void testAugmentationSalaireFail() {
        // Given
        Employe employe = new Employe();
        employe.setSalaire(1500d);
        Double augmentation = -0.01;

        // When
        try {
            employe.augmenterSalaire(augmentation);
            Assertions.fail("La méthode augmenterSalaire() aurait dû lancer une exception");
        } catch (EmployeException e) {
            //Then
            Assertions.assertThat(e.getMessage()).isEqualTo("Le taux d'augmentation ne peut être négatif");
        }
    }


    /**********  Cours  **********/

    // Tests méthode equals()
    @Test
    void testEmployeesAreEqual() {
        // Given, When, Then
        Employe employe1 = new Employe("Doe", "John", "C00001", LocalDate.now(), 1500d, 5, 1d);
        Employe employe2 = new Employe("Doe", "John", "C00001", LocalDate.now(), 1500d, 5, 1d);

        Assertions.assertThat(employe1).isInstanceOf(Employe.class);
        Assertions.assertThat(employe1).isEqualTo(employe2);
    }

    @Test
    void testEmployeesAreNotEqual() {
        // Given, When, Then
        Employe employe1 = new Employe("Doe", "John", "C00001", LocalDate.now(), 1500d, 5, 1d);
        Employe employe2 = new Employe("Doe", "Jane", "C00001", LocalDate.now(), 1500d, 5, 1d);

        Assertions.assertThat(employe1).isNotEqualTo(employe2);
    }

    @Test
    void testGetAnneeAncienneteDateEmbaucheInfNow() {
        // Given
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().minusYears(5));

        // When
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();

        // Then
        Assertions.assertThat(anneeAnciennete).isGreaterThanOrEqualTo(5);
    }

    @Test
    void testGetAnneeAncienneteDateEmbaucheSuppNow() {
        // Given
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().plusYears(5));

        // When
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();

        // Then
        Assertions.assertThat(anneeAnciennete).isNull();
    }

    @Test
    void testGetNbAnneeAncienneteDateEmbaucheNull() {
        //Given
        Employe employe = new Employe();
        employe.setDateEmbauche(null);

        //When
        Integer nbAnneeAnciennete = employe.getNombreAnneeAnciennete();

        //Then
        Assertions.assertThat(nbAnneeAnciennete).isNull();
    }

    @ParameterizedTest(name = "La prime pour le matricule {1}, {3} an(s) d'ancienneté, performance de {0} et un taux d'activité de {2} est de {4} euros")
    @CsvSource({
            "1, 'T12345', 1.0, 0, 1000.0",
            "1, 'T12345', 0.5, 2, 600.0",
            "1, 'T12345', 1.0, 2, 1200.0",
            "2, 'T12345', 1.0, 0, 2300.0",
            "2, 'T12345', 1.0, 1, 2400.0",
            "1, 'M12345', 1.0, 0, 1700.0",
            "1, 'M12345', 1.0, 5, 2200.0",
            "2, 'M12345', 1.0, 0, 1700.0",
            "2, 'M12345', 1.0, 8, 2500.0"
    })
    void testGetPrimeAnnuelle(Integer performance, String matricule, Double tauxActivite, Long nbAnneesAnciennete, Double result) {
        //Given, When, Then
        Employe employe = new Employe("Doe", "John", matricule, LocalDate.now().minusYears(nbAnneesAnciennete), 1500d, performance, tauxActivite);
        Assertions.assertThat(employe.getPrimeAnnuelle()).isEqualTo(result);
    }
}
