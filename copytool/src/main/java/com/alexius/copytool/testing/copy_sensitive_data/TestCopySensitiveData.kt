package com.alexius.copytool.testing.copy_sensitive_data

import com.alexius.copytool.old_regex_builder.RegexTemplates
import com.alexius.copytool.copyProject
import com.alexius.copytool.testing.CopyCodeTest
import java.io.File

fun testCopySensitiveData() {

    val test = CopyCodeTest()

    val SOURCE_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\copy_sensitive_data\\src"
    val TARGET_DIR = File(".").canonicalPath + "\\library\\src\\main\\java\\code\\apps\\als\\connector\\testing\\copy_sensitive_data\\target"

    val config = RegexTemplates.custom()
        .addDetectionPattern("API_KEY\\s*=\\s*\"[^\"]*\"", "it is an API key, it should not be copied")
        .addDetectionPattern("PASSWORD\\s*=\\s*\"[^\"]*\"")
        .addPackageAndImportsConversion("com.alexius.copytool.testing.copy_sensitive_data.src",
            "com.alexius.copytool.testing.copy_sensitive_data.target")
        .addIgnorePattern("SAMPLE_API_KEY", reason = "it is a sample key, so its fine")
        .build(SOURCE_DIR, test.TMP_DIR, TARGET_DIR)

    copyProject(config)

    test.cleanupTestDirectories()
}

fun main() {
    testCopySensitiveData()
}