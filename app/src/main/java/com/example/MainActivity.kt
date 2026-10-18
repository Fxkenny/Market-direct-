package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.outlined.CompareArrows
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.ui.MainTab
import com.example.ui.MarketViewModel
import com.example.ui.components.AlertsView
import com.example.ui.components.CommodityCard
import com.example.ui.components.ForecastDialog
import com.example.ui.components.LeaderboardView
import com.example.ui.components.MarketComparisonView
import com.example.ui.components.MarketHeader
import com.example.ui.components.PremiumForecastBanner
import com.example.ui.components.PriceAlertDialog
import com.example.ui.components.ReportPriceDialog
import com.example.ui.components.SearchAndFilterSection
import com.example.ui.components.SponsoredShopriteBanner
import com.example.ui.components.TopCheapestMarketsBanner
import com.example.ui.theme.MarketGold
import com.example.ui.theme.MarketGreenContainer
import com.example.ui.theme.MarketGreenDark
import com.example.ui.theme.MarketGreenVibrant
import com.example.ui.theme.MarketOnGreenContainer
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeutralDark
import com.example.ui.theme.NeutralMedium
import com.example.ui.theme.SurfaceBackground

class MainActivity : ComponentActivity() {

  private val viewModel: MarketViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      MyApplicationTheme {
        PriceMarketNgApp(viewModel = viewModel)
      }
    }
  }
}

