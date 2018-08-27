package at.htlwels.DIPLOMARBEITSTITEL;

import at.htlwels.DIPLOMARBEITSTITEL.cmmCompiler.Parser;
import at.htlwels.DIPLOMARBEITSTITEL.cmmCompiler.Scanner;
import at.htlwels.DIPLOMARBEITSTITEL.interpreter.Interpreter;


public class Main {
    public static void main(String[] args){
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
            }
        }
    }


}
