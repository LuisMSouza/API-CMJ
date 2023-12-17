package com.cmj.cmj.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

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
@Entity(name = "typepayment")
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "typepayment")
public class TypePayment {
    public enum TypeOfPayment {
        Convênio, Particular;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer typayId;

    @Column(nullable = false)
    private TypeOfPayment typayType;

    @Column(nullable = true)
    private String typayNameOfInsure;

    @Column(nullable = true)
    private Double typayValue;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime typayDateModification;

    @JsonBackReference
    @JoinColumn(foreignKey = @ForeignKey(name = "FK_typayId"))
    @ManyToOne
    private User typayUserRelation;

}
