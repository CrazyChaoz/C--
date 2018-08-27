package at.htlwels.DIPLOMARBEITSTITEL.ui

import at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME._
import javafx.application._
import javafx.geometry.Pos
import javafx.scene._
import javafx.scene.control._
import javafx.stage._
import javafx.scene.layout._

object Helpers {

  val symbolTable = Helpers.getOrError(GraphicalAstConfigurator.symbolTable)


  def getOrError[T](variable: T): T = if (variable equals null) throw new RuntimeException("Error") else variable
}

trait GUI_Generator {
  def generate(pane: Pane)
}


class GUI extends Application {
  override def start(myStage: Stage): Unit = {
    myStage.setTitle("AST Node Editor")

    val scrollPane = new ScrollPane
    val rootNode = new HBox

    Main_GUI_Generator.generate(rootNode)

    scrollPane.setContent(rootNode)

    val myScene = new Scene(scrollPane)
    myStage.setScene(myScene)
    myStage.show
  }
}

object Main_GUI_Generator extends GUI_Generator {


  override def generate(pane: Pane): Unit = {
    pane.getChildren.clear

    val interprete_button = new Button("Interprete")
    val show_ast_button = new Button("Show AST")

    interprete_button.setOnMouseClicked(event => {
      new at.htlwels.DIPLOMARBEITSTITEL.interpreter.Interpreter(Helpers.symbolTable).startProgramFrom("main")
    })

    show_ast_button.setOnMouseClicked(event => {
      AST_Viewer_Generator.generate(pane)
    })


    pane.getChildren.add(interprete_button)
    pane.getChildren.add(show_ast_button)

  }
}

object AST_Viewer_Generator extends GUI_Generator {


  def generate(parent: Pane) = {
    parent.getChildren.clear

    val backButton = new Button("<<")

    backButton.setOnMouseClicked(event => {
      Main_GUI_Generator.generate(parent)
    })
    parent.getChildren.add(backButton)

    parent.autosize()
    var outermostNode = Helpers.symbolTable.curScope

    while (outermostNode.outer != null) outermostNode = outermostNode.outer

    var node = outermostNode.locals


    while (node != null) {
      if (node.kind == ObjKind.PROC) {
        parent.getChildren.add(constructVisuals(node.ast, node.name))
      }
      node = node.next
    }

  }


  def constructVisuals(node: at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME.Node, name: String = null): Pane = {

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
      Platform.runLater(()=>{
        val stage = new Stage(StageStyle.DECORATED)
        val rootNode=new VBox
        val submitButton=new Button("Submit")


        rootNode.setAlignment(Pos.CENTER)

        node.kind match {
          case NodeKind.INTCON => {
            val textField=new TextField()


            rootNode.getChildren.add(new Label("Integer"))
            rootNode.getChildren.add(textField)


            submitButton.setOnMouseClicked(event=>{
              node.`val`=Integer.parseInt(textField.getText)
            })

          }
          case NodeKind.CHARCON => {
            val textField=new TextField()


            rootNode.getChildren.add(new Label("Character"))
            rootNode.getChildren.add(textField)


            submitButton.setOnMouseClicked(event=>{
              node.`val`=Integer.parseInt(textField.getText)
            })

          }
          case NodeKind.FLOATCON => {
            val textField=new TextField()


            rootNode.getChildren.add(new Label("Float"))
            rootNode.getChildren.add(textField)


            submitButton.setOnMouseClicked(event=>{
              node.fVal=java.lang.Float.parseFloat(textField.getText)
            })

          }
          case NodeKind.STRINGCON => {
            val textField=new TextField()


            rootNode.getChildren.add(new Label("String"))
            rootNode.getChildren.add(textField)


            submitButton.setOnMouseClicked(event=>{
              node.strVal=textField.getText
            })
          }
          case _=>{}
        }


        rootNode.getChildren.add(submitButton)

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


//def getDropdownButton = {
//  //
//  val menuButton = new MenuButton("Type of Node")
//
//  val nodeKinds = at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME.NodeKind.values
//  nodeKinds.foreach(nodeKind => {
//  val menuItem = new MenuItem(nodeKind.name)
//  menuItem.setOnAction(event => {
//  menuButton.setText(nodeKind.name)
//  println(nodeKind.name + " is Selected")
//})
//  menuButton.getItems.add(menuItem)
//})
//
//  menuButton
//}