package at.htlwels.cmm.compiler;

public class Interpreter {
    private Table vTab;
    private Table fTab;

    public Interpreter(Table vTab, Table fTab) {
        this.vTab = vTab;
        this.fTab = fTab;
    }

    void StatSeq(Node p) {

    }

    public Table getvTab() {
        return vTab;
    }

    public void setvTab(Table vTab) {
        this.vTab = vTab;
    }

    public Table getfTab() {
        return fTab;
    }

    public void setfTab(Table fTab) {
        this.fTab = fTab;
    }
}
