package com.elevatorsystem.state;


import com.elevatorsystem.entities.Elevator;
import com.elevatorsystem.enums.Trajectory;
import com.elevatorsystem.models.ElevatorRequest;

public interface ElevatorState {

    void move(Elevator elevator);
    void addRequest(Elevator elevator, ElevatorRequest request);
    Trajectory getDirection();
}
