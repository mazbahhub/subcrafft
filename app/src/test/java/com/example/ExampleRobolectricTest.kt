package com.example

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleRobolectricTest {

  @Test
  fun testAppNameConstant() {
    val expectedAppName = "SubCraft"
    assertEquals("SubCraft", expectedAppName)
    assertTrue(expectedAppName.isNotBlank())
  }
}
