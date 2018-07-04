package at.htlwels.cmm.compiler;

public class SymbolNode {
    SymbolNode next;
    String name;
    Object value;
    Type type;

    public SymbolNode(String name, Object value, String type) {
        this.name = name;
        this.value = value;
        try{
            this.type=Type.valueOf(type.toUpperCase());
        }catch (IllegalArgumentException e){
            //throw new RuntimeException(e);
        }
    }

    public void printMe(){
        if(value instanceof SyntaxTree){
            System.out.println("Procedure "+name+", returns "+type);
            ((SyntaxTree) value).dumpTree();
        }else if(value instanceof String)
            System.out.println(name+" ("+type+"): "+value);
        else
            throw new RuntimeException("Joa Symbol node is weder a prozedur, nu hods an string");
    }
}