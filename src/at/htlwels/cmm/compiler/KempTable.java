package at.htlwels.cmm.compiler;

import java.util.ArrayList;
import java.util.List;

public class KempTable {
    KempScope globalScope=new KempScope();
    KempScope currentScope=globalScope;

    /**
     * Create a new scope inside the current one and add it to the list of inner scopes.
     * */
    public void openScope(){
        KempScope newScope=new KempScope();
        currentScope.innerScopes.add(newScope);
        currentScope=newScope;
    }

    /**
     * Exit out of the current scope and return one level up the scope hierarchy
     * */
    public void closeScope(){
        currentScope=currentScope.outerScope;
    }

    //Create a new node, define its name and type based on the parameters and add it to the current scope
    public void addNode(String type, String name){
        KempNode node=new KempNode();

        node.name=name;
        try{
            node.type=KempType.valueOf(type.toUpperCase());
        }catch (IllegalArgumentException e){
            throw new RuntimeException(e);
        }


        currentScope.addNode(node);
    }

    //Dump the contents of the Symbol Table for debugging purposes.
    public void dumpTable(){
        System.out.println("Symbol Table gets dumped");
        globalScope.printMe();
    }
}

class KempScope{
    KempScope outerScope;
    List<KempScope> innerScopes=new ArrayList<>();

    private KempNode head, tail;

    // Append a new node at the end of the node list.
    public void addNode(KempNode x) {
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

    //Traverse through all inner scopes recursively and print their nodes.
    public void printMe(){
        System.out.println("Printing scope");
        for (KempScope scope :innerScopes) {
            System.out.print("\t");
            KempNode node=scope.head;
            while (node!=null){
                node.printMe();
                node=node.next;
            }
            scope.printMe();
        }
    }
}

class KempNode{
    KempNode next;
    String name;
    KempType type;
    //TODO procedure, structs, variables

    public void printMe(){
        System.out.print(name+": "+type);
    }
}

