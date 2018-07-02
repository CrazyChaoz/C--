package at.htlwels.cmm.compiler;

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
