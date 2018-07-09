package at.htlwels.cmm.compiler;

/*--------------------------------------------------------------------------------
Scope   Scope in the C-- symbol table
=====   =============================
There are 3 scopes in the C-- symbol table:
- universe: contains predeclared names
- global scope: contains globally declared names
- local scope: contains local names of a procedure
The scopes are linked by the "outer" pointer.
--------------------------------------------------------------------------------*/


import java.io.Serializable;

public class Scope implements Serializable {
    public Scope outer;        // to outer scope
    public Obj locals;    // to local variables of this scope
    public int size=0;    // total size of variables in this scope

    public void addVariable(Obj o) {
        if (locals != null) {
            Obj la = locals;
            while (la.next != null) la = la.next;
            la.next = o;
        }else
            locals=o;

        size++;
    }
}