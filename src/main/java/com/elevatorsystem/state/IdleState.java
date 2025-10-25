package com.elevatorsystem.state;


import com.elevatorsystem.entities.Elevator;
import com.elevatorsystem.enums.Trajectory;
import com.elevatorsystem.models.ElevatorRequest;

public class IdleState implements ElevatorState {
    @Override
    public void move(Elevator elevator) {
        if(!elevator.getUpRequests().isEmpty()) {
            elevator.setState(new MovingUpState());
        } else if(!elevator.getDownRequests().isEmpty()) {
            elevator.setState(new MovingDownState());
        }
        // stay idle otherwise
    }

    @Override
    public void addRequest(Elevator elevator, ElevatorRequest request) {
        if(request.getTargetFloor() > elevator.getCurrentFloor()) {
            elevator.getUpRequests().add(request.getTargetFloor());
        } else if(request.getTargetFloor() < elevator.getCurrentFloor()) {
            elevator.getDownRequests().add(request.getTargetFloor());
        }
    }

    @Override
    public Trajectory getDirection() {
        return Trajectory.IDLE;
    }
}
