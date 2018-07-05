
package at.htlwels.cmm.interpreter;

import at.htlwels.cmm.compiler.*;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Interpreter {
    private Tab tab;
    private Obj obj;
   // private ProcedureStack procedureStack = new ProcedureStack();
   // private GlobalData globalData = new GlobalData();

    private int stackPointer, framePointer, globalData = 0;

    byte[] stack = new byte[32768];

    public int loadInt(int adr) {
        byte[] b = new byte[4];

        for(int i = 0; i < 4; i++) {
            b[i] = stack[adr+i];
        }

        return ByteBuffer.wrap(b).getInt();
    }

    public float loadFloat(int adr) {
        byte[] b = new byte[4];

        for(int i = 0; i < 4; i++) {
            b[i] = stack[adr+i];
        }

        return ByteBuffer.wrap(b).getFloat();
    }

    public char loadChar(int adr) {
        return (char) stack[adr];
    }

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


    public Interpreter(Tab tab, Obj obj) {
        this.tab = tab;
        this.obj = obj;

        statSeq(obj.ast);
    }

    /**
     * Dummy constructor without parameters
     */

    public Interpreter() {

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
        return 0;
    }

    public int identAdr(Obj obj) {
        if(obj.level == 0) return globalData + obj.adr;

        return 0;

    }

    public void createFrame(Obj proc) {
        storeInt(stackPointer, 4);
        stackPointer +=4;
        storeInt(stackPointer, proc.val);
        stackPointer +=4;
        storeInt(stackPointer, framePointer);
        stackPointer +=4;
        framePointer = stackPointer;
        stackPointer += 5;
    }

    public void disposeFrame() {

    }

    public Tab gettab() {
        return tab;
    }

    public void settab(Tab tab) {
        this.tab = tab;
    }

    //Predeclared standard procedures
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
