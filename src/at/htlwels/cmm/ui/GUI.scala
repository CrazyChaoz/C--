import javafx.application._
import javafx.scene._
import javafx.scene.control.MenuItem
import javafx.stage._
import javafx.scene.layout._
import javafx.scene.control.MenuButton


class GUI extends Application {
  override def start(myStage: Stage): Unit = {
    myStage.setTitle("JavaFX GUI.")
    val rootNode = new FlowPane
    val menuButton = new MenuButton("Type of Node")

    val nodeKinds=at.htlwels.cmm.compiler.NodeKind.values()
    nodeKinds.foreach(nodeKind=>{
      val menuItem=new MenuItem(nodeKind.name())
      menuItem.setOnAction(event=>{
        menuButton.setText(nodeKind.name())
        println(nodeKind.name()+" is Selected")
      })
      menuButton.getItems.add(menuItem)
    })

    rootNode.getChildren.add(menuButton)
    val myScene = new Scene(rootNode, 300, 200)
    myStage.setScene(myScene)
    myStage.show()
  }
}