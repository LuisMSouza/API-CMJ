package com.cmj.cmj.model.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cmj.cmj.model.PatientRecord;
import com.cmj.cmj.repository.PatientRecordRepository;

@Service
public class PatientRecordServiceImpl implements PatientRecordService {

    @Autowired
    private PatientRecordRepository m_patientRecordRepository;

    public ResponseEntity<String> createPatientRecord(PatientRecord p_patientRecord) {
        try {
            // Salva o Patient Record no banco
            m_patientRecordRepository.save(p_patientRecord);
            return new ResponseEntity<String>(
                    "Create Patient Record successfully with id: " + p_patientRecord.getPtrId(), HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public ResponseEntity<String> editPatientRecord(Integer p_id, PatientRecord p_patientRecord) {
        try {
            // Busca o Patient Record para ser modificado
            PatientRecord v_patientRecordToChange = m_patientRecordRepository.findById(p_id).get();

            // Seta todas as mudanças do Patient Record
            v_patientRecordToChange.setPtrFullNameMom(p_patientRecord.getPtrFullNameMom());
            v_patientRecordToChange.setPtrAge(p_patientRecord.getPtrAge());
            v_patientRecordToChange.setPtrHeight(p_patientRecord.getPtrHeight());
            v_patientRecordToChange.setPtrWeight(p_patientRecord.getPtrWeight());
            v_patientRecordToChange.setPtrGender(p_patientRecord.getPtrGender());
            v_patientRecordToChange.setPtrVaccines(p_patientRecord.getPtrVaccines());
            v_patientRecordToChange.setPtrMedicines(p_patientRecord.getPtrMedicines());
            v_patientRecordToChange.setPtrAllergies(p_patientRecord.getPtrAllergies());
            v_patientRecordToChange.setPtrObservation(p_patientRecord.getPtrObservation());
            v_patientRecordToChange.setPtrLastDoctorModification(p_patientRecord.getPtrLastDoctorModification());
            v_patientRecordToChange.setPtrLastModification(p_patientRecord.getPtrLastModification());

            // Salva mudanças no banco
            m_patientRecordRepository.save(v_patientRecordToChange);

            return new ResponseEntity<String>("Changes made successfully", HttpStatus.OK);

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public ResponseEntity<String> deletePatientRecord(Integer p_id) {
        try {
            // Busca o Id do Patient Record by Id
            Optional<PatientRecord> v_patientRecordToDelete = m_patientRecordRepository.findById(p_id);

            // Delete a Patient Record de acordo com o id
            if (v_patientRecordToDelete != null)
                m_patientRecordRepository.deleteById(p_id);

            return new ResponseEntity<String>("Patient Record with id " + p_id + " deleted successfully.",
                    HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public ResponseEntity<Optional<PatientRecord>> returnPatientRecordById(Integer p_idPatientRecord) {
        try {
            Optional<PatientRecord> v_patientRecordToSend = m_patientRecordRepository.findById(p_idPatientRecord);
            if (v_patientRecordToSend != null)
                return new ResponseEntity<Optional<PatientRecord>>(v_patientRecordToSend, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
