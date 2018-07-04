package at.htlwels.cmm.interpreter;

public class GlobalData {
    private int gb;
    
    private byte[] globalData = new byte[32768];

    public int loadInt(int adr) {
        return (int) globalData[adr];
    }

    public float loadFloat(int adr) {
        return (float) globalData[adr];
    }

    public char loadChar(int adr) {
        return (char) globalData[adr];
    }

    public void storeInt(int adr, int val) {
        globalData[adr] = (byte) val;
    }

    public void storeFloat(int adr, float val) {
        globalData[adr] = (byte) val;
    }

    public void storeChar(int adr, char val) {
        globalData[adr] = (byte) val;
    }
}
