package com.cmj.cmj.controller;

import com.cmj.cmj.model.SchedulesAvailable;
import com.cmj.cmj.model.TypePayment;
import com.cmj.cmj.model.User;
import com.cmj.cmj.model.User.Role;
import com.cmj.cmj.model.services.UserService;
import com.cmj.cmj.senders.EmailSender;
import com.cmj.cmj.utils.EncryptPassword;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService m_userService;
    @Autowired
    private EmailSender m_emailSender;

    @GetMapping("/validate")
    public ResponseEntity<String> getUser(@RequestParam String p_email, @RequestParam String p_password)
            throws Exception {
        try {
            String v_password = EncryptPassword.encrypt(p_password);
            return m_userService.validateUser(p_email, v_password);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser(@RequestParam String p_email) {
        try {
            return m_userService.deleteUser(p_email);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editUser(@RequestParam String p_email, @ModelAttribute User p_user) throws Exception {
        try {
            User v_actuallyUser = m_userService.searchUserByEmail(p_email).getBody();
            if (p_user.getUsuarPassword() != null)
                p_user.setUsuarPassword(EncryptPassword.encrypt(p_user.getUsuarPassword()));
            if (v_actuallyUser != null)
                return m_userService.editUser(v_actuallyUser.getUsuarId(), p_user);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with Email " + p_email + " not found.");
        } catch (Exception ex) {
            throw ex;
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@ModelAttribute User p_user) throws Exception {
        try {
            p_user.setUsuarPassword(EncryptPassword.encrypt(p_user.getUsuarPassword()));
            return m_userService.createUser(p_user);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping("/returnAllUsers")
    public ResponseEntity<List<User>> returnAllUsers() {
        try {
            return m_userService.returnAllUsers();
        } catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping("/returnAllUsersByRole")
    public ResponseEntity<List<User>> returnAllUsersByRole(@RequestParam Role p_role) {
        try {
            return m_userService.returnAllUsersByRole(p_role);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping("/returnByEmail")
    public ResponseEntity<User> returnUserByEmail(@RequestParam String p_email) throws Exception {
        try {
            ResponseEntity<User> v_userToValidate = m_userService.searchUserByEmail(p_email);
            if (v_userToValidate.getStatusCode() == HttpStatus.NOT_FOUND)
                return v_userToValidate;
            User v_userToSend = v_userToValidate.getBody();
            if (v_userToSend != null)
                v_userToSend.setUsuarPassword(EncryptPassword.decrypt(v_userToSend.getUsuarPassword()));

            return new ResponseEntity<User>(v_userToSend, HttpStatus.OK);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping("/returnTypePaymentsById")
    public ResponseEntity<List<TypePayment>> returnTypePaymentsById(@RequestParam Integer p_id) throws Exception {
        try {
            return m_userService.returnTypePaymentsById(p_id);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @GetMapping("/returnSchedulesAvailableById")
    public ResponseEntity<List<SchedulesAvailable>> returnSchedulesAvailableById(@RequestParam Integer p_id) throws Exception {
        try {
            return m_userService.returnSchedulesAvailableById(p_id);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @PostMapping("/sendEmail/updatePassword")
    public ResponseEntity<String> sendEmailToUpdatePassword(@RequestParam String p_email, @RequestParam String p_redirect_url) {
        try {
            User user = m_userService.searchUserByEmail(p_email).getBody();
            if (user == null) {
                return new ResponseEntity<>("User with email " + p_email + " not found.", HttpStatus.NOT_FOUND);
            }
            return m_emailSender.sendUpdatePasswordEmail(user, p_redirect_url);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
