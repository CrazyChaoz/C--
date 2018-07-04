package at.htlwels.cmm.compiler;

import java.util.ArrayList;
import java.util.List;

public class SyntaxNode {
    SyntaxNode parent;
    private List<SyntaxNode> syntaxChildren = new ArrayList<>();
    private List<SymbolNode> symbolChildren = new ArrayList<>();

    Object value;
    Kind kind;

    public SyntaxNode(Object value, Kind kind) {
        this.value = value;
        this.kind = kind;
    }

    public void addChild(SyntaxNode node){
        syntaxChildren.add(node);
    }

    public void addChild(SymbolNode node){
        symbolChildren.add(node);

    }


    public void printMe(String einrueckung) {
        System.out.print(einrueckung);
        if (value instanceof SymbolNode)
            ((SymbolNode) value).printMe();
        else
            System.out.println(kind + ", " + value);

        for (SymbolNode symbolChild : symbolChildren) {
            symbolChild.printMe();
        }
        for (SyntaxNode child : syntaxChildren) {
            child.printMe(einrueckung+"\t");
        }

    }
}