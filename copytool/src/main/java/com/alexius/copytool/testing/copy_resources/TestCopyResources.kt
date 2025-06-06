package com.alexius.copytool.testing.copy_resources

import com.alexius.copytool.old_regex_builder.RegexTemplates
import com.alexius.copytool.copyProject
import com.alexius.copytool.testing.CopyCodeTest
import java.io.File

fun testCopyResources() {

    val test = CopyCodeTest()

    val SOURCE_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\copy_resources\\src"
    val TARGET_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\copy_resources\\target"

    val config = RegexTemplates.custom()
        .addResourceConversion("ms", "cmp")
        .addFileNameConversion("ms_([a-z_]+)\\.xml", "cmp_$1.xml")
        .build(SOURCE_DIR, test.TMP_DIR, TARGET_DIR)

    copyProject(config)

    test.cleanupTestDirectories()
}

fun main() {
    testCopyResources()
}