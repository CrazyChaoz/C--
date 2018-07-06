
package at.htlwels.cmm.interpreter;

import at.htlwels.cmm.compiler.*;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Interpreter {
    private SymbolTable symbolTable;
    private Obj obj;
   // private ProcedureStack procedureStack = new ProcedureStack();
   // private GlobalData globalData = new GlobalData();

    private int stackPointer, framePointer, globalData = 0;

    /**
     * Procedure stack
     */
    byte[] stack = new byte[32768];


    /**
     * Loads 4 bytes from the stack starting at adr and returns them as an integer.
     * @param adr
     * @return
     */
    public int loadInt(int adr) {
        byte[] b = new byte[4];

        for(int i = 0; i < 4; i++) {
            b[i] = stack[adr+i];
        }

        return ByteBuffer.wrap(b).getInt();
    }

    /**
     * Loads 4 bytes from the stack starting at adr and returns them as a float
     * @param adr
     * @return
     */
    public float loadFloat(int adr) {
        byte[] b = new byte[4];

        for(int i = 0; i < 4; i++) {
            b[i] = stack[adr+i];
        }

        return ByteBuffer.wrap(b).getFloat();
    }

    /**
     * Loads a single byte from the stack at address adr and returns it as a casted char.
     * Keep in mind that the chars are strictly ASCII and not the java standard UTF-16.
     * @param adr
     * @return
     */
    public char loadChar(int adr) {
        return (char) stack[adr];
    }

    /**
     * Stores an integer in the stack starting at adr.
     * @param adr
     * @param val
     */
    public void storeInt(int adr, int val) {
        byte[] b = ByteBuffer.allocate(4).putInt(val).array();
        for(int i = 0; i < 4; i++) {
            stack[adr+i] = b[i];
        }
    }

    public void storeFloat(int adr, float val) {
        byte[] b = ByteBuffer.allocate(4).putFloat(val).array();
        for(int i = 0; i < 4; i++) {
            stack[adr+i] = b[i];
        }
    }

    public void storeChar(int adr, char val) {
        stack[adr] = (byte) val;
    }


    public Interpreter(SymbolTable symbolTable, Obj obj) {
        this.symbolTable = symbolTable;
        this.obj = obj;

        createFrame(symbolTable.find("main"));
    }

    public void statSeq(Node p) {
        for(p = p.left; p!= null; p=p.next) {
            p.toString();
        }
    }

    public void statement(Node p) {

    }

    public int intExpr(Node p) {
        return 0;
    }

    public boolean condition(Node p) {
        return true;
    }

    public void call(Node p) {

    }

    public int adr(Node p) {
        switch(p.kind) {
            case IDENT:
                return identAdr(p.obj);
            case DOT:
                return adr(p.left) + p.right.val;
            case INDEX:
                return adr(p.left) + intExpr(p.right);
            default:
                return framePointer;
        }
    }

    public int identAdr(Obj obj) {
        if(obj.level == 0) return globalData + obj.adr;

        return 0;

    }

    public void createFrame(Obj proc) {
        storeInt(stackPointer, proc.ast.line);
        stackPointer +=4;
        storeInt(stackPointer, proc.val);
        stackPointer +=4;
        storeInt(stackPointer, framePointer);
        stackPointer +=4;
        framePointer = stackPointer;
        //TODO: SP += proc.varSize
    }

    public void disposeFrame() {

    }

    public SymbolTable gettab() {
        return symbolTable;
    }

    public void settab(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    /**
     * Pre-declared standard procedures
     */
    public char read() {
        char ch = '\0';

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
