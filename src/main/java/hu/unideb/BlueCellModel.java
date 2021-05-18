package hu.unideb;

public class BlueCellModel extends CellModel {
    public BlueCellModel(int x, int y) {
        this.pos = new Position(x, y);
        this.targets = new Direction[] {Direction.NORTH, Direction.WEST};
    }
}
