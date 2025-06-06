package com.alexius.copytool.testing.combined_directory_files


import com.alexius.copytool.copyProject
import com.alexius.copytool.new_regex_builder.RegexTemplates
import com.alexius.copytool.testing.CopyCodeTest
import java.io.File

fun testCombinedDirectoryAndFilesNewRegex() {

    val test = CopyCodeTest()

    val SOURCE_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\combined_directory_files\\src"
    val TARGET_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\combined_directory_files\\target"


    val config = RegexTemplates {
        conversions {
            // Component name conversions (PascalCase)
            component(
                fromPrefix = "Ms",
                toPrefix = "Cmp"
            )
            component(
                fromPrefix = "My",
                toPrefix = "Cmp"
            )

            // Variable name conversions (camelCase)
            variable(
                fromPrefix = "ms",
                toPrefix = "cmp"
            )
            variable(
                fromPrefix = "my",
                toPrefix = "cmp"
            )

            // Directory structure conversions
            directory(
                fromPattern = "ms_([a-z_]+)",
                toPattern = "cmp_$1"
            )
            directory(
                fromPattern = "my_([a-z_]+)",
                toPattern = "cmp_$1"
            )

            // File naming conversions
            fileName(
                fromPattern = "Ms([A-Z][a-zA-Z0-9]*)\\.kt",
                toPattern = "Cmp$1.kt"
            )
            fileName(
                fromPattern = "My([A-Z][a-zA-Z0-9]*)\\.kt",
                toPattern = "Cmp$1.kt"
            )
            fileName(
                fromPattern = "(ms|my)_([a-z_]+)\\.xml",
                toPattern = "cmp_$2.xml"
            )
        }
    }.build(SOURCE_DIR, test.TMP_DIR, TARGET_DIR)

    copyProject(config)

    test.cleanupTestDirectories()
}

fun main() {
    testCombinedDirectoryAndFilesNewRegex()
}