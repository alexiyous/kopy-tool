package com.alexius.copytool.testing.directories_conversion

import com.alexius.copytool.old_regex_builder.RegexTemplates
import com.alexius.copytool.copyProject
import com.alexius.copytool.testing.CopyCodeTest
import java.io.File

fun testDirectoriesConversion() {

    val test = CopyCodeTest()

    val SOURCE_DIR = File(".").canonicalPath + "\\connector\\src\\main\\java\\code\\apps\\als\\connector\\testing\\directories_conversion\\src"
    val TARGET_DIR = File(".").canonicalPath + "\\connector\\src\\main\\java\\code\\apps\\als\\connector\\testing\\directories_conversion\\target"

    println("Source Directory: $SOURCE_DIR")

    val config = RegexTemplates.custom()
        .addComponentConversion("Cs", "Amp")
        .addComponentConversion("Cy", "Amp")
        .addVariableConversion("cs", "amp")
        .addVariableConversion("cy", "amp")
        .addDirectoryConversion("cs_([a-z_]+)", "amp_$1")
        .addDirectoryConversion("cy_([a-z_]+)", "amp_$1")
        .build(SOURCE_DIR, test.TMP_DIR, TARGET_DIR)

    copyProject(config)

    test.cleanupTestDirectories()
}

fun main() {
    testDirectoriesConversion()
}