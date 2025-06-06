package com.alexius.copytool.testing.long_directory_name

import com.alexius.copytool.old_regex_builder.RegexTemplates
import com.alexius.copytool.copyProject
import com.alexius.copytool.testing.CopyCodeTest
import java.io.File

fun testLongDirectoryName() {

    val test = CopyCodeTest()

    val SOURCE_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\long_directory_name\\src"
    val TARGET_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\long_directory_name\\target"

    val config = RegexTemplates.custom()
        .addDirectoryConversion("ms_([a-zA-Z0-9_]+)", "cmp_$1")
        .addComponentConversion("Ms", "Cmp")
        .build(SOURCE_DIR, test.TMP_DIR, TARGET_DIR)

    copyProject(config)

    test.cleanupTestDirectories()
}

fun main() {
    testLongDirectoryName()
}