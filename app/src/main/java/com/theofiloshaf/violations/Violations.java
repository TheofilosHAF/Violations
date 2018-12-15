package com.theofiloshaf.violations;

import com.theofiloshaf.violations.ViolationEvent;

import java.util.ArrayList;
import java.util.Objects;

public class Violations {
    private ArrayList<ViolationEvent> violationEvents;

    public Violations(ArrayList<ViolationEvent> violationEvents) {
        this.violationEvents = violationEvents;
    }

    public ArrayList<ViolationEvent> getViolationEvents() {
        return violationEvents;
    }

    @Override
    public String toString() {
        return "Violations{" +
                "violationEvents=" + violationEvents +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Violations)) return false;
        Violations that = (Violations) o;
        return violationEvents.equals(that.violationEvents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(violationEvents);
    }
}
