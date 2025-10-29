package com.elevatorsystem.strategy;

import com.elevatorsystem.entities.Elevator;
import com.elevatorsystem.enums.Trajectory;
import com.elevatorsystem.models.ElevatorRequest;

import java.util.List;
import java.util.Optional;

public class FCFSSelectionStrategy implements ElevatorSelectionStrategy {

    @Override
    public Optional<Elevator> selectElevator(List<Elevator> elevators, ElevatorRequest request) {
        for(Elevator elevator : elevators) {
            if(isSuitable(elevator, request)) {
                return Optional.of(elevator);
            }
        }
        return Optional.empty();
    }

    private boolean isSuitable(Elevator elevator, ElevatorRequest request) {
        if(Trajectory.IDLE == elevator.getState().getDirection()) {
            return true;
        }
        if(request.getDirection() == elevator.getDirection()) {
            if(request.getTargetFloor() >  elevator.getCurrentFloor() &&
                    Trajectory.UP == elevator.getDirection()) {
                return true;
            }
            return request.getTargetFloor() < elevator.getCurrentFloor() &&
                    Trajectory.DOWN == elevator.getDirection();
        }
        return false;
    }
}
