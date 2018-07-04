package at.htlwels.cmm.interpreter;

public class ProcedureStack {
    private int stackPointer;
    private int framePointer;

    byte[] stack = new byte[32768];

    public int loadInt(int adr) {
        return (int) stack[adr];
    }

    public float loadFloat(int adr) {
        return (float) stack[adr];
    }

    public char loadChar(int adr) {
        return (char) stack[adr];
    }

    public void storeInt(int adr, int val) {
        stack[adr] = (byte) val;
    }

    public void storeFloat(int adr, float val) {
        stack[adr] = (byte) val;
    }

    public void storeChar(int adr, char val) {
        stack[adr] = (byte) val;
    }

}
