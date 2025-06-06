package com.alexius.copytool.testing.mixed_type_case


import com.alexius.copytool.copyProject
import com.alexius.copytool.new_regex_builder.RegexTemplates
import com.alexius.copytool.testing.CopyCodeTest
import java.io.File

fun testMixedTypeCaseNewRegex() {

    val test = CopyCodeTest()

    val SOURCE_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\mixed_type_case\\src"
    val TARGET_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\mixed_type_case\\target"


    val config = RegexTemplates {
        conversions {
            // Convert directory names (case insensitive)
            directory(
                fromPattern = "(?i)ms_([a-zA-Z_]+)",
                toPattern = "cmp_$1"
            )

            // Convert component names
            component(
                fromPrefix = "Ms",
                toPrefix = "Cmp"
            )
        }
    }.build(SOURCE_DIR, test.TMP_DIR, TARGET_DIR)


    copyProject(config)

    test.cleanupTestDirectories()
}

fun main() {
    testMixedTypeCaseNewRegex()
}