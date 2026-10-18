package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.AppLanguage
import com.example.model.Commodity
import com.example.model.CommodityCategory
import com.example.model.LeaderboardEntry
import com.example.model.MarketInfo
import com.example.model.PriceAlert
import com.example.model.PriceForecast
import com.example.model.PriceReport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.text.NumberFormat
import java.util.Locale

class MarketRepository(context: Context) {

  private val prefs: SharedPreferences =
    context.getSharedPreferences("pricemarket_ng_prefs", Context.MODE_PRIVATE)

  // Standard Lagos Markets
  val markets: List<MarketInfo> = listOf(
    MarketInfo(
      id = "mile12",
      name = "Mile 12 Market",
      nickname = "Mile 12 (Ketu)",
      lga = "Kosofe LGA",
      address = "Ikorodu Road, Ketu, Lagos",
      latitude = 6.6042,
      longitude = 3.3986,
      descriptionEn = "West Africa's premier wholesale foodstuff hub. Best bulk prices on yams, tomatoes, peppers & grains.",
      descriptionPidgin = "The king of all food market! Direct trailer from north dey land here. Cheapest bulk price guaranteed.",
      knownFor = "Bulk Vegetables, Yam & Northern Grains"
    ),
    MarketInfo(
      id = "mushin",
      name = "Daleko / Mushin Market",
      nickname = "Daleko (Mushin)",
      lga = "Mushin LGA",
      address = "Isolo Road, Daleko, Mushin, Lagos",
      latitude = 6.5298,
      longitude = 3.3551,
      descriptionEn = "Largest national distribution center for rice, palm oil, vegetable oil, and carton provisions.",
      descriptionPidgin = "Where rice dealers and palm oil distributors dey gather. Best price for bag of rice and 25L oil.",
      knownFor = "Bagged Rice, Palm Oil & Wholesale Provisions"
    ),
    MarketInfo(
      id = "oyingbo",
      name = "Oyingbo Market",
      nickname = "Oyingbo (Ebute Metta)",
      lga = "Lagos Mainland LGA",
      address = "Murtala Muhammed Way, Ebute Metta, Lagos",
      latitude = 6.4783,
      longitude = 3.3814,
      descriptionEn = "Historic ultra-modern market famed for fresh seafood, crayfish, stockfish, and fresh farm produce.",
      descriptionPidgin = "Clean modern market for mainland. Sharp seafood, fresh Titus fish, snails and Ijebu garri.",
      knownFor = "Seafood, Crayfish, Stockfish & Fresh Staples"
    ),
    MarketInfo(
      id = "oshodi",
      name = "Oshodi Market",
      nickname = "Oshodi Central",
      lga = "Oshodi-Isolo LGA",
      address = "Agege Motor Road, Oshodi, Lagos",
      latitude = 6.5451,
      longitude = 3.3444,
      descriptionEn = "High-energy commercial interchange known for competitive prices on frozen poultry and dry staples.",
      descriptionPidgin = "Center of Lagos hustle! Frozen chicken, turkey, cartons of spaghetti and quick bargains.",
      knownFor = "Frozen Chicken, Turkey & Dry Provisions"
    ),
    MarketInfo(
      id = "alaba",
      name = "Alaba Rago / International",
      nickname = "Alaba Rago (Ojo)",
      lga = "Ojo LGA",
      address = "Badagry Expressway, Ojo, Lagos",
      latitude = 6.4632,
      longitude = 3.1932,
      descriptionEn = "Major trade gateway with direct livestock and tuber supplies from northern & middle-belt farmers.",
      descriptionPidgin = "Big cattle and livestock market. Directly from farm trucks, good wholesale deals.",
      knownFor = "Livestock, Big Tubers & Wholesale Tubers"
    ),
    MarketInfo(
      id = "agege",
      name = "Agege Market",
      nickname = "Agege (Pen Cinema)",
      lga = "Agege LGA",
      address = "Old Abeokuta Motor Rd, Agege, Lagos",
      latitude = 6.6219,
      longitude = 3.3235,
      descriptionEn = "Fast-paced northern produce terminal with direct train links and fresh dairy / grain supplies.",
      descriptionPidgin = "Train station market. Plenty onions, beans and fresh vegetables straight from trailers.",
      knownFor = "Onions, Beans, Spices & Maize"
    ),
    MarketInfo(
      id = "ikeja",
      name = "Ikeja / Computer Village Hub",
      nickname = "Ikeja Mart",
      lga = "Ikeja LGA",
      address = "Otigba / Medical Road, Ikeja, Lagos",
      latitude = 6.5965,
      longitude = 3.3421,
      descriptionEn = "Central commercial retail spot convenient for shoppers and workers seeking packaged provisions.",
      descriptionPidgin = "Mainland business center. Handy for quick shopping, provisions and packaged food.",
      knownFor = "Packaged Groceries, Provisions & Drinks"
    )
  )

