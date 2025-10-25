package com.elevatorsystem;

import com.elevatorsystem.entities.Display;
import com.elevatorsystem.entities.Elevator;
import com.elevatorsystem.enums.RequestSource;
import com.elevatorsystem.enums.Trajectory;
import com.elevatorsystem.models.ElevatorRequest;
import com.elevatorsystem.strategy.ElevatorSelectionStrategy;
import com.elevatorsystem.strategy.NearestElevatorStrategy;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
public class ElevatorSystem {
    private static final Logger log = LoggerFactory.getLogger(ElevatorSystem.class);
    private static ElevatorSystem instance;
    private final Map<String, Elevator> elevators;
    private final ElevatorSelectionStrategy strategy;
    private final ExecutorService executorService;

    private ElevatorSystem(int numElevators) {
        this.strategy = new NearestElevatorStrategy();
        this.executorService = Executors.newFixedThreadPool(numElevators);

        List<Elevator> elevatorList = new ArrayList<>();
        Display display = new Display();
        for(int i=1; i<= numElevators; i++) {
            Elevator elevator = new Elevator("E"+i);
            elevator.addObserver(display);
            elevatorList.add(elevator);
        }

        this.elevators = elevatorList.stream().collect(Collectors.toMap(Elevator::getName, elevator -> elevator));
    }

    public void start() {
        for(Elevator elevator : elevators.values()) {
            executorService.submit(elevator);
        }
    }

    public static synchronized ElevatorSystem getInstance(int numElevators) {
        if(instance == null) {
            instance = new ElevatorSystem(numElevators);
        }
        return instance;
    }

    // External request handling
    public void requestElevator(int floor, Trajectory direction) {
        log.info("External request :: User at floor {} wants to go {}", floor, direction);
        ElevatorRequest request = new ElevatorRequest(floor, direction, RequestSource.EXTERNAL);

        Optional<Elevator> elevator = strategy.selectElevator(new ArrayList<>(this.elevators.values()), request);

        if(elevator.isPresent()) {
            elevator.get().addRequest(request);
        } else {
            log.info("Elevators are busy!!! Kindly wait for sometime");
        }
    }

    // Internal request handling
    public void selectFloor(String elevatorName, int targetFloor) {
        log.info("User in elevator {} wants to go to {}", elevatorName, targetFloor);
        ElevatorRequest request = new ElevatorRequest(targetFloor, Trajectory.IDLE, RequestSource.INTERNAL);

        Elevator elevator = elevators.get(elevatorName);
        if(elevator != null) {
            elevator.addRequest(request);
        } else {
            log.info("Invalid elevator name");
        }
    }

    public void shutDown() {
        log.info("Shutting down elevator system");
        for(Elevator elevator : elevators.values()) {
            elevator.stopElevator();
        }
        this.executorService.shutdown();
    }
}
