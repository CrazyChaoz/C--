package at.htlwels.cmm.compiler;

import java.util.ArrayList;
import java.util.List;

public class SyntaxNode {
    SyntaxNode parent;
    List<SyntaxNode> children = new ArrayList<>();

    Object value;
    Kind kind;

    public SyntaxNode(Object value, Kind kind) {
        this.value = value;
        this.kind = kind;
    }

    public void printMe(String einrueckung) {
        System.out.print(einrueckung);
        if (value instanceof SymbolNode)
            ((SymbolNode) value).printMe();
        else
            System.out.println(kind + ", " + value);

        for (SyntaxNode child : children) {
            child.printMe(einrueckung+"\t");
        }

    }
}