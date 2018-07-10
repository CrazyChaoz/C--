package at.htlwels.cmm.interpreter

import at.htlwels.cmm.compiler._


object NodeHandler {
  private val pathPrefix = "at.htlwels.cmm.interpreter."
  private val functions: Map[NodeKind, Node => Unit] = Map()


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