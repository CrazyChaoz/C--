package at.htlwels.cmm.interpreter;

public class ProcedureStack {
    private int stackPointer, framePointer = 0;

    byte[] stack = new byte[32768];

    public ProcedureStack() {

    }

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

    public int getStackPointer() {
        return stackPointer;
    }

    public void setStackPointer(int stackPointer) {
        this.stackPointer = stackPointer;
    }

    public int getFramePointer() {
        return framePointer;
    }

    public void setFramePointer(int framePointer) {
        this.framePointer = framePointer;
    }

    public byte[] getStack() {
        return stack;
    }

    public void setStack(byte[] stack) {
        this.stack = stack;
    }
}
