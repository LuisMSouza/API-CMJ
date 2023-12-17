package com.cmj.cmj.model.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cmj.cmj.model.SchedulesAvailable;
import com.cmj.cmj.repository.SchedulesAvailableRepository;

@Service
public class SchedulesAvailableServiceImpl implements SchedulesAvailableService {

    @Autowired
    private SchedulesAvailableRepository m_schedulesAvailableRepository;

     public ResponseEntity<String> createSchedulesAvailable(SchedulesAvailable p_schedule) {
        try {
            // Salva o tipo de pagamento no banco
            m_schedulesAvailableRepository.save(p_schedule);
            return new ResponseEntity<String>("Create ScheduleAvailable successfully with id: " + p_schedule.getSchAvaId(),
                    HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public ResponseEntity<String> editSchedulesAvailable(Integer p_id, SchedulesAvailable p_schedule) {
        try {
            // Busca o tipo de pagamento para ser modificado
            SchedulesAvailable v_scheduleToChange = m_schedulesAvailableRepository.findById(p_id).get();

            // Seta todas as mudanças do tipo de pagamento
            v_scheduleToChange.setSchAvaDateHour(p_schedule.getSchAvaDateHour());

            // Salva mudanças no banco
            m_schedulesAvailableRepository.save(v_scheduleToChange);

            return new ResponseEntity<String>("Changes made successfully", HttpStatus.OK);

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public ResponseEntity<String> deleteSchedulesAvailable(Integer p_id) {
        try {
            // Busca o Id do tipo de pagamento pelo id
            SchedulesAvailable v_typePaymentToDelete = m_schedulesAvailableRepository.findById(p_id).get();

            // Delete o tipo de pagamento de acordo com o id
            if (v_typePaymentToDelete != null)
            {
                v_typePaymentToDelete.setSchAvaDoctor(null);
                m_schedulesAvailableRepository.save(v_typePaymentToDelete);
                m_schedulesAvailableRepository.deleteById(p_id);
            }

            return new ResponseEntity<String>("ScheduleAvailable with id " + p_id + " deleted successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
