package at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME;

import java.io.Serializable;

public enum  NodeKind implements Serializable {

    //------------ statements
    STATSEQ ,   // statement sequence
    ASSIGN ,   // assignment
    CALL ,   // procedure or function call
    IF ,   // if statement without else branch
    IFELSE ,   // if statement with else branch
    WHILE ,   // while statement
    PRINT ,   // print statement
    RETURN ,   // return statement
    TRAP ,   // trap if a function reaches its end without a return
    //------------ leaf expressions
    IDENT ,   // identifier
    INTCON ,   // int constant
    FLOATCON ,   // float constant
    CHARCON ,   // char constant
    STRINGCON,  // string constant
    //------------ designators and ref parameters
    DOT ,   // field selection (x.y)
    INDEX ,   // array element (a[i])
    REF ,   // ref parameter
    //------------ expressions
    PLUS ,   // +
    MINUS ,   // -
    TIMES ,   // *
    DIV ,   // /
    REM ,   // %
    READINT,   // read operation
    READCHAR,   // read operation
    I2F ,   // conversion from int to float
    F2I ,   // conversion from float to int
    I2C ,   // conversion from int to char
    C2I ,   // conversion from char to int
    GENERIC_CAST,
    //------------ conditionals
    EQL ,   // ==
    NEQ ,   // !=
    LSS ,   // <
    LEQ ,   // <=
    GTR ,   // >
    GEQ ,   // >=
    NOT ,   // !
    OR ,   // ||
    AND ;   // &&

    public static boolean isBoolean(NodeKind nodeKind){
        return nodeKind.ordinal()>=EQL.ordinal()&&nodeKind.ordinal()<=AND.ordinal();
    }

    public static boolean isStatement(NodeKind nodeKind){
        return (nodeKind.ordinal()>STATSEQ.ordinal()&&nodeKind.ordinal()<TRAP.ordinal());
    }
}
