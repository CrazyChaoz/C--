package at.htlwels.cmm.JKU_FRAME;

/*--------------------------------------------------------------------------------
Obj   Object node of the C-- symbol table
===   ===================================
Every declared name in a C-- program is represented by an Obj node holding
information about this object.
--------------------------------------------------------------------------------*/


import java.util.Objects;

public class Obj{

    public ObjKind kind;        // CON, VAR, TYPE, PROC
    public String name;         // object name
    public Type type;           // object type
    public Obj next;            // next local object in this scope

    public int val;             // CON: int or char value
    public float fVal;          // CON: float value
    public String strVal;       // CON: string value

    public int adr;             // VAR: address
    public int level;           // VAR: declaration level
    public boolean isRef;       // VAR: ref parameter

    public Node ast;            // PROC: AST of this procedure
    public int size;            // PROC: frame size in bytes
    public int nPars;           // PROC: number of formal parameters
    public Scope localScope;    // PROC: parameters and local objects
    public boolean isForward;   // PROC: is it a forward declaration

    public Obj(ObjKind kind, String name, Type type) {
        this.kind = kind;
        this.name = name;
        this.type = type;



        if(kind==ObjKind.PROC||kind==ObjKind.TYPE){
            localScope=new Scope();
            nPars=0;
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Obj obj = (Obj) o;
        return adr == obj.adr &&
                level == obj.level &&
                isRef == obj.isRef &&
                kind == obj.kind &&
                Objects.equals(name, obj.name) &&
                Objects.equals(type, obj.type);
    }
}