package at.htlwels.cmm.ui

import at.htlwels.cmm.JKU_FRAME.{Node => _, _}
import javafx.application._
import javafx.geometry.Pos
import javafx.scene._
import javafx.scene.control.{Label, MenuButton, MenuItem, ScrollPane}
import javafx.stage._
import javafx.scene.layout._

object Helpers{
  def getOrError[T](variable: T):T=if(variable equals null)throw new RuntimeException("Error") else variable
}
trait GUI_Generator {
  def generate(pane: Pane)
}



class GUI extends Application {
  override def start(myStage: Stage): Unit = {
    myStage.setTitle("AST Node Editor")

    val scrollPane = new ScrollPane
    val rootNode = new HBox

    AST_Node_Generator.generate(rootNode)

    scrollPane.setContent(rootNode)

    val myScene = new Scene(scrollPane, 300, 200)
    myStage.setScene(myScene)
    myStage.show
  }
}

object Main_GUI_Generator extends GUI_Generator {
  def getDropdownButton = {
    val menuButton = new MenuButton("Type of Node")

    val nodeKinds = at.htlwels.cmm.JKU_FRAME.NodeKind.values
    nodeKinds.foreach(nodeKind => {
      val menuItem = new MenuItem(nodeKind.name)
      menuItem.setOnAction(event => {
        menuButton.setText(nodeKind.name)
        println(nodeKind.name + " is Selected")
      })
      menuButton.getItems.add(menuItem)
    })

    menuButton
  }

  override def generate(pane: Pane): Unit = {
    pane.getChildren.clear


  }
}

object AST_Node_Generator extends GUI_Generator {


  def generate(parent: Pane) = {
    parent.getChildren.clear

    var outermostNode = Helpers.getOrError(GraphicalAstConfigurator.symbolTable.curScope)

    while (outermostNode.outer != null) outermostNode = outermostNode.outer

    var node = outermostNode.locals


    while (node != null) {
      if (node.kind == ObjKind.PROC) {
        parent.getChildren.add(constructVisuals(node.ast, node.name))
        println(node.name)
      }
      node = node.next
    }

  }


  def constructVisuals(node: at.htlwels.cmm.JKU_FRAME.Node, name: String = null): Pane = {

    val rootBox = new HBox
    val superSelf = new VBox
    val self = new HBox
    var nodeLabel: Label = null


    node.kind match {
      case NodeKind.STATSEQ => nodeLabel = new Label(name)
      case NodeKind.IDENT => nodeLabel = new Label(node.obj.name)
      case NodeKind.INTCON => nodeLabel = new Label(node.`val` + "")
      case NodeKind.CHARCON => nodeLabel = new Label(node.`val` + "")
      case NodeKind.FLOATCON => nodeLabel = new Label(node.fVal + "")
      case NodeKind.STRINGCON => nodeLabel = new Label(node.strVal)
      case NodeKind.TRAP => return null
      case _ => nodeLabel = new Label(node.kind.name)
    }

    superSelf.getChildren.add(nodeLabel)
    nodeLabel.setOnMouseClicked(event => {
      Platform.runLater(() => {
        val stage = new Stage(StageStyle.DECORATED)
        val rootNode = new Label(node.kind + ";; ")
        val scene = new Scene(rootNode)
        stage.setScene(scene)
        stage.show
      })
    })


    superSelf.getChildren.add(self)
    rootBox.getChildren.addAll(superSelf)


    rootBox.setAlignment(Pos.TOP_CENTER)
    superSelf.setAlignment(Pos.TOP_CENTER)
    //    superSelf.setStyle("-fx-padding: 5;" + "-fx-border-style: solid inside;"
    //      + "-fx-border-width: 2;" + "-fx-border-insets: 2;"
    //      + "-fx-border-radius: 5;" + "-fx-border-color: yellow;")

    //Right Child
    if (node.left != null) {
      val leftChild = constructVisuals(node.left)

      if (leftChild != null) {
        leftChild.setStyle("-fx-padding: 5;" + "-fx-border-style: solid inside;"
          + "-fx-border-width: 2;" + "-fx-border-insets: 2;"
          + "-fx-border-radius: 2;" + "-fx-border-color: red;")
        self.getChildren.add(leftChild)
      }
    }

    //Left Child
    if (node.right != null) {
      val rightChild = constructVisuals(node.right)

      if (rightChild != null) {
        rightChild.setStyle("-fx-padding: 5;" + "-fx-border-style: solid inside;"
          + "-fx-border-width: 2;" + "-fx-border-insets: 2;"
          + "-fx-border-radius: 2;" + "-fx-border-color: blue;")
        self.getChildren.add(rightChild)
      }
    }


    if (node.next != null) {
      val yellowBox = new HBox
      yellowBox.setStyle("-fx-padding: 0;" + "-fx-border-style: solid inside;"
        + "-fx-border-width: 2;" + "-fx-border-insets: 2;"
        + "-fx-border-radius: 2;" + "-fx-border-color: magenta;")

      rootBox.getChildren.add(yellowBox)
      rootBox.getChildren.add(constructVisuals(node.next))
    }

    rootBox
  }
}