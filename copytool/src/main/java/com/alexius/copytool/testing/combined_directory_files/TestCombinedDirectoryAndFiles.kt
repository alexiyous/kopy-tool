package com.alexius.copytool.testing.combined_directory_files

import com.alexius.copytool.old_regex_builder.RegexTemplates
import com.alexius.copytool.copyProject
import com.alexius.copytool.testing.CopyCodeTest
import java.io.File

fun testCombinedDirectoryAndFiles() {

    val test = CopyCodeTest()

    val SOURCE_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\combined_directory_files\\src"
    val TARGET_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\combined_directory_files\\target"

    val config = RegexTemplates.custom()
        // Component and variable conversions
        .addComponentConversion("Ms", "Cmp")
        .addComponentConversion("My", "Cmp")
        .addVariableConversion("ms", "cmp")
        .addVariableConversion("my", "cmp")

        // Directory conversions
        .addDirectoryConversion("ms_([a-z_]+)", "cmp_$1")
        .addDirectoryConversion("my_([a-z_]+)", "cmp_$1")

        // File name conversions
        .addFileNameConversion("Ms([A-Z][a-zA-Z0-9]*)\\.kt", "Cmp$1.kt")
        .addFileNameConversion("My([A-Z][a-zA-Z0-9]*)\\.kt", "Cmp$1.kt")
        .addFileNameConversion("(ms|my)_([a-z_]+)\\.xml", "cmp_$2.xml")
        .build(SOURCE_DIR, test.TMP_DIR, TARGET_DIR)

    copyProject(config)

    test.cleanupTestDirectories()
}

fun main() {
    testCombinedDirectoryAndFiles()
}