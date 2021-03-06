package at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME;

/*--------------------------------------------------------------------------------
Type   Structure of a C-- type
======   =======================
A Type holds information about a C-- type. There are 3 primitive types
(int, float, char) and 2 structured types (arrays, structures). The type Bool
results from compare operations, but there is no boolean type in C--.
--------------------------------------------------------------------------------*/

import java.io.Serializable;

public class Type implements Serializable {
    public static final int // structure kinds
            NONE   = 0,
            INT    = 1,
            FLOAT  = 2,
            CHAR   = 3,
            BOOL   = 4,
            ARR    = 5,
            STRUCT = 6,
            STRING = 7;
    public int    kind;		  // NONE, INT, FLOAT, CHAR, ARR, STRUCT
    public int    size;     // size of this type in bytes
    public int    elements; // ARR: number of elements
    public Type   elemType; // ARR: element type
    public Scope  fields;   // STRUCT: fields
    public boolean isGlobalInsertedType;

    public Type(int kind) {
        this.kind = kind;
        switch (kind) {
            case INT:    size = 4; break;
            case FLOAT:  size = 4; break;
            case CHAR:   size = 1; break;
            case BOOL:   size = 1; break;
            case STRING: size = 4; break;
            case STRUCT: fields=new Scope();
            default:     size = 0; break;
        }
    }

    public Type(int kind, boolean isGlobalInsertedType){
        this(kind);
        this.isGlobalInsertedType=isGlobalInsertedType;
    }

    public Type(int kind, int elements, Type elemType) {
        this.kind = kind;
        this.elements = elements;
        this.elemType = elemType;
        size = elements * elemType.size;
    }

    // Checks whether this type is a primitive type
    public boolean isPrimitive() {
        return kind == INT || kind == FLOAT || kind == CHAR || kind == BOOL;
    }

    public static String name[]={
            "NONE","INT","FLOAT","CHAR","BOOL","ARR","STRUCT","STRING"
    };


    @Override
    public String toString() {
        return name[kind];
    }
}