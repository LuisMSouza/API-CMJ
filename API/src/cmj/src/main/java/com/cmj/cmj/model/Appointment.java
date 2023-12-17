package com.cmj.cmj.model;

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
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import static com.cmj.cmj.model.TypePayment.TypeOfPayment.Convênio;
import static com.cmj.cmj.model.TypePayment.TypeOfPayment.Particular;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "appointment")
public class Appointment {

    public enum Status {
        AguardandoConfirmação, Confirmada, Finalizada;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer appoinId;

    @JoinColumn(foreignKey = @ForeignKey(name = "FK_clientId"))
    @ManyToOne
    private User appoinClient;

    @JoinColumn(foreignKey = @ForeignKey(name = "FK_doctorId"))
    @ManyToOne(cascade = CascadeType.REMOVE)
    private User appoinDoctor;

    @JoinColumn(foreignKey = @ForeignKey(name = "FK_typePaymentId"))
    @ManyToOne(cascade = CascadeType.REMOVE)
    private TypePayment appoinPayment;

    @Column(nullable = false)
    private Status appoinStatus;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appoinDate;

    public String getAppoinPaymentType() {
        if (appoinPayment != null) {
            if (Particular.equals(appoinPayment.getTypayType())) {
                return "Particular - R$" + appoinPayment.getTypayValue();
            } else if (Convênio.equals(appoinPayment.getTypayType())) {
                return "Convênio - " + appoinPayment.getTypayNameOfInsure();
            }
        }
        return "Sem tipo de pagamento cadastrado";
    }
}
