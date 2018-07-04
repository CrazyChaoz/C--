package at.htlwels.cmm.compiler;

import java.util.ArrayList;
import java.util.List;

public class SymbolTable {
    SymbolScope globalSymbolScope =new SymbolScope();
    SymbolScope currentSymbolScope = globalSymbolScope;

    /**
     * Create a new scope inside the current one and add it to the list of inner scopes.
     */
    public void openScope(){
        SymbolScope newSymbolScope =new SymbolScope();
        currentSymbolScope.innerScopes.add(newSymbolScope);
        newSymbolScope.outerScope= currentSymbolScope;
        currentSymbolScope = newSymbolScope;
    }

    /**
     * Exit out of the current scope and return one level up the scope hierarchy.
     */
    public void closeScope(){
        currentSymbolScope = currentSymbolScope.outerScope;
    }

    /**
     * Create a new node, define its name and type based on the parameters and add it to the current scope.
     */
    public void addNode(String name, Object value,String type){
        SymbolNode symbolNode =new SymbolNode(name, value, type);
        currentSymbolScope.addNode(symbolNode);
    }

    public void addNode(SymbolNode symbolNode){
        currentSymbolScope.addNode(symbolNode);
    }

    /**
     * Dump the contents of the Symbol Table for debugging purposes.
     */
    public void dumpTable(){
        System.out.println("\nSymbol Table gets dumped\n");
        globalSymbolScope.printMe("");
    }
}

class SymbolScope {
    SymbolScope outerScope;
    List<SymbolScope> innerScopes=new ArrayList<>();

    private SymbolNode head, tail;

    // Append a new node at the end of the node list.
    public void addNode(SymbolNode x) {
//        System.out.println("Addnode");
//        System.out.println("Name: "+x.name);
//        System.out.println("Value: "+x.value);
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

        System.out.print(einrueckung);

//        System.out.println("Printing scope");
        SymbolNode symbolNode =this.head;

        while (symbolNode !=null){
            System.out.print(einrueckung);
            symbolNode.printMe();
            symbolNode = symbolNode.next;
        }

        for (SymbolScope scope :innerScopes) {
            scope.printMe(einrueckung+"\t");
        }
    }
}





