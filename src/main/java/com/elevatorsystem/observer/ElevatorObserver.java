package com.elevatorsystem.observer;


import com.elevatorsystem.entities.Elevator;

public interface ElevatorObserver {
    void update(Elevator elevator);
}
