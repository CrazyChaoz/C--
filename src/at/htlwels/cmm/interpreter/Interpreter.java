
package at.htlwels.cmm.interpreter;

import at.htlwels.cmm.compiler.*;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Interpreter {
    private Tab tab;
    private Obj obj;
   // private ProcedureStack procedureStack = new ProcedureStack();
   // private GlobalData globalData = new GlobalData();

    private int stackPointer, framePointer, GB = 0;

    /**
     * call stack
     */
    byte[] stack = new byte[32768];

    /**
     * global data
     */
    byte[] globalData;





    public Interpreter(Tab tab, Obj obj) {
        this.tab = tab;
        this.obj = obj;

    }

    public void statSeq(Node p) {
        for(p = p.left; p!= null; p=p.next) {
            statement(p);
        }
    }

    public void statement(Node p) {
        switch(p.kind) {
            case ASSIGN:
                switch(p.right.kind) {
                    //TODO: FINISH

                }
                break;
            case IF:
                if(condition(p.left)) statement(p.right);
                break;
            case IFELSE:
                if(condition(p.left)) {
                    statement(p.right);
                } else {
                    statement(p.right);
                }
                break;
            case WHILE:
                while(condition(p.left)) {
                    statement(p.right);
                }
                break;
                //TODO: FINISH
        }
    }

    public int intExpr(Node p) {
        return 0;
    }

    public boolean condition(Node p) {
        return true;
    }

    public void call(Node p) {
        //if(p.obj.name == "");
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
        if(obj.level == 0) return GB + obj.adr;

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
        stackPointer += proc.size;
       // storeLocals(proc);
    }

    public void disposeFrame() {
        stackPointer = framePointer - 12;
        framePointer = loadInt(stackPointer + 8);
    }

  /*  public void storeLocals(Obj proc) {
        Obj var;
        int curVarAdr = framePointer;
        for(var = proc.localScope.locals; (var != null) && (curVarAdr <= stackPointer); var = proc.localScope.locals.next) {
            switch(var.type) {
                case INT:
                    storeInt(curVarAdr, tab.find(var.name).val);
                    curVarAdr += 4;
                    break;
                case FLOAT:
                    storeFloat(curVarAdr, tab.find(var.name).fVal);
                    curVarAdr += 4;
                    break;
                case CHAR:
                    storeChar(curVarAdr, (char) tab.find(var.name).val);
                    curVarAdr++;
                    break;
                default:
                    break;

            }
        }
    }*/

    public Tab gettab() {
        return tab;
    }

    public void settab(Tab tab) {
        this.tab = tab;
    }


    /**
     * Stack operations
     */

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
