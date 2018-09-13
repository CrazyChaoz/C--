package at.htlwels.DIPLOMARBEITSTITEL.ui

import at.htlwels.DIPLOMARBEITSTITEL.cmmCompiler.{Parser, Scanner}
import javafx.application.Application

object GraphicalAstConfigurator {



  def main(args: Array[String]): Unit = {



    Application.launch(new GUI().getClass)
  }
}
