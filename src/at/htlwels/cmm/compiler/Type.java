package at.htlwels.cmm.compiler;

public enum Type {
    NONE, INT, FLOAT, CHAR, BOOL, ARR, STRUCT,VOID;

    //testing the values
    public static void main(String[] args) {
        System.out.println(valueOf("int".toUpperCase()));
        System.out.println(valueOf("sdf".toUpperCase()));
    }
}