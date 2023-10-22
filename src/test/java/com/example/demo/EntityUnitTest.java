package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.entities.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUnitTest {

	@Autowired
	private TestEntityManager entityManager;

	private Doctor d1;

	private Patient p1;

    private Room r1;

    private Appointment a1;
    private Appointment a2;
    private Appointment a3;

    /** TODO
     * Implement tests for each Entity class: Doctor, Patient, Room and Appointment.
     * Make sure you are as exhaustive as possible. Coverage is checked ;)
     */

    @Test
    void doctor_creation_test(){
        Doctor doctor = new Doctor("Ana", "Silvia", 34, "ana@gmail.com");
        assertEquals("Ana", doctor.getFirstName());
        assertEquals("Silvia", doctor.getLastName());
        assertEquals(34, doctor.getAge());
        assertEquals("ana@gmail.com", doctor.getEmail());
    }
    @Test
    void doctor_id_set_test(){
        Doctor doctor = new Doctor("Ana", "Silvia", 34, "ana@gmail.com");
        doctor.setId(22);
        assertEquals(22, doctor.getId());
    }

    @Test
    void doctor_creation_change_test(){
        Doctor doctor = new Doctor("Ana", "Silvia", 34, "ana@gmail.com");
        doctor.setAge(25);
        doctor.setEmail("ana@gggmail.com");
        doctor.setFirstName("Ana Bela");
        doctor.setLastName("Luna");
        assertEquals("Ana Bela", doctor.getFirstName());
        assertEquals("Luna", doctor.getLastName());
        assertEquals(25, doctor.getAge());
        assertEquals("ana@gggmail.com", doctor.getEmail());
    }
    @Test
    void patient_creation_test(){
        Doctor doctor = new Doctor("Laura", "Silvia", 44, "laura@gmail.com");
        assertEquals("Laura", doctor.getFirstName());
        assertEquals("Silvia", doctor.getLastName());
        assertEquals(44, doctor.getAge());
        assertEquals("laura@gmail.com", doctor.getEmail());
    }
    @Test
    void patient_id_set_test(){
        Patient patient = new Patient("Matheus", "Silvia", 34, "matheus@gmail.com");
        patient.setId(25);
        assertEquals(25, patient.getId());
    }

    @Test
    void patient_creation_change_test(){
        Doctor doctor = new Doctor("Matheus", "Souza", 45, "mat@gmail.com");
        doctor.setAge(32);
        doctor.setEmail("matheus@gmail.com");
        doctor.setFirstName("Ana Bela");
        doctor.setLastName("Luna");
        assertEquals("Ana Bela", doctor.getFirstName());
        assertEquals("Luna", doctor.getLastName());
        assertEquals(32, doctor.getAge());
        assertEquals("matheus@gmail.com", doctor.getEmail());
    }

    @Test
    void room_creation_test(){
        Room room = new Room("Cardiology");
        assertEquals("Cardiology", room.getRoomName());

    }
    @Test
    void appointment_creation_test(){
        Patient patient = new Patient("Matheus", "Silvia", 34, "matheus@gmail.com");
        Doctor doctor = new Doctor("Matheus", "Souza", 45, "mat@gmail.com");
        Room room = new Room("Anesthesiology");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        LocalDateTime startsAt= LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);

        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);

        assertEquals(patient, appointment.getPatient());
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(room, appointment.getRoom());
    }

    @Test
    void appointment_creation_change_test(){
        Patient patient = new Patient("Matheus", "Silvia", 34, "matheus@gmail.com");
        Doctor doctor = new Doctor("Matheus", "Souza", 45, "mat@gmail.com");
        Room room = new Room("Anesthesiology");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        LocalDateTime startsAt= LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);

        doctor.setAge(32);
        doctor.setEmail("matheus@gmail.com");
        doctor.setFirstName("Ana Bela");
        doctor.setLastName("Luna");

        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);

        assertEquals(patient, appointment.getPatient());
        assertEquals(doctor, appointment.getDoctor());
        assertEquals(room, appointment.getRoom());
        assertEquals(32, doctor.getAge());
        assertEquals("matheus@gmail.com", doctor.getEmail());
        assertEquals("Ana Bela", doctor.getFirstName());
        assertEquals("Luna", doctor.getLastName());
    }

    @Test
    void appointment_creation_set_patient_test(){
        Patient patient = new Patient("Matheus", "Silvia", 34, "matheus@gmail.com");
        Patient patient2 = new Patient("Ana", "Silvia", 34, "matheus@gmail.com");
        Doctor doctor = new Doctor("Matheus", "Souza", 45, "mat@gmail.com");
        Doctor doctor2 = new Doctor("Joana", "Souza", 45, "mat@gmail.com");
        Room room = new Room("Anesthesiology");
        Room room2 = new Room("Cardiology");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        LocalDateTime startsAt= LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);

        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);
        appointment.setPatient(patient2);
        appointment.setDoctor(doctor2);
        appointment.setRoom(room2);
        assertEquals(patient2, appointment.getPatient());
        assertEquals(doctor2, appointment.getDoctor());
        assertEquals(room2, appointment.getRoom());
    }
}
