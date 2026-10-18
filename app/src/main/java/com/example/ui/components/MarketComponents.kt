package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MarketRepository
import com.example.model.AppLanguage
import com.example.model.Commodity
import com.example.model.CommodityCategory
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
import com.example.ui.theme.SurfaceBackground
import com.example.ui.theme.SurfaceBorder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MarketHeader(
  language: AppLanguage,
  userPoints: Int,
  onToggleLanguage: () -> Unit,
  onReportClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val todayFormatted = remember {
    SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault()).format(Date())
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .background(
        brush = Brush.verticalGradient(
          colors = listOf(
            MarketGreenDark,
            MarketGreenPrimary,
            MarketGreenVibrant
          )
        ),
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
      )
      .padding(horizontal = 16.dp, vertical = 18.dp)
  ) {
    Column {
      // Top status row: Date + Language Switcher + User Points pill
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = todayFormatted,
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
          Text(
            text = if (language == AppLanguage.PIDGIN) "E Kaabo! How Market Today? 🇳🇬" else "PriceMarket NG • Lagos Hub 🇳🇬",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold
          )
        }

        // Language toggle pill
        Surface(
          onClick = onToggleLanguage,
          shape = RoundedCornerShape(20.dp),
          color = Color.White.copy(alpha = 0.2f),
          border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
          modifier = Modifier.testTag("language_toggle_button")
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = if (language == AppLanguage.PIDGIN) "🇳🇬 Pidgin" else "🇬🇧 English",
              color = Color.White,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // Main stats row like Opay/PalmPay dashboard
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        // Quick Stat 1: Today's Market Focus
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
          modifier = Modifier.weight(1f)
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Text(
              text = if (language == AppLanguage.PIDGIN) "Today Market" else "Tracked Markets",
              color = Color.White.copy(alpha = 0.8f),
              fontSize = 11.sp
            )
            Text(
              text = "7 Major Hubs",
              color = Color.White,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Mile 12, Mushin...",
              color = Color.White.copy(alpha = 0.9f),
              fontSize = 10.sp,
              maxLines = 1
            )
          }
        }

        // Quick Stat 2: User Points
        Card(
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = MarketGoldContainer),
          modifier = Modifier.weight(1f)
        ) {
          Column(modifier = Modifier.padding(10.dp)) {
            Text(
              text = if (language == AppLanguage.PIDGIN) "Your Points" else "Your Rewards",
              color = MarketOnGoldContainer.copy(alpha = 0.8f),
              fontSize = 11.sp
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "$userPoints pts",
                color = MarketOnGoldContainer,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(text = "⭐", fontSize = 12.sp)
            }
            Text(
              text = "+10 pts per report",
              color = MarketOnGoldContainer,
              fontSize = 10.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }

        // Quick Action: Big Report Button
        Button(
          onClick = onReportClick,
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = MarketGreenDark
          ),
          modifier = Modifier
            .weight(1.1f)
            .height(68.dp)
            .testTag("header_report_button")
        ) {
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
          ) {
            Text(text = "📢", fontSize = 16.sp)
            Text(
              text = if (language == AppLanguage.PIDGIN) "Report Price" else "Post Price",
              fontSize = 12.sp,
              fontWeight = FontWeight.ExtraBold,
              textAlign = TextAlign.Center
            )
          }
        }
      }
    }
  }
}

