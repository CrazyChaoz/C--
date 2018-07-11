package at.htlwels.cmm.unused

import at.htlwels.cmm.compiler.Node

trait NodeKindNode {
  def exec(node: Node): Unit
}


class STATSEQ extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class ASSIGN extends NodeKindNode {
  def exec(node: Node): Unit = {
    node.right.kind match {
      case _ =>
    }
  }
}

class CALL extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class IF extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class IFELSE extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class WHILE extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class PRINT extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class RETURN extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class TRAP extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class IDENT extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class INTCON extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class FLOATCON extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class CHARCON extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class DOT extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class INDEX extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}


class REF extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class PLUS extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class MINUS extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class TIMES extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class DIV extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class REM extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}


class READ extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}


class I2F extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}


class F2I extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}


class I2C extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class C2I extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class GENERIC_CAST extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class EQL extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class NEQ extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class LSS extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class LEQ extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class GTR extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}

class GEQ extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}


class NOT extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}


class OR extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}


class AND extends NodeKindNode {
  def exec(node: Node): Unit = {
    println(getClass)
    NodeHandler.handleNode(node.left)
    NodeHandler.handleNode(node.right)
  }
}