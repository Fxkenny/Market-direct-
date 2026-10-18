package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MarketRepository
import com.example.model.AppLanguage
import com.example.model.Commodity
import com.example.model.PriceAlert
import com.example.ui.theme.MarketGold
import com.example.ui.theme.MarketGoldContainer
import com.example.ui.theme.MarketGreenContainer
import com.example.ui.theme.MarketGreenDark
import com.example.ui.theme.MarketGreenPrimary
import com.example.ui.theme.MarketGreenVibrant
import com.example.ui.theme.MarketOnGoldContainer
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralLight
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.PriceDropGreen
import com.example.ui.theme.PriceDropGreenBg
import com.example.ui.theme.SurfaceBorder

@Composable
fun AlertsView(
  alerts: List<PriceAlert>,
  commodities: List<Commodity>,
  language: AppLanguage,
  onOpenCreateAlert: (Commodity) -> Unit,
  onDeleteAlert: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  var simulatedNotification by remember { mutableStateOf<String?>(null) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("alerts_screen")
  ) {
    // Header
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
            text = if (language == AppLanguage.PIDGIN) "Price Alarms & Alerts 🔔" else "Smart Price Alerts 🔔",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold
          )
          Text(
            text = if (language == AppLanguage.PIDGIN)
              "Tell app make e ping you sharp-sharp once foodstuff price fall below your target"
            else
              "Get notified when foodstuff prices reach your budget target",
            color = Color.White.copy(alpha = 0.85f),
            fontSize = 12.sp
          )
        }
      }
    }

    // Simulated Banner Trigger if user tests notification
    if (simulatedNotification != null) {
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          colors = CardDefaults.cardColors(containerColor = MarketGreenContainer),
          border = androidx.compose.foundation.BorderStroke(1.dp, MarketGreenVibrant),
          shape = RoundedCornerShape(14.dp)
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.NotificationsActive,
              contentDescription = null,
              tint = MarketGreenPrimary,
              modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "🔔 PRICE DROP ALERT TRIGGERED!",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp,
                color = MarketGreenDark
              )
              Text(
                text = simulatedNotification!!,
                fontSize = 12.sp,
                color = NeutralDark
              )
            }
            IconButton(onClick = { simulatedNotification = null }) {
              Text(text = "✕", fontWeight = FontWeight.Bold, color = NeutralMedium)
            }
          }
        }
      }
    }

    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (language == AppLanguage.PIDGIN) "Your Active Alerts:" else "Your Active Alerts:",
          fontWeight = FontWeight.Bold,
          fontSize = 14.sp,
          color = NeutralDark
        )

        // Button to add alert for top item
        val firstItem = commodities.firstOrNull()
        if (firstItem != null) {
          Button(
            onClick = { onOpenCreateAlert(firstItem) },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MarketGreenVibrant),
            modifier = Modifier.testTag("add_alert_button")
          ) {
            Icon(
              imageVector = Icons.Default.AddAlert,
              contentDescription = null,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "New Alert", fontSize = 11.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    if (alerts.isEmpty()) {
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          shape = RoundedCornerShape(16.dp),
          border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Text(text = "🔕", fontSize = 36.sp)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
              text = if (language == AppLanguage.PIDGIN) "No alerts set yet" else "No Active Price Alerts",
              fontWeight = FontWeight.Bold,
              fontSize = 15.sp,
              color = NeutralDark
            )
            Text(
              text = if (language == AppLanguage.PIDGIN)
                "Click 'Alert Me' under any foodstuff on home screen to track lowest price!"
              else
                "Tap 'Alert Me' on any commodity card to track when prices drop.",
              fontSize = 12.sp,
              color = NeutralMedium,
              textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
          }
        }
      }
    } else {
      items(alerts) { alert ->
        val item = commodities.find { it.id == alert.commodityId }

        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("alert_item_${alert.id}"),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = Color.White),
          border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
          Column(modifier = Modifier.padding(14.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                  modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MarketGoldContainer),
                  contentAlignment = Alignment.Center
                ) {
                  Text(text = item?.iconEmoji ?: "🌾", fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                  Text(
                    text = alert.commodityName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = NeutralDark
                  )
                  Text(
                    text = "Current Avg: ${MarketRepository.formatNaira(item?.avgPrice ?: alert.currentPrice)}",
                    fontSize = 11.sp,
                    color = NeutralMedium
                  )
                }
              }

              IconButton(
                onClick = { onDeleteAlert(alert.id) },
                modifier = Modifier.testTag("delete_alert_${alert.id}")
              ) {
                Icon(
                  imageVector = Icons.Default.Delete,
                  contentDescription = "Delete",
                  tint = Color.Gray,
                  modifier = Modifier.size(20.dp)
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (alert.isTriggered) PriceDropGreenBg else Color(0xFFF6F8F7),
              modifier = Modifier.fillMaxWidth()
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column {
                  Text(
                    text = "Target Price Trigger:",
                    fontSize = 11.sp,
                    color = NeutralMedium
                  )
                  Text(
                    text = "Below ${MarketRepository.formatNaira(alert.targetPrice)}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = if (alert.isTriggered) PriceDropGreen else MarketGreenDark
                  )
                }

                Button(
                  onClick = {
                    simulatedNotification =
                      "${alert.commodityName} just hit ${MarketRepository.formatNaira(alert.targetPrice)} in Mile 12 Market! Go buy now!"
                  },
                  shape = RoundedCornerShape(8.dp),
                  colors = ButtonDefaults.buttonColors(containerColor = MarketGold),
                  modifier = Modifier.testTag("test_alert_button_${alert.id}")
                ) {
                  Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MarketOnGoldContainer
                  )
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "Test Ping",
                    fontSize = 11.sp,
                    color = MarketOnGoldContainer,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}
