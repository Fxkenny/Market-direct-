package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
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
import com.example.ui.theme.SurfaceBorder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportPriceDialog(
  commodities: List<Commodity>,
  markets: List<MarketInfo>,
  preselectedCommodity: Commodity?,
  language: AppLanguage,
  onDismiss: () -> Unit,
  onSubmit: (commodityId: String, marketId: String, price: Int, reporterName: String, phoneNumber: String?, notes: String?, photoUri: String?) -> Unit
) {
  var selectedCommodity by remember {
    mutableStateOf(preselectedCommodity ?: commodities.firstOrNull())
  }
  var selectedMarket by remember {
    mutableStateOf(markets.firstOrNull())
  }
  var priceInput by remember {
    val initialPrice = selectedCommodity?.avgPrice ?: 0
    mutableStateOf(if (initialPrice > 0) initialPrice.toString() else "")
  }
  var reporterName by remember { mutableStateOf("Mama Chinyere / You") }
  var phoneNumber by remember { mutableStateOf("") }
  var notes by remember { mutableStateOf("") }
  var receiptUri by remember { mutableStateOf<Uri?>(null) }
  var validationError by remember { mutableStateOf<String?>(null) }

  // Photo picker launcher (complies with zero storage permissions policy)
  val photoPickerLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri: Uri? ->
    receiptUri = uri
  }

  var isItemDropdownExpanded by remember { mutableStateOf(false) }
  var isMarketDropdownExpanded by remember { mutableStateOf(false) }

  Dialog(
    onDismissRequest = onDismiss,
    properties = DialogProperties(usePlatformDefaultWidth = false)
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth(0.95f)
        .heightIn(max = 700.dp)
        .testTag("report_price_dialog_container"),
      shape = RoundedCornerShape(24.dp),
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(20.dp)
      ) {
        // Dialog Header with Title, Close icon & +10 Points badge
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = if (language == AppLanguage.PIDGIN) "Report Wetin You Buy 📢" else "Report Market Price 📢",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp,
                color = NeutralDark
              )
            }
            Text(
              text = if (language == AppLanguage.PIDGIN)
                "Help everyone know the real price today"
              else
                "Crowdsource verified Lagos foodstuff rates",
              fontSize = 12.sp,
              color = NeutralMedium
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.testTag("close_report_dialog_button")
          ) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
          }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Points incentive badge
        Surface(
          shape = RoundedCornerShape(12.dp),
          color = MarketGoldContainer,
          modifier = Modifier.fillMaxWidth()
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "🎁", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (language == AppLanguage.PIDGIN)
                "You go earn +10 Market Points when you submit!"
              else
                "Earn +10 Price Reporter Points on verified submission!",
              color = MarketOnGoldContainer,
              fontWeight = FontWeight.Bold,
              fontSize = 12.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Step 1: Select Commodity
        Text(
          text = if (language == AppLanguage.PIDGIN) "1. Wetin you buy? (Select Item)" else "1. Select Item Bought:",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          color = NeutralDark
        )
        Spacer(modifier = Modifier.height(6.dp))

        ExposedDropdownMenuBox(
          expanded = isItemDropdownExpanded,
          onExpandedChange = { isItemDropdownExpanded = !isItemDropdownExpanded }
        ) {
          OutlinedTextField(
            value = selectedCommodity?.let { "${it.iconEmoji} ${it.name} (${it.unit})" } ?: "Select Item",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isItemDropdownExpanded) },
            modifier = Modifier
              .menuAnchor()
              .fillMaxWidth()
              .testTag("report_item_dropdown"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = Color.White,
              unfocusedContainerColor = Color.White
            )
          )

          ExposedDropdownMenu(
            expanded = isItemDropdownExpanded,
            onDismissRequest = { isItemDropdownExpanded = false }
          ) {
            commodities.forEach { item ->
              DropdownMenuItem(
                text = {
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = item.iconEmoji, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "${item.name} (${item.unit})", fontSize = 13.sp)
                  }
                },
                onClick = {
                  selectedCommodity = item
                  priceInput = item.avgPrice.toString()
                  isItemDropdownExpanded = false
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Step 2: Select Market
        Text(
          text = if (language == AppLanguage.PIDGIN) "2. Which market you buy am?" else "2. Which Market Did You Buy From?",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          color = NeutralDark
        )
        Spacer(modifier = Modifier.height(6.dp))

        ExposedDropdownMenuBox(
          expanded = isMarketDropdownExpanded,
          onExpandedChange = { isMarketDropdownExpanded = !isMarketDropdownExpanded }
        ) {
          OutlinedTextField(
            value = selectedMarket?.let { "${it.name} (${it.nickname})" } ?: "Select Market",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isMarketDropdownExpanded) },
            modifier = Modifier
              .menuAnchor()
              .fillMaxWidth()
              .testTag("report_market_dropdown"),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = Color.White,
              unfocusedContainerColor = Color.White
            )
          )

          ExposedDropdownMenu(
            expanded = isMarketDropdownExpanded,
            onDismissRequest = { isMarketDropdownExpanded = false }
          ) {
            markets.forEach { market ->
              DropdownMenuItem(
                text = {
                  Column {
                    Text(text = market.name, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(text = market.knownFor, fontSize = 11.sp, color = NeutralMedium)
                  }
                },
                onClick = {
                  selectedMarket = market
                  isMarketDropdownExpanded = false
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Step 3: Enter Price ₦ with Quick Increments (designed for market women)
        Text(
          text = if (language == AppLanguage.PIDGIN) "3. How much you pay? (Naira ₦)" else "3. Enter Price Paid (₦):",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          color = NeutralDark
        )
        Spacer(modifier = Modifier.height(6.dp))

        OutlinedTextField(
          value = priceInput,
          onValueChange = { input ->
            if (input.all { it.isDigit() }) {
              priceInput = input
              validationError = null
            }
          },
          keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
          prefix = {
            Text(text = "₦ ", fontWeight = FontWeight.Bold, color = MarketGreenDark, fontSize = 18.sp)
          },
          modifier = Modifier
            .fillMaxWidth()
            .testTag("report_price_input"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = MarketGreenVibrant
          )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Quick add buttons (+500, +1000, +5000, -500)
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          listOf(-1000, 500, 1000, 5000).forEach { delta ->
            val sign = if (delta > 0) "+₦" else "-₦"
            val label = "$sign${Math.abs(delta)}"
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFFF1F5F2),
              border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder),
              modifier = Modifier
                .weight(1f)
                .clickable {
                  val currentVal = priceInput.toIntOrNull() ?: 0
                  val newVal = Math.max(0, currentVal + delta)
                  priceInput = newVal.toString()
                }
            ) {
              Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = NeutralDark,
                modifier = Modifier.padding(vertical = 6.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Step 4: Upload Receipt / Photo (Optional)
        Text(
          text = if (language == AppLanguage.PIDGIN) "4. Receipt / Photo (Optional)" else "4. Upload Receipt or Market Photo (Optional):",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          color = NeutralDark
        )
        Spacer(modifier = Modifier.height(6.dp))

        if (receiptUri != null) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(MarketGreenContainer)
              .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              AsyncImage(
                model = receiptUri,
                contentDescription = "Receipt Photo",
                modifier = Modifier
                  .size(44.dp)
                  .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
              )
              Spacer(modifier = Modifier.width(10.dp))
              Text(
                text = "Receipt Photo Attached ✅",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MarketOnGreenContainer
              )
            }
            IconButton(onClick = { receiptUri = null }) {
              Icon(imageVector = Icons.Default.Close, contentDescription = "Remove")
            }
          }
        } else {
          OutlinedButton(
            onClick = {
              photoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
              )
            },
            modifier = Modifier
              .fillMaxWidth()
              .testTag("pick_receipt_photo_button"),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
          ) {
            Icon(
              imageVector = Icons.Default.AddAPhoto,
              contentDescription = "Upload",
              tint = MarketGreenPrimary
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (language == AppLanguage.PIDGIN) "Add Photo / Receipt" else "Add Receipt Photo (Optional)",
              fontSize = 12.sp,
              color = NeutralDark
            )
          }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Step 5: Reporter Name & Phone (Optional)
        Text(
          text = if (language == AppLanguage.PIDGIN) "5. Your Name / Phone Number (Optional)" else "5. Reporter Name / Phone (Optional):",
          fontWeight = FontWeight.Bold,
          fontSize = 13.sp,
          color = NeutralDark
        )
        Spacer(modifier = Modifier.height(6.dp))

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedTextField(
            value = reporterName,
            onValueChange = { reporterName = it },
            placeholder = { Text("Your nickname") },
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = NeutralMedium) },
            modifier = Modifier
              .weight(1f)
              .testTag("reporter_name_input"),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
          )

          OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            placeholder = { Text("080...") },
            leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = NeutralMedium) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            modifier = Modifier
              .weight(1f)
              .testTag("reporter_phone_input"),
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
          )
        }

        if (validationError != null) {
          Spacer(modifier = Modifier.height(8.dp))
          Text(text = validationError!!, color = Color.Red, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Big Submit Button
        Button(
          onClick = {
            val price = priceInput.toIntOrNull()
            if (selectedCommodity == null) {
              validationError = "Please select an item"
              return@Button
            }
            if (selectedMarket == null) {
              validationError = "Please select a market"
              return@Button
            }
            if (price == null || price <= 0) {
              validationError = "Please enter a valid price amount"
              return@Button
            }

            onSubmit(
              selectedCommodity!!.id,
              selectedMarket!!.id,
              price,
              reporterName,
              phoneNumber.takeIf { it.isNotBlank() },
              notes.takeIf { it.isNotBlank() },
              receiptUri?.toString()
            )
          },
          shape = RoundedCornerShape(14.dp),
          colors = ButtonDefaults.buttonColors(containerColor = MarketGreenVibrant),
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("submit_report_button")
        ) {
          Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White)
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (language == AppLanguage.PIDGIN) "Submit Price & Claim +10 Pts 🎉" else "Submit Price (+10 Points) 🎉",
            fontWeight = FontWeight.ExtraBold,
            fontSize = 14.sp,
            color = Color.White
          )
        }
      }
    }
  }
}
