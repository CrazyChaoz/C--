//package at.htlwels.cmm.interpreter;
//
//import java.io.IOException;
//
//public class Interpreter {
//    private SymbolTable vTab;
//    private int stackPointer;
//    private int framePointer;
//
//
//    public Interpreter(SymbolTable vTab) {
//        this.vTab = vTab;
//    }
//
//    /**
//     * Dummy constructor without parameters
//     */
//
//    public Interpreter() {
//
//    }
//
//    void statSeq(SyntaxNode p) {
//
//    }
//
//    void statement(SyntaxNode p) {
//
//    }
//
//    int intExpr(SyntaxNode p) {
//        return 0;
//    }
//
//    boolean condition(SyntaxNode p) {
//        return true;
//    }
//
//    void call(SyntaxNode p) {
//
//    }
//
//    int adr(SyntaxNode p) {
//        return 0;
//    }
//
//    int identAdr() {
//        return 0;
//    }
//
//    void createFrame() {
//
//    }
//
//    void disposeFrame() {
//
//    }
//
//    public SymbolTable getvTab() {
//        return vTab;
//    }
//
//    public void setvTab(SymbolTable vTab) {
//        this.vTab = vTab;
//    }
//
//    //Predeclared standard procedures
//    public char read() {
//        char ch = '0';
//
//        try {
//            ch = (char) System.in.read();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//            return ch;
//    }
//
//    public void print(char ch) {
//        System.out.print(ch);
//    }
//
//    public int length(String s) {
//        return s.length();
//    }
//
//}