  private val _commodities = MutableStateFlow<List<Commodity>>(emptyList())
  val commodities: StateFlow<List<Commodity>> = _commodities.asStateFlow()

  private val _priceReports = MutableStateFlow<List<PriceReport>>(emptyList())
  val priceReports: StateFlow<List<PriceReport>> = _priceReports.asStateFlow()

  private val _priceAlerts = MutableStateFlow<List<PriceAlert>>(emptyList())
  val priceAlerts: StateFlow<List<PriceAlert>> = _priceAlerts.asStateFlow()

  private val _userPoints = MutableStateFlow(prefs.getInt("user_points", 30))
  val userPoints: StateFlow<Int> = _userPoints.asStateFlow()

  private val _currentLanguage = MutableStateFlow(
    if (prefs.getString("app_lang", "ENGLISH") == "PIDGIN") AppLanguage.PIDGIN else AppLanguage.ENGLISH
  )
  val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

  val forecasts: List<PriceForecast> = listOf(
    PriceForecast(
      commodityId = "tomatoes",
      commodityName = "Tomatoes (Big Basket)",
      currentPrice = 65000,
      projectedPrice = 57000,
      trend = "DROP",
      percentChange = -12,
      reasonEn = "New harvest trucks arriving from Kano and Plateau state by Thursday; supply increase expected.",
      reasonPidgin = "New harvest trucks dey land from Kano and Jos by Thursday; basket price go fall sharp!"
    ),
    PriceForecast(
      commodityId = "rice50kg",
      commodityName = "Bag of Rice (50kg Mama Gold)",
      currentPrice = 92000,
      projectedPrice = 90000,
      trend = "DROP",
      percentChange = -2,
      reasonEn = "Mild drop in forex landing costs for grain millers in Daleko and Mile 12.",
      reasonPidgin = "Dollar landing cost calm down small for millers, bag go reduce small by next week."
    ),
    PriceForecast(
      commodityId = "palmoil25l",
      commodityName = "Palm Oil (25L Jerrycan)",
      currentPrice = 48000,
      projectedPrice = 51000,
      trend = "HIKE",
      percentChange = 6,
      reasonEn = "Off-peak milling season in eastern palm plantations; slight cost jump anticipated.",
      reasonPidgin = "Palm fruits dey finish small for eastern mills, supply tight small so price fit jump."
    ),
    PriceForecast(
      commodityId = "garri_ijebu",
      commodityName = "Garri Ijebu (50kg Bag)",
      currentPrice = 45000,
      projectedPrice = 44500,
      trend = "STEADY",
      percentChange = -1,
      reasonEn = "Cassava processing in Ogun State remains abundant; price projected stable.",
      reasonPidgin = "Cassava plenty well well for Ijebu factories, price no go shake much."
    )
  )

  init {
    loadInitialData()
    loadReports()
    loadAlerts()
  }

