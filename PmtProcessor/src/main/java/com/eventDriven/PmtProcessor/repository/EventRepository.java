package com.eventDriven.PmtProcessor.repository;

import com.eventDriven.PmtProcessor.enums.Status;
import com.eventDriven.PmtProcessor.model.Event;
import com.eventDriven.PmtProcessor.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, String> {

}
