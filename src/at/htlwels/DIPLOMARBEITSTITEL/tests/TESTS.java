package at.htlwels.DIPLOMARBEITSTITEL.tests;

import at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME.*;
import at.htlwels.DIPLOMARBEITSTITEL.cmmCompiler.Parser;
import at.htlwels.DIPLOMARBEITSTITEL.cmmCompiler.Scanner;
import at.htlwels.DIPLOMARBEITSTITEL.interpreter.Interpreter;
import at.htlwels.DIPLOMARBEITSTITEL.interpreter.Strings;
import at.htlwels.DIPLOMARBEITSTITEL.ui.CommandLineStuff;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME.SymbolTable.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class TESTS {




	@Test
	@DisplayName("Simple Structure Test")
	public void structTest() {
		SymbolTable table = new SymbolTable(new Parser(null));
		table.insert(ObjKind.CON, "asd", intType);


		Type struktur = new Type(Type.STRUCT);
		table.insert(ObjKind.TYPE, "struktur", struktur);
		table.openScope(struktur.fields);
		table.insert(ObjKind.VAR, "var", intType);
		table.closeScope();


		table.insert(ObjKind.VAR, "struuu", table.find("struktur").type);

		Obj o = table.insert(ObjKind.PROC, "main", intType);
		table.openScope(o.localScope);
		table.insert(ObjKind.VAR, "x", intType);
		table.insert(ObjKind.VAR, "y", intType);

		Obj var = table.findField("var", table.find("struktur").type);
		Node dot = new Node(NodeKind.DOT, new Node(table.find("struuu")), new Node(var), var.type);
		Node assign = new Node(NodeKind.ASSIGN, dot, new Node(12), intType);

		o.ast = new Node(NodeKind.STATSEQ, assign, new Node(NodeKind.TRAP, null, null, 0), 0);
		table.insert(ObjKind.VAR, "asasd", charType);
		table.closeScope();


//        table.dumpTable();

		assertEquals(false, table.parser.hasErrors());
	}

	@Test
	@DisplayName("String in Structure Test")
	public void stringInStructTest() {
		SymbolTable table = new SymbolTable(new Parser(null));
		Obj o1 = new Obj(ObjKind.CON, "str", stringType);
		o1.strVal = "rofl";
		table.insert(o1);


		Type struktur = new Type(Type.STRUCT);
		table.insert(ObjKind.TYPE, "struktur", struktur);
		table.openScope(struktur.fields);
		table.insert(ObjKind.VAR, "s", stringType);
		table.closeScope();



		Obj o2 = table.insert(ObjKind.PROC, "main", intType);
		table.openScope(o2.localScope);
		table.insert(ObjKind.VAR, "struuu", table.find("struktur").type);

		Obj var = table.findField("s", table.find("struktur").type);
		Node dot = new Node(NodeKind.DOT, new Node(table.find("struuu")), new Node(var), var.type);
		Node assign = new Node(NodeKind.ASSIGN, dot, new Node("ASDF"), intType);


		o2.ast = new Node(NodeKind.STATSEQ, assign, new Node(NodeKind.TRAP, null, null, 0), 0);
		table.closeScope();


//        table.dumpTable();

		assertEquals(false, table.parser.hasErrors());
	}

	@Test
	@DisplayName("Example AST Test")
	public void procAstTest() {

		SymbolTable table = new SymbolTable(new Parser(null));

		Obj o = table.insert(ObjKind.PROC, "main", intType);
		table.openScope(o.localScope);
		table.insert(ObjKind.VAR, "x", intType);
		table.insert(ObjKind.VAR, "y", intType);

		Node rest1 = new Node(table.find("asasd"));
		Node x1 = new Node(table.find("x"));
		Node y1 = new Node(table.find("y"));
		Node rem1 = new Node(NodeKind.REM, x1, y1, 0);
		Node assign = new Node(NodeKind.ASSIGN, rest1, rem1, 0);
		o.ast = new Node(NodeKind.STATSEQ, assign, new Node(NodeKind.TRAP, null, null, 0), 0);

		table.insert(ObjKind.VAR, "asasd", charType);
		table.closeScope();

		table.dumpTable();

		assertNotEquals(false, table.parser.hasErrors());

	}


	@Test
	@DisplayName("Basic Interpreter Execution")
	public void basicInterpreterExecution() {
		Parser parser = new Parser(new Scanner("testfiles/testfile.c"));
		parser.Parse();

		parser.symbolTable.dumpTable();


		Interpreter it = new Interpreter(parser.symbolTable);
		it.statSeq(parser.symbolTable.find("main").ast);
		it.dumpStack();
		it.dumpGlobalData();
	}


	@Test
	@DisplayName("Interpreter struct Test")
	public void interpreterStructTest() {
		Parser parser = new Parser(new Scanner("testfiles/testfile2.c"));
		parser.Parse();

		//parser.symbolTable.dumpTable();


		Interpreter it = new Interpreter(parser.symbolTable);
		it.statSeq(parser.symbolTable.find("main").ast);
		it.dumpStack();
		it.dumpGlobalData();
	}

	@Test
	@DisplayName("Interpreter Array Test")
	public void interpreterArrayTest() {
		Parser parser = new Parser(new Scanner("testfiles/arrayTest.c"));
		parser.Parse();


		parser.symbolTable.dumpTable();


		Interpreter it = new Interpreter(parser.symbolTable);
		it.statSeq(parser.symbolTable.find("main").ast);
		it.dumpStack();
		it.dumpGlobalData();
	}

	@Test
	@DisplayName("Interpreter String Test")
	public void interpreterStringTest() {
		Parser parser = new Parser(new Scanner("testfiles/stringTest.c"));
		parser.Parse();


		parser.symbolTable.dumpTable();


		Interpreter it = new Interpreter(parser.symbolTable);
		it.statSeq(parser.symbolTable.find("main").ast);
		it.dumpStack();
		it.dumpGlobalData();
		it.dumpStringStorage();
	}

	@Test
	@DisplayName("Interpreter Read Test")
	public void interpreterReadTest() {
		Parser parser = new Parser(new Scanner("testfiles/readTest.c"));
		parser.Parse();

		parser.symbolTable.dumpTable();


		Interpreter it = new Interpreter(parser.symbolTable);
		it.statSeq(parser.symbolTable.find("main").ast);
		it.dumpStack();
		it.dumpGlobalData();
		it.dumpStringStorage();
	}

	@Test
	@DisplayName("Interpreter Array Struct Test")
	public void interpreterArrayStructFile() {
		Parser parser = new Parser(new Scanner("testfiles/arraysStructsTest.c"));
		parser.Parse();


		parser.symbolTable.dumpTable();


		Interpreter it = new Interpreter(parser.symbolTable);
		it.statSeq(parser.symbolTable.find("main").ast);
		it.dumpStack();
		it.dumpGlobalData();
	}

	@Test
	@DisplayName("Interpreter Test")
	public void interpreterTest() {
		Interpreter it = new Interpreter(null);

		it.storeChar(0, 't');
		it.storeChar(1, 'e');
		it.storeChar(2, 's');
		it.storeChar(3, 't');

		for (int i = 0; i <= 3; i++) {
			System.out.print(it.loadChar(i));
		}
		System.out.print('\n');

		it.storeInt(4, 344);
		it.storeInt(16, 3442);
		System.out.println(it.loadInt(4));

		it.storeFloat(8, 32.3f);
		System.out.println(it.loadFloat(8));
	}


	@Test
	@DisplayName("Interpreter Loop Test")
	public void interpreterLoopTest() {
		Parser parser = new Parser(new Scanner("testfiles/onlyLoop.c"));
		parser.Parse();

		parser.symbolTable.dumpTable();


		Interpreter it = new Interpreter(parser.symbolTable);
		it.statSeq(parser.symbolTable.find("main").ast);
		it.dumpStack();
		it.dumpGlobalData();
		it.dumpStringStorage();
	}

	@Test
	@DisplayName("String Test")
	public void stringTest() {
		System.out.println("String Test");
		Strings strings = new Strings();

		System.out.println(strings.put("test"));
		System.out.println(strings.put("te\ts\rt"));
		System.out.println(strings.put("testtest"));
		System.out.println(strings.put("string"));
		System.out.println(strings.charAt(0));
		System.out.println(strings.charAt(1));
		System.out.println(strings.charAt(2));
		System.out.println(strings.charAt(3));
//        System.out.println(strings.get(0));
//        System.out.println(strings.get(5));
//        System.out.println(strings.get(14));
	}

	@Test
	@DisplayName("Symboltable Generate Test")
	public void symboltableGenerateTest() {
		CommandLineStuff.parseFile("importTestA.c").dumpTable();
	}

	@Test
	@DisplayName("Initial Lang2 Test")
	public void lang2Test() {
		at.htlwels.DIPLOMARBEITSTITEL.lang2Compiler.Parser parser =
				new at.htlwels.DIPLOMARBEITSTITEL.lang2Compiler.Parser(new at.htlwels.DIPLOMARBEITSTITEL.lang2Compiler.Scanner("testfiles/lang2testfile.l2"));
		parser.Parse();

		parser.symbolTable.dumpTable();


		Interpreter it = new Interpreter(parser.symbolTable);
		it.statSeq(parser.symbolTable.find("main").ast);
		it.dumpStack();
		it.dumpGlobalData();
		it.dumpStringStorage();
	}


	@Test
	@DisplayName("Another Lang2 Test")
	public void differentLang2Test() {
		at.htlwels.DIPLOMARBEITSTITEL.lang2Compiler.Parser parser =
				new at.htlwels.DIPLOMARBEITSTITEL.lang2Compiler.Parser(new at.htlwels.DIPLOMARBEITSTITEL.lang2Compiler.Scanner("testfiles/l2t.l2"));
		parser.Parse();

		parser.symbolTable.dumpTable();


		Interpreter it = new Interpreter(parser.symbolTable);
		it.statSeq(parser.symbolTable.find("main").ast);
	}

	@Test
	@DisplayName("Ref Test")
	public void refTest() {
		Parser parser = new Parser(new Scanner("testfiles/refTest.c"));
		parser.Parse();

		parser.symbolTable.dumpTable();


		Interpreter it = new Interpreter(parser.symbolTable);
		it.statSeq(parser.symbolTable.find("main").ast);
		it.dumpStack();
		it.dumpGlobalData();
		it.dumpStringStorage();
	}
}