  private fun loadInitialData() {
    val savedCommoditiesJson = prefs.getString("commodities_cache", null)
    if (savedCommoditiesJson != null) {
      try {
        val list = parseCommoditiesJson(savedCommoditiesJson)
        if (list.isNotEmpty()) {
          _commodities.value = list
          return
        }
      } catch (_: Exception) {}
    }

    // Default mock data reflecting today's realistic Nigerian market prices
    val defaultList = listOf(
      Commodity(
        id = "rice50kg",
        name = "Bag of Rice (50kg Mama Gold)",
        pidginName = "Bag of Rice 50kg (Original)",
        nativeAliases = "Iresi / Osikapa / Shinkafa",
        category = CommodityCategory.FOODSTUFF,
        unit = "50kg bag",
        iconEmoji = "🌾",
        avgPrice = 92000,
        prevPrice = 90000, // +₦2000 hike
        marketPrices = mapOf(
          "mile12" to 85000,
          "mushin" to 89500,
          "oyingbo" to 92000,
          "oshodi" to 93500,
          "alaba" to 94000,
          "agege" to 91000,
          "ikeja" to 97000
        ),
        description = "Long grain parboiled Nigerian milled rice. Clean, stone-free 50kg bag.",
        freshnessTip = "Check seal stitch and weight with bag scale before loading transport.",
        lastUpdated = "Today 8:00 AM"
      ),
      Commodity(
        id = "beans50kg",
        name = "Beans (50kg Bag Oloyin / Honey)",
        pidginName = "Sweet Honey Beans 50kg",
        nativeAliases = "Ewa Oloyin / Agwa / Wake",
        category = CommodityCategory.FOODSTUFF,
        unit = "50kg bag",
        iconEmoji = "🫘",
        avgPrice = 85000,
        prevPrice = 87500, // -₦2500 drop
        marketPrices = mapOf(
          "mile12" to 80000,
          "mushin" to 83000,
          "agege" to 82500,
          "oyingbo" to 86500,
          "oshodi" to 87000,
          "alaba" to 88000,
          "ikeja" to 91000
        ),
        description = "Freshly harvested sweet brown honey beans (Oloyin) from Maiduguri.",
        freshnessTip = "Look for shiny, weevil-free beans with a rich caramel brown coat.",
        lastUpdated = "Today 7:45 AM"
      ),
      Commodity(
        id = "garri_ijebu",
        name = "Garri Ijebu (50kg Bag)",
        pidginName = "Garri Ijebu (Sour & Sharp)",
        nativeAliases = "Gari Ijebu / Garri",
        category = CommodityCategory.FOODSTUFF,
        unit = "50kg bag",
        iconEmoji = "🥣",
        avgPrice = 45000,
        prevPrice = 46000, // -₦1000 drop
        marketPrices = mapOf(
          "mile12" to 42000,
          "oyingbo" to 43500,
          "mushin" to 44000,
          "agege" to 45500,
          "oshodi" to 46000,
          "alaba" to 47000,
          "ikeja" to 49000
        ),
        description = "Crisp, tart, finely processed Ijebu cassava flakes. Floats perfectly in chilled water.",
        freshnessTip = "Squeeze a handful: dry Ijebu garri crackles smoothly without doughy moisture.",
        lastUpdated = "Today 8:30 AM"
      ),
      Commodity(
        id = "garri_white",
        name = "White Garri (50kg Bag)",
        pidginName = "White Garri for Eba 50kg",
        nativeAliases = "Gari Funfun / Garri",
        category = CommodityCategory.FOODSTUFF,
        unit = "50kg bag",
        iconEmoji = "🍚",
        avgPrice = 38000,
        prevPrice = 37000, // +₦1000 hike
        marketPrices = mapOf(
          "mile12" to 35000,
          "agege" to 36500,
          "mushin" to 37500,
          "oyingbo" to 38500,
          "oshodi" to 39000,
          "alaba" to 40000,
          "ikeja" to 42000
        ),
        description = "Coarse dry white garri ideal for thick, smooth eba swallow.",
        freshnessTip = "Check for pleasant toasted scent and absence of damp clumping.",
        lastUpdated = "Today 8:15 AM"
      ),
      Commodity(
        id = "yam_5tubers",
        name = "New Yam (5 Big Tubers)",
        pidginName = "Yam 5 Big Tubers (Benue / Abuja)",
        nativeAliases = "Isu / Ji / Doya",
        category = CommodityCategory.FOODSTUFF,
        unit = "5 big tubers",
        iconEmoji = "🥔",
        avgPrice = 18500,
        prevPrice = 20000, // -₦1500 drop
        marketPrices = mapOf(
          "mile12" to 15000,
          "alaba" to 16500,
          "agege" to 17500,
          "mushin" to 18500,
          "oyingbo" to 19500,
          "oshodi" to 20000,
          "ikeja" to 22500
        ),
        description = "Large, heavy Zakibiam & Abuja tubers. Excellent for pounded yam.",
        freshnessTip = "Ensure neck is firm with no softness or rot patches near the head.",
        lastUpdated = "Today 7:30 AM"
      ),
      Commodity(
        id = "palmoil25l",
        name = "Pure Red Palm Oil (25L Jerrycan)",
        pidginName = "Pure Palm Oil 25L (No Mixture)",
        nativeAliases = "Epo Pupa / Mmanụ nri / Man Ja",
        category = CommodityCategory.OILS_SPICES,
        unit = "25L Jerrycan",
        iconEmoji = "🛢️",
        avgPrice = 48000,
        prevPrice = 46500, // +₦1500 hike
        marketPrices = mapOf(
          "mushin" to 45000,
          "mile12" to 46000,
          "oyingbo" to 47500,
          "agege" to 48000,
          "oshodi" to 49000,
          "alaba" to 49500,
          "ikeja" to 52000
        ),
        description = "Thick, aromatic unadulterated red oil sourced directly from Edo / Ondo groves.",
        freshnessTip = "Notice deep ruby red tint with pleasant fresh palm scent, zero soapy foam.",
        lastUpdated = "Today 9:00 AM"
      ),
      Commodity(
        id = "groundnutoil25l",
        name = "Vegetable / Groundnut Oil (25L Kings)",
        pidginName = "Kings Veg Oil 25L Jerrycan",
        nativeAliases = "Ororo / Mmanụ akwụkwọ nri / Man Gyada",
        category = CommodityCategory.OILS_SPICES,
        unit = "25L Jerrycan",
        iconEmoji = "🧴",
        avgPrice = 52000,
        prevPrice = 53000, // -₦1000 drop
        marketPrices = mapOf(
          "mushin" to 49500,
          "mile12" to 50500,
          "oshodi" to 52000,
          "oyingbo" to 52500,
          "agege" to 53000,
          "alaba" to 53500,
          "ikeja" to 55000
        ),
        description = "Cholesterol-free, crystal-clear triple refined vegetable cooking oil.",
        freshnessTip = "Confirm safety seal on the red cap and clear transparent clarity.",
        lastUpdated = "Today 8:40 AM"
      ),
      Commodity(
        id = "tomatoes_basket",
        name = "Fresh Tomatoes (Derica / Big Basket)",
        pidginName = "Tomatoes Big Basket (Kano Fresh)",
        nativeAliases = "Tomati / Tomato / Tumatur",
        category = CommodityCategory.VEGETABLES,
        unit = "Big basket",
        iconEmoji = "🍅",
        avgPrice = 65000,
        prevPrice = 72000, // -₦7000 big drop
        marketPrices = mapOf(
          "mile12" to 58000,
          "agege" to 61000,
          "mushin" to 64000,
          "oshodi" to 66000,
          "oyingbo" to 67000,
          "alaba" to 68000,
          "ikeja" to 73000
        ),
        description = "Plump, firm Jos & Kano Roma tomatoes. Thick skin with high paste yield.",
        freshnessTip = "Inspect the bottom of basket for crushed tomatoes before negotiating.",
        lastUpdated = "Today 6:30 AM"
      ),
      Commodity(
        id = "pepper_bag",
        name = "Fresh Rodo / Pepper (Bag)",
        pidginName = "Habanero / Atarodo Big Bag",
        nativeAliases = "Ata Rodo / Ose / Barkono",
        category = CommodityCategory.VEGETABLES,
        unit = "Wholesale bag",
        iconEmoji = "🌶️",
        avgPrice = 42000,
        prevPrice = 40000, // +₦2000 hike
        marketPrices = mapOf(
          "mile12" to 38000,
          "agege" to 40000,
          "mushin" to 41500,
          "oyingbo" to 43000,
          "oshodi" to 44000,
          "alaba" to 44500,
          "ikeja" to 48000
        ),
        description = "Spicy, vibrant scotch bonnet / habanero peppers freshly unpacked from trailers.",
        freshnessTip = "Choose tight-skinned, glossy peppers with green stems.",
        lastUpdated = "Today 6:45 AM"
      ),
      Commodity(
        id = "onions_bag",
        name = "Dry Onions (100kg Bag Aliero)",
        pidginName = "Dry Purple Onions (100kg Big Bag)",
        nativeAliases = "Alubosa / Yabas / Albasa",
        category = CommodityCategory.VEGETABLES,
        unit = "100kg bag",
        iconEmoji = "🧅",
        avgPrice = 55000,
        prevPrice = 58000, // -₦3000 drop
        marketPrices = mapOf(
          "mile12" to 48000,
          "agege" to 51000,
          "mushin" to 53500,
          "oyingbo" to 55500,
          "oshodi" to 56000,
          "alaba" to 57000,
          "ikeja" to 62000
        ),
        description = "Crisp Aliero (Kebbi State) purple onions with papery skin and long shelf-life.",
        freshnessTip = "Feel neck firmness: soft necks indicate moisture spoilage.",
        lastUpdated = "Today 7:15 AM"
      ),
      Commodity(
        id = "chicken_carton",
        name = "Chicken (Carton Hard Chicken)",
        pidginName = "Hard Chicken Carton (Orobro)",
        nativeAliases = "Adie / Okuko / Kaza",
        category = CommodityCategory.MEAT_FISH,
        unit = "Carton (10kg)",
        iconEmoji = "🍗",
        avgPrice = 68000,
        prevPrice = 66000, // +₦2000 hike
        marketPrices = mapOf(
          "oshodi" to 64000,
          "oyingbo" to 65500,
          "mushin" to 67000,
          "mile12" to 68000,
          "agege" to 69000,
          "alaba" to 70000,
          "ikeja" to 73000
        ),
        description = "Top-tier imported frozen hard layer chicken cuts. Rich taste for party jollof.",
        freshnessTip = "Verify thick carton seal and absence of melting pink refrozen ice.",
        lastUpdated = "Today 9:15 AM"
      ),
      Commodity(
        id = "turkey_carton",
        name = "Turkey Wings (Carton 10kg)",
        pidginName = "Turkey Wings Carton 10kg",
        nativeAliases = "Tolotolo / Torotoro / Tolotolo",
        category = CommodityCategory.MEAT_FISH,
        unit = "Carton (10kg)",
        iconEmoji = "🦃",
        avgPrice = 75000,
        prevPrice = 75000, // stable
        marketPrices = mapOf(
          "oshodi" to 71000,
          "oyingbo" to 72500,
          "mushin" to 74000,
          "mile12" to 75000,
          "agege" to 76000,
          "alaba" to 77000,
          "ikeja" to 80000
        ),
        description = "Heavy meaty turkey wings. Perfect cut size for catering and home grills.",
        freshnessTip = "Ensure deep freeze solid block state upon delivery.",
        lastUpdated = "Today 9:20 AM"
      ),
      Commodity(
        id = "fish_carton",
        name = "Fish (Titus / Mackerel Carton 20kg)",
        pidginName = "Original Titus Fish Carton 20kg",
        nativeAliases = "Eja Titus / Azụ / Kifi",
        category = CommodityCategory.MEAT_FISH,
        unit = "Carton (20kg)",
        iconEmoji = "🐟",
        avgPrice = 88000,
        prevPrice = 90000, // -₦2000 drop
        marketPrices = mapOf(
          "oyingbo" to 82000,
          "oshodi" to 85000,
          "mushin" to 87000,
          "mile12" to 88500,
          "agege" to 89000,
          "alaba" to 90000,
          "ikeja" to 94000
        ),
        description = "Original Norwegian blue mackerel (Titus) carton. High in Omega-3.",
        freshnessTip = "Look for distinctive dark zig-zag stripes on silvery belly.",
        lastUpdated = "Today 8:50 AM"
      ),
      Commodity(
        id = "eggs_crate",
        name = "Fresh Eggs (Crate of 30 Jumbo)",
        pidginName = "Fresh Eggs Crate (Jumbo Size)",
        nativeAliases = "Eyin Adie / Àkwá / Qwai",
        category = CommodityCategory.MEAT_FISH,
        unit = "Crate of 30",
        iconEmoji = "🥚",
        avgPrice = 5200,
        prevPrice = 5500, // -₦300 drop
        marketPrices = mapOf(
          "mushin" to 4800,
          "mile12" to 4900,
          "agege" to 5000,
          "oyingbo" to 5200,
          "oshodi" to 5300,
          "alaba" to 5400,
          "ikeja" to 5800
        ),
        description = "Farm-fresh jumbo brown eggs from direct poultry pens in Ogun / Oyo belt.",
        freshnessTip = "Shake egg gently: no liquid sloshing means supreme freshness.",
        lastUpdated = "Today 9:30 AM"
      ),
      Commodity(
        id = "indomie_carton",
        name = "Indomie Instant Noodles (Carton 40pcs)",
        pidginName = "Indomie Onion / Chicken (Super Pack Carton)",
        nativeAliases = "Indomie / Noodle / Noodles",
        category = CommodityCategory.PROVISIONS,
        unit = "Carton 40 packs",
        iconEmoji = "🍜",
        avgPrice = 11500,
        prevPrice = 11000, // +₦500 hike
        marketPrices = mapOf(
          "mushin" to 10800,
          "mile12" to 11000,
          "oshodi" to 11300,
          "oyingbo" to 11400,
          "agege" to 11500,
          "alaba" to 11600,
          "ikeja" to 12200
        ),
        description = "Indomie Onion Chicken Flavour 120g Super Pack 40 count carton.",
        freshnessTip = "Verify uncrushed carton edges and manufacturer production stamp.",
        lastUpdated = "Today 10:00 AM"
      ),
      Commodity(
        id = "spaghetti_carton",
        name = "Golden Penny Spaghetti (Carton 20pcs)",
        pidginName = "Golden Penny Spaghetti Carton 20",
        nativeAliases = "Spaghetti / Talia / Nasta",
        category = CommodityCategory.PROVISIONS,
        unit = "Carton (20 x 500g)",
        iconEmoji = "🍝",
        avgPrice = 19000,
        prevPrice = 18500, // +₦500 hike
        marketPrices = mapOf(
          "mushin" to 17800,
          "mile12" to 18200,
          "oshodi" to 18600,
          "agege" to 18800,
          "oyingbo" to 19000,
          "alaba" to 19200,
          "ikeja" to 20500
        ),
        description = "Enriched durum wheat semolina pasta made in Nigeria by Flour Mills.",
        freshnessTip = "Carton must have secure tape and zero broken strands.",
        lastUpdated = "Today 9:45 AM"
      )
    )

    _commodities.value = defaultList
    saveCommodities(defaultList)
  }

