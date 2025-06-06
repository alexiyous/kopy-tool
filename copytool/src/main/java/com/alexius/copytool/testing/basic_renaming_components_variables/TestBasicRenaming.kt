package com.alexius.copytool.testing.basic_renaming_components_variables


import com.alexius.copytool.copyProject
import com.alexius.copytool.old_regex_builder.RegexTemplates

import com.alexius.copytool.testing.CopyCodeTest
import java.io.File

// STILL NEED VALIDATION (I'm having a hard time with detecting a current file's path with kotlin automatically, so currently is hardcoded)

// 1. Basic Component and Variable Renaming Test
fun testBasicRenaming() {
    val test = CopyCodeTest()

    val SOURCE_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\basic_renaming_components_variables\\src"
    val TARGET_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\basic_renaming_components_variables\\target"

    val config = RegexTemplates.custom()
        .addComponentConversion("Ms", "Cmp")
        .addVariableConversion("ms", "cmp")
        .addPackageAndImportsConversion(
            "com.alexius.copytool.testing.basic_renaming_components_variables.src",
            "com.alexius.copytool.testing.basic_renaming_components_variables.target"
        )
        .addFileNameConversion("Ms([A-Z][a-zA-Z0-9]*)\\.kt", "Cmp$1.kt")
        .build(SOURCE_DIR, test.TMP_DIR, TARGET_DIR)

    copyProject(config)

    test.cleanupTestDirectories()
}


fun main() {
    testBasicRenaming()
}