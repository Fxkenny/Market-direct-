package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.MarketRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("PriceMarket NG", appName)
  }

  @Test
  fun `market repository loads commodities and allows reporting`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val repo = MarketRepository(context)

    val commodities = repo.commodities.value
    assertTrue("Commodities list should not be empty", commodities.isNotEmpty())

    val rice = commodities.find { it.id == "rice50kg" }
    assertNotNull("Rice should be in commodity list", rice)

    val initialPoints = repo.userPoints.value
    repo.reportPrice(
      commodityId = "rice50kg",
      marketId = "mile12",
      price = 84000,
      reporterName = "Tester",
      phoneNumber = "08012345678",
      notes = "Fresh arrival",
      photoUri = null
    )

    assertEquals("User points should increment by 10", initialPoints + 10, repo.userPoints.value)
    val updatedRice = repo.commodities.value.find { it.id == "rice50kg" }
    assertEquals(84000, updatedRice?.marketPrices?.get("mile12"))
  }
}
