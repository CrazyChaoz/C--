
package at.htlwels.DIPLOMARBEITSTITEL.interpreter;

import at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME.*;
import at.htlwels.DIPLOMARBEITSTITEL.error.DivisionByZeroSin;
import at.htlwels.DIPLOMARBEITSTITEL.error.ExprSin;
import at.htlwels.DIPLOMARBEITSTITEL.error.Sin;

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


	boolean isReturned = false;



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

	public void startProgramFrom(String entryPoint) {
		this.statSeq(tab.find(entryPoint).ast);
	}

	public void statSeq(Node p) {
		for (p = p.left; p != null && !isReturned; p = p.next) {
			//TODO: Implement Single Stepping
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
					case Type.STRING:
						storeString(adr(p.left), stringExpr(p.right));
						break;
				}
				break;
			case IF:
				if (p.right.kind == NodeKind.IFELSE) {
					if (condition(p.left))
						for (Node node = p.right.left; node != null && !isReturned; node = node.next)
							statement(node);
					else
						for (Node node = p.right.right; node != null && !isReturned; node = node.next)
							statement(node);
				} else if (condition(p.left))
					for (Node node = p.right; node != null && !isReturned; node = node.next)
						statement(node);
				break;
			case WHILE:
				while (condition(p.left)) {
					for (Node inner = p.right; inner != null; inner = inner.next) {
						statement(inner);
					}
				}
				break;
			case RETURN:

				switch (p.left.kind) {
					case FLOATCON:
						retAdr = stackPointer;
						storeFloat(stackPointer, p.left.fVal);
						break;
					case CHARCON:
						retAdr = stackPointer;
						storeChar(stackPointer, (char) p.left.val);
					case INTCON:
						retAdr = stackPointer;
						storeInt(stackPointer, p.left.val);
						break;
					case STRINGCON:
						retAdr = stackPointer;
						storeString(stackPointer, p.left.strVal);
						break;
					case IDENT:
					case INDEX:
					case DOT:
						retAdr = adr(p.left);
						break;

				}

				isReturned = true;
				break;
			case PRINT:
				switch (p.left.type.kind) {
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
						print(stringExpr((p.left)));
						break;
					default:
						System.err.println("ERROR IN PRINT");
				}
				break;
		}

	}

	public int intExpr(Node p) {
		switch (p.kind) {
			case IDENT:
				if (p.obj.level > 0) {
					return loadInt(identAdr(p.obj));
				} else
					return globalLoadInt(identAdr(p.obj));
			case INTCON:
				return p.val;
			case DOT:
				return loadInt(adr(p.left) + p.right.obj.adr);
			case INDEX:
				return loadInt(adr(p.left) + intExpr(p.right) * 4);
			case PLUS:
				return intExpr(p.left) + intExpr(p.right);
			case MINUS:
				return intExpr(p.left) - intExpr(p.right);
			case DIV:
				int asdf = intExpr(p.right);
				if (asdf == 0)
					Sin.commit(new DivisionByZeroSin());
				return intExpr(p.left) / asdf;
			case TIMES:
				return intExpr(p.left) * intExpr(p.right);
			case C2I:
				return (int) charExpr(p.left);
			case F2I:
				return (int) floatExpr(p.left);
			case READINT:
				return readInt();
			case CALL:
				call(p);
				isReturned = false;
				return loadInt(retAdr);

			default:
				Sin.commit(new ExprSin("IntExpr, " + p.kind));
		}
		return 0;
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
				return loadFloat(adr(p.left) + p.right.obj.adr);
			case INDEX:
				return loadFloat(adr(p.left) + intExpr(p.right) * 4);
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
				isReturned = false;
				return loadFloat(retAdr);
			default:
				Sin.commit(new ExprSin("FloatExpr"));
		}
		return 0;
	}

	public char charExpr(Node p) {
		switch (p.kind) {
			case IDENT:
				if (p.obj.level > 0)
					return loadChar(identAdr(p.obj));
				else
					return globalLoadChar(identAdr(p.obj));

			case DOT:
				return loadChar(adr(p.left) + p.right.obj.adr);
			case INDEX:
				return loadChar(adr(p.left) + intExpr(p.right));
			case I2C:
				return (char) intExpr(p.left);
			case CHARCON:
				return (char) p.val;
			case READCHAR:
				return readChar();
			case CALL:
				call(p);
				isReturned = false;
				return loadChar(retAdr);
			default:
				Sin.commit(new ExprSin("CharExpr"));
		}
		return '0';
	}

	public String stringExpr(Node p) {
		switch (p.kind) {
			case IDENT:
				if (p.obj.level > 0) {
					return stringStorage.get(loadInt(identAdr(p.obj)));
				} else {
					return stringStorage.get(globalLoadInt(identAdr(p.obj)));
				}
			case DOT:
				return stringStorage.get((adr(p.left) + p.right.obj.adr));
			case INDEX:
				return stringStorage.get((adr(p.left) + intExpr(p.right)));
			case STRINGCON:
				return p.strVal;
			case CALL:
				call(p);
				isReturned = false;
				return stringStorage.get(loadInt(retAdr));
			default:
				Sin.commit(new ExprSin("StringExpr"));
		}
		return "error";
	}

	public boolean condition(Node p) {
		switch (p.kind) {
			case IDENT:
				if (p.obj.level > 0)
					return loadBool(identAdr(p.obj));
				else
					return globalloadBool(identAdr(p.obj));
			case DOT:
				return loadBool(adr(p.left) + p.right.val);
			case INDEX:
				return loadBool(adr(p.left) + intExpr(p.right));

			case AND:
				return condition(p.left) && condition(p.right);
			case OR:
				return condition(p.left) || condition(p.right);

			default:
				switch (p.left.type.kind) {
					case Type.INT:
						switch (p.kind) {
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
						switch (p.kind) {
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

		int fp = framePointer;
		createFrame(p.left.obj);
		int fp2 = framePointer;
		framePointer = fp;

		Obj formPar = p.left.obj.localScope.locals;

		for (Node actPar = p.right; actPar != null && formPar != null; actPar = actPar.next, formPar = formPar.next) {
			if (formPar.isRef) {
				storeInt(fp2 + formPar.adr, adr(actPar));
			} else {
				switch (formPar.type.kind) {
					case Type.INT:
						storeInt(fp2 + formPar.adr, intExpr(actPar));
						break;
					case Type.FLOAT:
						storeFloat(fp2 + formPar.adr, floatExpr(actPar));
						break;
					case Type.CHAR:
						storeChar(fp2 + formPar.adr, charExpr(actPar));
						break;
					case Type.STRING:
						stringStorage.put(actPar.strVal);
						break;
				}
			}

		}

		framePointer = fp2;
		statSeq(p.left.obj.ast);
		disposeFrame(p.left.obj);
	}

	public int adr(Node p) {
		switch (p.kind) {
			case IDENT:
				return identAdr(p.obj);
			case DOT:
				return adr(p.left) + adr(p.right);
			case INDEX:
				return adr(p.left) + intExpr(p.right) * p.type.size;
			default:
				return framePointer;
		}
	}

	public int identAdr(Obj obj) {
		if (obj.level == 0)
			return GB + obj.adr;
		else if(obj.isRef)
			return loadInt(loadInt(framePointer+obj.adr));
		else
			return framePointer + obj.adr;

	}

	public void createFrame(Obj proc) {
//	    System.out.println("CREATING FRAME");
		storeInt(stackPointer, framePointer);
		framePointer = stackPointer;
//	    System.out.println("FRAMEPOINTER:"+framePointer);
//	    System.out.println("STACKPOINTER:"+stackPointer);
//	    System.out.println("FRAME SIZE:"+proc.localScope.size);
		//stackPointer += proc.localScope.size;
	}

	public void disposeFrame(Obj proc) {
//	    System.out.println("DISPOSING FRAME");
//	    System.out.println("FRAMEPOINTER:"+framePointer);
//	    System.out.println("STACKPOINTER:"+stackPointer);
//        stackPointer = framePointer - 4;
		framePointer = loadInt(framePointer - 4);

//	    System.out.println("FRAMEPOINTER:"+framePointer);
//	    System.out.println("STACKPOINTER:"+stackPointer);
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
		int retval;

		for (int i = 0; i < 4; i++) {
			b[i] = stack[adr + i];
		}

		retval = ByteBuffer.wrap(b).getInt();
//		System.out.println("LOAD::\tAddress: "+adr+", \tValue: "+retval);

		return retval;
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


//		System.out.println("STORE::\tAddress: "+adr+", \tValue: "+val);


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

	public void globalStoreString(int adr, String val) {

		int localAdr = stringStorage.put(val);

		byte[] b = ByteBuffer.allocate(4).putInt(localAdr).array();
		for (int i = 0; i < 4; i++) {
			globalData[adr + i] = b[i];
			GB++;
		}
	}

	public String globalloadString(int adr) {
		return stringStorage.get(loadInt(adr));
	}

	public void storeString(int adr, String val) {
		int localAdr = stringStorage.put(val);

		byte[] b = ByteBuffer.allocate(4).putInt(localAdr).array();
		for (int i = 0; i < 4; i++) {
			stack[adr + i] = b[i];
			stackPointer++;
		}
	}

	public String loadString(int adr) {
		return stringStorage.get(loadInt(adr));
	}

	public void globalStoreBool(int adr, boolean val) {
		byte b = (byte) (val ? 1 : 0);

		globalData[adr] = b;
		GB++;
	}

	public boolean globalloadBool(int adr) {
		return (int) globalData[adr] == 1;
	}

	public void storeBool(int adr, boolean val) {
		byte b = (byte) (val ? 1 : 0);

		stack[adr] = b;
		stackPointer++;
	}

	public boolean loadBool(int adr) {
		return ((int) stack[adr] == 1);
	}


	/**
	 * Pre-declared standard procedures
	 */
	public int readInt() {
		int ch = 0;

		try {
			ch = new java.util.Scanner(System.in).nextInt();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ch;
	}


	public char readChar() {
		char ch = '\0';

		try {
			ch = (char) System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ch;
	}


	public void print(int ch) {
		System.out.println(ch);
	}

	public void print(char ch) {
		System.out.print(ch);
	}

	public void print(float ch) {
		System.out.print(ch);
	}

	public void print(String s) {
		System.out.println(s);
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
				System.out.println("Position " + i + /*\n\tValue:*/ "\t\t\t| " + stack[i] /*+ "\n\tBinary: \t\t| " + Integer.toString(stack[i], 2) + "\n\tHexadecimal: \t| " + Integer.toString(stack[i], 16)*/);
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
				System.out.println("Position " + i + "\t= " + globalData[i] + "\n\t\t\t| " + Integer.toString(globalData[i], 2) + "\n\t\t\t| " + Integer.toString(globalData[i], 16));
		}
		System.out.println("#~#  -------------  #~#");
		System.out.println();
	}

	public void dumpStringStorage() {
		System.out.println();
		System.out.println("#~#  DUMP  #~#  STRING STORAGE");
		System.out.println();
		System.out.println("#~#  -------------  #~#");
		for (byte b : stringStorage.data) {
			if (b != 0)
				System.out.println((char) b);
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