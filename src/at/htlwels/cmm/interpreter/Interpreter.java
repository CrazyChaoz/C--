package at.htlwels.cmm.interpreter;

import at.htlwels.cmm.compiler.*;

import java.io.IOException;

public class Interpreter {
    private SymbolTable vTab;
    private ProcedureStack procedureStack = new ProcedureStack();
    private GlobalData globalData = new GlobalData();


    public Interpreter(SymbolTable vTab) {
        this.vTab = vTab;
    }

    /**
     * Dummy constructor without parameters
     */

    public Interpreter() {

    }

    public void statSeq(SyntaxNode p) {

    }

    public void statement(SyntaxNode p) {

    }

    public int intExpr(SyntaxNode p) {
        return 0;
    }

    public boolean condition(SyntaxNode p) {
        return true;
    }

    public void call(SyntaxNode p) {

    }

    public int adr(SyntaxNode p) {
        return 0;
    }

    public int identAdr() {
        return 0;
    }

    public void createFrame() {
        //procedureStack.storeInt(procedureStack.stack[procedureStack.getFramePointer()]++, 2);
    }

    public void disposeFrame() {

    }

    public SymbolTable getvTab() {
        return vTab;
    }

    public void setvTab(SymbolTable vTab) {
        this.vTab = vTab;
    }

    //Predeclared standard procedures
    public char read() {
        char ch = '0';

        try {
            ch = (char) System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
            return ch;
    }

    public void print(char ch) {
        System.out.print(ch);
    }

    public int length(String s) {
        return s.length();
    }

}
