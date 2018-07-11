
package at.htlwels.cmm.interpreter;

import at.htlwels.cmm.compiler.*;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Interpreter {
    private SymbolTable tab;
    //private Obj obj;
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





    public Interpreter(SymbolTable tab) {
        this.tab = tab;
        //this.obj = obj;

    }

    public void statSeq(Node p) {
        for(p = p.left; p!= null; p=p.next) {
            statement(p);
        }
    }

    public void statement(Node p) {
        switch(p.kind) {
            case ASSIGN:
                switch(p.right.type.kind) {
                    case Type.INT:
                        storeInt(adr(p.left), intExpr(p.right));
                        break;
                    case Type.FLOAT:
                        storeFloat(adr(p.left), intExpr(p.right));
                        break;
                    case Type.CHAR:
                        storeChar(adr(p.left), charExpr(p.right));
                        break;
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
        }
    }

    public int intExpr(Node p) {
        switch(p.kind) {
            case IDENT:
                return loadInt(identAdr(p.obj));
            case INTCON:
                return p.val;
            case DOT:
                return loadInt(adr(p.left) + p.right.val);
            case INDEX:
                return loadInt(adr(p.left)) + intExpr(p.right);
            case PLUS:
                return intExpr(p.left) + intExpr(p.right);
            case MINUS:
                return intExpr(p.left) - intExpr(p.right);
            case DIV:
                return intExpr(p.left) / intExpr(p.right);
            case TIMES:
                return intExpr(p.left) * intExpr(p.right);
            case C2I:
                return (int) charExpr(p.left);
            case F2I:
                return (int) floatExpr(p.left);
            case CALL:
                call(p);

            default: return 0;
        }
    }

    public float floatExpr(Node p) {
        switch(p.kind) {
            case IDENT:
                return loadFloat(identAdr(p.obj));
            case FLOATCON:
                return p.val;
            case DOT:
                return loadFloat(adr(p.left) + p.right.val);
            case INDEX:
                return loadFloat(adr(p.left)) + intExpr(p.right);
            case PLUS:
                return floatExpr(p.left) + floatExpr(p.right);
            case MINUS:
                return floatExpr(p.left) - floatExpr(p.right);
            case DIV:
                return floatExpr(p.left) / floatExpr(p.right);
            case TIMES:
                return floatExpr(p.left) * floatExpr(p.right);
            case I2F:
                return (float) intExpr(p.left);
            default: return 0f;
        }
    }

    public char charExpr(Node p) {
        switch(p.kind) {
            case IDENT:
                return loadChar(identAdr(p.obj));
            case I2C:
                return (char) intExpr(p.left);
            case CHARCON:
                return (char) p.val;
            default:
                return '0';
        }
    }

    public boolean condition(Node p) {
        return true;
    }

    public void call(Node p) {
        createFrame(p.obj);
        Obj formPar = p.obj.localScope.locals;
        for(Node actPar = p.left; actPar != null; actPar = actPar.next) {

        }
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
        else if (obj.kind == ObjKind.REFPAR) return loadInt(framePointer + obj.adr);
        else return framePointer + obj.adr;

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

    public SymbolTable gettab() {
        return tab;
    }

    public void settab(SymbolTable tab) {
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
