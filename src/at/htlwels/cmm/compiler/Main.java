package at.htlwels.cmm.compiler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Main {

    public static void main(String[] args) {
        Parser parser = new Parser(new Scanner("testfile.source"));
        parser.Parse();
        System.out.println("Number of Errors: " + parser.errors.count);

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
