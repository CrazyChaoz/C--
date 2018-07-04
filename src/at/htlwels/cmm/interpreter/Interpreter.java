package at.htlwels.cmm.interpreter;

import at.htlwels.cmm.compiler.*;

public class Interpreter {
    private SymbolTable vTab;

    public Interpreter(SymbolTable vTab) {
        this.vTab = vTab;
    }

    /**
     * Dummy constructor without parameters
     */

    public Interpreter() {

    }

    void statSeq(SyntaxNode p) {

    }

    void statement(SyntaxNode p) {

    }

    int intExpr(SyntaxNode p) {
        return 0;
    }

    boolean condition(SyntaxNode p) {
        return true;
    }

    void call(SyntaxNode p) {

    }

    int adr(SyntaxNode p) {
        return 0;
    }

    int identAdr() {
        return 0;
    }

    void createFrame() {
    }

    void disposeFrame() {

    }

    public SymbolTable getvTab() {
        return vTab;
    }

    public void setvTab(SymbolTable vTab) {
        this.vTab = vTab;
    }
}