  fun reportPrice(
    commodityId: String,
    marketId: String,
    price: Int,
    reporterName: String,
    phoneNumber: String?,
    notes: String?,
    photoUri: String?
  ): PriceReport {
    val commodity = _commodities.value.find { it.id == commodityId }
    val market = markets.find { it.id == marketId }

    val report = PriceReport(
      id = "rep_${System.currentTimeMillis()}",
      commodityId = commodityId,
      commodityName = commodity?.name ?: "Market Item",
      marketId = marketId,
      marketName = market?.name ?: "Lagos Market",
      price = price,
      reporterName = reporterName.ifBlank { "You (Market Reporter)" },
      phoneNumber = phoneNumber,
      notes = notes,
      photoUri = photoUri,
      timestamp = System.currentTimeMillis(),
      pointsAwarded = 10
    )

    // Update community report list
    val currentReports = _priceReports.value.toMutableList()
    currentReports.add(0, report)
    _priceReports.value = currentReports
    saveReports(currentReports)

    // Update commodity live prices
    val updatedCommodities = _commodities.value.map { item ->
      if (item.id == commodityId) {
        val updatedMap = item.marketPrices.toMutableMap()
        updatedMap[marketId] = price
        val newAvg = if (updatedMap.isNotEmpty()) updatedMap.values.average().toInt() else price
        item.copy(
          avgPrice = newAvg,
          marketPrices = updatedMap,
          lastUpdated = "Just now (Verified)"
        )
      } else {
        item
      }
    }
    _commodities.value = updatedCommodities
    saveCommodities(updatedCommodities)

    // Award +10 points to user
    val newPoints = _userPoints.value + 10
    _userPoints.value = newPoints
    prefs.edit().putInt("user_points", newPoints).apply()

    // Check if any price alerts were triggered by this update
    checkAlertsForCommodity(commodityId, price)

    return report
  }

