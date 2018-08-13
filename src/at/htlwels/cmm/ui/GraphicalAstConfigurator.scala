package at.htlwels.cmm.ui

import at.htlwels.cmm.compiler.{Parser, Scanner}
import javafx.application.Application

object GraphicalAstConfigurator {
  val parser = new Parser(new Scanner("testfiles/testfile.c"))
  parser.Parse()

  val symbolTable=parser.symbolTable


  def main(args: Array[String]): Unit = {



    Application.launch(new GUI().getClass)
  }
}
