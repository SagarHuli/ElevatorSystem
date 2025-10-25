package com.elevatorsystem.observer;


import com.elevatorsystem.entities.Elevator;

import java.util.ArrayList;
import java.util.List;

public abstract class ElevatorSubject {
    List<ElevatorObserver> observers = new ArrayList<>();

    public void addObserver(ElevatorObserver observer) {
        this.observers.add(observer);
        observer.update((Elevator) this); // Send initial state
    }

    public void removeObserver(ElevatorObserver observer) {
        this.observers.remove(observer);
    }

    public void notifyObservers() {
        for(ElevatorObserver observer : this.observers) {
            observer.update((Elevator) this);
        }
    }
}
