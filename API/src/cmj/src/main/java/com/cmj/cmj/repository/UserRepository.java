package com.cmj.cmj.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cmj.cmj.model.PatientRecord;
import com.cmj.cmj.model.User;
import com.cmj.cmj.model.User.Role;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query(value = "SELECT usuar_name FROM usuario WHERE usuar_email = ?1 and usuar_password = ?2", nativeQuery = true)
    String findByEmailandPassword(String p_email, String p_password);

    User findByUsuarEmail(String usuar_email);

    List<User> findByUsuarRole(Role usuarRole);

    User findByAppoinPatientRecord(PatientRecord appoinPatientRecord);
}
