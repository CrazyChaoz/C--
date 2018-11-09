package at.htlwels.DIPLOMARBEITSTITEL.ui

import java.nio.file.Paths

import at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME.SymbolTable
import at.htlwels.DIPLOMARBEITSTITEL.{cmmCompiler, lang2Compiler}

object Helpers {
	var symbolTable: SymbolTable = _
	var filename = ""


	def getOrError[T](variable: T): T = if (variable equals null) throw new RuntimeException("Error, variable is null") else variable

	def parseSymboltable(filename: String): Unit = {
		if (!Paths.get("testfiles/" + filename).toFile.exists())
			return

		val parser:at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME.Parser =
			if (filename.endsWith(".l2"))
				new lang2Compiler.Parser(new lang2Compiler.Scanner("testfiles/" + filename))
			else
				new cmmCompiler.Parser(new cmmCompiler.Scanner("testfiles/" + filename))

		parser.Parse
		symbolTable = parser.getSymbolTable
		this.filename = filename
	}

}