package at.htlwels.cmm.error;

public class Sin{
    private String message="You commited a sin !\n";

    public Sin(String message){
        this.message+=message;
    }

    public String getMessage() {
        return message;
    }

    public static void commit(Sin s){
        System.err.println(s.getMessage());
        System.exit(-1);
    }
}
