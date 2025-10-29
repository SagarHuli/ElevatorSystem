package com.elevatorsystem.state;


import com.elevatorsystem.entities.Elevator;
import com.elevatorsystem.enums.RequestSource;
import com.elevatorsystem.enums.Trajectory;
import com.elevatorsystem.models.ElevatorRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Slf4j
public class MovingDownState implements ElevatorState{

    private static final Logger log = LoggerFactory.getLogger(MovingDownState.class);
    @Override
    public void move(Elevator elevator) {
        if(elevator.getDownRequests().isEmpty()) {
            elevator.setState(new IdleState());
            return;
        }

        Integer nextFloor = elevator.getDownRequests().first();
        elevator.setCurrentFloor(elevator.getCurrentFloor() - 1);

        if(elevator.getCurrentFloor() == nextFloor) {
            log.info("Elevator {} stopped at floor {}", elevator.getName(), elevator.getCurrentFloor());
            elevator.getDownRequests().pollFirst();
        }

        // check if all requests are served
        if(elevator.getDownRequests().isEmpty()) {
            elevator.setState(new IdleState());
        }
    }

    @Override
    public void addRequest(Elevator elevator, ElevatorRequest request) {
        // Handle internal requests
        if(request.getRequestSource() == RequestSource.INTERNAL) {
            if(request.getTargetFloor() < elevator.getCurrentFloor()) {
                elevator.getDownRequests().add(request.getTargetFloor());
            } else if(request.getTargetFloor() > elevator.getCurrentFloor()) {
                elevator.getUpRequests().add(request.getTargetFloor());
            }
        }
        // Handle external requests
        else {
            // targetFloor in the context of external requests is the floor at which this request originated.
            // Add to the downRequests only if the targetFloor is less than the current floor of the elevator.
            if(request.getDirection() == Trajectory.DOWN && request.getTargetFloor() <= elevator.getCurrentFloor()) {
                elevator.getDownRequests().add(request.getTargetFloor());
            } else if(request.getDirection() == Trajectory.UP) {
                elevator.getUpRequests().add(request.getTargetFloor());
            }
        }
    }

    @Override
    public Trajectory getDirection() {
        return Trajectory.DOWN;
    }
}
