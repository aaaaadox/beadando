package hu.unideb;

import java.util.List;

public class PlayerModel {
    private String name;
    private Position pos;
    private Direction prevStep;
    private List<Direction> prevSteps;

    public String getName() {
        return name;
    }

    public Position getPos() {
        return pos;
    }

    public Direction getPrevStep() {
        return prevStep;
    }

    public List<Direction> getPrevSteps() {
        return prevSteps;
    }

    public void move(Direction d) {
        pos.setTarget(d);
        prevStep = d;
        prevSteps.add(d);
    }
}
