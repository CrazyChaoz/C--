package at.htlwels.cmm.compiler;


import java.util.ArrayList;
import java.util.List;

public class SyntaxTree {
    SyntaxScope globalSymbolScope =new SyntaxScope();
    SyntaxScope currentScope = globalSymbolScope;

    /**
     * Create a new scope inside the current one and add it to the list of inner scopes.
     */
    public void openScope(){
        SyntaxScope newSymbolScope =new SyntaxScope();
        currentScope.innerScopes.add(newSymbolScope);
        newSymbolScope.outerScope= currentScope;
        currentScope = newSymbolScope;
    }

    /**
     * Exit out of the current scope and return one level up the scope hierarchy.
     */
    public void closeScope(){
        currentScope = currentScope.outerScope;
    }

    /**
     * Create a new node, define its name and type based on the parameters and add it to the current scope.
     */
    public void addNode(Kind kind, Object value){
        SyntaxNode syntaxNode =new SyntaxNode();

        syntaxNode.kind=kind;
        syntaxNode.value=value;

        currentScope.addNode(syntaxNode);
    }

    public void addNode(Kind kind){
        SyntaxNode syntaxNode =new SyntaxNode();

        syntaxNode.kind=kind;
        syntaxNode.value=null;

        currentScope.addNode(syntaxNode);
    }


    /**
     * Dump the contents of the Symbol Table for debugging purposes.
     */
    public void dumpTree() {
        System.out.println("Syntax Tree gets dumped");
        globalSymbolScope.printMe("");
    }
}

class SyntaxScope {
    SyntaxScope outerScope;
    List<SyntaxScope> innerScopes=new ArrayList<>();

    private SyntaxNode head, tail;

    // Append a new node at the end of the node list.
    public void addNode(SyntaxNode x) {

        if (x != null) {
            if (head == null)
                head = x;
            else
                tail.next = x;

            tail = x;
        }
    }

    /**
     * Traverse through all inner scopes recursively and print their nodes.
     */
    public void printMe(String einrueckung){

        SyntaxNode symbolNode =this.head;

        while (symbolNode !=null){
            System.out.print(einrueckung);
            symbolNode.printMe();
            symbolNode = symbolNode.next;
        }

        for (SyntaxScope scope :innerScopes) {
            scope.printMe(einrueckung+"\t");
        }
    }
}

class SyntaxNode {
    SyntaxNode next;

    Object value;
    Kind kind;


    public void printMe() {


        if (value instanceof SymbolNode)
            ((SymbolNode) value).printMe();
        else if (value instanceof String)
            System.out.println(kind+", "+value);
        else
            System.out.println(kind);

    }
}