@Composable
fun SearchAndFilterSection(
  searchQuery: String,
  onSearchChange: (String) -> Unit,
  selectedCategory: CommodityCategory,
  onCategorySelect: (CommodityCategory) -> Unit,
  language: AppLanguage,
  modifier: Modifier = Modifier
) {
  Column(modifier = modifier.fillMaxWidth()) {
    // Search Field
    OutlinedTextField(
      value = searchQuery,
      onValueChange = onSearchChange,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .testTag("search_text_input"),
      placeholder = {
        Text(
          text = if (language == AppLanguage.PIDGIN) "Search garri, rice, yam, palm oil..." else "Search garri, rice, yam, indomie...",
          color = NeutralLight,
          fontSize = 14.sp
        )
      },
      leadingIcon = {
        Icon(
          imageVector = Icons.Default.Search,
          contentDescription = "Search",
          tint = MarketGreenPrimary
        )
      },
      trailingIcon = {
        if (searchQuery.isNotEmpty()) {
          IconButton(
            onClick = { onSearchChange("") },
            modifier = Modifier.testTag("clear_search_button")
          ) {
            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
          }
        }
      },
      singleLine = true,
      shape = RoundedCornerShape(16.dp),
      colors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedBorderColor = MarketGreenVibrant,
        unfocusedBorderColor = SurfaceBorder
      )
    )

    Spacer(modifier = Modifier.height(10.dp))

    // Horizontal category chips
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .horizontalScroll(rememberScrollState())
        .padding(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      CommodityCategory.values().forEach { category ->
        val isSelected = category == selectedCategory
        val label = if (language == AppLanguage.PIDGIN) category.displayNamePidgin else category.displayNameEn

        FilterChip(
          selected = isSelected,
          onClick = { onCategorySelect(category) },
          label = {
            Text(
              text = "${category.emoji} $label",
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
              fontSize = 13.sp
            )
          },
          colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MarketGreenVibrant,
            selectedLabelColor = Color.White,
            containerColor = Color.White,
            labelColor = NeutralDark
          ),
          border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = isSelected,
            borderColor = SurfaceBorder,
            selectedBorderColor = MarketGreenVibrant
          ),
          shape = RoundedCornerShape(20.dp),
          modifier = Modifier.testTag("category_chip_${category.name.lowercase()}")
        )
      }
    }
  }
}

@Composable
fun TopCheapestMarketsBanner(
  markets: List<Triple<MarketInfo, String, String>>,
  language: AppLanguage,
  onMarketClick: (MarketInfo) -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp)
      .testTag("top_cheapest_markets_card"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MarketGreenContainer),
    border = androidx.compose.foundation.BorderStroke(1.dp, MarketGreenVibrant.copy(alpha = 0.3f))
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            color = MarketGreenVibrant,
            shape = RoundedCornerShape(6.dp)
          ) {
            Text(
              text = "CHEAPEST",
              color = Color.White,
              fontSize = 11.sp,
              fontWeight = FontWeight.ExtraBold,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (language == AppLanguage.PIDGIN) "Top 5 Cheap Markets Today" else "Top 5 Cheapest Markets Today",
            fontWeight = FontWeight.Bold,
            color = MarketOnGreenContainer,
            fontSize = 14.sp
          )
        }
        Text(
          text = "Lagos, NG",
          fontSize = 11.sp,
          color = MarketGreenDark,
          fontWeight = FontWeight.Medium
        )
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Horizontal list of top 5 markets
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        markets.take(5).forEachIndexed { index, (market, deal, savings) ->
          Surface(
            shape = RoundedCornerShape(12.dp),
            color = Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
            modifier = Modifier
              .width(220.dp)
              .clickable { onMarketClick(market) }
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "#${index + 1} ${market.name}",
                  fontWeight = FontWeight.ExtraBold,
                  fontSize = 13.sp,
                  color = NeutralDark,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                  modifier = Modifier.weight(1f)
                )
                Surface(
                  color = MarketGoldContainer,
                  shape = CircleShape
                ) {
                  Text(
                    text = "⭐ Best",
                    color = MarketOnGoldContainer,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                  )
                }
              }

              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = deal,
                color = MarketGreenDark,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
              )
              Text(
                text = savings,
                color = NeutralMedium,
                fontSize = 10.sp
              )
            }
          }
        }
      }
    }
  }
}

