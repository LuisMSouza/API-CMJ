package com.cmj.cmj.model.services;

import com.cmj.cmj.model.PatientRecord;
import com.cmj.cmj.model.SchedulesAvailable;
import com.cmj.cmj.model.TypePayment;
import com.cmj.cmj.model.User;
import com.cmj.cmj.model.User.Role;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<String> validateUser(String p_email, String p_password);
    ResponseEntity<String> createUser(User p_user);
    ResponseEntity<String> editUser(Integer p_id, User p_user);
    ResponseEntity<String> deleteUser(String p_email);
    ResponseEntity<User> searchUserByEmail(String p_email);
    ResponseEntity<List<User>> returnAllUsers();
    ResponseEntity<List<User>> returnAllUsersByRole(Role p_role);
    ResponseEntity<User> searchUserById(Integer p_id);
    ResponseEntity<List<TypePayment>> returnTypePaymentsById(Integer p_id);
    ResponseEntity<List<SchedulesAvailable>> returnSchedulesAvailableById(Integer p_id);
    ResponseEntity<User> searchUserByPatientRecord(PatientRecord p_appoinPatientRecord);
}
