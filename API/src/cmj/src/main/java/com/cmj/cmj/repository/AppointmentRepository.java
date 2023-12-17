package com.cmj.cmj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cmj.cmj.model.Appointment;
import com.cmj.cmj.model.TypePayment;
import com.cmj.cmj.model.User;

import java.time.LocalDateTime;
import java.util.List;



@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Integer> {

    List<Appointment> findByAppoinClient(User appoinClient);
    Appointment findByAppoinId(Integer appoinId);
    List<Appointment> findByAppoinDoctor(User appoinDoctor);
    List<Appointment> findByAppoinPayment(TypePayment appoinPayment);
    @Query("SELECT a FROM Appointment a WHERE a.appoinDoctor = :doctorId AND a.appoinDate BETWEEN :startDate AND :endDate")
    List<Appointment> findByAppoinDateBetween(@Param("doctorId") User doctorId, @Param("startDate") LocalDateTime  startDate, @Param("endDate") LocalDateTime endDate);
}
