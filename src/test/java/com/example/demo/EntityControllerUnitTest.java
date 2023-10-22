
package com.example.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import java.time.LocalDateTime;
import java.time.format.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.demo.controllers.*;
import com.example.demo.repositories.*;
import com.example.demo.entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;



/** TODO
 * Implement all the unit test in its corresponding class.
 * Make sure to be as exhaustive as possible. Coverage is checked ;)
 */

@WebMvcTest(DoctorController.class)
class DoctorControllerUnitTest{

    @MockBean
    private DoctorRepository doctorRepository;

    @Autowired 
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldNotGetAnyDoctorById() throws Exception{
        long id = 31;
        mockMvc.perform(get("/api/doctors/" + id))
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldNotGetAnyDoctorBySring() throws Exception{
        String id = "Paula";
        mockMvc.perform(get("/api/doctors/" + id))
                .andExpect(status().isBadRequest());

    }

    @Test
    void shouldNotGetAnyDoctor() throws Exception{
        String id = "Paula";
        mockMvc.perform(post("/api/doctors/" + id))
                .andExpect(status().isMethodNotAllowed());

    }
    @Test
    void shouldDeleteDoctorById() throws Exception{
        Doctor doctor = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");

        doctor.setId(1);

        Optional<Doctor> opt = Optional.of(doctor);

        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(doctor.getId());
        assertThat(doctor.getId()).isEqualTo(1);

        when(doctorRepository.findById(doctor.getId())).thenReturn(opt);
        mockMvc.perform(delete("/api/doctors/" + doctor.getId()))
                .andExpect(status().isOk());

    }

    @Test
    void shouldGetDoctorById() throws Exception{
        Doctor doctor = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");

        doctor.setId(2);

        Optional<Doctor> opt = Optional.of(doctor);

        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(doctor.getId());
        assertThat(doctor.getId()).isEqualTo(2);

        mockMvc.perform(post("/api/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isCreated());

        when(doctorRepository.findById(doctor.getId())).thenReturn(opt);
        mockMvc.perform(get("/api/doctors/" + doctor.getId()))
                .andExpect(status().isOk());

    }

    @Test
    void shouldCreateDoctor() throws Exception{

        Doctor doctor = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");

        doctor.setId(1);

        mockMvc.perform(post("/api/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldNotCreateDoctorShowAnErrorXMLFormatter() throws Exception{

        Doctor doctor = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");

        doctor.setId(1);

        mockMvc.perform(post("/api/doctor").contentType(MediaType.APPLICATION_ATOM_XML)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldNotCreateDuplicateDoctor() throws Exception{

        Doctor doctor = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Doctor doctor2 = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");

        //doctor.setId(1);
        //doctor2.setId(2);

        mockMvc.perform(post("/api/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor2)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void shouldCreateDoctorWithTheSameName() throws Exception{

        Doctor doctor = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Doctor doctor2 = new Doctor ("Perla", "Rosa", 42, "p.rosahospital.accwe");

        //doctor.setId(1);
        //doctor2.setId(2);

        mockMvc.perform(post("/api/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor2)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldCreateDoctorWithoutName() throws Exception{

        Doctor doctor = new Doctor ("", "Amalia", 24, "p.amalia@hospital.accwe");

        doctor.setId(1);

        mockMvc.perform(post("/api/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void shouldNotCreateDoctorWithPatchMethod() throws Exception{

        Doctor doctor = new Doctor ("", "Amalia", 24, "p.amalia@hospital.accwe");

        doctor.setId(1);

        mockMvc.perform(patch("/api/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isMethodNotAllowed());
    }
    @Test
    void shouldNotCreateDoctorWithoutData() throws Exception{

        Doctor doctor = new Doctor ();

        doctor.setId(1);

        mockMvc.perform(post("/api/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void shouldCreateDoctorsWithDifferentIds() throws Exception{

        Doctor doctor = new Doctor ("Paula", "Amalia", 24, "p.amalia@hospital.accwe");
        Doctor doctor2 = new Doctor ("Ana", "Rosa", 24, "ana.rosa@hospital.accwe");

        doctor.setId(1);
        doctor2.setId(2);

        mockMvc.perform(post("/api/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isCreated());

        Optional<Doctor> opt = Optional.of(doctor);

        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(doctor.getId());
        assertThat(doctor.getId()).isEqualTo(1);

        when(doctorRepository.findById(doctor.getId())).thenReturn(opt);
        mockMvc.perform(get("/api/doctors/" + doctor.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor2)));

        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(doctor.getId());
        assertThat(doctor.getId()).isEqualTo(2);

        when(doctorRepository.findById(doctor.getId())).thenReturn(opt);
        mockMvc.perform(get("/api/doctors/" + doctor.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldNotDeleteDoctor() throws Exception{
        long id = 31;
        mockMvc.perform(delete("/api/doctors/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotDeleteAnyDoctor() throws Exception{
        String id = "Paula";
        mockMvc.perform(delete("/api/doctors/" + id))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldCreateDoctorAndDelete() throws Exception{

        Doctor doctor = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");

        doctor.setId(22);

        mockMvc.perform(post("/api/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isCreated());
        long id = 22;
        mockMvc.perform(delete("/api/doctors/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteAllDoctors() throws Exception{
        mockMvc.perform(delete("/api/doctors"))
                .andExpect(status().isOk());
    }
    @Test
    void shouldListAllDoctors() throws Exception{

        Doctor doctor = new Doctor ("Paula", "Amalia", 24, "p.amalia@hospital.accwe");
        Doctor doctor2 = new Doctor ("Ana", "Rosa", 24, "ana.rosa@hospital.accwe");
        Doctor doctor3 = new Doctor ("Ana", "Batista", 50, "ana.batista@hospital.accwe");

        doctor.setId(1);
        doctor2.setId(2);
        doctor3.setId(3);

        mockMvc.perform(post("/api/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor2)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/doctor").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor3)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk());

    }
}


@WebMvcTest(PatientController.class)
class PatientControllerUnitTest{

    @MockBean
    private PatientRepository patientRepository;

    @Autowired 
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldNotGetAnyPatientById() throws Exception{
        long id = 31;
        mockMvc.perform(get("/api/patients/" + id))
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldNotGetAnyPatientByString() throws Exception{
        String id = "Paula";
        mockMvc.perform(post("/api/doctors/" + id))
                .andExpect(status().isMethodNotAllowed());

    }

    @Test
    void shouldNotGetAnyPatient() throws Exception{
        String id = "Paula";
        mockMvc.perform(post("/api/patients/" + id))
                .andExpect(status().isMethodNotAllowed());

    }

    @Test
    void shouldGetPatientById() throws Exception{
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");

        patient.setId(2);

        Optional<Patient> opt = Optional.of(patient);

        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(patient.getId());
        assertThat(patient.getId()).isEqualTo(2);

        mockMvc.perform(post("/api/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated());

        when(patientRepository.findById(patient.getId())).thenReturn(opt);
        mockMvc.perform(get("/api/patients/" + patient.getId()))
                .andExpect(status().isOk());

    }

    @Test
    void shouldCreatePatient() throws Exception{

        Patient patient = new Patient ("Ana", "Amalia", 30, "a.amalia@hospital.accwe");

        patient.setId(1);

        mockMvc.perform(post("/api/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldNotCreatePatientShowAnErrorXMLFormatter() throws Exception{

        Patient patient = new Patient ("Silvia", "Silva", 24, "s.silva@hospital.accwe");

        patient.setId(1);

        mockMvc.perform(post("/api/patient").contentType(MediaType.APPLICATION_ATOM_XML)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void shouldNotCreateDuplicatePatient() throws Exception{

        Patient patient = new Patient ("Ana", "Amalia", 24, "a.amalia@hospital.accwe");
        Patient patient2 = new Patient ("Ana", "Amalia", 24, "a.amalia@hospital.accwe");

        //doctor.setId(1);
        //doctor2.setId(2);

        mockMvc.perform(post("/api/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient2)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void shouldCreatePatientAndDelete() throws Exception{
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");

        patient.setId(22);

        mockMvc.perform(post("/api/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated());
        long id = 22;
        mockMvc.perform(delete("/api/patients/" + id))
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreatePatientsWithTheSameName() throws Exception{

        Doctor doctor = new Doctor ("Jose", "Amalia", 24, "p.amalia@hospital.accwe");
        Doctor doctor2 = new Doctor ("Jose", "Rosa", 42, "p.rosahospital.accwe");

        //doctor.setId(1);
        //doctor2.setId(2);

        mockMvc.perform(post("/api/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isCreated());
        mockMvc.perform(post("/api/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor2)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldNotCreatePatientWithoutName() throws Exception{

        Doctor doctor = new Doctor ("", "Santos", 34, "p.santos@hospital.accwe");

        doctor.setId(1);

        mockMvc.perform(post("/api/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void shouldNotCreatePatientWithoutData() throws Exception{

        Patient patient = new Patient ();

        patient.setId(1);

        mockMvc.perform(post("/api/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void shouldCreatePatientsWithDifferentIds() throws Exception{

        Patient patient = new Patient ("Maria", "Nunes", 59, "maria@hospital.accwe");
        Patient patient2 = new Patient ("Lara", "Walter", 42, "lara@hospital.accwe");

        patient.setId(1);
        patient2.setId(2);

        mockMvc.perform(post("/api/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated());

        Optional<Patient> opt = Optional.of(patient);

        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(patient.getId());
        assertThat(patient.getId()).isEqualTo(1);

        when(patientRepository.findById(patient.getId())).thenReturn(opt);
        mockMvc.perform(get("/api/patients/" + patient.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/patient").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patient2)));

        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(patient.getId());
        assertThat(patient.getId()).isEqualTo(2);

        when(patientRepository.findById(patient.getId())).thenReturn(opt);
        mockMvc.perform(get("/api/patients/" + patient.getId()))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeletePatientById() throws Exception{
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");

        patient.setId(1);

        Optional<Patient> opt = Optional.of(patient);

        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(patient.getId());
        assertThat(patient.getId()).isEqualTo(1);

        when(patientRepository.findById(patient.getId())).thenReturn(opt);
        mockMvc.perform(get("/api/patients/" + patient.getId()))
                .andExpect(status().isOk());

    }

    @Test
    void shouldNotDeletePatient() throws Exception{
        long id = 31;
        mockMvc.perform(delete("/api/patients/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldNotDeleteAnyPatient() throws Exception{
        long id = 31;
        mockMvc.perform(post("/api/doctors/" + id))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void shouldDeleteAllPatients() throws Exception{
        mockMvc.perform(delete("/api/patients"))
                .andExpect(status().isOk());
    }
    @Test
    void shouldListAllPatients() throws Exception{

        Patient patient = new Patient ("Paula", "Amalia", 24, "p.amalia@hospital.accwe");
        Patient patient2 = new Patient ("Ana", "Rosa", 24, "ana.rosa@hospital.accwe");
        Patient patient3 = new Patient ("Ana", "Batista", 50, "ana.batista@hospital.accwe");

        patient.setId(1);
        patient2.setId(2);
        patient3.setId(3);

        mockMvc.perform(post("/api/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient2)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/patient").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient3)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isCreated());

    }

}

@WebMvcTest(RoomController.class)
class RoomControllerUnitTest{

    @MockBean
    private RoomRepository roomRepository;

    @Autowired 
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldNotGetAnyRoomByName() throws Exception{
        String id = "Test";
        mockMvc.perform(get("/api/rooms/" + id))
                .andExpect(status().isNotFound());

    }

    @Test
    void shouldNotGetAnyRoomByNameNoEntity() throws Exception{
        mockMvc.perform(get("/api/rooms/" ))
                .andExpect(status().isNoContent());

    }

    @Test
    void shouldCreateRoom() throws Exception{

        Room room = new Room ("Dentistry");

        mockMvc.perform(post("/api/room").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldCreateAndGetTheRoom() throws Exception{

        Room room = new Room ("Dentistry");

        mockMvc.perform(post("/api/room").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/api/rooms/Dentistry").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteRoomByName() throws Exception{
        Room room = new Room("Dermatology");

        Optional<Room> opt = Optional.of(room);

        when(roomRepository.findByRoomName(room.getRoomName())).thenReturn(opt);
        mockMvc.perform(get("/api/rooms/" + room.getRoomName()))
                .andExpect(status().isOk());

    }

    @Test
    void shouldNotDeleteRoom() throws Exception{
        String id = "Test";
        mockMvc.perform(delete("/api/rooms/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteAllRooms() throws Exception{
        mockMvc.perform(delete("/api/rooms"))
                .andExpect(status().isOk());
    }

}
