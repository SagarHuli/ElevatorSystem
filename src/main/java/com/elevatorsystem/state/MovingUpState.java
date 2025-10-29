package com.elevatorsystem.state;

import com.elevatorsystem.entities.Elevator;
import com.elevatorsystem.enums.RequestSource;
import com.elevatorsystem.enums.Trajectory;
import com.elevatorsystem.models.ElevatorRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class MovingUpState implements ElevatorState{
    private static final Logger log = LoggerFactory.getLogger(MovingUpState.class);

    @Override
    public void move(Elevator elevator) {
        if(elevator.getUpRequests().isEmpty()) {
            elevator.setState(new IdleState());
            return;
        }

        Integer nextFloor = elevator.getUpRequests().first();
        elevator.setCurrentFloor(elevator.getCurrentFloor() + 1);

        if(elevator.getCurrentFloor() == nextFloor) {
            log.info("Elevator {} is at floor {}", elevator.getName(), elevator.getCurrentFloor());
            elevator.getUpRequests().pollFirst();
        }

        if(elevator.getUpRequests().isEmpty()) {
            elevator.setState(new IdleState());
        }
    }

    @Override
    public void addRequest(Elevator elevator, ElevatorRequest request) {
        // Handle internal requests
        if(request.getRequestSource() == RequestSource.INTERNAL) {
            if(request.getTargetFloor() > elevator.getCurrentFloor()) {
                elevator.getUpRequests().add(request.getTargetFloor());
            } else {
                elevator.getDownRequests().add(request.getTargetFloor());
            }
        }
        // handle external requests
        else {
            // targetFloor in the context of external requests is the floor at which this request originated.
            // Add to the upRequests only if the targetFloor is greater than the current floor of the elevator.
            if(request.getDirection() == Trajectory.UP && request.getTargetFloor() >= elevator.getCurrentFloor()) {
                elevator.getUpRequests().add(request.getTargetFloor());
            } else if(request.getDirection() == Trajectory.DOWN) {
                elevator.getDownRequests().add(request.getTargetFloor());
            }
        }
    }

    @Override
    public Trajectory getDirection() {
        return Trajectory.UP;
    }
}
