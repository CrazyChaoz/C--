package at.htlwels.cmm.compiler;

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


public class Tab {

    public static void main(String[] args) {
        Tab table = new Tab(new Parser(null));
        table.insert(ObjKind.VAR, "asd", Type.INT);
        table.insert(ObjKind.VAR, "asasd", Type.INT);
        table.insert(ObjKind.VAR, "asasd", Type.INT);


        Obj o = table.insert(ObjKind.PROC, "main", Type.INT);
        table.openScope(o);
        table.insert(ObjKind.VAR, "x", Type.INT);
        table.insert(ObjKind.VAR, "y", Type.INT);



        Node rest1 = new Node(table.find("asasd"));
        Node x1 = new Node(table.find("x"));
        Node y1 = new Node(table.find("x"));
        Node rem1 = new Node(NodeKind.REM, x1, y1, 0);
        Node assign = new Node(NodeKind.ASSIGN, rest1, rem1, 0);

        o.ast = new Node(NodeKind.STATSEQ, assign, new Node(NodeKind.TRAP, null, null, 0), 0);
        table.insert(ObjKind.VAR, "asasd", Type.CHAR);
        table.closeScope();

//        Node.dump(o.ast, 0);

        table.dumpTable();

    }

    public Scope curScope;                  // current scope
    public int curLevel;                    // nesting level of current scope

    public static Type intType;             // predefined types
    public static Type floatType;
    public static Type charType;
    public static Type boolType;
    public static Type noType;
    public static Obj noObj;                // predefined objects



    public Parser parser;

    //------------------ scope management ---------------------

    public void openScope(Obj o) {
        o.localScope.outer = curScope;
        curScope = o.localScope;
        curLevel++;
    }

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

//        if (find(object.name) != noObj) {
//            parser.errors.count++;
//        }

        Obj nxt = curScope.locals;
        if (nxt == null)
            curScope.locals = object;
        else {
            while (nxt.next != null)
                nxt = nxt.next;

            nxt.next = object;
        }
        return object;
    }


    // Look up the object with the given name in all open scopes.
    // Report an error if not found.
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

        parser.errors.count++;
        //Detailed error messages
        return noObj;
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
        switch (type) {
            case INT:
                System.out.print("Int(" + type.size + ")");
                break;
            case FLOAT:
                System.out.print("Float(" + type.size + ")");
                break;
            case CHAR:
                System.out.print("Char(" + type.size + ")");
                break;
            case BOOL:
                System.out.print("Bool(" + type.size + ")");
                break;
            case ARR:
                System.out.print("Arr[" + type.elements + "(" + type.size + ")] of ");
                dumpStruct(type.elemType, indent);
                break;
            case STRUCT:
                System.out.println("Type(" + type.size + ") {");
                for (Obj o = type.fields; o != null; o = o.next)
                    dumpObj(o, indent + 1);
                for (int i = 0; i < indent; i++) System.out.print("  ");
                System.out.print("}");
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
                if (o.type == Tab.floatType)
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
                Node.dump(o.ast,0);
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

    public void dumpTable() {
        Scope scope = curScope;
        while (scope.outer != null) scope = scope.outer;

        dumpScope(scope.locals, 0);
    }

    //-------------- initialization of the symbol table ------------

    public Tab(Parser parser) {


        this.parser=parser;


        curScope = new Scope();
        curScope.outer = null;
        curLevel = -1;

        // create predeclared types
        intType = Type.INT;
        floatType = Type.FLOAT;
        charType = Type.CHAR;
        boolType = Type.BOOL;
        noType = Type.NONE;
        noObj = new Obj(ObjKind.VAR, "???", noType);

        // insert predeclared types into universe
        insert(ObjKind.TYPE, "int", intType);
        insert(ObjKind.TYPE, "float", floatType);
        insert(ObjKind.TYPE, "char", charType);
    }
}
