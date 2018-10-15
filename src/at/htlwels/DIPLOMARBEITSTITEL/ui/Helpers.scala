package at.htlwels.DIPLOMARBEITSTITEL.ui

import at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME.SymbolTable
import at.htlwels.DIPLOMARBEITSTITEL.cmmCompiler.{Parser, Scanner}

object Helpers {
	var symbolTable: SymbolTable = _
	var filename = ""

	def getOrError[T](variable: T): T = if (variable equals null) throw new RuntimeException("Error, variable is null") else variable

	def parseSymboltable(filename: String): Unit = {
		val parser = new Parser(new Scanner("testfiles/" + filename))
		parser.Parse
		symbolTable = parser.symbolTable
		this.filename = filename
	}

}