package com.cmj.cmj.model.services;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.cmj.cmj.model.Appointment;
import com.cmj.cmj.model.TypePayment;
import com.cmj.cmj.model.User;


public interface AppointmentService {
    ResponseEntity<String> createAppointment(Appointment p_appointment);
    ResponseEntity<String> deleteAppointment(Integer p_idAppointment);
    ResponseEntity<String> editAppointment(Integer p_idAppointment, Appointment p_appointment);
    ResponseEntity<List<Appointment>> seachAppointmentByClient(User p_client);
    ResponseEntity<List<Appointment>> seachAppointmentByDoctor(User p_doctor);
    ResponseEntity<Appointment> seachAppointmentById(Integer p_id);
    ResponseEntity<List<Appointment>> getAll();
    ResponseEntity<List<Appointment>> searchWeeklyAppointmentsByDoctor(User p_doctor, String p_diaDaSemana);
    ResponseEntity<List<Appointment>> getByTypePayment(TypePayment p_appoinPaymentType);
    ResponseEntity<String> confirmAppointment(Integer p_idAppointment);
}