  fun addPriceAlert(commodityId: String, targetPrice: Int) {
    val commodity = _commodities.value.find { it.id == commodityId } ?: return
    val alert = PriceAlert(
      id = "alert_${System.currentTimeMillis()}",
      commodityId = commodityId,
      commodityName = commodity.name,
      targetPrice = targetPrice,
      currentPrice = commodity.avgPrice,
      isTriggered = commodity.avgPrice <= targetPrice
    )
    val updated = _priceAlerts.value.toMutableList()
    updated.add(0, alert)
    _priceAlerts.value = updated
    saveAlerts(updated)
  }

  fun removePriceAlert(alertId: String) {
    val updated = _priceAlerts.value.filter { it.id != alertId }
    _priceAlerts.value = updated
    saveAlerts(updated)
  }

  fun toggleLanguage() {
    val newLang = if (_currentLanguage.value == AppLanguage.ENGLISH) AppLanguage.PIDGIN else AppLanguage.ENGLISH
    _currentLanguage.value = newLang
    prefs.edit().putString("app_lang", newLang.name).apply()
  }

  fun getTopCheapestMarketsToday(): List<Triple<MarketInfo, String, String>> {
    // Return top 5 markets that host the lowest prices for staple food items today
    return listOf(
      Triple(markets[0], "Cheapest Rice, Yam & Tomatoes", "Save up to ₦14,000 on bulk baskets"),
      Triple(markets[1], "Cheapest Palm Oil & Indomie", "Wholesale distributor hub in Lagos"),
      Triple(markets[2], "Cheapest Fresh Fish & Crayfish", "Direct from coastal boat landing"),
      Triple(markets[5], "Cheapest Beans & Northern Onions", "Direct offloading from train / trailers"),
      Triple(markets[3], "Cheapest Frozen Chicken & Turkey", "Major cold room terminal")
    )
  }

