package javachess.parser;

public class RoqueInstruction implements Instruction {
    boolean isGrandRoque;

    public RoqueInstruction(boolean isGrandRoque) {
        this.isGrandRoque = isGrandRoque;
    }
}
