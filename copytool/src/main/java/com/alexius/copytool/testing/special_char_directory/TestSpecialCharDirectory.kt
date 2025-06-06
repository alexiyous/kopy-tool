package com.alexius.copytool.testing.special_char_directory

import com.alexius.copytool.old_regex_builder.RegexTemplates
import com.alexius.copytool.copyProject
import com.alexius.copytool.testing.CopyCodeTest

fun testSpecialCharDirectory() {
    val test = CopyCodeTest()

    val SOURCE_DIR = "library/src/main/java/code/apps/als/connector/testing/special_char_directory/src"
    val TARGET_DIR = "library/src/main/java/code/apps/als/connector/testing/special_char_directory/target"

    println("SOURCE_DIR: $SOURCE_DIR")

    val config = RegexTemplates.custom()
        .addDirectoryConversion("ms_test([^/]*?)dir", "cmp_test$1dir")
        .addComponentConversion("Ms", "Cmp")
        .build(SOURCE_DIR, test.TMP_DIR, TARGET_DIR)

    copyProject(config)

    test.cleanupTestDirectories()
}

fun main() {
    testSpecialCharDirectory()
}