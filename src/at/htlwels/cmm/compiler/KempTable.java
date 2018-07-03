package at.htlwels.cmm.compiler;

import java.util.ArrayList;
import java.util.List;

public class KempTable {
    KempScope globalScope=new KempScope();
    KempScope currentScope=globalScope;

    public void openScope(){
        KempScope newScope=new KempScope();
        currentScope.innerScopes.add(newScope);
        currentScope=newScope;
    }

    public void closeScope(){
        currentScope=currentScope.outerScope;
    }


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


    public void dumpTable(){
        System.out.println("Symbol Table gets dumped");
        globalScope.printMe();
    }
}

class KempScope{
    KempScope outerScope;
    List<KempScope> innerScopes=new ArrayList<>();

    private KempNode head, tail;

    // Append x to the list
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

    public void printMe(){
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