  fun getLeaderboard(): List<LeaderboardEntry> {
    val userPts = _userPoints.value
    val userReportsCount = _priceReports.value.count { it.reporterName.contains("You") || it.reporterName == "Market Sharp Guy" }

    val baseLeaders = mutableListOf(
      LeaderboardEntry(1, "Mama Chinedu (Mile 12)", 240, "👑 Market Queen", "Mile 12", 24),
      LeaderboardEntry(2, "Alhaji Musa (Daleko)", 190, "⭐ Wholesale Guru", "Mushin", 19),
      LeaderboardEntry(3, "Ayo Lagos (Oyingbo)", 140, "🚀 Price Hunter", "Oyingbo", 14),
      LeaderboardEntry(4, "Iya Moria (Agege)", 90, "🔍 Bargain Elder", "Agege", 9),
      LeaderboardEntry(5, "Chidi Retail (Oshodi)", 60, "💡 Sharp Shopper", "Oshodi", 6)
    )

    val userRank = when {
      userPts >= 250 -> 1
      userPts >= 200 -> 2
      userPts >= 150 -> 3
      userPts >= 100 -> 4
      userPts >= 50 -> 5
      else -> 6
    }

    val userBadge = when {
      userPts >= 200 -> "🏆 Master Reporter"
      userPts >= 100 -> "⚡ Market Sharp Guy"
      userPts >= 30 -> "🎖️ Active Scout"
      else -> "🌱 New Reporter"
    }

    val userEntry = LeaderboardEntry(
      rank = userRank,
      name = "You (Market Sharp Guy)",
      points = userPts,
      badge = userBadge,
      marketBase = "Lagos Active",
      reportsCount = userReportsCount + (userPts / 10),
      isCurrentUser = true
    )

    val combined = (baseLeaders + userEntry).sortedByDescending { it.points }
    return combined.mapIndexed { index, entry -> entry.copy(rank = index + 1) }
  }

