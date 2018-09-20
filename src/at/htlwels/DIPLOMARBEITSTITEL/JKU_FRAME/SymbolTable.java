package at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME;


/*--------------------------------------------------------------------------------
SymbolTable   Symbol table for C--
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


import java.io.Serializable;

public class SymbolTable implements Serializable {


    public Scope curScope;                  // current scope
    public int curLevel;                    // nesting level of current scope

    public static Type intType;             // predefined types
    public static Type floatType;
    public static Type charType;
    public static Type boolType;
    public static Type stringType;
    public static Type noType;
    public static Obj noObj;                // predefined objects


    public transient Parser parser;

    //------------------ scope management ---------------------

    /**
     * Jump into a local scope, setting the current scope as the outer scope.
     *
     * @param scope
     */
    public void openScope(Scope scope) {
        scope.outer = curScope;
        curScope = scope;
        curLevel++;
    }


    /**
     * Leaves the current scope and jumps to the next outer scope.
     */
    public void closeScope() {
        curScope = curScope.outer;
        curLevel--;
    }

    //------------- Object insertion and retrieval --------------

    // Create a new object with the given kind, name and type
    // and insert it into the current scope.
    public Obj insert(ObjKind kind, String name, Type type) {
        return insert(new Obj(kind, name, type));
    }

    public Obj insert(Obj object) {


        Obj found = lookup(object.name);


        if (object.isForward) {
            found.ast = object.ast;
            found.localScope = object.localScope;
            found.isForward = false;
            return found;
        } else if (found != noObj) {
            parser.addError("Already defined: "+object.name);
            System.err.println(object.name);
        }

        Obj nxt = curScope.locals;
        if (nxt == null)
            curScope.locals = object;
        else {
            while (nxt.next != null)
                nxt = nxt.next;

            nxt.next = object;
        }

        object.level = curLevel;

        if (object.kind == ObjKind.VAR || object.kind == ObjKind.CON) {
            object.adr = curScope.size;
            curScope.size += object.type.size;
        }

        return object;
    }


    // Look up the object with the given name in all open scopes.
    // Report an error if not found.
    // Error ++ if not found
    public Obj find(String name) {
        Scope scope = curScope;
        Obj obj;


        while (scope != null) {
            obj = scope.locals;

            while (obj != null) {
                if (obj.name.equals(name))
                    return obj;
                obj = obj.next;
            }
            scope = scope.outer;
        }

        System.out.println(name);

        parser.addError("Not (yet) defined: "+name);
        //Detailed error messages
        return noObj;
    }


    // Retrieve a struct field with the given name from the fields of "type"
    public Obj findField(String name, Type type) {
        Obj retval;
        openScope(type.fields);
        retval = find(name);
        closeScope();
        return retval;
    }

    // Look up the object with the given name in the current scope.
    // Return noObj if not found.
    public Obj lookup(String name) {
        Scope scope = curScope;
        Obj obj;


        while (scope != null) {
            obj = scope.locals;

            while (obj != null) {
                if (obj.name.equals(name))
                    return obj;
                obj = obj.next;
            }
            scope = scope.outer;
        }

        //Detailed error messages
        return noObj;
    }


    //-----------------  handling multiple source files  -----------------

    public void mergeWithSymboltable(SymbolTable symbolTable){
        Obj globalsOfOtherSymboltable=symbolTable.curScope.locals;
        while (globalsOfOtherSymboltable!=null){
            if(!globalsOfOtherSymboltable.type.isGlobalInsertedType)
                this.insert(globalsOfOtherSymboltable);
            globalsOfOtherSymboltable=globalsOfOtherSymboltable.next;
        }
    }
    //----------------- handling of forward declaration  -----------------

    // Check if parameters of forward declaration and actual declaration match
    public boolean checkForwardParams(Obj oldPar, Obj newPar) {

        for (Obj localOldVar = oldPar.localScope.locals, localNewVar = newPar.localScope.locals;
             localOldVar != null && localNewVar != null;
             localOldVar = localOldVar.next, localNewVar = localNewVar.next) {

            if (!localNewVar.equals(localOldVar))
                return false;
        }
        return true;
    }

    // Check if all forward declarations were resolved at the end of the program
    public void checkIfForwardsResolved(Scope scope) {

        for (Obj localVar = scope.locals; localVar != null; localVar = localVar.next) {
            if (localVar.kind == ObjKind.PROC)
                if (localVar.ast == null) {
                    System.err.println("Forward decleration not resolved");
                    parser.addError("Forward decleration not resolved: "+localVar.name);
                }
        }
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
        return s.charAt(1);
    }

    //---------------- methods for dumping the symbol table --------------

    // Print a type
    public void dumpType(Type type, int indent) {
        switch (type.kind) {
            case Type.INT:
                System.out.print("Int(" + type.size + ")");
                break;
            case Type.FLOAT:
                System.out.print("Float(" + type.size + ")");
                break;
            case Type.CHAR:
                System.out.print("Char(" + type.size + ")");
                break;
            case Type.BOOL:
                System.out.print("Bool(" + type.size + ")");
                break;
            case Type.ARR:
                System.out.print("Arr[" + type.elements + "(" + type.size + ")] of ");
                dumpType(type.elemType, indent);
                break;
            case Type.STRUCT:
                System.out.println("Type(" + type.size + ") {");
                dumpScope(type.fields.locals, indent + 1);

                for (int i = 0; i < indent; i++) System.out.print("  ");

                System.out.print("}");
                break;
            case Type.STRING:
                System.out.print("String");
                break;
            default:
                System.out.print("None");
                break;
        }
    }

    // Print an object
    public void dumpObj(Obj o, int indent) {
        for (int i = 0; i < indent; i++)
            System.out.print("\t");
        switch (o.kind) {
            case CON:
                System.out.print("Constant " + o.name);
                if (o.type == SymbolTable.floatType)
                    System.out.print(" fVal=" + o.fVal);
                else
                    System.out.print(" val=" + o.val);
                break;
            case VAR:
                System.out.print("Variable " + o.name + " adr=" + o.adr + " level=" + o.level);
                if (o.isRef) System.out.print(" isRef");
                break;
            case TYPE:
                System.out.print("Type " + o.name);
                break;
            case PROC:
                System.out.println("Procedure " + o.name + " size=" + o.size + " nPars=" + o.nPars + " isForw=" + o.isForward + " {");
                dumpScope(o.localScope.locals, indent + 1);
                Node.dump(o.ast, 0);
                System.out.print("}");
                break;
            default:
                System.out.print("None " + o.name);
                break;
        }
        System.out.print("\t");
        dumpType(o.type, indent);
        System.out.println();
    }

    // Print all objects of a scope
    public void dumpScope(Obj head, int indent) {
        for (Obj o = head; o != null; o = o.next)
            dumpObj(o, indent);
    }

    public void dumpTable() {
        Scope scope = curScope;
        while (scope.outer != null) scope = scope.outer;

        dumpScope(scope.locals, 0);
    }

    //-------------- initialization of the symbol table ------------

    public SymbolTable(Parser parser) {


        this.parser = parser;


        curScope = new Scope();
        curScope.outer = null;
        curLevel = -1;

        // create predeclared types
        intType = new Type(Type.INT,true);
        floatType = new Type(Type.FLOAT,true);
        charType = new Type(Type.CHAR,true);
        boolType = new Type(Type.BOOL,true);
        stringType = new Type(Type.STRING,true);
        noType = new Type(Type.NONE,true);
        noObj = new Obj(ObjKind.VAR, "???", noType);

        // insert predeclared types into universe
        insert(ObjKind.TYPE, "int", intType);
        insert(ObjKind.TYPE, "float", floatType);
        insert(ObjKind.TYPE, "char", charType);
        insert(ObjKind.TYPE, "string", stringType);
        insert(ObjKind.TYPE, "void", noType);

        curLevel += 1;
    }
}
