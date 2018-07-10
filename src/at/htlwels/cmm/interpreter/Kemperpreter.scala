package at.htlwels.cmm.interpreter

import at.htlwels.cmm.compiler._


object Kemperpreter {
  private val pathPrefix = "at.htlwels.cmm.perdix."

  val functions: Map[NodeKind, Node => Unit] = Map()
  val variables = new VariableMemoryScope
  val constants = new VariableMemoryScope


  def main(args: Array[String]) {
    val parser = new Parser(new Scanner("testfile.c"))
    parser.Parse
    interprete(parser.symbolTable)


    for (elem <- constants.varValMap) {
      println(elem._1 + ": " + elem._2)
    }
  }


  private def interprete(symbolTable: SymbolTable) = {
    var scope = symbolTable.curScope
    while (scope.outer != null)
      scope = scope.outer

    var o = scope.locals
    while (o != null) {
      o.kind match {
        case ObjKind.CON =>
          o.`type`.kind match {
            case Type.INT => constants addVar (o.name, o.`val`)
            case Type.CHAR => constants addVar (o.name, o.`val`)
            case Type.FLOAT => constants addVar (o.name, o.fVal)
            case _ =>println("Wrong Constant Type")
          }
        case ObjKind.VAR =>
          variables + o.name
        case ObjKind.TYPE =>
          println("Type: " + Type.name(o.`type`.kind))
        case ObjKind.PROC =>
          println(o.name)
          handleNode(o.ast)
      }
      o = o.next
    }
  }

  def handleNode(node: Node) =
    if (node ne null) {
      functions.get(node.kind) match {
        case Some(func) => func(node)
        case None =>
          val func = (node: Node) => Class.forName(pathPrefix + node.kind.name()).newInstance().asInstanceOf[NodeKindNode].exec(node)
          functions + ((node.kind, func))
          func(node)
      }
    }
}


class VariableMemoryScope {

  var varValMap = scala.collection.mutable.Map[String, Any]()
  val subScopes=scala.collection.mutable.MutableList[VariableMemoryScope]()

  def addVar(ident: String, value: Any) = varValMap += ident -> value

}


