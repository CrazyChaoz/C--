package at.htlwels.cmm.compiler;


public class SyntaxTree {
    SyntaxNode currentNode = new SyntaxNode();

    public SyntaxTree() {
        openScope();
    }

    /**
     * Create a new scope inside the current one and add it to the list of inner scopes.
     */
    public void openScope() {
        SyntaxNode newNode = new SyntaxNode();
        currentNode.addChild(newNode);
        newNode.parent = currentNode;
        currentNode = newNode;

    }

    /**
     * Exit out of the current scope and return one level up the scope hierarchy.
     */
    public void closeScope() {
        currentNode = currentNode.parent;
    }

    /**
     * Create a new node, define its name and type based on the parameters and add it to the current scope.
     */
    public void addNode(Kind kind, Object value) {
        System.out.println("Added Node with Kind."+kind+" and Value "+value);
        SyntaxNode syntaxNode = new SyntaxNode();


        syntaxNode.kind = kind;
        syntaxNode.value = value;
        currentNode.addSibling(syntaxNode);
    }

    public void addNode(Kind kind) {
        System.out.println("Added Node with Kind."+kind+" and no Value");
        SyntaxNode syntaxNode = new SyntaxNode();


        syntaxNode.kind = kind;
        syntaxNode.value = null;

        currentNode.addSibling(syntaxNode);
    }


    /**
     * Dump the contents of the Symbol Table for debugging purposes.
     */
    public void dumpTree() {
        System.out.println("Syntax Tree gets dumped");
        currentNode.printMe();
    }
}


class SyntaxNode {
    SyntaxNode childHead, childTail, next, parent;

    Object value;
    Kind kind;

    public void addSibling(SyntaxNode node) {
        if (this.next == null)
            next = node;
    }

    public void addChild(SyntaxNode node) {
        if (childHead == null)
            childHead = node;
        else
            childTail.next = node;

        childTail = node;

    }

    public void printMe() {
        if (value instanceof SymbolNode)
            ((SymbolNode) value).printMe();
        else if (value instanceof String)
            System.out.println(value);
        else
            System.out.println(kind);

        if (childHead != null)
            childHead.printMe();
        else if (next != null)
            next.printMe();
    }
}



