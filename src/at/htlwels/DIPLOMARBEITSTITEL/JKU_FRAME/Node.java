package at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME;

/*--------------------------------------------------------------------------------
Node   Node of the abstract syntax tree (AST) of a C-- program
====   =======================================================
Every node has a left and a right child. Some nodes (such as statements or parameters)
can also be linked by a "next" pointer.
Nodes representing a statement have a line number, whereas nodes representing
a part of an expression have a type.
--------------------------------------------------------------------------------*/


import java.io.Serializable;

public final class Node implements Serializable {


    public NodeKind kind;       // STATSEQ, ASSIGN, ...
    public Type     type;       // only used in expressions
    public int      line;       // only used in statement nodes

    public Node     left;       // left child
    public Node     right;      // right child
    public Node     next;       // for linking statements, parameters, ...

    public Obj      obj;        // object node of an IDENT
    public int      val;        // value of an INTCON or CHARCON
    public float    fVal;       // value of a FLOATCON
    public String   strVal;     //


    // for expression nodes
    public Node(NodeKind kind, Node left, Node right, Type type) {
        this.kind = kind;
        this.left = left;
        this.right = right;
        this.type = type;
    }

    // for statement nodes
    public Node(NodeKind kind, Node left, Node right, int line) {
        this(kind, left, right, null);
        this.line = line;
    }

    // for leaf nodes
    public Node(Obj obj) {
        this.kind = NodeKind.IDENT;
        this.type = obj.type;
        this.obj = obj;
    }

    // a int Node
    public Node(int val) {
        this.kind =  NodeKind.INTCON;
        this.type = SymbolTable.intType;
        this.val = val;
    }

    // a float Node
    public Node(float fValue) {
        this.kind =  NodeKind.FLOATCON;
        this.type = SymbolTable.floatType;
        this.fVal = fValue;
    }

    // a char Node
    public Node(char ch) {
        this.kind =  NodeKind.CHARCON;
        this.type = SymbolTable.charType;
        this.val = ch;
    }

    // a string Node
    public Node(String str) {
        this.kind =  NodeKind.STRINGCON;
        this.type = SymbolTable.stringType;
        this.strVal = str.substring(1,str.length()-1);
    }

    //----------------------- for dumping ASTs -----------------------------------


    public static void dump(Node x, int indent) {
        for (int i = 0; i < indent; i++) System.out.print("  ");
        if (x == null) System.out.println("-null-");
        else {
            System.out.print(x.kind);

            switch(x.kind){
                case IDENT:
                    System.out.print(" " + x.obj.name + " level=" + x.obj.level);
                    break;
                case INTCON:
                    System.out.print(" " + x.val);
                    break;
                case FLOATCON:
                    System.out.print(" " + x.fVal);
                    break;
                case CHARCON:
                    System.out.print(" \'" + (char) x.val + "\'");
                    break;
                case CALL:
                    if (x.obj!=null)
                        System.out.print(" " + x.obj.name);
                    break;
            }

            if (x.type != null)
                System.out.print(" type=" + x.type);

            if (x.kind.ordinal() >=  NodeKind.STATSEQ.ordinal() && x.kind.ordinal() <=  NodeKind.TRAP.ordinal())
                System.out.print(" line=" + x.line);

            System.out.println();

            if (x.left != null || x.right != null) {
                dump(x.left, indent + 1);
                dump(x.right, indent + 1);
            }

            if (x.next != null) {
                for (int i = 0; i < indent; i++) System.out.print("  ");
                System.out.println("--- next ---");
                dump(x.next, indent);
            }
        }
    }

}
