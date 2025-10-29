package com.elevatorsystem.strategy;


import com.elevatorsystem.entities.Elevator;
import com.elevatorsystem.models.ElevatorRequest;

import java.util.List;
import java.util.Optional;

public interface ElevatorSelectionStrategy {

    Optional<Elevator> selectElevator(List<Elevator> elevators, ElevatorRequest request);
}
