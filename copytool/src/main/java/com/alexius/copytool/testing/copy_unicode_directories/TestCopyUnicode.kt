package com.alexius.copytool.testing.copy_unicode_directories

import com.alexius.copytool.old_regex_builder.RegexTemplates
import com.alexius.copytool.copyProject
import com.alexius.copytool.testing.CopyCodeTest
import java.io.File

fun testCopyUnicode() {

    val test = CopyCodeTest()

    val SOURCE_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\copy_unicode_directories\\src"
    val TARGET_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\copy_unicode_directories\\target"

    val config = RegexTemplates.custom()
        .addDirectoryConversion("ms_(.+)", "cmp_$1")
        .addComponentConversion("Ms", "Cmp")
        .build(SOURCE_DIR, test.TMP_DIR, TARGET_DIR)

    copyProject(config)

    test.cleanupTestDirectories()

}

fun main() {
    testCopyUnicode()
}