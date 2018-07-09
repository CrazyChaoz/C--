package at.htlwels.cmm.compiler;

import at.htlwels.cmm.interpreter.Interpreter;
import at.htlwels.cmm.interpreter.Strings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import static at.htlwels.cmm.compiler.SymbolTable.charType;
import static at.htlwels.cmm.compiler.SymbolTable.intType;
import static org.junit.jupiter.api.Assertions.*;


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


        Node assign = new Node(NodeKind.ASSIGN, new Node(table.findField("var", table.find("struktur").type)), new Node(12), intType);

        o.ast = new Node(NodeKind.STATSEQ, assign, new Node(NodeKind.TRAP, null, null, 0), 0);
        table.insert(ObjKind.VAR, "asasd", charType);
        table.closeScope();


        table.dumpTable();

        assertEquals(0,table.parser.errors.count);
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

        assertNotEquals(0, table.parser.errors.count);
    }


    @Test
    @DisplayName("Interpreter Test")
    public void interpreterTest() {
        Interpreter it = new Interpreter(null, null);

        it.storeChar(0, 't');
        it.storeChar(1, 'e');
        it.storeChar(2, 's');
        it.storeChar(3, 't');

        for (int i = 0; i <= 3; i++) {
            System.out.print(it.loadChar(i));
        }
        System.out.print('\n');

        it.storeInt(4, 344);
        System.out.println(it.loadInt(4));

        it.storeFloat(8, 32.3f);
        System.out.println(it.loadFloat(8));
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
        System.out.println(strings.get(0));
        System.out.println(strings.get(5));
        System.out.println(strings.get(14));
    }


    @Test
    @DisplayName("Token Test")
    public void tokenTest() throws UnsupportedEncodingException {
        System.out.println("TokenTest");
        // Initialize
        String sContent = "=";
        InputStream is = new ByteArrayInputStream(sContent.getBytes("UTF-8"));

        Scanner instance = new Scanner(is);
        Token expected = new Token();
        expected.kind = Parser._assign;
        expected.val = sContent;


        // Test
        Token result = instance.Scan();
        // Validate
        assertNotNull(result);
        assertNotNull(result.val);
        assertEquals(expected.kind, result.kind);
        assertEquals(expected.val, result.val);
    }

}
