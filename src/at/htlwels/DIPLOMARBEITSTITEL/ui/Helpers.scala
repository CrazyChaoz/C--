package at.htlwels.DIPLOMARBEITSTITEL.ui

import java.nio.file.Paths

import at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME.SymbolTable

object Helpers {
	var symbolTable: SymbolTable = _
	var filename = ""


	def getOrError[T](variable: T): T = if (variable equals null) throw new RuntimeException("Error, variable is null") else variable

	def parseSymboltable(filename: String): Unit = {
		if (!Paths.get("testfiles/" + filename).toFile.exists())
			return

		val parser:at.htlwels.DIPLOMARBEITSTITEL.JKU_FRAME.Parser =
			if (filename.endsWith(".l2"))
				new at.htlwels.DIPLOMARBEITSTITEL.compiler.lang2.Parser(new at.htlwels.DIPLOMARBEITSTITEL.compiler.lang2.Scanner("testfiles/" + filename))
			else
				new at.htlwels.DIPLOMARBEITSTITEL.compiler.cmm.Parser(new at.htlwels.DIPLOMARBEITSTITEL.compiler.cmm.Scanner("testfiles/" + filename))

		parser.Parse
		symbolTable = parser.getSymbolTable
		this.filename = filename
	}

}