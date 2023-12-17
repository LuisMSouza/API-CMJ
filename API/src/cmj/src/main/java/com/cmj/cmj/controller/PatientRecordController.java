package com.cmj.cmj.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cmj.cmj.model.PatientRecord;
import com.cmj.cmj.model.User;
import com.cmj.cmj.model.services.PatientRecordService;
import com.cmj.cmj.model.services.UserService;

@RestController
@RequestMapping("/patientrecord")
public class PatientRecordController {

    @Autowired
    private PatientRecordService m_patientRecordService;

    @Autowired
    private UserService m_userService;

    @DeleteMapping("/delete")
    public ResponseEntity<String> deletePatientRecord(@RequestParam Integer p_tokenPatientRecord) {
        try {
            Optional<PatientRecord> v_patientRecordToDelete = m_patientRecordService.returnPatientRecordById(p_tokenPatientRecord).getBody();
            if(v_patientRecordToDelete != null)
            {
                User v_userToModify = m_userService.searchUserByPatientRecord(v_patientRecordToDelete.get()).getBody();
                if(v_userToModify!= null)
                {
                    v_userToModify.setAppoinPatientRecord(null);
                    m_userService.editUser(v_userToModify.getUsuarId(), v_userToModify);
                    return m_patientRecordService.deletePatientRecord(p_tokenPatientRecord);
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editPatientRecord(@ModelAttribute PatientRecord p_patientRecord) {
        try {
            return m_patientRecordService.editPatientRecord(p_patientRecord.getPtrId(), p_patientRecord);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createPatientRecord(@ModelAttribute PatientRecord p_patientRecord) {
        try {
            return m_patientRecordService.createPatientRecord(p_patientRecord);
        } catch (Exception ex) {
            throw ex;
        }
    }
    
}
