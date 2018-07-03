package at.htlwels.cmm.compiler;

/*--------------------------------------------------------------------------------
Strings   String storage of a C-- program
=======   ===============================
A string is an immutable, null-terminated sequence of characters.
The string storage holds all string constants of a C-- program as well as all
strings that are created by casting a char array to a string or by concattenating
strings. Strings with the same values are stored only once in the string storage.
--------------------------------------------------------------------------------*/

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Strings {
	private int storageSize = 4096;
	private byte[] data = new byte[storageSize]; // grows automatically
	private int top = 0;
	private Map<String, Integer> map = new HashMap<>();

	// Puts the string s into the string storage and returns its address there.
	// If s is already in the string storage, s is not added again, but the address of
	// the existing string is returned.
	// s may still contain escape sequences (such as \t or \r) that must be converted first.
	public int put(String s) {
		//TODO: convert escape sequences + grow data automatically
		byte[] sByte = null;
		int adr;
		int i = 0;
		int l = 0;

		if(!map.containsKey(s)) {
			try {
				sByte = s.getBytes("UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			//Grow the array if the next string doesn't fit.
			if((data.length-top) <= sByte.length) {
				byte temp[] = data;
				storageSize *= 2;

				data = new byte[storageSize];

				for(i = 0; i < top; i++) {
					data[i] = temp[i];
				}
			}

			adr = top;
			top += sByte.length;

			for(i = adr; i < top; i++) {
				data[i] = sByte[l];
				l++;
			}
			data[i+1] = '\0';
			top++;

			map.put(s, adr);

		}

		return map.get(s);
	}

	// Returns the string that is stored at adr in the string storage
	public String get(int adr) {
		String s;
		int i;
		StringBuilder sb = new StringBuilder();

		for(i = adr; data[i]!='\0'; i++) {
			sb.append((char) data[i]);
		}
		s = sb.toString();

		return s;
	}

	// Returns the character at adr in the string storage
	public char charAt(int adr) {
		return (char) data[adr];
	}
}