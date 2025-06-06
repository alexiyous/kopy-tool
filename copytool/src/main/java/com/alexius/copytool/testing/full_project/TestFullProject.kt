package com.alexius.copytool.testing.full_project

import com.alexius.copytool.old_regex_builder.RegexTemplates
import com.alexius.copytool.copyProject
import com.alexius.copytool.testing.CopyCodeTest
import java.io.File

fun testFullProject() {

    val test = CopyCodeTest()

    val SOURCE_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\full_project\\src"
    val TARGET_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\full_project\\target"

    val config = RegexTemplates.custom()
        .addComponentConversion("Ms", "Cmp")
        .addVariableConversion("ms", "cmp")
        .addPackageAndImportsConversion(
            "com.example.msapp",
            "com.example.cmpapp"
        )
        .addResourceConversion("ms", "cmp")
        .addFileNameConversion("Ms([A-Z][a-zA-Z0-9]*)\\.kt", "Cmp$1.kt")
        .addFileNameConversion("ms_([a-z_]+)\\.xml", "cmp_$1.xml")
        .addDirectoryConversion("ms([a-z_]+)", "cmp$1")
        .build(SOURCE_DIR, test.TMP_DIR, TARGET_DIR)

    copyProject(config)

    test.cleanupTestDirectories()
}

fun main() {
    testFullProject()
}