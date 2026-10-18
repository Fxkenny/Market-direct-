package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.MarketRepository
import com.example.model.AppLanguage
import com.example.model.PriceForecast
import com.example.ui.theme.MarketGold
import com.example.ui.theme.MarketGoldContainer
import com.example.ui.theme.MarketGreenContainer
import com.example.ui.theme.MarketGreenDark
import com.example.ui.theme.MarketGreenVibrant
import com.example.ui.theme.MarketOnGoldContainer
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.PriceDropGreen
import com.example.ui.theme.PriceDropGreenBg
import com.example.ui.theme.PriceHikeRed
import com.example.ui.theme.PriceHikeRedBg
import com.example.ui.theme.SurfaceBorder

@Composable
fun ForecastDialog(
  forecasts: List<PriceForecast>,
  language: AppLanguage,
  onDismiss: () -> Unit,
  onSubscribeMock: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier
        .fillMaxWidth(0.96f)
        .testTag("forecast_dialog")
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(20.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MarketGoldContainer),
              contentAlignment = Alignment.Center
            ) {
              Text(text = "🔮", fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "Next Week Price Forecast",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp,
                color = NeutralDark
              )
              Text(
                text = "AI Market Intelligence & Truck Telemetry",
                fontSize = 11.sp,
                color = NeutralMedium
              )
            }
          }

          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Subscription Value Proposition Box
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = Color(0xFF1B231F),
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Premium Plan: ₦500 / month",
                color = MarketGold,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 14.sp
              )
              Text(
                text = "Know exactly when prices fall before going to Mile 12 or Daleko. Save tens of thousands of Naira on bulk buying.",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 11.sp,
                lineHeight = 14.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
          text = "Preview Upcoming Price Moves:",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          color = NeutralDark
        )

        Spacer(modifier = Modifier.height(8.dp))

        forecasts.forEach { item ->
          val isDrop = item.trend == "DROP"
          val isHike = item.trend == "HIKE"

          Card(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 4.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAF9)),
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
          ) {
            Column(modifier = Modifier.padding(12.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = item.commodityName,
                  fontWeight = FontWeight.Bold,
                  fontSize = 13.sp,
                  color = NeutralDark
                )

                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = if (isDrop) PriceDropGreenBg else if (isHike) PriceHikeRedBg else MarketGoldContainer
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Icon(
                      imageVector = if (isDrop) Icons.Default.TrendingDown else Icons.Default.TrendingUp,
                      contentDescription = null,
                      tint = if (isDrop) PriceDropGreen else if (isHike) PriceHikeRed else MarketOnGoldContainer,
                      modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                      text = "${item.percentChange}%",
                      fontWeight = FontWeight.ExtraBold,
                      fontSize = 11.sp,
                      color = if (isDrop) PriceDropGreen else if (isHike) PriceHikeRed else MarketOnGoldContainer
                    )
                  }
                }
              }

              Spacer(modifier = Modifier.height(4.dp))

              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
              ) {
                Text(
                  text = "Today: ${MarketRepository.formatNaira(item.currentPrice)}",
                  fontSize = 11.sp,
                  color = NeutralMedium
                )
                Text(
                  text = "Next Week: ${MarketRepository.formatNaira(item.projectedPrice)}",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = if (isDrop) PriceDropGreen else NeutralDark
                )
              }

              Spacer(modifier = Modifier.height(4.dp))

              Text(
                text = if (language == AppLanguage.PIDGIN) item.reasonPidgin else item.reasonEn,
                fontSize = 11.sp,
                color = NeutralMedium,
                lineHeight = 14.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
          onClick = onSubscribeMock,
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = MarketGreenVibrant),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("subscribe_premium_button")
        ) {
          Icon(imageVector = Icons.Default.Verified, contentDescription = null, tint = Color.White)
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "Unlock Weekly Forecast (₦500 / mo)",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = Color.White
          )
        }
      }
    }
  }
}
