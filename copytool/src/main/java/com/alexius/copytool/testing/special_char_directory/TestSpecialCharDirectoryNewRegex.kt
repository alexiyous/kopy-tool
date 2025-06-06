package com.alexius.copytool.testing.special_char_directory


import com.alexius.copytool.copyProject
import com.alexius.copytool.new_regex_builder.RegexTemplates
import com.alexius.copytool.testing.CopyCodeTest

fun testSpecialCharDirectoryNewRegex() {
    val test = CopyCodeTest()

    val SOURCE_DIR = "library/src/main/java/code/apps/als/connector/testing/special_char_directory/src"
    val TARGET_DIR = "library/src/main/java/code/apps/als/connector/testing/special_char_directory/target"

    println("SOURCE_DIR: $SOURCE_DIR")
    val config = RegexTemplates {
        conversions {
            // Convert directory names (non-greedy match for anything except forward slash)
            directory(
                fromPattern = "ms_test([^/]*?)dir",
                toPattern = "cmp_test$1dir"
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
    testSpecialCharDirectoryNewRegex()
}