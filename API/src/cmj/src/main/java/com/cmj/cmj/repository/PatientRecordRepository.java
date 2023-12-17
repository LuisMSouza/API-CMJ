package com.cmj.cmj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cmj.cmj.model.PatientRecord;


@Repository
public interface PatientRecordRepository extends JpaRepository<PatientRecord, Integer> {
}
