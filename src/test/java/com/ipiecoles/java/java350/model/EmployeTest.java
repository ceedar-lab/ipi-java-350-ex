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
}
