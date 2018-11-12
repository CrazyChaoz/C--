package at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME;

/*--------------------------------------------------------------------------------
NodeList   Builds lists of AST nodes
========   =========================
--------------------------------------------------------------------------------*/

public class NodeList {
	Node head, tail;

	// Append x to the list
	public void add(Node x) {
		if (x != null) {
			if (head == null)
				head = x;
			else
				tail.next = x;


			//addition by stefan kempinger
			//to eliminate the need for an additional "for" type
			while(x!=null) {
				tail = x;
				x=x.next;
			}
		}
	}

	// Retrieve the head of the list
	public Node get() {
		return head;
	}
}