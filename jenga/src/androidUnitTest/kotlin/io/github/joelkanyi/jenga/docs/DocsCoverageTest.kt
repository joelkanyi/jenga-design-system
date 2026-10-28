package io.github.joelkanyi.jenga.docs

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class DocsCoverageTest {

    private val repo = File("..")

    private val publicComponents: Set<String> by lazy {
        val declaration = Regex("""@Composable\s*\n(?:@[^\n]*\n)*public fun (?:<[^>]*> )?(Jenga[A-Za-z]+)\(""")
        File(repo, "jenga/src/commonMain/kotlin/io/github/joelkanyi/jenga/component")
            .walkTopDown()
            .filter { it.extension == "kt" }
            .flatMap { file -> declaration.findAll(file.readText()).map { it.groupValues[1] } }
            .toSortedSet()
    }

    @Test
    fun everyPublicComponentIsListedInTheReadme() {
        val readme = File(repo, "README.md").readText()
        assertEquals(emptyList<String>(), publicComponents.filterNot { "`$it`" in readme })
    }

    @Test
    fun everyPublicComponentIsInTheCatalog() {
        val catalog = File(repo, "catalog/src/commonMain")
            .walkTopDown()
            .filter { it.extension == "kt" }
            .joinToString("\n") { it.readText() }
        assertEquals(emptyList<String>(), publicComponents.filterNot { Regex("""\b$it\s*[({]""").containsMatchIn(catalog) })
    }
}
