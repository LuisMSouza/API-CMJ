package com.cmj.cmj.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

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
@Table(name = "patientrecord")
public class PatientRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ptrId;

    @JoinColumn(foreignKey = @ForeignKey(name = "FK_prtDoctor"))
    @ManyToOne
    private User ptrLastDoctorModification;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime ptrLastModification;

    @Column(nullable = false)
    private String ptrFullNameMom;

    @Column(nullable = false)
    private Double ptrHeight;
    
    @Column(nullable = false)
    private Double ptrWeight;

    @Column(nullable = false)
    private Integer ptrAge;

    @Column(nullable = false)
    private String ptrGender;
    
    @Column(nullable = true, columnDefinition = "text")
    private String ptrVaccines;

    @Column(nullable = true, columnDefinition = "text")
    private String ptrMedicines;

    @Column(nullable = true, columnDefinition = "text")
    private String ptrSurgeries;

    @Column(nullable = true, columnDefinition = "text")
    private String ptrAllergies;

    @Column(nullable = true, columnDefinition = "text")
    private String ptrObservation;
    
}
