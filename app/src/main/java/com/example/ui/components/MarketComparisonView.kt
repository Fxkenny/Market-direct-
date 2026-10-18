package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MarketRepository
import com.example.model.AppLanguage
import com.example.model.Commodity
import com.example.model.MarketInfo
import com.example.ui.theme.MarketGold
import com.example.ui.theme.MarketGoldContainer
import com.example.ui.theme.MarketGreenContainer
import com.example.ui.theme.MarketGreenDark
import com.example.ui.theme.MarketGreenPrimary
import com.example.ui.theme.MarketGreenVibrant
import com.example.ui.theme.MarketOnGoldContainer
import com.example.ui.theme.MarketOnGreenContainer
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralLight
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.PriceDropGreen
import com.example.ui.theme.PriceDropGreenBg
import com.example.ui.theme.PriceHikeRed
import com.example.ui.theme.PriceHikeRedBg
import com.example.ui.theme.SurfaceBorder

@Composable
fun MarketComparisonView(
  commodities: List<Commodity>,
  markets: List<MarketInfo>,
  selectedCommodity: Commodity?,
  language: AppLanguage,
  onSelectCommodity: (Commodity) -> Unit,
  onOpenReport: (Commodity) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val activeCommodity = selectedCommodity ?: commodities.firstOrNull()

  if (activeCommodity == null) {
    Box(
      modifier = modifier.fillMaxSize(),
      contentAlignment = Alignment.Center
    ) {
      Text(text = "No commodities available to compare.")
    }
    return
  }

  val marketPrices = activeCommodity.marketPrices
  val minPrice = marketPrices.values.minOrNull() ?: activeCommodity.avgPrice
  val maxPrice = marketPrices.values.maxOrNull() ?: activeCommodity.avgPrice
  val maxSavings = maxPrice - minPrice

  val (cheapestMarket, _) = activeCommodity.getCheapestMarket(markets)

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("market_comparison_screen")
  ) {
    // Header title
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .background(
            brush = Brush.verticalGradient(
              listOf(MarketGreenDark, MarketGreenPrimary)
            )
          )
          .padding(horizontal = 16.dp, vertical = 20.dp)
      ) {
        Column {
          Text(
            text = if (language == AppLanguage.PIDGIN) "Market Price War 🔥" else "Lagos Market Price Comparison",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold
          )
          Text(
            text = if (language == AppLanguage.PIDGIN)
              "See which market get the cheapest price for ${activeCommodity.name}"
            else
              "Real-time price difference across all 7 Lagos wholesale markets",
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 12.sp
          )
        }
      }
    }

    // Commodity Selection Horizontal Row
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 12.dp)
      ) {
        Text(
          text = if (language == AppLanguage.PIDGIN) "Choose Item to Compare:" else "Select Item to Compare:",
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold,
          color = NeutralMedium,
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          commodities.forEach { item ->
            val isSelected = item.id == activeCommodity.id
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = if (isSelected) MarketGreenVibrant else Color.White,
              border = androidx.compose.foundation.BorderStroke(
                1.dp,
                if (isSelected) MarketGreenVibrant else SurfaceBorder
              ),
              modifier = Modifier
                .clickable { onSelectCommodity(item) }
                .testTag("compare_chip_${item.id}")
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = item.iconEmoji, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = item.name.split("(")[0].trim(),
                  fontSize = 12.sp,
                  fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                  color = if (isSelected) Color.White else NeutralDark
                )
              }
            }
          }
        }
      }
    }

    // Best Value Highlight Banner
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 6.dp)
          .testTag("best_value_banner"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MarketGreenContainer),
        border = androidx.compose.foundation.BorderStroke(1.dp, MarketGreenVibrant)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Surface(
                color = PriceDropGreen,
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = "⭐ CHEAPEST TODAY",
                  color = Color.White,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.ExtraBold,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = cheapestMarket?.name ?: "Mile 12",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MarketOnGreenContainer
              )
            }

            Text(
              text = MarketRepository.formatNaira(minPrice),
              fontSize = 18.sp,
              fontWeight = FontWeight.ExtraBold,
              color = MarketGreenDark
            )
          }

          Spacer(modifier = Modifier.height(6.dp))

          Text(
            text = if (language == AppLanguage.PIDGIN)
              "You fit save up to ${MarketRepository.formatNaira(maxSavings)} if you buy for ${cheapestMarket?.nickname ?: "here"} instead of other markets!"
            else
              "Save up to ${MarketRepository.formatNaira(maxSavings)} by purchasing at ${cheapestMarket?.name ?: "the cheapest market"} instead of high-retail zones.",
            fontSize = 12.sp,
            color = MarketOnGreenContainer
          )

          Spacer(modifier = Modifier.height(12.dp))

          // Big Directions Button
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = {
                val marketAddress = cheapestMarket?.address ?: "Mile 12 Market Lagos"
                val mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(marketAddress))
                val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                try {
                  context.startActivity(mapIntent)
                } catch (_: Exception) {
                  // Fallback to web browser maps
                  val webMapIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://www.google.com/maps/search/?api=1&query=" + Uri.encode(marketAddress))
                  )
                  context.startActivity(webMapIntent)
                }
              },
              shape = RoundedCornerShape(12.dp),
              colors = ButtonDefaults.buttonColors(containerColor = MarketGreenVibrant),
              modifier = Modifier
                .weight(1f)
                .height(46.dp)
                .testTag("directions_button")
            ) {
              Icon(
                imageVector = Icons.Default.Navigation,
                contentDescription = "Directions",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = if (language == AppLanguage.PIDGIN) "Get Directions" else "Get Directions to Market",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
            }

            OutlinedButton(
              onClick = {
                val shareText = "Price Alert for ${activeCommodity.name}:\n" +
                    "Lowest in ${cheapestMarket?.name}: ${MarketRepository.formatNaira(minPrice)}\n" +
                    "Track real-time Lagos food prices on PriceMarket NG!"
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                  type = "text/plain"
                  putExtra(Intent.EXTRA_TEXT, shareText)
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share Price Comparison"))
              },
              shape = RoundedCornerShape(12.dp),
              modifier = Modifier
                .height(46.dp)
                .testTag("share_comparison_button"),
              colors = ButtonDefaults.outlinedButtonColors(contentColor = MarketGreenDark),
              border = androidx.compose.foundation.BorderStroke(1.dp, MarketGreenVibrant)
            ) {
              Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                modifier = Modifier.size(16.dp)
              )
            }
          }
        }
      }
    }

    // Visual Comparison Header
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (language == AppLanguage.PIDGIN) "Price Difference Chart" else "Price Difference Chart",
          fontWeight = FontWeight.Bold,
          fontSize = 15.sp,
          color = NeutralDark
        )
        Text(
          text = "Unit: ${activeCommodity.unit}",
          fontSize = 12.sp,
          color = NeutralMedium
        )
      }
    }

    // Market Price Bars sorted cheapest first
    val sortedMarkets = markets.mapNotNull { m ->
      val price = activeCommodity.marketPrices[m.id]
      if (price != null) Pair(m, price) else null
    }.sortedBy { it.second }

    items(sortedMarkets) { (market, price) ->
      val isCheapest = price == minPrice
      val isHighest = price == maxPrice
      val range = if (maxPrice > minPrice) (maxPrice - minPrice).toFloat() else 1f
      val fraction = if (maxPrice > minPrice) ((price - minPrice) / range) else 0f

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp)
          .testTag("market_bar_${market.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (isCheapest) MarketGreenContainer else Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(
          1.dp,
          if (isCheapest) MarketGreenVibrant else SurfaceBorder
        )
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(32.dp)
                  .clip(CircleShape)
                  .background(if (isCheapest) MarketGreenVibrant else Color(0xFFF0F4F2)),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Storefront,
                  contentDescription = null,
                  tint = if (isCheapest) Color.White else NeutralMedium,
                  modifier = Modifier.size(16.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Text(
                    text = market.nickname,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = NeutralDark
                  )
                  if (isCheapest) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                      color = PriceDropGreen,
                      shape = RoundedCornerShape(4.dp)
                    ) {
                      Text(
                        text = "CHEAPEST",
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                      )
                    }
                  } else if (isHighest) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                      color = PriceHikeRedBg,
                      shape = RoundedCornerShape(4.dp)
                    ) {
                      Text(
                        text = "HIGHEST",
                        color = PriceHikeRed,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                      )
                    }
                  }
                }
                Text(
                  text = market.knownFor,
                  fontSize = 11.sp,
                  color = NeutralMedium,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
              }
            }

            Column(horizontalAlignment = Alignment.End) {
              Text(
                text = MarketRepository.formatNaira(price),
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isCheapest) MarketGreenDark else NeutralDark
              )
              if (price > minPrice) {
                Text(
                  text = "+${MarketRepository.formatNaira(price - minPrice)} more",
                  fontSize = 10.sp,
                  color = PriceHikeRed,
                  fontWeight = FontWeight.Medium
                )
              } else {
                Text(
                  text = "Best deal!",
                  fontSize = 10.sp,
                  color = PriceDropGreen,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Comparative Progress Bar
          val barFill = 0.35f + (0.65f * (price.toFloat() / maxPrice.toFloat()))
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(8.dp)
              .clip(RoundedCornerShape(4.dp))
              .background(Color(0xFFE8ECE9))
          ) {
            Box(
              modifier = Modifier
                .fillMaxWidth(barFill)
                .fillMaxHeight()
                .clip(RoundedCornerShape(4.dp))
                .background(
                  if (isCheapest) MarketGreenVibrant
                  else if (isHighest) PriceHikeRed
                  else MarketGold
                )
            )
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(16.dp))
      // Bottom reporting prompt
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
        shape = RoundedCornerShape(12.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = if (language == AppLanguage.PIDGIN) "You buy cheaper than this?" else "Found a better price?",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = NeutralDark
            )
            Text(
              text = if (language == AppLanguage.PIDGIN)
                "Report your price give everybody, earn 10 points sharp!"
              else
                "Crowdsource today's price and earn 10 market reporter points.",
              fontSize = 11.sp,
              color = NeutralMedium
            )
          }
          Button(
            onClick = { onOpenReport(activeCommodity) },
            colors = ButtonDefaults.buttonColors(containerColor = MarketGreenVibrant),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.testTag("report_from_compare_button")
          ) {
            Text("Report ₦", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
      Spacer(modifier = Modifier.height(24.dp))
    }
  }
}
