package com.ipiecoles.java.java350.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

public class EmployeTest {

    @Test
    public void testGetAnneeAncienneteDateEmbaucheInfNow() {
        // Given
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().minusYears(5));

        // When
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();

        // Then
        Assertions.assertThat(anneeAnciennete).isGreaterThanOrEqualTo(5);
    }

    @Test
    public void testGetAnneeAncienneteDateEmbaucheSuppNow() {
        // Given
        Employe employe = new Employe();
        employe.setDateEmbauche(LocalDate.now().plusYears(5));

        // When
        Integer anneeAnciennete = employe.getNombreAnneeAnciennete();

        // Then
        Assertions.assertThat(anneeAnciennete).isNull();
    }

    @Test
    public void testGetNbAnneeAncienneteDateEmbaucheNull() {
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
            "1, 'T12345', 1.0, 5, 1500.0",
            "1, 'T12345', 0.5, 0, 500.0",
            "2, 'T12345', 1, 0, 2300.0",
    })
    public void testGetPrimeAnnuelle(Integer performance, String matricule, Double tauxActivite, Long nbAnneesAnciennete, Double result) {
        //Given, When, Then
        Employe employe = new Employe("Doe", "John", matricule, LocalDate.now().minusYears(nbAnneesAnciennete), 1500d, performance, tauxActivite);
        Assertions.assertThat(employe.getPrimeAnnuelle()).isEqualTo(result);
    }
}
