package com.alexius.copytool.testing.mixed_type_case

import com.alexius.copytool.old_regex_builder.RegexTemplates
import com.alexius.copytool.copyProject
import com.alexius.copytool.testing.CopyCodeTest
import java.io.File

fun testMixedTypeCase() {

    val test = CopyCodeTest()

    val SOURCE_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\mixed_type_case\\src"
    val TARGET_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\mixed_type_case\\target"


    val config = RegexTemplates.custom()
        .addDirectoryConversion("(?i)ms_([a-zA-Z_]+)", "cmp_$1")
        .addComponentConversion("Ms", "Cmp")
        .build(SOURCE_DIR, test.TMP_DIR, TARGET_DIR)

    copyProject(config)

    test.cleanupTestDirectories()
}

fun main() {
    testMixedTypeCase()
}