  private fun checkAlertsForCommodity(commodityId: String, currentLowest: Int) {
    val updated = _priceAlerts.value.map { alert ->
      if (alert.commodityId == commodityId && currentLowest <= alert.targetPrice) {
        alert.copy(isTriggered = true, currentPrice = currentLowest)
      } else {
        alert
      }
    }
    _priceAlerts.value = updated
    saveAlerts(updated)
  }

  private fun loadReports() {
    val saved = prefs.getString("price_reports", null)
    if (saved != null) {
      try {
        val arr = JSONArray(saved)
        val list = mutableListOf<PriceReport>()
        for (i in 0 until arr.length()) {
          val obj = arr.getJSONObject(i)
          list.add(
            PriceReport(
              id = obj.getString("id"),
              commodityId = obj.getString("commodityId"),
              commodityName = obj.getString("commodityName"),
              marketId = obj.getString("marketId"),
              marketName = obj.getString("marketName"),
              price = obj.getInt("price"),
              reporterName = obj.getString("reporterName"),
              phoneNumber = obj.optString("phoneNumber").takeIf { it.isNotEmpty() },
              notes = obj.optString("notes").takeIf { it.isNotEmpty() },
              photoUri = obj.optString("photoUri").takeIf { it.isNotEmpty() },
              timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
              pointsAwarded = obj.optInt("pointsAwarded", 10)
            )
          )
        }
        _priceReports.value = list
        return
      } catch (_: Exception) {}
    }

    // Default recent crowdsourced community reports
    val defaultReports = listOf(
      PriceReport(
        id = "rep_1",
        commodityId = "rice50kg",
        commodityName = "Bag of Rice (50kg Mama Gold)",
        marketId = "mile12",
        marketName = "Mile 12 Market",
        price = 85000,
        reporterName = "Mama Chinedu",
        notes = "Trailer offload at Ketu gate this morning, price solid at ₦85k.",
        timestamp = System.currentTimeMillis() - 1000 * 60 * 35
      ),
      PriceReport(
        id = "rep_2",
        commodityId = "tomatoes_basket",
        commodityName = "Fresh Tomatoes (Big Basket)",
        marketId = "mile12",
        marketName = "Mile 12 Market",
        price = 58000,
        reporterName = "Alhaji Musa",
        notes = "Very fresh northern baskets just unpacked. Sweet red tomatoes.",
        timestamp = System.currentTimeMillis() - 1000 * 60 * 75
      ),
      PriceReport(
        id = "rep_3",
        commodityId = "indomie_carton",
        commodityName = "Indomie Instant Noodles (Carton)",
        marketId = "mushin",
        marketName = "Daleko / Mushin Market",
        price = 10800,
        reporterName = "Ayo Trader",
        notes = "Distributor discount when buying 5 cartons and above.",
        timestamp = System.currentTimeMillis() - 1000 * 60 * 120
      )
    )
    _priceReports.value = defaultReports
  }

  private fun saveReports(reports: List<PriceReport>) {
    val arr = JSONArray()
    for (r in reports.take(50)) {
      val obj = JSONObject().apply {
        put("id", r.id)
        put("commodityId", r.commodityId)
        put("commodityName", r.commodityName)
        put("marketId", r.marketId)
        put("marketName", r.marketName)
        put("price", r.price)
        put("reporterName", r.reporterName)
        put("phoneNumber", r.phoneNumber ?: "")
        put("notes", r.notes ?: "")
        put("photoUri", r.photoUri ?: "")
        put("timestamp", r.timestamp)
        put("pointsAwarded", r.pointsAwarded)
      }
      arr.put(obj)
    }
    prefs.edit().putString("price_reports", arr.toString()).apply()
  }

