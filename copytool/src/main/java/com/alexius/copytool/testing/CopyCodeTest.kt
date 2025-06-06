package com.alexius.copytool.testing


import java.io.File

class CopyCodeTest {
    private val TEST_ROOT = File(".").canonicalPath + "\\connector\\src\\main\\java\\code\\apps\\als\\connector\\testing\\test_result"
    internal val TMP_DIR = "$TEST_ROOT${File.separator}tempDir"

    internal fun cleanupTestDirectories() {
        File(TEST_ROOT).deleteRecursively()
    }
}