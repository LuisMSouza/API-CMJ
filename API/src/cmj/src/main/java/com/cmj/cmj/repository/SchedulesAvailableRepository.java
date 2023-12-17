package com.cmj.cmj.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cmj.cmj.model.SchedulesAvailable;


@Repository
public interface SchedulesAvailableRepository extends JpaRepository<SchedulesAvailable, Integer> {
    @Query("SELECT sa.schAvaId FROM SchedulesAvailable sa WHERE sa.schAvaDateHour = :dateHour and sa.schAvaDoctor.usuarId = :doctorId")
    Integer findIdByDateHourAndDoctorId(@Param("dateHour") LocalDateTime dateHour, @Param("doctorId") Integer doctorId);

}
