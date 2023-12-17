package com.cmj.cmj.model.services;

import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.cmj.cmj.model.PatientRecord;

public interface PatientRecordService {
    ResponseEntity<String> createPatientRecord(PatientRecord p_patientRecord);
    ResponseEntity<String> editPatientRecord(Integer p_id, PatientRecord p_patientRecord);
    ResponseEntity<String> deletePatientRecord(Integer p_id);
    ResponseEntity<Optional<PatientRecord>> returnPatientRecordById(Integer p_idPatientRecord);
}
