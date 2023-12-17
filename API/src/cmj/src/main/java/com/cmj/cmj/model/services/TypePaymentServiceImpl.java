package com.cmj.cmj.model.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cmj.cmj.model.TypePayment;
import com.cmj.cmj.repository.TypePaymentRepository;

@Service
public class TypePaymentServiceImpl implements TypePaymentService {

    @Autowired
    private TypePaymentRepository m_typePaymentRepository;

    public ResponseEntity<String> createTypePayment(TypePayment p_typePayment) {
        try {
            // Salva o tipo de pagamento no banco
            m_typePaymentRepository.save(p_typePayment);
            return new ResponseEntity<String>("Create TypePayment successfully with id: " + p_typePayment.getTypayId(),
                    HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public ResponseEntity<String> editTypePayment(Integer p_id, TypePayment p_typePayment) {
        try {
            // Busca o tipo de pagamento para ser modificado
            TypePayment v_typePaymentToChange = m_typePaymentRepository.findById(p_id).get();

            // Seta todas as mudanças do tipo de pagamento
            v_typePaymentToChange.setTypayNameOfInsure(p_typePayment.getTypayNameOfInsure());
            v_typePaymentToChange.setTypayType(p_typePayment.getTypayType());
            v_typePaymentToChange.setTypayValue(p_typePayment.getTypayValue());
            v_typePaymentToChange.setTypayDateModification(p_typePayment.getTypayDateModification());

            // Salva mudanças no banco
            m_typePaymentRepository.save(v_typePaymentToChange);

            return new ResponseEntity<String>("Changes made successfully", HttpStatus.OK);

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public ResponseEntity<String> deleteTypePayment(Integer p_id) {
        try {
            // Busca o Id do tipo de pagamento pelo id
            TypePayment v_typePaymentToDelete = m_typePaymentRepository.findById(p_id).get();

            // Delete o tipo de pagamento de acordo com o id
            if (v_typePaymentToDelete != null) {
                v_typePaymentToDelete.setTypayUserRelation(null);
                m_typePaymentRepository.save(v_typePaymentToDelete);
                m_typePaymentRepository.deleteById(p_id);
            }

            return new ResponseEntity<String>("TypePayment with id " + p_id + " deleted successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public ResponseEntity<List<TypePayment>> returnAllTypePayments() {
        try {
            List<TypePayment> v_listTypePaymentToSend = m_typePaymentRepository.findAll();
            // Retorna todos os usuários do sistema
            if (v_listTypePaymentToSend != null)
            return new ResponseEntity<List<TypePayment>>(v_listTypePaymentToSend, HttpStatus.OK);
            else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public ResponseEntity<Optional<TypePayment>> returnTypePaymentsById(Integer p_idTypePayment) {
        try {
            Optional<TypePayment> v_typePaymentToSend = m_typePaymentRepository.findById(p_idTypePayment);
            if (v_typePaymentToSend != null)
                return new ResponseEntity<Optional<TypePayment>>(v_typePaymentToSend, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
