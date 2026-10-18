package com.example.model

enum class AppLanguage {
  ENGLISH,
  PIDGIN
}

enum class CommodityCategory(val displayNameEn: String, val displayNamePidgin: String, val emoji: String) {
  ALL("All Items", "Everything", "🧺"),
  FOODSTUFF("Foodstuff", "Foodstuff & Swallow", "🌾"),
  MEAT_FISH("Meat & Fish", "Eran, Chicken & Eja", "🍗"),
  PROVISIONS("Provisions", "Provisions & Noodles", "🥫"),
  VEGETABLES("Vegetables", "Tomatoes & Pepper", "🍅"),
  OILS_SPICES("Oils & Spices", "Palm Oil & G/Nut Oil", "🛢️")
}

data class MarketInfo(
  val id: String,
  val name: String,
  val nickname: String,
  val lga: String,
  val address: String,
  val latitude: Double,
  val longitude: Double,
  val descriptionEn: String,
  val descriptionPidgin: String,
  val knownFor: String
)

data class Commodity(
  val id: String,
  val name: String,
  val pidginName: String,
  val nativeAliases: String, // Yoruba, Igbo, Hausa (e.g. "Iresi / Osikapa / Shinkafa")
  val category: CommodityCategory,
  val unit: String,
  val iconEmoji: String,
  val avgPrice: Int,
  val prevPrice: Int,
  val marketPrices: Map<String, Int>, // marketId -> price in Naira
  val description: String,
  val freshnessTip: String,
  val lastUpdated: String
) {
  val priceDiff: Int get() = avgPrice - prevPrice
  val isPriceUp: Boolean get() = priceDiff > 0
  val isPriceDown: Boolean get() = priceDiff < 0

  fun getCheapestMarket(markets: List<MarketInfo>): Pair<MarketInfo?, Int> {
    if (marketPrices.isEmpty()) return Pair(null, avgPrice)
    val cheapestEntry = marketPrices.minByOrNull { it.value } ?: return Pair(null, avgPrice)
    val market = markets.find { it.id == cheapestEntry.key }
    return Pair(market, cheapestEntry.value)
  }

  fun getHighestMarket(markets: List<MarketInfo>): Pair<MarketInfo?, Int> {
    if (marketPrices.isEmpty()) return Pair(null, avgPrice)
    val highestEntry = marketPrices.maxByOrNull { it.value } ?: return Pair(null, avgPrice)
    val market = markets.find { it.id == highestEntry.key }
    return Pair(market, highestEntry.value)
  }
}

data class PriceReport(
  val id: String,
  val commodityId: String,
  val commodityName: String,
  val marketId: String,
  val marketName: String,
  val price: Int,
  val reporterName: String,
  val phoneNumber: String? = null,
  val notes: String? = null,
  val photoUri: String? = null,
  val timestamp: Long = System.currentTimeMillis(),
  val pointsAwarded: Int = 10
)

data class PriceAlert(
  val id: String,
  val commodityId: String,
  val commodityName: String,
  val targetPrice: Int,
  val currentPrice: Int,
  val isTriggered: Boolean = false,
  val createdAt: Long = System.currentTimeMillis()
)

data class LeaderboardEntry(
  val rank: Int,
  val name: String,
  val points: Int,
  val badge: String,
  val marketBase: String,
  val reportsCount: Int,
  val isCurrentUser: Boolean = false
)

data class PriceForecast(
  val commodityId: String,
  val commodityName: String,
  val currentPrice: Int,
  val projectedPrice: Int,
  val trend: String, // "DROP", "HIKE", "STEADY"
  val percentChange: Int,
  val reasonEn: String,
  val reasonPidgin: String
)
