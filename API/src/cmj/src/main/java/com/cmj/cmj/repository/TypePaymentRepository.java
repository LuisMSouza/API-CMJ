package com.cmj.cmj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cmj.cmj.model.TypePayment;

@Repository
public interface TypePaymentRepository extends JpaRepository<TypePayment, Integer> {
    
}
