package com.elevatorsystem.entities;


import com.elevatorsystem.observer.ElevatorObserver;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Slf4j
public class Display implements ElevatorObserver {

    private static final Logger log = LoggerFactory.getLogger(Display.class);

    @Override
    public void update(Elevator elevator) {
        log.info("Elevator {} | Current floor {} | Direction {}", elevator.getName(), elevator.getCurrentFloor(), elevator.getDirection());
    }
}
