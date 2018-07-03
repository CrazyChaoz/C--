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
    public void addNode(String type, String name, String value,KempKind kind){
        KempNode node=new KempNode();

        node.name=name;
        try{
            node.type=KempType.valueOf(type.toUpperCase());
        }catch (IllegalArgumentException e){
            throw new RuntimeException(e);
        }
        node.value=value;
        node.kind=kind;


        currentScope.addNode(node);
    }

    //Dump the contents of the Symbol Table for debugging purposes.
    public void dumpTable(){
        System.out.println("Symbol Table gets dumped");
        globalScope.printMe("#");
    }
}

class KempScope{
    KempScope outerScope;
    List<KempScope> innerScopes=new ArrayList<>();

    private KempNode head=null, tail=null;

    // Append a new node at the end of the node list.
    public void addNode(KempNode x) {
        if (x != null) {
            if (head == null)
                head = x;
            else
                tail.next = x;

            tail = x;
        }
    }

    //Traverse through all inner scopes recursively and print their nodes.
    public void printMe(String einrueckung){

        System.out.print(einrueckung+"|");
        System.out.println("Printing scope");
        KempNode node=this.head;

        while (node!=null){
            System.out.print(einrueckung+"|");
            node.printMe();
            node=node.next;
        }

        for (KempScope scope :innerScopes) {
            scope.printMe(einrueckung+"___");
        }
    }
}

class KempNode{
    KempNode next;
    String name;
    String value;
    KempType type;
    KempKind kind;


    public void printMe(){
        System.out.println(kind+" "+name+"("+type+"): "+value);
    }
}

