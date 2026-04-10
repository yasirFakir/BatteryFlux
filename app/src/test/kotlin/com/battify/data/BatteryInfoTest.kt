package com.battify.data

import org.junit.Assert.assertEquals
import org.junit.Test

class BatteryInfoTest {
    @Test
    fun testBatteryInfo() {
        val info = BatteryInfo(level = 80, isCharging = true, health = 2, status = 2)
        assertEquals(80, info.level)
        assertEquals(true, info.isCharging)
        assertEquals(2, info.health)
        assertEquals(2, info.status)
    }
}