@Composable
fun CommodityCard(
  commodity: Commodity,
  allMarkets: List<MarketInfo>,
  language: AppLanguage,
  onCompareClick: (Commodity) -> Unit,
  onAlertClick: (Commodity) -> Unit,
  onReportClick: (Commodity) -> Unit,
  modifier: Modifier = Modifier
) {
  val (cheapestMarket, cheapestPrice) = remember(commodity) {
    commodity.getCheapestMarket(allMarkets)
  }

  val savings = remember(commodity, cheapestPrice) {
    val highest = commodity.marketPrices.values.maxOrNull() ?: commodity.avgPrice
    highest - cheapestPrice
  }

  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
      .testTag("commodity_card_${commodity.id}"),
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = Color.White),
    border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // Top Row: Icon + Name + Native alias + Average Price
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
      ) {
        // Emoji avatar
        Box(
          modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MarketGreenContainer),
          contentAlignment = Alignment.Center
        ) {
          Text(text = commodity.iconEmoji, fontSize = 24.sp)
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Commodity Name & Aliases
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = if (language == AppLanguage.PIDGIN) commodity.pidginName else commodity.name,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = NeutralDark,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
          )
          if (commodity.nativeAliases.isNotEmpty()) {
            Text(
              text = commodity.nativeAliases,
              fontSize = 11.sp,
              color = NeutralMedium,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
          Text(
            text = "Unit: ${commodity.unit}",
            fontSize = 11.sp,
            color = MarketGreenDark,
            fontWeight = FontWeight.Medium
          )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Price + Change Indicator
        Column(horizontalAlignment = Alignment.End) {
          Text(
            text = MarketRepository.formatNaira(commodity.avgPrice),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 16.sp,
            color = NeutralDark
          )

          // Price Change Badge (+₦2000 in red, -₦500 in green)
          if (commodity.priceDiff != 0) {
            val isHike = commodity.priceDiff > 0
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = if (isHike) PriceHikeRedBg else PriceDropGreenBg
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = if (isHike) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                  contentDescription = null,
                  tint = if (isHike) PriceHikeRed else PriceDropGreen,
                  modifier = Modifier.size(11.dp)
                )
                Spacer(modifier = Modifier.width(2.dp))
                val sign = if (isHike) "+" else "-"
                val diffAbs = Math.abs(commodity.priceDiff)
                Text(
                  text = "$sign${MarketRepository.formatNaira(diffAbs)}",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isHike) PriceHikeRed else PriceDropGreen
                )
              }
            }
          } else {
            Text(
              text = "Steady",
              fontSize = 11.sp,
              color = NeutralMedium
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Cheapest Market Pill / Callout
      if (cheapestMarket != null) {
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = MarketGreenContainer,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Storefront,
                contentDescription = null,
                tint = MarketGreenPrimary,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Cheapest in ${cheapestMarket.nickname}: ",
                fontSize = 12.sp,
                color = MarketOnGreenContainer,
                fontWeight = FontWeight.Medium
              )
              Text(
                text = MarketRepository.formatNaira(cheapestPrice),
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MarketGreenDark
              )
            }

            if (savings > 0) {
              Surface(
                color = PriceDropGreen,
                shape = RoundedCornerShape(6.dp)
              ) {
                Text(
                  text = "Save ${MarketRepository.formatK(savings)}",
                  color = Color.White,
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Action buttons: Compare Markets + Set Alert + Report
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        OutlinedButton(
          onClick = { onCompareClick(commodity) },
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .weight(1f)
            .height(42.dp)
            .testTag("compare_button_${commodity.id}"),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = MarketGreenPrimary),
          border = androidx.compose.foundation.BorderStroke(1.dp, MarketGreenVibrant)
        ) {
          Icon(
            imageVector = Icons.Outlined.CompareArrows,
            contentDescription = null,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (language == AppLanguage.PIDGIN) "Compare" else "Compare",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
          )
        }

        OutlinedButton(
          onClick = { onAlertClick(commodity) },
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .weight(1f)
            .height(42.dp)
            .testTag("alert_button_${commodity.id}"),
          colors = ButtonDefaults.outlinedButtonColors(contentColor = NeutralDark),
          border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
          Icon(
            imageVector = Icons.Outlined.Notifications,
            contentDescription = null,
            tint = MarketGold,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = if (language == AppLanguage.PIDGIN) "Price Alert" else "Alert Me",
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
          )
        }

        Button(
          onClick = { onReportClick(commodity) },
          shape = RoundedCornerShape(10.dp),
          modifier = Modifier
            .weight(1.1f)
            .height(42.dp)
            .testTag("report_item_button_${commodity.id}"),
          colors = ButtonDefaults.buttonColors(containerColor = MarketGreenVibrant)
        ) {
          Text(
            text = if (language == AppLanguage.PIDGIN) "I Buy This" else "Report Price",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
          )
        }
      }
    }
  }
}

