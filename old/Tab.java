package at.htlwels.cmm.compiler.old;

/*--------------------------------------------------------------------------------
Tab   Symbol table for C--
===   ====================
The symbol table is a stack of scopes
- universe: contains predeclared names
- global scope: contains the globally declared names
- local scope: contains the local names of a procedure

The symbol table has methods for
- opening and closing scopes
- inserting and retrieving named objects
- checking of forward declarations
- utilities for converting strings to constants
--------------------------------------------------------------------------------*/


import at.htlwels.cmm.compiler.Parser;
import jdk.nashorn.internal.ir.debug.ObjectSizeCalculator;

public class Tab {
    public Scope curScope;             // current scope
    public int curLevel;             // nesting level of current scope

	public static Type intType;    // predefined types
	public static Type floatType;
	public static Type charType;
	public static Type boolType;
	public static Type noType;
	public static Obj noObj;		     // predefined objects

    private Parser parser;           // for error messages

    //------------------ scope management ---------------------

    public void openScope() {
        Scope newScope=new Scope();
        newScope.outer=curScope;
        curScope = newScope;
        curLevel++;
    }

    public void closeScope() {
        curScope = curScope.outer;
        curLevel--;
    }

    //------------- Object insertion and retrieval --------------

    // Create a new object with the given kind, name and type
    // and insert it into the current scope.
    public Obj insert(int kind, String name, Type type) {

        Obj object = new Obj(kind, name, type);
        curScope.locals.next = object;
        curScope.size += ObjectSizeCalculator.getObjectSize(object);
        return object;
    }

    // Look up the object with the given name in all open scopes.
    // Report an error if not found.
    public Obj find(String name) {
        // TODO
        return null;
    }

	// Retrieve a struct field with the given name from the fields of "type"
	public Obj findField(String name, Type type) {
		// TODO
		return null;
	}

    // Look up the object with the given name in the current scope.
    // Return noObj if not found.
    public Obj lookup(String name) {
        // TODO
        return null;
    }

    //----------------- handling of forward declaration  -----------------

    // Check if parameters of forward declaration and actual declaration match
    public void checkForwardParams(Obj oldPar, Obj newPar) {
        // TODO
    }

    // Check if all forward declarations were resolved at the end of the program
    public void checkIfForwardsResolved(Scope scope) {
        // TODO
    }

    //---------------- conversion of strings to constants ----------------

	// Convert a digit string into an int
	public int intVal(String s) {
		return Integer.parseInt(s);
	}

	// Convert a string representation of a float constant into a float value
	public float floatVal(String s) {
		return Float.parseFloat(s);
	}

	// Convert a string representation of a char constant into a char value
	public char charVal(String s) {
		return s.charAt(0);
	}

    //---------------- methods for dumping the symbol table --------------

	// Print a type
	public void dumpStruct(Type type, int indent) {
		switch (type.kind) {
			case Type.INT:
			  System.out.print("Int(" + type.size + ")"); break;
			case Type.FLOAT:
			  System.out.print("Float(" + type.size + ")"); break;
			case Type.CHAR:
			  System.out.print("Char(" + type.size + ")"); break;
			case Type.BOOL:
				System.out.print("Bool(" + type.size + ")"); break;
			case Type.ARR:
			  System.out.print("Arr[" + type.elements + "(" + type.size + ")] of ");
			  dumpStruct(type.elemType, indent);
			  break;
			case Type.STRUCT:
			  System.out.println("Type(" + type.size + ") {");
			  for (Obj o = type.fields; o != null; o = o.next) dumpObj(o, indent + 1);
			  for (int i = 0; i < indent; i++) System.out.print("  ");
			  System.out.print("}");
			  break;
			default:
			  System.out.print("None"); break;
		}
	}

    // Print an object
    public void dumpObj(Obj o, int indent) {
        for (int i = 0; i < indent; i++)
            System.out.print("\t");
        switch (o.kind) {
            case Obj.CON:
                System.out.print("Constant " + o.name);
                if (o.type == Tab.floatType) System.out.print(" fVal=" + o.fVal);
                else System.out.print(" val=" + o.val);
                break;
            case Obj.VAR:
                System.out.print("Variable " + o.name + " adr=" + o.adr + " level=" + o.level);
                if (o.isRef) System.out.print(" isRef");
                break;
            case Obj.TYPE:
                System.out.print("Type " + o.name);
                break;
            case Obj.PROC:
                System.out.println("Procedure " + o.name + " size=" + o.size + " nPars=" + o.nPars + " isForw=" + o.isForward + " {");
                dumpScope(o.locals, indent + 1);
                System.out.print("}");
                break;
            default:
                System.out.print("None " + o.name);
                break;
        }
        System.out.print("\t");
        dumpStruct(o.type, indent);
        System.out.println();
    }

    // Print all objects of a scope
    public void dumpScope(Obj head, int indent) {
        for (Obj o = head; o != null; o = o.next)
            dumpObj(o, indent);
    }


    //-------------- initialization of the symbol table ------------

    public Tab(Parser parser) {

        this.parser = parser;
        curScope = new Scope();
        curScope.outer = null;
        curLevel = -1;

		// create predeclared types
		intType   = new Type(Type.INT);
		floatType = new Type(Type.FLOAT);
		charType  = new Type(Type.CHAR);
		boolType  = new Type(Type.BOOL);
		noType    = new Type(Type.NONE);
		noObj     = new Obj(Obj.VAR, "???", noType);

        // insert predeclared types into universe
        insert(Obj.TYPE, "int", intType);
        insert(Obj.TYPE, "float", floatType);
        insert(Obj.TYPE, "char", charType);
    }
}
