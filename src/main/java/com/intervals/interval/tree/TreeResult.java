package com.intervals.interval.tree;

import java.util.ArrayList;
import java.util.List;

public class TreeResult {

    private List<String> owners = new ArrayList<>();
    private boolean result = true;

    public void addOwner(String owner) {
        this.owners.add(owner);
    }

    public List<String> getOwners() {
        return new ArrayList<>(owners);
    }

    public void printOwners() {
        System.out.println("This time is suitable for:");
        for (String owner : owners) {
            System.out.println(owner);
        }
    }

    public boolean getResult() {
        return result;
    }

    public void and(boolean partial, String owner) {
        if (partial) {
            addOwner(owner);
        }
        this.result &= partial;
    }
}