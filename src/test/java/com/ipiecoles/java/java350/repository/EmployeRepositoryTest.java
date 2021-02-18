package com.ipiecoles.java.java350.repository;

import com.ipiecoles.java.java350.model.Employe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {Java350Application.class})
//@DataJpaTest
@SpringBootTest
class EmployeRepositoryTest {

    @Autowired
    private EmployeRepository employeRepository;

    @BeforeEach
    @AfterEach
    public void purge(){ employeRepository.deleteAll(); }


    /**********  TP  **********/

    // Test méthode avgPerformanceWhereMatriculeStartsWith()
    @Test
    void testAvgPerformanceWhereMatriculeStartsWith() {
        // Given
        List<Employe> employeList = new ArrayList<>();
        employeList.add(new Employe("Doe", "John", "C00001", LocalDate.now(), 1500d, 5, 1d));
        employeList.add(new Employe("Doe", "John", "T00002", LocalDate.now(), 1500d, 10, 1d));
        employeList.add(new Employe("Doe", "John", "C00003", LocalDate.now(), 1500d, 7, 1d));
        employeList.add(new Employe("Doe", "John", "T00004", LocalDate.now(), 1500d, 3, 1d));
        employeList.add(new Employe("Doe", "John", "C00005", LocalDate.now(), 1500d, 12, 1d));
        employeList.add(new Employe("Doe", "John", "T00006", LocalDate.now(), 1500d, 10, 1d));
        employeList.add(new Employe("Doe", "John", "C00007", LocalDate.now(), 1500d, 1, 1d));
        employeList.add(new Employe("Doe", "John", "M00008", LocalDate.now(), 1500d, 9, 1d));
        employeList.add(new Employe("Doe", "John", "C00009", LocalDate.now(), 1500d, 7, 1d));
        employeList.add(new Employe("Doe", "John", "T00010", LocalDate.now(), 1500d, 10, 1d));
        employeList.add(new Employe("Doe", "John", "C00011", LocalDate.now(), 1500d, 6, 1d));
        employeList.add(new Employe("Doe", "John", "M00012", LocalDate.now(), 1500d, 12, 1d));
        employeList.add(new Employe("Doe", "John", "C00013", LocalDate.now(), 1500d, 8, 1d));
        employeList.add(new Employe("Doe", "John", "C00014", LocalDate.now(), 1500d, 11, 1d));
        employeList.add(new Employe("Doe", "John", "T00015", LocalDate.now(), 1500d, 8, 1d));
        for (Employe e : employeList) employeRepository.save(e);

        // When
        Double perfManagers = employeRepository.avgPerformanceWhereMatriculeStartsWith("M");
        Double perfCommerciaux = employeRepository.avgPerformanceWhereMatriculeStartsWith("C");
        Double perfTechniciens = employeRepository.avgPerformanceWhereMatriculeStartsWith("T");

        // Then
        Assertions.assertThat(perfManagers).isEqualTo(10.5);
        Assertions.assertThat(perfCommerciaux).isEqualTo(7.125);
        Assertions.assertThat(perfTechniciens).isEqualTo(8.2);
    }


    /**********  Cours  **********/

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