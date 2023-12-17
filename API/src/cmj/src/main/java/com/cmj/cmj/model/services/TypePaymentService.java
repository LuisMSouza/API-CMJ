package com.cmj.cmj.model.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.cmj.cmj.model.TypePayment;


public interface TypePaymentService {
    ResponseEntity<String> createTypePayment(TypePayment p_typePayment);
    ResponseEntity<String> editTypePayment(Integer p_id, TypePayment p_typePayment);
    ResponseEntity<String> deleteTypePayment(Integer p_id);
    ResponseEntity<List<TypePayment>> returnAllTypePayments();
    ResponseEntity<Optional<TypePayment>> returnTypePaymentsById(Integer p_idTypePayment);
}
