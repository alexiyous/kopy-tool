package com.alexius.copytool.testing.remove_deleted

import com.alexius.copytool.copyProject
import com.alexius.copytool.new_regex_builder.RegexTemplates
import java.io.File

fun testRemoveDeleted() {
    val SOURCE_DIR = File(".").canonicalPath + "\\connector\\src\\main\\java\\code\\apps\\als\\connector\\testing\\remove_deleted\\src"
    val TARGET_DIR = File(".").canonicalPath + "\\connector\\src\\main\\java\\code\\apps\\als\\connector\\testing\\remove_deleted\\target"
    val TMP_DIR = File(".").canonicalPath + "\\connector\\src\\main\\java\\code\\apps\\als\\connector\\testing\\remove_deleted\\test_result\\tempDir"

    val config1 = RegexTemplates {
        skipNewerFiles(false)
        removeDeleted(true)
        conversions {
            component("Some", "Another")
            variable("some", "another")
            fileName("Some([A-Z][a-zA-Z0-9]*)\\.kt", "Another$1.kt")
        }
    }.build(
        sourceFolder = SOURCE_DIR,
        tempFolder = TMP_DIR,
        folderToCompare = TARGET_DIR
    )

    copyProject(config1)
}

fun main() {
    testRemoveDeleted()
}