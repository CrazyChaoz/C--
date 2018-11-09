package at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME;

public interface Parser {
    boolean hasErrors();
    void addError(String message);
    void Parse();
    SymbolTable getSymbolTable();
}
