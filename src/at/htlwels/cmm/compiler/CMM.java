package at.htlwels.cmm.compiler;

/*--------------------------------------------------------------------------------
CMM   Main program of the C-- compiler
===   ================================
This class can be used to invoke the C-- compiler.

Synopsis:
java cmm.compiler.CMM <sourcefile> [ -debug {n} ]

Debug switches:
0: dump the symbol table
1: dump the AST
--------------------------------------------------------------------------------*/
class CMM {

public static void main(String[] args) {
	if (args.length > 0) {
		// create the scanner and the parser
		Scanner scanner = new Scanner(args[0]);
		Parser parser = new Parser(scanner);
		// parse the debug switches
		if (args.length > 2 && args[1].equals("-debug")) {
			for (int i = 2; i < args.length; i++) {
				int val = Integer.parseInt(args[i]);
				if (val >= 0 && val < parser.debug.length) parser.debug[val] = true;
			}
		}
		// parse the input and invoke the interpreter
		parser.Parse();
		System.out.println(parser.errors.count + " error(s) detected");
		if (parser.errors.count == 0) {
			// invoke the interpreter
		}
	} else {
		System.out.println("-- synopsis: java cmm.compiler.CMM <sourcefile> [ -debug {n} ]");
	}
}

}