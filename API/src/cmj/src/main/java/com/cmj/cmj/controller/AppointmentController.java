package com.cmj.cmj.controller;

import com.cmj.cmj.model.Appointment;
import com.cmj.cmj.model.User;
import com.cmj.cmj.model.services.AppointmentService;
import com.cmj.cmj.model.services.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService m_appointService;

    @Autowired
    private UserService m_userService;

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteAppointment(@RequestParam Integer p_idAppointment) {
        try {
            return m_appointService.deleteAppointment(p_idAppointment);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editAppointment(@ModelAttribute Appointment p_appointment) {
        try {
            return m_appointService.editAppointment(p_appointment.getAppoinId(), p_appointment);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAppointment(@ModelAttribute Appointment p_appointment) {
        try {
            return m_appointService.createAppointment(p_appointment);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<String> confirmAppointment(@RequestParam Integer p_appointment) {
        try {
            return m_appointService.confirmAppointment(p_appointment);
        } catch (Exception ex) {
            throw ex;
        }
    }
    @GetMapping("/searchByClientEmail")
    public ResponseEntity<List<Appointment>> searchAllAppointmentByClient(@RequestParam String p_emailClient) {
        try {
            User v_actuallyUser = m_userService.searchUserByEmail(p_emailClient).getBody();
            if (v_actuallyUser != null)
                return m_appointService.seachAppointmentByClient(v_actuallyUser);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with Email " + p_emailClient + " not found.");
        } catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping("/searchById")
    public ResponseEntity<Appointment> searchAllAppointmentByClient(@RequestParam Integer p_id) {
        try {
            return m_appointService.seachAppointmentById(p_id);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Appointment>> getAll() {
        try {
            return m_appointService.getAll();
        } catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping("/searchWeeklyAppointmentsByDoctor")
    public ResponseEntity<List<Appointment>> searchWeeklyAppointmentByDoctor(@RequestParam Integer id, @RequestParam String diaDaSemana) {
        User actualDoctor = m_userService.searchUserById(id).getBody();
        return m_appointService.searchWeeklyAppointmentsByDoctor(actualDoctor, diaDaSemana);
    }
}