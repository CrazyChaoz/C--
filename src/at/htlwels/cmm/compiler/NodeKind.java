package at.htlwels.cmm.compiler;

public enum  NodeKind {

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
    READ ,   // read operation
    I2F ,   // conversion from int to float
    F2I ,   // conversion from float to int
    I2C ,   // conversion from int to char
    C2I ,   // conversion from char to int
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

    public static NodeKind get(int kind){
        switch (kind){
            case Parser._eql:return EQL;
            case Parser._neq:return NEQ;
            case Parser._lss:return LSS;
            case Parser._leq:return LEQ;
            case Parser._gtr: return GTR;
            case Parser._geq:return GEQ;
            default:  return null;
        }
    }
}
