package com.elevatorsystem.entities;


import com.elevatorsystem.enums.Door;
import com.elevatorsystem.enums.Trajectory;
import com.elevatorsystem.models.ElevatorRequest;
import com.elevatorsystem.observer.ElevatorObserver;
import com.elevatorsystem.observer.ElevatorSubject;
import com.elevatorsystem.state.ElevatorState;
import com.elevatorsystem.state.IdleState;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Elevator extends ElevatorSubject implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(Elevator.class);
    String name;
    ElevatorState state;
    AtomicInteger currentFloor;
    Door doorState;
    private volatile boolean isRunning = true;
    private final TreeSet<Integer> upRequests;
    private final TreeSet<Integer> downRequests;
    private final List<ElevatorObserver> observers = new ArrayList<>();

    public Elevator(String name) {
        this.name = name;
        this.state = new IdleState();
        this.currentFloor = new AtomicInteger(1);
        this.doorState = Door.CLOSED;
        this.upRequests = new TreeSet<>();
        this.downRequests = new TreeSet<>((a,b) -> b-a);
    }

    public void move() {
        this.state.move(this);
    }


    public void setState(ElevatorState state) {
        this.state = state;
    }

    public synchronized void addRequest(ElevatorRequest request) {

        log.info("Elevator {} processing {}", this.name, request);
        this.state.addRequest(this, request);
    }

    public void setCurrentFloor(int floor) {
        this.currentFloor.set(floor);
        notifyObservers();
    }
    public int getCurrentFloor() {
        return currentFloor.get();
    }

    public ElevatorState getState() {
        return state;
    }

    public String getName() {
        return name;
    }

    public Trajectory getDirection() { return this.state.getDirection();}

    public TreeSet<Integer> getUpRequests() { return upRequests; }

    public TreeSet<Integer> getDownRequests() { return downRequests; }

    public void stopElevator() { this.isRunning= false;}

    @Override
    public void run() {
        while (isRunning) {
            move();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                isRunning = false;
            }
        }
    }
}
