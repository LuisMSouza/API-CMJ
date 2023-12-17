package com.cmj.cmj.model.services;

import com.cmj.cmj.model.Appointment;
import com.cmj.cmj.model.PatientRecord;
import com.cmj.cmj.model.SchedulesAvailable;
import com.cmj.cmj.model.TypePayment;
import com.cmj.cmj.model.User;
import com.cmj.cmj.model.User.Role;
import com.cmj.cmj.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository m_userRepository;

    @Autowired
    private AppointmentService m_appointmentService;

    public ResponseEntity<String> validateUser(String p_email, String p_password) {
        try {
            // Busca se existe um usuário com email e senha igual a esse
            boolean v_validate = m_userRepository.findByEmailandPassword(p_email, p_password) != null ? true : false;
            if (v_validate == true)
                return new ResponseEntity<String>("Email and Password are correct", HttpStatus.OK);
            else
                return new ResponseEntity<String>("Email and Password aren't correct", HttpStatus.UNAUTHORIZED);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public ResponseEntity<String> editUser(Integer p_id, User p_user) {
        try {
            // Busca o usuário para ser modificado
            User v_userToChange = m_userRepository.findById(p_id).get();

            // Seta todas as mudanças do usuário
            v_userToChange.setUsuarCpf(p_user.getUsuarCpf());
            v_userToChange.setUsuarCellphone(p_user.getUsuarCellphone());
            v_userToChange.setUsuarDateBirth(p_user.getUsuarDateBirth());
            v_userToChange.setUsuarName(p_user.getUsuarName());
            v_userToChange.setUsuarPassword(p_user.getUsuarPassword());
            v_userToChange.setUsuarEmail(p_user.getUsuarEmail());
            v_userToChange.setUsuarRole(p_user.getUsuarRole());
            v_userToChange.setAppoinPatientRecord(p_user.getAppoinPatientRecord());

            // Salva mudanças no banco
            m_userRepository.save(v_userToChange);

            return new ResponseEntity<String>("Changes made successfully", HttpStatus.OK);

        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public ResponseEntity<User> searchUserByEmail(String p_email) {
        try {
            // Busca o Id do usuário pelo email
            User v_userToSend = m_userRepository.findByUsuarEmail(p_email);
            if (v_userToSend != null)
                return new ResponseEntity<User>(v_userToSend, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public ResponseEntity<String> createUser(User p_user) {
        try {
            // Salva o usuário no banco
            m_userRepository.save(p_user);
            return new ResponseEntity<String>("Create user successfully", HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public ResponseEntity<List<User>> returnAllUsers() {

        try {
            List<User> v_userListToSend = m_userRepository.findAll();
            if (v_userListToSend != null)
                return new ResponseEntity<>(v_userListToSend, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public ResponseEntity<List<User>> returnAllUsersByRole(Role p_role) {

        try {
            List<User> v_userListToSend = m_userRepository.findByUsuarRole(p_role);
            if (v_userListToSend != null)
                return new ResponseEntity<>(v_userListToSend, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public ResponseEntity<String> deleteUser(String p_email) {
        try {
            // Busca o Id do usuário pelo e-mail
            User v_userToDelete = searchUserByEmail(p_email).getBody();
            if (v_userToDelete != null && v_userToDelete.getUsuarRole() == Role.Médico) {
                List<Appointment> v_listAppointments = m_appointmentService.seachAppointmentByDoctor(v_userToDelete)
                        .getBody();
                if (v_listAppointments != null) {
                    for (Appointment t_appointment : v_listAppointments) {
                        t_appointment.setAppoinDoctor(null);
                        t_appointment.setAppoinPayment(null);
                        m_appointmentService.editAppointment(t_appointment.getAppoinId(), t_appointment);
                    }
                }
            } else if (v_userToDelete != null && v_userToDelete.getUsuarRole() == Role.Cliente) {
                List<Appointment> v_listAppointments = m_appointmentService.seachAppointmentByClient(v_userToDelete)
                        .getBody();
                if (v_listAppointments != null) {
                    for (Appointment t_appointment : v_listAppointments) {
                        m_appointmentService.deleteAppointment(t_appointment.getAppoinId());
                    }
                }
            }

            // Delete o usuário de acordo com o id
            if (v_userToDelete != null)
                m_userRepository.deleteById(v_userToDelete.getUsuarId());

            return new ResponseEntity<String>("User with E-mail " + p_email + " deleted successfully.", HttpStatus.OK);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }

    public ResponseEntity<User> searchUserById(Integer p_id) {
        Optional<User> optDoctor = m_userRepository.findById(p_id);
        if (optDoctor.isPresent()) {
            return new ResponseEntity<>(optDoctor.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<List<TypePayment>> returnTypePaymentsById(Integer p_id) {
        Optional<User> optDoctor = m_userRepository.findById(p_id);
        if (optDoctor.isPresent()) {
            return new ResponseEntity<>(optDoctor.get().getAcceptedTypePayments(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<List<SchedulesAvailable>> returnSchedulesAvailableById(Integer p_id) {
        Optional<User> optDoctor = m_userRepository.findById(p_id);
        if (optDoctor.isPresent()) {
            return new ResponseEntity<>(optDoctor.get().getAcceptedSchedulesServices(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<User> searchUserByPatientRecord(PatientRecord p_appoinPatientRecord) {
        try {
            // Busca o Id do usuário pelo PatientRecord
            User v_userToSend = m_userRepository.findByAppoinPatientRecord(p_appoinPatientRecord);
            if (v_userToSend != null)
                return new ResponseEntity<User>(v_userToSend, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
    }
}
