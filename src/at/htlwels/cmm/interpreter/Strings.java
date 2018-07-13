package at.htlwels.cmm.interpreter;

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
	private final int GROW_INCREMENT = 4096;
	private int storageSize = GROW_INCREMENT;
	private byte[] data = new byte[GROW_INCREMENT]; // grows automatically
	private int top = 0;
	private Map<String, Integer> map = new HashMap<>();

	// Puts the string s into the string storage and returns its address there.
	// If s is already in the string storage, s is not added again, but the address of
	// the existing string is returned.
	// s may still contain escape sequences (such as \t or \r) that must be converted first.
	public int put(String s) {
		//TODO: convert escape sequences
		byte[] sByte = null;
		int adr;
		int i = 0;
		int l = 0;

		if(!map.containsKey(s)) {
			try {
				sByte = s.getBytes("ASCII");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

			/**
			 * Grow the array if the next String to be appended to it can't fit.
			 */
			if((data.length-top) <= sByte.length) {
				growData();
			}

			/**
			 * Copy the contents of the string into the byte array, then append a null terminator
			 * to denote the end of the string.
			 */
			adr = top;
			top += sByte.length;

			for(i = adr; i < top; i++) {
				data[i] = sByte[l];
				l++;
			}
			data[i+1] = '\0';
			top++;

			map.put(s, adr);

			return adr;
		} else {
			return map.get(s);
		}
	}

	// Returns the string that is stored at adr in the string storage
	public int get(String s) {
		return map.get(s);
	}

	// Returns the character at adr in the string storage
	public char charAt(int adr) {
		return (char) data[adr];
	}

	/**
	 * Create a temporal byte-array variable and point it to the old array, then copy the contents of the old array
	 * to the new, bigger one. Since temp[] is a variable declared in the scope of this function, once it exits
	 * there will be no more references to the old array left and the garbage collector will deallocate it.
	 */
	public void growData(){
		int i;
		byte temp[] = data;
		storageSize +=  GROW_INCREMENT;

		data = new byte[storageSize];

		for(i = 0; i < top; i++) {
			data[i] = temp[i];
		}
	}
}