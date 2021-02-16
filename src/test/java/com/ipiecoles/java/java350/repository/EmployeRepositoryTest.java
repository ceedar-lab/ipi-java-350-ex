package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.model.Employe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {Java350Application.class})
//@DataJpaTest
@DataJpaTest
class EmployeRepositoryTest {

    @Autowired
    EmployeRepository employeRepository;

    @BeforeEach
    @AfterEach
    public void purge(){
        employeRepository.deleteAll();
    }

    @Test
    void test() {
        // Given
        employeRepository.save(new Employe("Doe", "John", "T12345", LocalDate.now(), 1500d, 1, 1.0));

        // When
        String employeMatricule = employeRepository.findLastMatricule();

        // Then
        Assertions.assertThat(employeMatricule).isEqualTo("12345");
    }

    @Test
    void test2() {
        // Given
        employeRepository.save(new Employe());

        // When
        String employeMatricule = employeRepository.findLastMatricule();

        // Then
        Assertions.assertThat(employeMatricule).isNull();
    }

    @Test
    void test3() {
        // Given
        employeRepository.save(new Employe("Doe", "Jean", "C06432", LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe", "John", "T12345", LocalDate.now(), 1500d, 1, 1.0));
        employeRepository.save(new Employe("Doe", "Jane", "M40325", LocalDate.now(), 1500d, 1, 1.0));

        // When
        String employeMatricule = employeRepository.findLastMatricule();

        // Then
        Assertions.assertThat(employeMatricule).isEqualTo("40325");
    }
}