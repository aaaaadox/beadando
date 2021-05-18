package hu.unideb;

public class RedCellModel extends CellModel{
    public RedCellModel(int x, int y) {
        this.pos = new Position(x, y);
        this.targets = new Direction[] {Direction.NORTH, Direction.EAST};
    }
}
