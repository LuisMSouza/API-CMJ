package com.cmj.cmj.controller;

import java.util.List;
import java.util.Optional;

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

import com.cmj.cmj.model.Appointment;
import com.cmj.cmj.model.TypePayment;
import com.cmj.cmj.model.services.AppointmentService;
import com.cmj.cmj.model.services.TypePaymentService;

@RestController
@RequestMapping("/typepayment")
public class TypePaymentController {
    @Autowired
    private TypePaymentService m_typePaymentService;

    @Autowired
    private AppointmentService m_appointmentService;

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteTypePayment(@RequestParam Integer p_tokenTypePayment) {
        try {
            Optional<TypePayment> v_typePaymentToDelete = m_typePaymentService
                    .returnTypePaymentsById(p_tokenTypePayment).getBody();
            if (v_typePaymentToDelete != null) {
                List<Appointment> v_listOfTypePayment = m_appointmentService
                        .getByTypePayment(v_typePaymentToDelete.get()).getBody();
                if (v_listOfTypePayment != null) {
                    for (Appointment t_appointment : v_listOfTypePayment) {
                        if (t_appointment.getAppoinPayment() != null
                                && t_appointment.getAppoinPayment().getTypayId() == p_tokenTypePayment) {
                            t_appointment.setAppoinPayment(null);
                            m_appointmentService.editAppointment(t_appointment.getAppoinId(), t_appointment);
                        }
                    }
                    return m_typePaymentService.deleteTypePayment(p_tokenTypePayment);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editTypePayment(@ModelAttribute TypePayment p_patientRecord) {
        try {
            return m_typePaymentService.editTypePayment(p_patientRecord.getTypayId(), p_patientRecord);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createTypePayment(@ModelAttribute TypePayment p_patientRecord) {
        try {
            return m_typePaymentService.createTypePayment(p_patientRecord);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping("/returnAllTypePayments")
    public ResponseEntity<List<TypePayment>> returnAllTypePayments() {
        try {
            return m_typePaymentService.returnAllTypePayments();
        } catch (Exception ex) {
            throw ex;
        }
    }
}
