package at.htlwels.cmm.compiler;


import java.util.ArrayList;
import java.util.List;

public class SyntaxTree {

    public static void main(String[] args) {
        SyntaxTree tree=new SyntaxTree();

        tree.addNodeAndStepInto(Kind.CONSTANT);
        tree.addNode(Kind.IDENT,"asd");
        tree.addNode(Kind.VALUE,12);
        tree.stepOut();


        tree.addNodeAndStepInto(Kind.CONSTANT);
        tree.addNode(Kind.IDENT,"dasd");
        tree.addNode(Kind.VALUE,12);
        tree.stepOut();


        tree.dumpTree();
    }


    SyntaxNode globalNode, currentNode;


    public SyntaxTree() {
        globalNode = new SyntaxNode(Kind.PARENT,Kind.PARENT);
        currentNode = globalNode;
        globalNode.parent = new SyntaxNode(Kind.PARENT,Kind.PARENT);
    }

    /**
     * Create a new scope inside the current one and add it to the list of inner scopes.
     */

    public void addNodeAndStepInto(Kind kind) {
        addNodeAndStepInto(kind,null);
    }

    public void addNodeAndStepInto(Kind kind, Object value) {
        SyntaxNode newSyntaxNode = new SyntaxNode(value,kind);

        currentNode.children.add(newSyntaxNode);
        newSyntaxNode.parent = currentNode;
        currentNode = newSyntaxNode;
    }


    /**
     * Exit out of the current scope and return one level up the scope hierarchy.
     */
    public void stepOut() {
        currentNode = currentNode.parent;
    }

    /**
     * Create a new node, define its name and type based on the parameters and add it to the current scope.
     */


    public void addNode(Kind kind) {
        addNode(kind,null);
    }

    public void addNode(Kind kind, Object value) {
//        System.out.println("New Node, Kind." + kind + ", Value: " + value);

        SyntaxNode syntaxNode = new SyntaxNode(value,kind);

        currentNode.children.add(syntaxNode);
    }



    /**
     * Dump the contents of the Symbol Table for debugging purposes.
     */
    public void dumpTree() {
//        System.out.println("Syntax Tree gets dumped");

        globalNode.printMe("");
    }
}






