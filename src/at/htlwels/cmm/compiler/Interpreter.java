package at.htlwels.cmm.compiler;

public class Interpreter {
    private SymbolTable vTab;
    private SymbolTable fTab;

    public Interpreter(SymbolTable vTab, SymbolTable fTab) {
        this.vTab = vTab;
        this.fTab = fTab;
    }

    void StatSeq(SyntaxNode p) {

    }

    public SymbolTable getvTab() {
        return vTab;
    }

    public void setvTab(SymbolTable vTab) {
        this.vTab = vTab;
    }

    public SymbolTable getfTab() {
        return fTab;
    }

    public void setfTab(SymbolTable fTab) {
        this.fTab = fTab;
    }
}
