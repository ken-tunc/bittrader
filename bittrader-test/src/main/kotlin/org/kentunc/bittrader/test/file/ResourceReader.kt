package org.kentunc.bittrader.test.file

import java.io.File

object ResourceReader {

    fun readResource(relPath: String): String {
        return File("src/test/resources/$relPath").readText()
    }
}
