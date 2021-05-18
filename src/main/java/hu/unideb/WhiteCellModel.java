package hu.unideb;

public class WhiteCellModel extends CellModel {
    public WhiteCellModel(int x, int y) {
        this.pos = new Position(x, y);
        this.targets = new Direction[] {Direction.NORTH};
    }
}
