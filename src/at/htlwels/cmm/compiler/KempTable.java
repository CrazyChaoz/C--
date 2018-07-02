package at.htlwels.cmm.compiler;

public class KempTable {

    public void dumpTable(){

    }
}

class KempScope{
    int level;
    KempNode head;
}

class KempNode{
    KempNode next;
    String name;
    String type;
}