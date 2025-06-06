package com.alexius.copytool.testing.directories_conversion


import com.alexius.copytool.copyProject
import com.alexius.copytool.new_regex_builder.RegexTemplates
import com.alexius.copytool.testing.CopyCodeTest
import java.io.File

fun testDirectoriesConversionNewRegex() {

    val test = CopyCodeTest()

    val SOURCE_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\directories_conversion\\src"
    val TARGET_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\directories_conversion\\target"

    val config = RegexTemplates {
        conversions {
            // Component name conversions (PascalCase)
            component(
                fromPrefix = "Cs",
                toPrefix = "Amp"
            )
            component(
                fromPrefix = "Cy",
                toPrefix = "Amp"
            )

            // Variable name conversions (camelCase)
            variable(
                fromPrefix = "cs",
                toPrefix = "amp"
            )
            variable(
                fromPrefix = "cy",
                toPrefix = "amp"
            )

            // Directory path pattern conversions
            directory(
                fromPattern = "cs_([a-z_]+)",
                toPattern = "amp_$1"
            )
            directory(
                fromPattern = "cy_([a-z_]+)",
                toPattern = "amp_$1"
            )
        }
    }.build(SOURCE_DIR, test.TMP_DIR, TARGET_DIR)


    copyProject(config)

    test.cleanupTestDirectories()
}

fun main() {
    testDirectoriesConversionNewRegex()
}