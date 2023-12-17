package com.cmj.cmj.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuario")
public class User {
    public enum Role {
        Cliente, Médico, Recepcionista, Admin;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer usuarId;

    @Column(nullable = false, unique = true, length = 11)
    private String usuarCpf;

    @Column(nullable = false, unique = true, length = 80)
    private String usuarEmail;

    @Column(nullable = false)
    private String usuarPassword;

    @Column(nullable = false, length = 80)
    private String usuarName;

    @Column(nullable = false)
    private Role usuarRole;

    @Column(nullable = false)
    private Date usuarDateBirth;

    @Column(nullable = false, length = 11)
    private String usuarCellphone;

    @JoinColumn(foreignKey = @ForeignKey(name = "FK_patientRecordId"))
    @OneToOne
    private PatientRecord appoinPatientRecord;

    @JsonManagedReference
    @OneToMany(mappedBy = "typayUserRelation", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<TypePayment> acceptedTypePayments = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "schAvaDoctor", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<SchedulesAvailable> acceptedSchedulesServices    = new ArrayList<>();
}
