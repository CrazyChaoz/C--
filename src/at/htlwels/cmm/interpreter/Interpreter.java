
package at.htlwels.cmm.interpreter;

import at.htlwels.cmm.compiler.*;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Interpreter {
    private SymbolTable tab;
    private int retAdr;
    //private Obj obj;
    // private ProcedureStack procedureStack = new ProcedureStack();
    // private GlobalData globalData = new GlobalData();

    private int stackPointer, framePointer, GB = 0;
    Strings stringStorage = new Strings();

    /**
     * call stack
     */
    byte[] stack = new byte[32768];

    /**
     * global data
     */
    byte[] globalData = new byte[32768];


    public Interpreter(SymbolTable tab) {
        this.tab = tab;
        storeGlobals();


        //this.obj = obj;

    }

    public void statSeq(Node p) {
        for (p = p.left; p != null; p = p.next) {
            statement(p);
        }
    }

    public void statement(Node p) {
        switch (p.kind) {
            case ASSIGN:
                switch (p.right.type.kind) {
                    case Type.INT:
                        storeInt(adr(p.left), intExpr(p.right));
                        break;
                    case Type.FLOAT:
                        storeFloat(adr(p.left), floatExpr(p.right));
                        break;
                    case Type.CHAR:
                        storeChar(adr(p.left), charExpr(p.right));
                        break;
                }
                break;
            case IF:
                if(p.right.kind==NodeKind.IFELSE) {
                    if (condition(p.left))
                        statement(p.right.left);
                    else
                        statement(p.right.right);
                }else if(condition(p.left))
                    statement(p.right);
                break;
            case WHILE:
                while (condition(p.left)) {
                    for(Node inner = p.right; inner != null; inner = inner.next) {
                        statement(inner);
                    }
                }
                break;
            case RETURN:
                retAdr = framePointer + p.left.obj.adr;
                break;
            case PRINT:
                switch(p.left.type.kind) {
                    case Type.INT:
                        print(intExpr(p.left));
                        break;
                    case Type.FLOAT:
                        print(floatExpr(p.left));
                        break;
                    case Type.CHAR:
                        print(charExpr(p.left));
                        break;
                    case Type.STRING:
                        break;
                    default:
                        System.out.println("ErrrrrAwrXd");
                }
                break;
        }
    }

    public int intExpr(Node p) {
        switch (p.kind) {
            case IDENT:
                if (p.obj.level > 0)
                    return loadInt(identAdr(p.obj));
                else
                    return globalLoadInt(identAdr(p.obj));
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
                return loadInt(retAdr);

            default:
                return 0;
        }
    }

    public float floatExpr(Node p) {
        switch (p.kind) {
            case IDENT:
                if (p.obj.level > 0)
                    return loadFloat(identAdr(p.obj));
                else
                    return globalLoadFloat(identAdr(p.obj));
            case FLOATCON:
                return p.fVal;
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
            case CALL:
                call(p);
                return loadFloat(retAdr);
            default:
                return 0f;
        }
    }

    public char charExpr(Node p) {
        switch (p.kind) {
            case IDENT:
                if (p.obj.level > 0)
                    return loadChar(identAdr(p.obj));
                else
                    return globalLoadChar(identAdr(p.obj));
            case I2C:
                return (char) intExpr(p.left);
            case CHARCON:
                return (char) p.val;
            case CALL:
                call(p);
                return loadChar(retAdr);
            default:
                return '0';
        }
    }

    public boolean condition(Node p) {
        switch(p.kind) {
            case IDENT:
                if (p.obj.level > 0)
                    return loadBool(identAdr(p.obj));
                else
                    return globalloadBool(identAdr(p.obj));
            case DOT:
                return loadBool(adr(p.left) + p.right.val);
            case INDEX:
                return loadBool(adr(p.left) + intExpr(p.right));
            default:
                switch(p.left.type.kind) {
                    case Type.INT:
                        switch(p.kind) {
                            case EQL:
                                return intExpr(p.left) == intExpr(p.right);
                            case NEQ:
                                return intExpr(p.left) != intExpr(p.right);
                            case GTR:
                                return intExpr(p.left) > intExpr(p.right);

                            case LSS:
                                return intExpr(p.left) < intExpr(p.right);

                            case GEQ:
                                return intExpr(p.left) >= intExpr(p.right);
                            case LEQ:
                                return intExpr(p.left) <= intExpr(p.right);

                            default:
                                return false;
                        }

                    case Type.FLOAT:
                        switch(p.kind) {
                            case EQL:
                                return floatExpr(p.left) == floatExpr(p.right);

                            case NEQ:
                                return floatExpr(p.left) != floatExpr(p.right);
                            case GTR:
                                return floatExpr(p.left) > floatExpr(p.right);

                            case LSS:
                                return floatExpr(p.left) < floatExpr(p.right);

                            case GEQ:
                                return floatExpr(p.left) >= floatExpr(p.right);
                            case LEQ:
                                return floatExpr(p.left) <= floatExpr(p.right);

                            default:
                                return false;
                        }
                }
        }
        return false;
    }

    public void call(Node p) {
        createFrame(p.left.obj);
        Obj formPar = p.left.obj.localScope.locals;
        for (Node actPar = p.right; actPar != null; actPar = actPar.next) {
            if (formPar.isRef) {
                storeInt(framePointer + formPar.adr, adr(actPar));
            } else {
                switch (formPar.type.kind) {
                    case Type.INT:
                        storeInt(framePointer + formPar.adr, intExpr(actPar));
                        break;
                    case Type.FLOAT:
                        storeFloat(framePointer + formPar.adr, floatExpr(actPar));
                        break;
                    case Type.CHAR:
                        storeChar(framePointer + formPar.adr, charExpr(actPar));
                        break;
                    case Type.STRING:
                        stringStorage.put(formPar.strVal);
                        break;
                }
            }

        }

        statSeq(p.left.obj.ast);
        disposeFrame();
    }

    public int adr(Node p) {
        switch (p.kind) {
            case IDENT:
                return identAdr(p.obj);
            case DOT:
                return adr(p.left) + p.right.obj.adr;
            case INDEX:
                return adr(p.left) + intExpr(p.right);
            default:
                return framePointer;
        }
    }

    public int identAdr(Obj obj) {
        if (obj.level == 0) return GB + obj.adr;
        else if (obj.kind == ObjKind.REFPAR) return loadInt(framePointer + obj.adr);
        else return framePointer + obj.adr;

    }

    public void createFrame(Obj proc) {
        //  storeInt(stackPointer, proc.);
//        storeInt(stackPointer, proc.val);
        storeInt(stackPointer, framePointer);
        framePointer = stackPointer;
        stackPointer += proc.localScope.size;
    }

    public void disposeFrame() {
        stackPointer = framePointer - 4;
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
     *
     * @param adr
     * @return
     */
    public int globalLoadInt(int adr) {
        byte[] b = new byte[4];

        for (int i = 0; i < 4; i++) {
            b[i] = globalData[adr + i];
        }

        return ByteBuffer.wrap(b).getInt();
    }

    /**
     * Loads 4 bytes from the stack starting at adr and returns them as a float
     *
     * @param adr
     * @return
     */
    public float globalLoadFloat(int adr) {
        byte[] b = new byte[4];

        for (int i = 0; i < 4; i++) {
            b[i] = globalData[adr + i];
        }

        return ByteBuffer.wrap(b).getFloat();
    }

    /**
     * Loads a single byte from the stack at address adr and returns it as a casted char.
     * Keep in mind that the chars are strictly ASCII and not the java standard UTF-16.
     *
     * @param adr
     * @return
     */
    public char globalLoadChar(int adr) {
        return (char) globalData[adr];
    }

    /**
     * Stores an integer in the stack starting at adr.
     *
     * @param adr
     * @param val
     */
    public void globalStoreInt(int adr, int val) {
        byte[] b = ByteBuffer.allocate(4).putInt(val).array();
        for (int i = 0; i < 4; i++) {
            globalData[adr + i] = b[i];
            GB++;
        }
    }

    public void globalStoreFloat(int adr, float val) {
        byte[] b = ByteBuffer.allocate(4).putFloat(val).array();
        for (int i = 0; i < 4; i++) {
            globalData[adr + i] = b[i];
            GB++;
        }
    }

    public void globalStoreChar(int adr, char val) {
        globalData[adr] = (byte) val;
        GB++;
    }


    /**
     * Loads 4 bytes from the stack starting at adr and returns them as an integer.
     *
     * @param adr
     * @return
     */
    public int loadInt(int adr) {
        byte[] b = new byte[4];

        for (int i = 0; i < 4; i++) {
            b[i] = stack[adr + i];
        }

        return ByteBuffer.wrap(b).getInt();
    }

    /**
     * Loads 4 bytes from the stack starting at adr and returns them as a float
     *
     * @param adr
     * @return
     */
    public float loadFloat(int adr) {
        byte[] b = new byte[4];

        for (int i = 0; i < 4; i++) {
            b[i] = stack[adr + i];
        }

        return ByteBuffer.wrap(b).getFloat();
    }

    /**
     * Loads a single byte from the stack at address adr and returns it as a casted char.
     * Keep in mind that the chars are strictly ASCII and not the java standard UTF-16.
     *
     * @param adr
     * @return
     */
    public char loadChar(int adr) {
        return (char) stack[adr];
    }

    /**
     * Stores an integer in the stack starting at adr.
     *
     * @param adr
     * @param val
     */
    public void storeInt(int adr, int val) {
        byte[] b = ByteBuffer.allocate(4).putInt(val).array();
        for (int i = 0; i < 4; i++) {
            stack[adr + i] = b[i];
            stackPointer++;
        }
    }

    public void storeFloat(int adr, float val) {
        byte[] b = ByteBuffer.allocate(4).putFloat(val).array();
        for (int i = 0; i < 4; i++) {
            stack[adr + i] = b[i];
            stackPointer++;
        }
    }

    public void storeChar(int adr, char val) {
        stack[adr] = (byte) val;
        stackPointer++;
    }

    public void globalStoreBool(int adr, boolean val) {
        byte b = (byte)(val?1:0);

        globalData[adr] = b;
    }

    public boolean globalloadBool(int adr) {
        return (int)globalData[adr] == 1;
    }

    public void storeBool(int adr, boolean val) {
        byte b = (byte)(val?1:0);

        stack[adr] = b;
    }

    public boolean loadBool(int adr) {
        return ((int)stack[adr] == 1);
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

    public void print(int ch) {
        System.out.print(ch);
    }

    public void print(char ch) {
        System.out.print(ch);
    }

    public void print(float ch) {
        System.out.print(ch);
    }

    public int length(String s) {
        return s.length();
    }


    public void dumpStack() {
        System.out.println();
        System.out.println("#~#  DUMP  #~#  STACK");
        System.out.println();
        System.out.println("#~#  -------------  #~#");
        for (int i = 0; i < stack.length; i++) {
            if (stack[i] != 0)
                System.out.println("Position " + i + "\t= " + stack[i] + "\t| " + Integer.toString(stack[i], 2) + "\t| " + Integer.toString(stack[i], 16));
        }
        System.out.println("#~#  -------------  #~#");
        System.out.println();
    }

    public void dumpGlobalData() {
        System.out.println();
        System.out.println("#~#  DUMP  #~#  GLOBAL DATA");
        System.out.println();
        System.out.println("#~#  -------------  #~#");
        for (int i = 0; i < globalData.length; i++) {
            if (globalData[i] != 0)
                System.out.println("Position " + i + "\t= " + globalData[i] + "\t| " + Integer.toString(globalData[i], 2) + "\t| " + Integer.toString(globalData[i], 16));
        }
        System.out.println("#~#  -------------  #~#");
        System.out.println();
    }

    public void dumpStringStorage() {
        System.out.println();
        System.out.println("#~#  DUMP  #~#  STRING STORAGE");
        System.out.println();
        System.out.println("#~#  -------------  #~#");
        for (int i = 0; i < globalData.length; i++) {
            if (globalData[i] != 0)
                System.out.println("Position " + i + "\t= " + globalData[i]);
        }
        System.out.println("#~#  -------------  #~#");
        System.out.println();
    }

    public void storeGlobals() {
        for (Obj globVar = tab.curScope.locals; globVar != null; globVar = globVar.next)
            if (globVar.kind == ObjKind.CON || globVar.kind == ObjKind.VAR) {
                switch (globVar.type.kind) {
                    case Type.INT:
                        globalStoreInt(globVar.adr, globVar.val);
                        break;
                    case Type.FLOAT:
                        globalStoreFloat(globVar.adr, globVar.fVal);
                        break;
                    case Type.CHAR:
                        globalStoreChar(globVar.adr, (char) globVar.val);
                        break;
                    case Type.STRING:
                        stringStorage.put(globVar.strVal);
                        break;
                }
            }
    }

}
