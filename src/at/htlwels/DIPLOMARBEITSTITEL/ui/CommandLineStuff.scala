package at.htlwels.DIPLOMARBEITSTITEL.ui

import at.htlwels.DIPLOMARBEITSTITEL.cmmCompiler.Parser
import at.htlwels.DIPLOMARBEITSTITEL.cmmCompiler.Scanner
import at.htlwels.DIPLOMARBEITSTITEL.interpreter.Interpreter
import java.io._

import at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME.SymbolTable


object CommandLineStuff {

  def main(args: Array[String]): Unit = {
    parseProgram(args)
  }

  def parseAndStripFile(location: String): SymbolTable = parseFile(location.substring(1, location.length - 1))

  def parseFile(location: String): SymbolTable = {
    val parser = new Parser(new Scanner(location))
    parser.Parse()

    if (parser.hasErrors) {
      System.out.println("\nParser has Errors\n")
      parser.symbolTable.dumpTable()
    }
    parser.symbolTable
  }


  def parseProgram(args: Array[String]): Unit = {
    val symbolTable = parseFile(args(0))
    val interpreter = new Interpreter(symbolTable)
    interpreter.statSeq(symbolTable.find("main").ast)


    for (arg <- args)
      arg match {
        case "-g" =>
        case "--dumpglobal" =>
          interpreter.dumpGlobalData()
        case "-s" =>
        case "--dumpstack" =>
          interpreter.dumpStack()
        case "-t" =>
        case "--dumptable" =>
          symbolTable.dumpTable()
        case "-str" =>
        case "--dumpstringstorage" =>
          interpreter.dumpStringStorage()
      }
  }
}
