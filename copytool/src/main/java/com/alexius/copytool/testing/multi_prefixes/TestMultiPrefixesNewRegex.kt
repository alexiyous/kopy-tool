package com.alexius.copytool.testing.multi_prefixes

import com.alexius.copytool.copyProject
import com.alexius.copytool.new_regex_builder.RegexTemplates
import com.alexius.copytool.testing.CopyCodeTest
import java.io.File

fun testMultiPrefixesNewRegex() {
    val test = CopyCodeTest()

    val SOURCE_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\multi_prefixes\\src"
    val TARGET_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\multi_prefixes\\target"

    val config = RegexTemplates {
        conversions {
            // Convert component names
            component(
                fromPrefix = "Ms",
                toPrefix = "Cmp"
            )
            component(
                fromPrefix = "My",
                toPrefix = "Cmp"
            )
            component(
                fromPrefix = "Base",
                toPrefix = "Cmp"
            )

            // Convert variable names
            variable(
                fromPrefix = "ms",
                toPrefix = "cmp"
            )
            variable(
                fromPrefix = "my",
                toPrefix = "cmp"
            )
            variable(
                fromPrefix = "base",
                toPrefix = "cmp"
            )

            // Convert Kotlin file names
            fileName(
                fromPattern = "Ms([A-Z][a-zA-Z0-9]*)\\.kt",
                toPattern = "Cmp$1.kt"
            )
            fileName(
                fromPattern = "My([A-Z][a-zA-Z0-9]*)\\.kt",
                toPattern = "Cmp$1.kt"
            )
            fileName(
                fromPattern = "Base([A-Z][a-zA-Z0-9]*)\\.kt",
                toPattern = "Cmp$1.kt"
            )
        }
    }.build(
        "$SOURCE_DIR\\components",
        test.TMP_DIR + "\\components",
        "$TARGET_DIR\\components"
    )

    copyProject(config)

    test.cleanupTestDirectories()
}

fun main() {
    testMultiPrefixesNewRegex()
}