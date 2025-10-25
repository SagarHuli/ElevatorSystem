package com.elevatorsystem;


import com.elevatorsystem.enums.Trajectory;

public class ElevatorDemo {

    public static void main(String[] ar) throws InterruptedException {
        int numElevators = 2;

        ElevatorSystem elevatorSystem = ElevatorSystem.getInstance(numElevators);

        elevatorSystem.start();
        // --- SIMULATION START ---

        // 1. External Request: User at floor 5 wants to go UP.
        elevatorSystem.requestElevator(5, Trajectory.UP);
        Thread.sleep(100); // Wait for the elevator to start moving

        // 2. Internal Request
        elevatorSystem.selectFloor("E1", 10);
        Thread.sleep(200);

        // 3. External Request: User at floor 3 wants to go DOWN.
        elevatorSystem.requestElevator(3, Trajectory.DOWN);
        Thread.sleep(300);

        // 4. Internal Request: User in E2 presses 1.
        elevatorSystem.selectFloor("E2", 1);

        Thread.sleep(1000);

        // Shutdown the system
        elevatorSystem.shutDown();
    }
}