@Composable
fun PriceMarketNgApp(viewModel: MarketViewModel) {
  val uiState by viewModel.uiState.collectAsState()
  val filteredCommodities by viewModel.filteredCommodities.collectAsState()
  val allCommodities by viewModel.commodities.collectAsState()
  val priceReports by viewModel.priceReports.collectAsState()
  val priceAlerts by viewModel.priceAlerts.collectAsState()
  val userPoints by viewModel.userPoints.collectAsState()
  val currentLanguage by viewModel.currentLanguage.collectAsState()

  val snackbarHostState = remember { SnackbarHostState() }

  // Handle back button to return to main prices tab if on other tab
  BackHandler(enabled = uiState.currentTab != MainTab.PRICES) {
    viewModel.setTab(MainTab.PRICES)
  }

  // Handle snackbar feedback
  LaunchedEffect(uiState.snackbarMessage) {
    uiState.snackbarMessage?.let { msg ->
      snackbarHostState.showSnackbar(msg)
      viewModel.dismissSnackbar()
    }
  }

  Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState) },
    bottomBar = {
      NavigationBar(
        containerColor = Color.White,
        contentColor = MarketGreenDark,
        tonalElevation = 8.dp,
        modifier = Modifier.testTag("main_navigation_bar")
      ) {
        // Tab 1: Prices
        NavigationBarItem(
          selected = uiState.currentTab == MainTab.PRICES,
          onClick = { viewModel.setTab(MainTab.PRICES) },
          icon = {
            Icon(
              imageVector = if (uiState.currentTab == MainTab.PRICES) Icons.Default.Storefront else Icons.Outlined.Storefront,
              contentDescription = "Prices"
            )
          },
          label = {
            Text(
              text = if (currentLanguage == AppLanguage.PIDGIN) "Market" else "Prices",
              fontWeight = if (uiState.currentTab == MainTab.PRICES) FontWeight.Bold else FontWeight.Normal,
              fontSize = 11.sp
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MarketGreenDark,
            selectedTextColor = MarketGreenDark,
            indicatorColor = MarketGreenContainer,
            unselectedIconColor = NeutralMedium,
            unselectedTextColor = NeutralMedium
          ),
          modifier = Modifier.testTag("nav_tab_prices")
        )

        // Tab 2: Compare
        NavigationBarItem(
          selected = uiState.currentTab == MainTab.COMPARE,
          onClick = { viewModel.setTab(MainTab.COMPARE) },
          icon = {
            Icon(
              imageVector = Icons.Outlined.CompareArrows,
              contentDescription = "Compare"
            )
          },
          label = {
            Text(
              text = if (currentLanguage == AppLanguage.PIDGIN) "Compare" else "Compare",
              fontWeight = if (uiState.currentTab == MainTab.COMPARE) FontWeight.Bold else FontWeight.Normal,
              fontSize = 11.sp
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MarketGreenDark,
            selectedTextColor = MarketGreenDark,
            indicatorColor = MarketGreenContainer,
            unselectedIconColor = NeutralMedium,
            unselectedTextColor = NeutralMedium
          ),
          modifier = Modifier.testTag("nav_tab_compare")
        )

        // Tab 3: Big Report Price Button
        NavigationBarItem(
          selected = uiState.currentTab == MainTab.REPORT,
          onClick = { viewModel.openReportDialog() },
          icon = {
            Icon(
              imageVector = Icons.Default.AddCircle,
              contentDescription = "Report Price",
              tint = MarketGreenVibrant
            )
          },
          label = {
            Text(
              text = if (currentLanguage == AppLanguage.PIDGIN) "Report ₦" else "Report ₦",
              fontWeight = FontWeight.ExtraBold,
              fontSize = 11.sp,
              color = MarketGreenVibrant
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MarketGreenVibrant,
            selectedTextColor = MarketGreenVibrant,
            indicatorColor = MarketGreenContainer,
            unselectedIconColor = MarketGreenVibrant,
            unselectedTextColor = MarketGreenVibrant
          ),
          modifier = Modifier.testTag("nav_tab_report")
        )

        // Tab 4: Leaderboard
        NavigationBarItem(
          selected = uiState.currentTab == MainTab.LEADERBOARD,
          onClick = { viewModel.setTab(MainTab.LEADERBOARD) },
          icon = {
            Icon(
              imageVector = if (uiState.currentTab == MainTab.LEADERBOARD) Icons.Default.EmojiEvents else Icons.Outlined.EmojiEvents,
              contentDescription = "Leaderboard"
            )
          },
          label = {
            Text(
              text = if (currentLanguage == AppLanguage.PIDGIN) "Leaders" else "Reporters",
              fontWeight = if (uiState.currentTab == MainTab.LEADERBOARD) FontWeight.Bold else FontWeight.Normal,
              fontSize = 11.sp
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MarketGreenDark,
            selectedTextColor = MarketGreenDark,
            indicatorColor = MarketGreenContainer,
            unselectedIconColor = NeutralMedium,
            unselectedTextColor = NeutralMedium
          ),
          modifier = Modifier.testTag("nav_tab_leaderboard")
        )

        // Tab 5: Alerts
        NavigationBarItem(
          selected = uiState.currentTab == MainTab.ALERTS,
          onClick = { viewModel.setTab(MainTab.ALERTS) },
          icon = {
            Icon(
              imageVector = if (uiState.currentTab == MainTab.ALERTS) Icons.Default.NotificationsActive else Icons.Outlined.Notifications,
              contentDescription = "Alerts"
            )
          },
          label = {
            Text(
              text = if (currentLanguage == AppLanguage.PIDGIN) "Alarms" else "Alerts",
              fontWeight = if (uiState.currentTab == MainTab.ALERTS) FontWeight.Bold else FontWeight.Normal,
              fontSize = 11.sp
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MarketGreenDark,
            selectedTextColor = MarketGreenDark,
            indicatorColor = MarketGreenContainer,
            unselectedIconColor = NeutralMedium,
            unselectedTextColor = NeutralMedium
          ),
          modifier = Modifier.testTag("nav_tab_alerts")
        )
      }
    },
    floatingActionButton = {
      if (uiState.currentTab == MainTab.PRICES) {
        ExtendedFloatingActionButton(
          onClick = { viewModel.openReportDialog() },
          containerColor = MarketGreenVibrant,
          contentColor = Color.White,
          icon = {
            Icon(imageVector = Icons.Default.AddComment, contentDescription = null)
          },
          text = {
            Text(
              text = if (currentLanguage == AppLanguage.PIDGIN) "Report Wetin You Buy (+10 pts)" else "Report Today's Price (+10 pts)",
              fontWeight = FontWeight.ExtraBold,
              fontSize = 12.sp
            )
          },
          modifier = Modifier.testTag("fab_report_price")
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(SurfaceBackground)
        .padding(innerPadding)
    ) {
      when (uiState.currentTab) {
        MainTab.PRICES -> {
          LazyColumn(
            modifier = Modifier
              .fillMaxSize()
              .testTag("main_prices_list")
          ) {
            // Header Banner
            item {
              MarketHeader(
                language = currentLanguage,
                userPoints = userPoints,
                onToggleLanguage = { viewModel.toggleLanguage() },
                onReportClick = { viewModel.openReportDialog() }
              )
            }

            // Search bar & Category chips
            item {
              Spacer(modifier = Modifier.height(14.dp))
              SearchAndFilterSection(
                searchQuery = uiState.searchQuery,
                onSearchChange = { viewModel.setSearchQuery(it) },
                selectedCategory = uiState.selectedCategory,
                onCategorySelect = { viewModel.setCategory(it) },
                language = currentLanguage
              )
            }

            // Top 5 Cheapest Markets Card
            item {
              Spacer(modifier = Modifier.height(12.dp))
              TopCheapestMarketsBanner(
                markets = viewModel.repository.getTopCheapestMarketsToday(),
                language = currentLanguage,
                onMarketClick = { market ->
                  viewModel.setSelectedMarket(market.id)
                  viewModel.setTab(MainTab.COMPARE)
                }
              )
            }

            // Monetization 1: Sponsored Shoprite Banner
            item {
              Spacer(modifier = Modifier.height(6.dp))
              SponsoredShopriteBanner(language = currentLanguage)
            }

            // Monetization 2: Premium Price Forecast Preview Banner
            item {
              PremiumForecastBanner(
                language = currentLanguage,
                onForecastClick = { viewModel.openForecastDialog() }
              )
            }

            // Commodity List Header
            item {
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = if (currentLanguage == AppLanguage.PIDGIN)
                  "Today's Foodstuff Prices (${filteredCommodities.size} items)"
                else
                  "Today's Market Prices – Lagos (${filteredCommodities.size} items)",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 15.sp,
                color = NeutralDark,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
              )
            }

            // Main Commodities List
            items(filteredCommodities, key = { it.id }) { commodity ->
              CommodityCard(
                commodity = commodity,
                allMarkets = viewModel.repository.markets,
                language = currentLanguage,
                onCompareClick = { item ->
                  viewModel.selectCommodity(item)
                  viewModel.setTab(MainTab.COMPARE)
                },
                onAlertClick = { item ->
                  viewModel.openAlertDialog(item)
                },
                onReportClick = { item ->
                  viewModel.openReportDialog(item)
                }
              )
            }

            item {
              Spacer(modifier = Modifier.height(80.dp)) // Extra space for FAB and navigation bar
            }
          }
        }

        MainTab.COMPARE -> {
          MarketComparisonView(
            commodities = allCommodities,
            markets = viewModel.repository.markets,
            selectedCommodity = uiState.selectedCommodity,
            language = currentLanguage,
            onSelectCommodity = { viewModel.selectCommodity(it) },
            onOpenReport = { item -> viewModel.openReportDialog(item) }
          )
        }

        MainTab.REPORT -> {
          // Fallback to Prices tab since dialog handles reporting
          LaunchedEffect(Unit) {
            viewModel.openReportDialog()
            viewModel.setTab(MainTab.PRICES)
          }
        }

        MainTab.LEADERBOARD -> {
          LeaderboardView(
            leaderboard = viewModel.repository.getLeaderboard(),
            recentReports = priceReports,
            userPoints = userPoints,
            language = currentLanguage
          )
        }

        MainTab.ALERTS -> {
          AlertsView(
            alerts = priceAlerts,
            commodities = allCommodities,
            language = currentLanguage,
            onOpenCreateAlert = { item -> viewModel.openAlertDialog(item) },
            onDeleteAlert = { alertId -> viewModel.removePriceAlert(alertId) }
          )
        }
      }

      // Dialog: Report Price
      if (uiState.isReportingOpen) {
        ReportPriceDialog(
          commodities = allCommodities,
          markets = viewModel.repository.markets,
          preselectedCommodity = uiState.selectedCommodity,
          language = currentLanguage,
          onDismiss = { viewModel.closeReportDialog() },
          onSubmit = { cId, mId, price, name, phone, notes, photo ->
            viewModel.submitPriceReport(cId, mId, price, name, phone, notes, photo)
          }
        )
      }

      // Dialog: Price Alert
      if (uiState.isAlertDialogOpen && uiState.alertTargetCommodity != null) {
        PriceAlertDialog(
          commodity = uiState.alertTargetCommodity!!,
          language = currentLanguage,
          onDismiss = { viewModel.closeAlertDialog() },
          onSaveAlert = { cId, targetPrice ->
            viewModel.createPriceAlert(cId, targetPrice)
          }
        )
      }

      // Dialog: Premium Forecast
      if (uiState.isForecastOpen) {
        ForecastDialog(
          forecasts = viewModel.repository.forecasts,
          language = currentLanguage,
          onDismiss = { viewModel.closeForecastDialog() },
          onSubscribeMock = {
            viewModel.subscribePremiumMock()
          }
        )
      }
    }
  }
}
