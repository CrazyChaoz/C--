package at.htlwels.cmm.compiler;

/*--------------------------------------------------------------------------------
Type   Structure of a C-- type
======   =======================
A Type holds information about a C-- type. There are 3 primitive types
(int, float, char) and 2 structured types (arrays, structures). The type Bool
results from compare operations, but there is no boolean type in C--.
--------------------------------------------------------------------------------*/

public enum Type {

    NONE,
    INT,
    FLOAT,
    CHAR,
    BOOL,
    ARR,
    STRUCT;

    public int size;     // size of this type in bytes
    public int elements; // ARR: number of elements
    public Type elemType; // ARR: element type
    public Obj fields;   // STRUCT: fields
}