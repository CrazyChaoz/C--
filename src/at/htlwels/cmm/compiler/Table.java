package at.htlwels.cmm.compiler;

import java.util.ArrayList;
import java.util.List;

public class Table {
    Scope globalScope=new Scope();
    Scope currentScope=globalScope;

    /**
     * Create a new scope inside the current one and add it to the list of inner scopes.
     */
    public void openScope(){
        Scope newScope=new Scope();
        currentScope.innerScopes.add(newScope);
        currentScope=newScope;
    }

    /**
     * Exit out of the current scope and return one level up the scope hierarchy.
     */
    public void closeScope(){
        currentScope=currentScope.outerScope;
    }

    /**
     * Create a new node, define its name and type based on the parameters and add it to the current scope.
     */
    public void addNode(String type, String name, String value, Kind kind){
        Node node=new Node();

        node.name=name;
        try{
            node.type=Type.valueOf(type.toUpperCase());
        }catch (IllegalArgumentException e){
            throw new RuntimeException(e);
        }
        node.value=value;
        node.kind=kind;


        currentScope.addNode(node);
    }

    /**
     * Dump the contents of the Symbol Table for debugging purposes.
     */
    public void dumpTable(){
        System.out.println("Symbol Table gets dumped");
        globalScope.printMe("#");
    }
}

class Scope {
    Scope outerScope;
    List<Scope> innerScopes=new ArrayList<>();

    private Node head, tail;

    // Append a new node at the end of the node list.
    public void addNode(Node x) {
        System.out.println("Addnode");
        System.out.println("Name: "+x.name);
        System.out.println("Value: "+x.name);
        if (x != null) {
            if (head == null)
                head = x;
            else
                tail.next = x;

            tail = x;
        }
    }

    /**
     * Traverse through all scopes recursively and print their nodes.
     */
    public void printMe(String einrueckung){

        System.out.print(einrueckung+"|");

        System.out.println("Printing scope");
        Node node=this.head;

        while (node!=null){
            System.out.print(einrueckung+"|");
            node.printMe();
            node=node.next;
        }

        for (Scope scope :innerScopes) {
            scope.printMe(einrueckung+"___");
        }
    }
}

class Node {
    Node next;
    String name;
    String value;
    Type type;
    Kind kind;


    public void printMe(){
        System.out.println(kind+" "+name+"("+type+"): "+value);
    }
}

