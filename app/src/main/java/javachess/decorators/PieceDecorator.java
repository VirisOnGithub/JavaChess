package javachess.decorators;

import javachess.Cell;

import java.util.ArrayList;

public interface PieceDecorator {
    ArrayList<Cell> getValidCells();
}
