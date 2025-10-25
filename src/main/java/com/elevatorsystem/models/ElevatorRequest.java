package com.elevatorsystem.models;


import com.elevatorsystem.enums.RequestSource;
import com.elevatorsystem.enums.Trajectory;

public class ElevatorRequest {

    private final int targetFloor;
    private final Trajectory direction;
    private final RequestSource requestSource;

    public ElevatorRequest(int targetFloor, Trajectory direction, RequestSource requestSource) {
        this.targetFloor = targetFloor;
        this.requestSource = requestSource;
        this.direction = direction;
    }

    public Trajectory getDirection() {
        return this.direction;
    }

    public int getTargetFloor() {
        return this.targetFloor;
    }

    public RequestSource getRequestSource() {
        return this.requestSource;
    }

    @Override
    public String toString() {
        return requestSource + " Request to Floor "+ targetFloor +
                (requestSource == RequestSource.EXTERNAL ? "going " + direction : "");
    }
}
