package com.alexius.copytool.testing.full_project


import com.alexius.copytool.copyProject
import com.alexius.copytool.new_regex_builder.RegexTemplates
import com.alexius.copytool.testing.CopyCodeTest
import java.io.File

fun testFullProjectNewRegex() {

    val test = CopyCodeTest()

    val SOURCE_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\full_project\\src"
    val TARGET_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\full_project\\target"

    val config = RegexTemplates {
        conversions {
            // Component and variable name conversions
            component(
                fromPrefix = "Ms",
                toPrefix = "Cmp"
            )
            variable(
                fromPrefix = "ms",
                toPrefix = "cmp"
            )

            // Package path conversion
            packagesAndImports(
                fromPackage = "com.example.msapp",
                toPackage = "com.example.cmpapp"
            )

            // Resource ID conversions (layout and id)
            resource(
                fromPrefix = "ms",
                toPrefix = "cmp"
            )

            // File naming patterns
            fileName(
                fromPattern = "Ms([A-Z][a-zA-Z0-9]*)\\.kt",  // Kotlin files
                toPattern = "Cmp$1.kt"
            )
            fileName(
                fromPattern = "ms_([a-z_]+)\\.xml",  // XML layout files
                toPattern = "cmp_$1.xml"
            )

            // Directory structure pattern
            directory(
                fromPattern = "ms([a-z_]+)",
                toPattern = "cmp$1"
            )
        }
    }.build(SOURCE_DIR, test.TMP_DIR, TARGET_DIR)


    copyProject(config)

    test.cleanupTestDirectories()
}

fun main() {
    testFullProjectNewRegex()
}