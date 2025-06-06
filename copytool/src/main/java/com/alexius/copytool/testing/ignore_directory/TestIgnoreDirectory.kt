package com.alexius.copytool.testing.ignore_directory

import com.alexius.copytool.old_regex_builder.RegexTemplates
import com.alexius.copytool.copyProject
import com.alexius.copytool.testing.CopyCodeTest
import java.io.File

fun testIgnoreDirectory() {

    val test = CopyCodeTest()

    val SOURCE_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\ignore_directory\\src"
    val TARGET_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\ignore_directory\\target"

    val config = RegexTemplates.custom()
        .addComponentConversion("Ms", "Cmp")
        .addDirectoryConversion("ms_([a-z_]+)", "cmp_$1")
        .addIgnoreDirectory("deprecated_.*", "it is deprecated, so we ignore it")
        .addIgnoreDirectory("temp_.*", "it is temporary, so we ignore it")
        .build(SOURCE_DIR, test.TMP_DIR, TARGET_DIR)

    copyProject(config)

    test.cleanupTestDirectories()
}

fun main() {
    testIgnoreDirectory()
}