  private fun loadAlerts() {
    val saved = prefs.getString("price_alerts", null)
    if (saved != null) {
      try {
        val arr = JSONArray(saved)
        val list = mutableListOf<PriceAlert>()
        for (i in 0 until arr.length()) {
          val obj = arr.getJSONObject(i)
          list.add(
            PriceAlert(
              id = obj.getString("id"),
              commodityId = obj.getString("commodityId"),
              commodityName = obj.getString("commodityName"),
              targetPrice = obj.getInt("targetPrice"),
              currentPrice = obj.getInt("currentPrice"),
              isTriggered = obj.optBoolean("isTriggered", false),
              createdAt = obj.optLong("createdAt", System.currentTimeMillis())
            )
          )
        }
        _priceAlerts.value = list
        return
      } catch (_: Exception) {}
    }

    // Default sample alert
    val defaultAlerts = listOf(
      PriceAlert(
        id = "alert_default",
        commodityId = "rice50kg",
        commodityName = "Bag of Rice (50kg Mama Gold)",
        targetPrice = 86000,
        currentPrice = 92000,
        isTriggered = false
      )
    )
    _priceAlerts.value = defaultAlerts
  }

  private fun saveAlerts(alerts: List<PriceAlert>) {
    val arr = JSONArray()
    for (a in alerts) {
      val obj = JSONObject().apply {
        put("id", a.id)
        put("commodityId", a.commodityId)
        put("commodityName", a.commodityName)
        put("targetPrice", a.targetPrice)
        put("currentPrice", a.currentPrice)
        put("isTriggered", a.isTriggered)
        put("createdAt", a.createdAt)
      }
      arr.put(obj)
    }
    prefs.edit().putString("price_alerts", arr.toString()).apply()
  }

  private fun saveCommodities(commodities: List<Commodity>) {
    val arr = JSONArray()
    for (c in commodities) {
      val obj = JSONObject().apply {
        put("id", c.id)
        put("name", c.name)
        put("pidginName", c.pidginName)
        put("nativeAliases", c.nativeAliases)
        put("category", c.category.name)
        put("unit", c.unit)
        put("iconEmoji", c.iconEmoji)
        put("avgPrice", c.avgPrice)
        put("prevPrice", c.prevPrice)
        put("description", c.description)
        put("freshnessTip", c.freshnessTip)
        put("lastUpdated", c.lastUpdated)

        val mapObj = JSONObject()
        for ((mId, price) in c.marketPrices) {
          mapObj.put(mId, price)
        }
        put("marketPrices", mapObj)
      }
      arr.put(obj)
    }
    prefs.edit().putString("commodities_cache", arr.toString()).apply()
  }

  private fun parseCommoditiesJson(jsonStr: String): List<Commodity> {
    val arr = JSONArray(jsonStr)
    val list = mutableListOf<Commodity>()
    for (i in 0 until arr.length()) {
      val obj = arr.getJSONObject(i)
      val mapObj = obj.getJSONObject("marketPrices")
      val map = mutableMapOf<String, Int>()
      val keys = mapObj.keys()
      while (keys.hasNext()) {
        val k = keys.next()
        map[k] = mapObj.getInt(k)
      }
      list.add(
        Commodity(
          id = obj.getString("id"),
          name = obj.getString("name"),
          pidginName = obj.optString("pidginName", obj.getString("name")),
          nativeAliases = obj.optString("nativeAliases", ""),
          category = try {
            CommodityCategory.valueOf(obj.getString("category"))
          } catch (_: Exception) {
            CommodityCategory.FOODSTUFF
          },
          unit = obj.getString("unit"),
          iconEmoji = obj.getString("iconEmoji"),
          avgPrice = obj.getInt("avgPrice"),
          prevPrice = obj.getInt("prevPrice"),
          marketPrices = map,
          description = obj.getString("description"),
          freshnessTip = obj.getString("freshnessTip"),
          lastUpdated = obj.getString("lastUpdated")
        )
      )
    }
    return list
  }

  companion object {
    fun formatNaira(amount: Int): String {
      val formatter = NumberFormat.getNumberInstance(Locale.US)
      return "₦" + formatter.format(amount)
    }

    fun formatK(amount: Int): String {
      return if (amount >= 1000) {
        val k = amount / 1000.0
        if (k % 1.0 == 0.0) "₦${k.toInt()}k" else "₦${String.format(Locale.US, "%.1f", k)}k"
      } else {
        "₦$amount"
      }
    }
  }
}
