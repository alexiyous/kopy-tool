package com.alexius.copytool.testing.detect_directories

import com.alexius.copytool.old_regex_builder.RegexTemplates
import com.alexius.copytool.copyProject
import com.alexius.copytool.testing.CopyCodeTest
import java.io.File

fun testDetectDirectories() {

    val test = CopyCodeTest()

    val SOURCE_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\detect_directories\\src"
    val TARGET_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\detect_directories\\target"

    val config = RegexTemplates.custom()
        .addComponentConversion("Ms", "Cmp")
        .addDirectoryConversion("ms_([a-z_]+)", "cmp_$1")
        .addDetectDirectory("secret_.*")
        .addDetectDirectory("private_.*")
        .build(SOURCE_DIR, test.TMP_DIR, TARGET_DIR)

    copyProject(config)

    test.cleanupTestDirectories()
}

fun main() {
    testDetectDirectories()
}