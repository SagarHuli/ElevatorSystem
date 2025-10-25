package com.elevatorsystem.strategy;


import com.elevatorsystem.entities.Elevator;
import com.elevatorsystem.enums.Trajectory;
import com.elevatorsystem.models.ElevatorRequest;

import java.util.List;
import java.util.Optional;

public class NearestElevatorStrategy implements ElevatorSelectionStrategy {
    @Override
    public Optional<Elevator> selectElevator(List<Elevator> elevators, ElevatorRequest request) {
        Elevator selected = null;
        int minDistance = Integer.MAX_VALUE;
        for(Elevator elevator : elevators) {
            if(isSuitable(elevator, request)) {
                int distance = Math.abs(request.getTargetFloor() - elevator.getCurrentFloor());
                if (distance < minDistance) {
                    selected = elevator;
                    minDistance = distance;
                }
            }
        }
        return Optional.ofNullable(selected);
    }

    private boolean isSuitable(Elevator elevator, ElevatorRequest request) {
        if(elevator.getDirection() == Trajectory.IDLE)
            return true;
        if(elevator.getDirection() == request.getDirection()) {
            if(elevator.getDirection() == Trajectory.UP && request.getTargetFloor() > elevator.getCurrentFloor()) {
                return true;
            } else return elevator.getDirection() == Trajectory.DOWN && request.getTargetFloor() < elevator.getCurrentFloor();
        }
        return false;
    }
}
