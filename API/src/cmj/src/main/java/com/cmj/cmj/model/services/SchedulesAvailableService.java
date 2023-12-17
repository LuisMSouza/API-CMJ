package com.cmj.cmj.model.services;

import org.springframework.http.ResponseEntity;

import com.cmj.cmj.model.SchedulesAvailable;

public interface SchedulesAvailableService {
    ResponseEntity<String> createSchedulesAvailable(SchedulesAvailable p_schedule);
    ResponseEntity<String> editSchedulesAvailable(Integer p_id, SchedulesAvailable p_schedule);
    ResponseEntity<String> deleteSchedulesAvailable(Integer p_id);
}
