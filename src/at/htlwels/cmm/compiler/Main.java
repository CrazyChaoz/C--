package at.htlwels.cmm.compiler;

import at.htlwels.cmm.interpreter.Interpreter;
import at.htlwels.cmm.interpreter.Strings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Main {

    public static void main(String[] args) {
        Parser parser = new Parser(new Scanner("testfile.c"));
        parser.Parse();
        System.out.println("Number of Errors: " + parser.errors.count);

    }


    @Test
    @DisplayName("Interpreter Test")
    public void interpreterTest() {
        Interpreter it = new Interpreter(new Tab(new Parser(new Scanner("testfile.c"))), new Obj(ObjKind.CON, "lel", Type.INT));

        it.storeChar(0, 't');
        it.storeChar(1, 'e');
        it.storeChar(2, 's');
        it.storeChar(3, 't');

        for(int i = 0; i <=3; i++) {
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
    public void tokenTest() throws UnsupportedEncodingException {
        System.out.println("Der TokenTest");
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
