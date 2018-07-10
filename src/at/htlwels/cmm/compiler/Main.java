package at.htlwels.cmm.compiler;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Main {
    public static void main(String[] args) throws IOException {
        Parser parser = new Parser(new Scanner("testfile.c"));
        parser.Parse();
        System.out.println("Number of Errors: " + parser.errors.count);
        parser.symbolTable.dumpTable();
//        ObjectOutputStream outputStream=new ObjectOutputStream(new FileOutputStream("CompleteSymbolTable.symTab"));
//        outputStream.writeObject(parser.symbolTable);
    }


}
