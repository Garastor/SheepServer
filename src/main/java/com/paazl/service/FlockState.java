package com.paazl.service;

public class FlockState {
    private final int numberOfHealthySheep;
    private final int numberOfDeadSheep;

    public FlockState (
            int numberOfHealthySheep,
            int numberOfDeadSheep) {
        this.numberOfHealthySheep = numberOfHealthySheep;
        this.numberOfDeadSheep = numberOfDeadSheep;
    }

    public int getNumberOfHealthySheep() {
        return numberOfHealthySheep;
    }

    public int getNumberOfDeadSheep() {
        return numberOfDeadSheep;
    }
}