@Composable
fun SponsoredShopriteBanner(
  language: AppLanguage,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current

  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
      .testTag("sponsored_banner_shoprite"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9E6)),
    border = androidx.compose.foundation.BorderStroke(1.dp, MarketGold.copy(alpha = 0.5f))
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(12.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(MarketGold),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.LocalOffer,
          contentDescription = "Sponsored",
          tint = Color.White,
          modifier = Modifier.size(20.dp)
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Surface(
            color = MarketGold,
            shape = RoundedCornerShape(4.dp)
          ) {
            Text(
              text = "SPONSORED",
              color = Color.White,
              fontSize = 9.sp,
              fontWeight = FontWeight.ExtraBold,
              modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
            )
          }
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Shoprite Ikeja City Mall",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = NeutralDark
          )
        }
        Text(
          text = if (language == AppLanguage.PIDGIN)
            "Cheap Mama Gold Rice & Fresh Veggies in stock! Call 0802-774-2364"
          else
            "Buy wholesale Rice, Oil & Fresh Veggies at lowest mall rates. Call 0802-774-2364",
          fontSize = 11.sp,
          color = NeutralMedium,
          lineHeight = 14.sp
        )
      }

      Spacer(modifier = Modifier.width(8.dp))

      // Direct dial button
      Button(
        onClick = {
          val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:08027742364"))
          try {
            context.startActivity(dialIntent)
          } catch (_: Exception) {}
        },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MarketGreenVibrant),
        modifier = Modifier.testTag("sponsor_call_button")
      ) {
        Icon(
          imageVector = Icons.Default.Call,
          contentDescription = "Call",
          tint = Color.White,
          modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "Call", fontSize = 11.sp, fontWeight = FontWeight.Bold)
      }
    }
  }
}

@Composable
fun PremiumForecastBanner(
  language: AppLanguage,
  onForecastClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp)
      .testTag("premium_forecast_card"),
    shape = RoundedCornerShape(14.dp),
    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B231F))
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(14.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(text = "🔮", fontSize = 16.sp)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = if (language == AppLanguage.PIDGIN) "Next Week Price Forecast" else "Next Week Price Forecast",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
          )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
          text = if (language == AppLanguage.PIDGIN)
            "Know whether Tomato & Rice go drop before you go market! Only ₦500/month."
          else
            "AI-powered commodity forecast: Tomato supply expected to rise +15% this Thursday.",
          color = Color.White.copy(alpha = 0.8f),
          fontSize = 11.sp,
          lineHeight = 14.sp
        )
      }

      Spacer(modifier = Modifier.width(10.dp))

      Button(
        onClick = onForecastClick,
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MarketGold),
        modifier = Modifier.testTag("view_forecast_button")
      ) {
        Text(
          text = "Preview ₦500",
          color = MarketOnGoldContainer,
          fontSize = 11.sp,
          fontWeight = FontWeight.ExtraBold
        )
      }
    }
  }
}
