package at.htlwels.cmm.compiler;

/*--------------------------------------------------------------------------------
Obj   Object node of the C-- symbol table
===   ===================================
Every declared name in a C-- program is represented by an Obj node holding
information about this object.
--------------------------------------------------------------------------------*/

import java.io.Serializable;

public class Obj implements Serializable {

    public ObjKind kind;        // CON, VAR, TYPE, PROC
    public String name;         // object name
    public Type type;           // object type
    public Obj next;            // next local object in this scope

    public int val;             // CON: int or char value
    public float fVal;          // CON: float value

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
        }
    }

}