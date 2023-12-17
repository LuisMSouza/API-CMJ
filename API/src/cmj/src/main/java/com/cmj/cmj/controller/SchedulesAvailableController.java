package com.cmj.cmj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cmj.cmj.model.SchedulesAvailable;
import com.cmj.cmj.model.services.SchedulesAvailableService;

@RestController
@RequestMapping("/schedulesAvailable")
public class SchedulesAvailableController {

    @Autowired
    private SchedulesAvailableService m_schedulesAvailableService;

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteSchedulesAvailable(@RequestParam Integer p_scheduleId) {
        try {
            return m_schedulesAvailableService.deleteSchedulesAvailable(p_scheduleId);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<String> editSchedulesAvailable(@ModelAttribute SchedulesAvailable p_schedule) {
        try {
            return m_schedulesAvailableService.editSchedulesAvailable(p_schedule.getSchAvaId(), p_schedule);
        } catch (Exception ex) {
            throw ex;
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createSchedulesAvailable(@ModelAttribute SchedulesAvailable p_schedule) {
        try {
            return m_schedulesAvailableService.createSchedulesAvailable(p_schedule);
        } catch (Exception ex) {
            throw ex;
        }
    }
    
}
