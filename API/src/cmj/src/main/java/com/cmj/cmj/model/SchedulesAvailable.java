package com.cmj.cmj.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedulesAvailable")
public class SchedulesAvailable {

    public SchedulesAvailable(LocalDateTime p_schAvaDateHour, User p_schAvaDoctor)
    {
        schAvaDateHour = p_schAvaDateHour;
        schAvaDoctor = p_schAvaDoctor;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer schAvaId;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime schAvaDateHour;

    @JsonBackReference
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_schAvaId"), name = "sch_ava_doctor_usuar_id")
    @ManyToOne(cascade = CascadeType.REMOVE)
    private User schAvaDoctor;
}
