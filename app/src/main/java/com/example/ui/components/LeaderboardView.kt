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
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.example.model.LeaderboardEntry
import com.example.model.PriceReport
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
import com.example.ui.theme.SurfaceBorder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LeaderboardView(
  leaderboard: List<LeaderboardEntry>,
  recentReports: List<PriceReport>,
  userPoints: Int,
  language: AppLanguage,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("leaderboard_screen")
  ) {
    // Header Hero Banner
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
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = if (language == AppLanguage.PIDGIN) "Top Price Reporters 🏆" else "Top Price Reporters 🏆",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold
              )
              Text(
                text = if (language == AppLanguage.PIDGIN)
                  "Lagos community heroes saving money for everybody"
                else
                  "Community shoppers tracking fair market prices in Lagos",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp
              )
            }

            Surface(
              color = MarketGoldContainer,
              shape = RoundedCornerShape(16.dp)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(text = "👑", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "$userPoints pts",
                  fontWeight = FontWeight.ExtraBold,
                  fontSize = 13.sp,
                  color = MarketOnGoldContainer
                )
              }
            }
          }
        }
      }
    }

    // How to earn points info card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 10.dp)
          .testTag("rewards_info_card"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MarketGreenContainer),
        border = androidx.compose.foundation.BorderStroke(1.dp, MarketGreenVibrant.copy(alpha = 0.3f))
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(MarketGreenVibrant),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.EmojiEvents,
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.size(24.dp)
            )
          }
          Spacer(modifier = Modifier.width(12.dp))
          Column {
            Text(
              text = if (language == AppLanguage.PIDGIN) "Earn +10 Points per Price Report" else "Earn 10 Points per Report",
              fontWeight = FontWeight.Bold,
              fontSize = 13.sp,
              color = MarketOnGreenContainer
            )
            Text(
              text = if (language == AppLanguage.PIDGIN)
                "Buy something in market? Post the price! Climb to #1 and win monthly recharge cards & market hampers."
              else
                "Report food prices when you shop. Verified reporters get featured and earn merchant shopping vouchers.",
              fontSize = 11.sp,
              color = NeutralMedium,
              lineHeight = 14.sp
            )
          }
        }
      }
    }

    // Leaderboard Table Title
    item {
      Text(
        text = if (language == AppLanguage.PIDGIN) "Today's Top 6 Price Reporters:" else "Community Leaderboard:",
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = NeutralDark,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
      )
    }

    // Leaderboard entries
    items(leaderboard) { entry ->
      val isUser = entry.isCurrentUser
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp)
          .testTag("leaderboard_row_${entry.rank}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (isUser) MarketGoldContainer else Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(
          1.dp,
          if (isUser) MarketGold else SurfaceBorder
        )
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // Rank Badge
          val rankEmoji = when (entry.rank) {
            1 -> "🥇"
            2 -> "🥈"
            3 -> "🥉"
            else -> "#${entry.rank}"
          }
          Box(
            modifier = Modifier.size(36.dp),
            contentAlignment = Alignment.Center
          ) {
            if (entry.rank <= 3) {
              Text(text = rankEmoji, fontSize = 20.sp)
            } else {
              Text(
                text = rankEmoji,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = NeutralMedium
              )
            }
          }

          Spacer(modifier = Modifier.width(8.dp))

          // Reporter Details
          Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = entry.name,
                fontWeight = if (isUser) FontWeight.ExtraBold else FontWeight.Bold,
                fontSize = 14.sp,
                color = if (isUser) MarketOnGoldContainer else NeutralDark
              )
              if (isUser) {
                Spacer(modifier = Modifier.width(4.dp))
                Surface(
                  color = MarketGreenVibrant,
                  shape = RoundedCornerShape(4.dp)
                ) {
                  Text(
                    text = "YOU",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                  )
                }
              }
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = entry.badge,
                fontSize = 11.sp,
                color = MarketGreenDark,
                fontWeight = FontWeight.Medium
              )
              Text(
                text = " • ${entry.marketBase}",
                fontSize = 11.sp,
                color = NeutralMedium
              )
            }
          }

          // Points
          Column(horizontalAlignment = Alignment.End) {
            Text(
              text = "${entry.points} pts",
              fontWeight = FontWeight.ExtraBold,
              fontSize = 15.sp,
              color = if (isUser) MarketOnGoldContainer else MarketGreenDark
            )
            Text(
              text = "${entry.reportsCount} reports",
              fontSize = 11.sp,
              color = NeutralMedium
            )
          }
        }
      }
    }

    // Recent Crowdsourced Reports Feed
    item {
      Spacer(modifier = Modifier.height(16.dp))
      Text(
        text = if (language == AppLanguage.PIDGIN) "Live Market Reports Feed (Just Now):" else "Live Verified Market Reports Feed:",
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = NeutralDark,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
      )
    }

    items(recentReports.take(10)) { report ->
      val timeAgo = remember(report.timestamp) {
        val diffMins = ((System.currentTimeMillis() - report.timestamp) / (1000 * 60)).coerceAtLeast(1)
        if (diffMins < 60) "${diffMins}m ago" else "${diffMins / 60}h ago"
      }

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp)
          .testTag("report_feed_item_${report.id}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = report.reporterName,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = NeutralDark
              )
              Spacer(modifier = Modifier.width(6.dp))
              Surface(
                color = PriceDropGreenBg,
                shape = RoundedCornerShape(4.dp)
              ) {
                Text(
                  text = "Verified ✓",
                  color = PriceDropGreen,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Bold,
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
              }
            }

            Text(
              text = timeAgo,
              fontSize = 11.sp,
              color = NeutralLight
            )
          }

          Spacer(modifier = Modifier.height(4.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "${report.commodityName} in ${report.marketName}",
              fontSize = 12.sp,
              color = NeutralMedium
            )
            Text(
              text = MarketRepository.formatNaira(report.price),
              fontWeight = FontWeight.ExtraBold,
              fontSize = 14.sp,
              color = MarketGreenDark
            )
          }

          if (!report.notes.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "\"${report.notes}\"",
              fontSize = 11.sp,
              color = NeutralDark.copy(alpha = 0.8f),
              fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(30.dp))
    }
  }
}
