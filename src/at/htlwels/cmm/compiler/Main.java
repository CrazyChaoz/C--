package at.htlwels.cmm.compiler;

public class Main {

    public static void main(String[] args) {
        Parser parser = new Parser(new Scanner("testfile.c"));
        parser.Parse();
        System.out.println("Number of Errors: " + parser.errors.count);
    }


}
