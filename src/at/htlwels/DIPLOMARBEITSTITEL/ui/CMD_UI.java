package at.htlwels.DIPLOMARBEITSTITEL.ui;

import at.htlwels.DIPLOMARBEITSTITEL.cmmCompiler.Parser;
import at.htlwels.DIPLOMARBEITSTITEL.cmmCompiler.Scanner;
import at.htlwels.DIPLOMARBEITSTITEL.interpreter.Interpreter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;


public class CMD_UI {
    public static void main(String[] args) throws IOException {
        Parser parser = new Parser(new Scanner(args[0]));
        parser.Parse();

        if (parser.hasErrors()){
            System.out.println("\nParser has Errors\n");
            parser.symbolTable.dumpTable();
            return;
        }
        Interpreter interpreter = new Interpreter(parser.symbolTable);
        interpreter.statSeq(parser.symbolTable.find("main").ast);

        for (int i = 1; i < args.length; i++) {
            switch (args[i].toLowerCase()) {
                case "-g":
                case "--dumpglobal":
                    interpreter.dumpGlobalData();
                    break;
                case "-s":
                case "--dumpstack":
                    interpreter.dumpStack();
                    break;
                case "-t":
                case "--dumptable":
                    parser.symbolTable.dumpTable();
                    break;
                case "-str":
                case "--dumpstringstorage":
                    interpreter.dumpStringStorage();
                    break;
                case "-b":
                case "--build-lib":
                    File file=new File(args[i+1]);
                    file.createNewFile();
                    new ObjectOutputStream(new FileOutputStream(file)).writeObject(parser.symbolTable);
                    break;
            }
        }
    }


}
