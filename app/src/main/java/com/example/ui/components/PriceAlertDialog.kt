package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.MarketRepository
import com.example.model.AppLanguage
import com.example.model.Commodity
import com.example.ui.theme.MarketGold
import com.example.ui.theme.MarketGoldContainer
import com.example.ui.theme.MarketGreenDark
import com.example.ui.theme.MarketGreenVibrant
import com.example.ui.theme.MarketOnGoldContainer
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.SurfaceBorder

@Composable
fun PriceAlertDialog(
  commodity: Commodity,
  language: AppLanguage,
  onDismiss: () -> Unit,
  onSaveAlert: (commodityId: String, targetPrice: Int) -> Unit
) {
  // Recommend a price 5% or 10% lower than current
  val defaultTarget = ((commodity.avgPrice * 0.9) / 500).toInt() * 500
  var targetPriceInput by remember { mutableStateOf(defaultTarget.toString()) }
  var errorText by remember { mutableStateOf<String?>(null) }

  Dialog(onDismissRequest = onDismiss) {
    Card(
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
        .testTag("price_alert_dialog")
    ) {
      Column(modifier = Modifier.padding(20.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.NotificationsActive,
              contentDescription = null,
              tint = MarketGold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (language == AppLanguage.PIDGIN) "Price Alarm 🔔" else "Set Price Alert 🔔",
              fontWeight = FontWeight.Bold,
              fontSize = 17.sp,
              color = NeutralDark
            )
          }
          IconButton(onClick = onDismiss) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
          text = if (language == AppLanguage.PIDGIN)
            "Alert me when ${commodity.name} price fall below this amount:"
          else
            "Notify me immediately when ${commodity.name} falls below:",
          fontSize = 13.sp,
          color = NeutralMedium
        )

        Spacer(modifier = Modifier.height(6.dp))

        Surface(
          color = MarketGoldContainer,
          shape = RoundedCornerShape(8.dp),
          modifier = Modifier.fillMaxWidth()
        ) {
          Text(
            text = "Current Average: ${MarketRepository.formatNaira(commodity.avgPrice)}",
            fontWeight = FontWeight.Bold,
            color = MarketOnGoldContainer,
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
          )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = targetPriceInput,
          onValueChange = {
            if (it.all { ch -> ch.isDigit() }) {
              targetPriceInput = it
              errorText = null
            }
          },
          prefix = {
            Text(text = "₦ ", fontWeight = FontWeight.Bold, color = MarketGreenDark)
          },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          modifier = Modifier
            .fillMaxWidth()
            .testTag("alert_price_input"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MarketGreenVibrant
          )
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Quick discount presets (-5%, -10%, -15%)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          listOf(0.95 to "-5%", 0.90 to "-10%", 0.85 to "-15%").forEach { (factor, pct) ->
            val calc = ((commodity.avgPrice * factor) / 500).toInt() * 500
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFFF1F5F2),
              border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
              modifier = Modifier
                .weight(1f)
                .clickable { targetPriceInput = calc.toString() }
            ) {
              Text(
                text = "$pct (${MarketRepository.formatK(calc)})",
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                color = NeutralDark,
                modifier = Modifier.padding(vertical = 6.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
              )
            }
          }
        }

        if (errorText != null) {
          Spacer(modifier = Modifier.height(6.dp))
          Text(text = errorText!!, color = Color.Red, fontSize = 11.sp)
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
          onClick = {
            val amount = targetPriceInput.toIntOrNull()
            if (amount == null || amount <= 0) {
              errorText = "Enter a valid target price"
              return@Button
            }
            onSaveAlert(commodity.id, amount)
          },
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(containerColor = MarketGreenVibrant),
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("save_price_alert_button")
        ) {
          Text(
            text = if (language == AppLanguage.PIDGIN) "Set Alert Sharp! 🔔" else "Save Price Alert",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = Color.White
          )
        }
      }
    }
  }
}
