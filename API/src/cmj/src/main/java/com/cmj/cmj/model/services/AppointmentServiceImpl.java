package com.cmj.cmj.model.services;

import com.cmj.cmj.model.Appointment;
import com.cmj.cmj.model.SchedulesAvailable;
import com.cmj.cmj.model.TypePayment;
import com.cmj.cmj.model.User;
import com.cmj.cmj.repository.AppointmentRepository;
import com.cmj.cmj.repository.SchedulesAvailableRepository;
import com.cmj.cmj.senders.EmailSender;
import com.cmj.cmj.senders.SMSSender;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static com.cmj.cmj.model.Appointment.Status.Confirmada;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentRepository m_appointmentRepository;

    @Autowired
    private SchedulesAvailableRepository m_schedulesAvailableRepository;

    @Autowired
    private SchedulesAvailableServiceImpl m_schedulesAvailableService;

    @Autowired
    private EmailSender m_emailSender;
    @Autowired
    private SMSSender m_smsSender;

    public ResponseEntity<List<Appointment>> seachAppointmentByClient(User p_client) {
        try {
            List<Appointment> v_listOfAppointmentsToSend = m_appointmentRepository.findByAppoinClient(p_client);
            if (v_listOfAppointmentsToSend != null)
                return new ResponseEntity<List<Appointment>>(v_listOfAppointmentsToSend, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public ResponseEntity<List<Appointment>> seachAppointmentByDoctor(User p_doctor) {
        try {
            List<Appointment> v_listOfAppointmentsToSend = m_appointmentRepository.findByAppoinDoctor(p_doctor);
            if (v_listOfAppointmentsToSend != null)
                return new ResponseEntity<List<Appointment>>(v_listOfAppointmentsToSend, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public ResponseEntity<String> deleteAppointment(Integer p_idAppointment) {
        try {
            // Deleta a linha do banco de dados
            Appointment v_appointment = m_appointmentRepository.findByAppoinId(p_idAppointment);
            Appointment auxAppointment = clone(v_appointment);
            v_appointment.setAppoinClient(null);
            v_appointment.setAppoinDoctor(null);
            v_appointment.setAppoinPayment(null);
            m_appointmentRepository.save(v_appointment);
            m_appointmentRepository.deleteById(p_idAppointment);

            m_emailSender.sendDeleteAppointmentEmail(auxAppointment);
            return new ResponseEntity<String>("Data with ID " + p_idAppointment + " deleted successfully.",
                    HttpStatus.OK);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Data with ID " + p_idAppointment + " not found.");
        }
    }

    public ResponseEntity<String> editAppointment(Integer p_idAppointment, Appointment p_appointment) {
        try {
            // Busca o appointment no banco de dados pelo ID
            Appointment v_appointmentToChange = m_appointmentRepository.findById(p_idAppointment).get();
            if (p_appointment.getAppoinDate() != v_appointmentToChange.getAppoinDate()) {
                Integer v_schedule = m_schedulesAvailableRepository.findIdByDateHourAndDoctorId(
                        p_appointment.getAppoinDate(), p_appointment.getAppoinDoctor().getUsuarId());
                        if(v_schedule!=null)
                        m_schedulesAvailableService.deleteSchedulesAvailable(v_schedule);
                SchedulesAvailable v_scheduleToInsert = new SchedulesAvailable(v_appointmentToChange.getAppoinDate(),
                        v_appointmentToChange.getAppoinDoctor());
                m_schedulesAvailableRepository.save(v_scheduleToInsert);
            }
            Appointment oldAppointment = clone(v_appointmentToChange);

            // Seta os novos atributos que mudaram no appointment
            v_appointmentToChange.setAppoinDate(p_appointment.getAppoinDate());
            v_appointmentToChange.setAppoinDoctor(p_appointment.getAppoinDoctor());
            v_appointmentToChange.setAppoinClient(p_appointment.getAppoinClient());
            v_appointmentToChange.setAppoinStatus(p_appointment.getAppoinStatus());
            v_appointmentToChange.setAppoinPayment(p_appointment.getAppoinPayment());

            // Update no banco de dados
            m_appointmentRepository.save(v_appointmentToChange);


            m_emailSender.sendUpdatedAppointmentEmail(oldAppointment, v_appointmentToChange);
            return new ResponseEntity<String>("Modify Appointment with ID " + p_idAppointment + " successfully.",
                    HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    public ResponseEntity<String> confirmAppointment(Integer p_idAppointment) {
        try {
            // Busca o appointment no banco de dados pelo ID
            Appointment v_appointmentToChange = m_appointmentRepository.findById(p_idAppointment).get();
            v_appointmentToChange.setAppoinStatus(Confirmada);

            // Update no banco de dados
            m_appointmentRepository.save(v_appointmentToChange);
            m_emailSender.sendConfirmedAppointmentEmail(v_appointmentToChange);
            m_smsSender.sendConfirmedAppointmentMessage(v_appointmentToChange);
            m_smsSender.sendAppointmentNotification(v_appointmentToChange, 60);
            return new ResponseEntity<String>("Modify Appointment with ID " + p_idAppointment + " successfully.",
                    HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    public ResponseEntity<String> createAppointment(Appointment p_appointment) {
        try {
            // Cria a linha no banco de dados
            Integer v_schedule = m_schedulesAvailableRepository.findIdByDateHourAndDoctorId(
                    p_appointment.getAppoinDate(), p_appointment.getAppoinDoctor().getUsuarId());
            m_schedulesAvailableService.deleteSchedulesAvailable(v_schedule);
            m_appointmentRepository.save(p_appointment);
            m_emailSender.sendCreatedAppointmentEmail(p_appointment);
            return new ResponseEntity<String>("Create appointment successfully", HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public ResponseEntity<List<Appointment>> getAll() {
        try {
            List<Appointment> v_listOfAppointmentsToSend = m_appointmentRepository.findAll();
            if (v_listOfAppointmentsToSend != null)
                return new ResponseEntity<List<Appointment>>(v_listOfAppointmentsToSend, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public ResponseEntity<Appointment> seachAppointmentById(Integer p_id) {
        try {
            Appointment v_appointmentToSend = m_appointmentRepository.findByAppoinId(p_id);
            // Busca uma consulta específica pelo Id
            if (v_appointmentToSend != null)
                return new ResponseEntity<Appointment>(v_appointmentToSend, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public ResponseEntity<List<Appointment>> searchWeeklyAppointmentsByDoctor(User p_doctor, String p_diaDaSemana) {
        try {
            List<LocalDateTime> v_datesToFilter = obterExtremidadesDaSemana(p_diaDaSemana);
            List<Appointment> v_appointments = m_appointmentRepository.findByAppoinDateBetween(p_doctor,v_datesToFilter.get(0),
                    v_datesToFilter.get(1));

            if (v_appointments != null)
                return ResponseEntity.ok(v_appointments);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    private List<LocalDateTime> obterExtremidadesDaSemana(String diaDaSemana) {
        List<LocalDateTime> datas = new ArrayList<>();
        LocalDateTime v_actualDay = LocalDateTime.now();
        int v_diaActual = LocalDateTime.now().getDayOfWeek().getValue();
        int v_toSum = 0;

        if ("all".equalsIgnoreCase(diaDaSemana)) {
            v_toSum = (7 - v_diaActual) > 0 ? 7 - v_diaActual : 7 - v_diaActual + 7;
            datas.add(v_actualDay.withHour(00).withMinute(00).withSecond(00));
            datas.add(v_actualDay.plusDays(v_toSum).withHour(23).withMinute(59).withSecond(59));
        } else if(v_diaActual == DayOfWeek.valueOf(diaDaSemana.toUpperCase()).getValue())
        {
            datas.add(v_actualDay.plusDays(v_toSum).withHour(00).withMinute(00).withSecond(00));
            datas.add(v_actualDay.plusDays(v_toSum).withHour(23).withMinute(59).withSecond(59));
        }else {

            int v_diaGoal = DayOfWeek.valueOf(diaDaSemana.toUpperCase()).getValue();
            // Dia da semana - Dia atual = Qnt. dias q eu tenho q adicionar em hoje
            // Porém se der negativo a qnt. de dia é só adicionar + 7
            v_toSum = (v_diaGoal - v_diaActual) > 0 ? v_diaGoal - v_diaActual : v_diaGoal - v_diaActual + 7;
            datas.add(v_actualDay.plusDays(v_toSum).withHour(00).withMinute(00).withSecond(00));
            datas.add(v_actualDay.plusDays(v_toSum).withHour(23).withMinute(59).withSecond(59));
        }
        return datas;
    }

    public ResponseEntity<List<Appointment>> getByTypePayment(TypePayment p_appoinPaymentType) {
        try {
            List<Appointment> v_listOfAppointmentsToSend = m_appointmentRepository.findByAppoinPayment(p_appoinPaymentType);
            // Busca uma consulta específica pelo Id
            if (v_listOfAppointmentsToSend != null)
                return new ResponseEntity<List<Appointment>>(v_listOfAppointmentsToSend, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, ex.getMessage());
        }
    }

    private Appointment clone(Appointment appointment) {
        Appointment clone = new Appointment();
        clone.setAppoinId(appointment.getAppoinId());
        clone.setAppoinClient(appointment.getAppoinClient());
        clone.setAppoinStatus(appointment.getAppoinStatus());
        clone.setAppoinDoctor(appointment.getAppoinDoctor());
        clone.setAppoinDate(appointment.getAppoinDate());
        clone.setAppoinPayment(appointment.getAppoinPayment());
        return clone;
    }
}
