package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MarketRepository
import com.example.model.AppLanguage
import com.example.model.Commodity
import com.example.model.CommodityCategory
import com.example.model.LeaderboardEntry
import com.example.model.MarketInfo
import com.example.model.PriceAlert
import com.example.model.PriceForecast
import com.example.model.PriceReport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class MainTab {
  PRICES,
  COMPARE,
  REPORT,
  LEADERBOARD,
  ALERTS
}

data class MarketUiState(
  val currentTab: MainTab = MainTab.PRICES,
  val language: AppLanguage = AppLanguage.ENGLISH,
  val selectedCategory: CommodityCategory = CommodityCategory.ALL,
  val searchQuery: String = "",
  val selectedMarketId: String? = null,
  val selectedCommodity: Commodity? = null,
  val isReportingOpen: Boolean = false,
  val isAlertDialogOpen: Boolean = false,
  val isForecastOpen: Boolean = false,
  val isSponsorInfoOpen: Boolean = false,
  val alertTargetCommodity: Commodity? = null,
  val snackbarMessage: String? = null
)

class MarketViewModel(application: Application) : AndroidViewModel(application) {

  val repository = MarketRepository(application.applicationContext)

  private val _uiState = MutableStateFlow(MarketUiState())
  val uiState: StateFlow<MarketUiState> = _uiState

  val commodities: StateFlow<List<Commodity>> = repository.commodities
  val priceReports: StateFlow<List<PriceReport>> = repository.priceReports
  val priceAlerts: StateFlow<List<PriceAlert>> = repository.priceAlerts
  val userPoints: StateFlow<Int> = repository.userPoints
  val currentLanguage: StateFlow<AppLanguage> = repository.currentLanguage

  val filteredCommodities: StateFlow<List<Commodity>> = combine(
    commodities,
    _uiState
  ) { list, state ->
    list.filter { item ->
      val matchesCategory = (state.selectedCategory == CommodityCategory.ALL) || (item.category == state.selectedCategory)
      val query = state.searchQuery.trim().lowercase()
      val matchesSearch = query.isEmpty() ||
          item.name.lowercase().contains(query) ||
          item.pidginName.lowercase().contains(query) ||
          item.nativeAliases.lowercase().contains(query) ||
          item.description.lowercase().contains(query)

      val matchesMarket = state.selectedMarketId == null || item.marketPrices.containsKey(state.selectedMarketId)

      matchesCategory && matchesSearch && matchesMarket
    }
  }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  fun setTab(tab: MainTab) {
    _uiState.value = _uiState.value.copy(currentTab = tab)
  }

  fun setCategory(category: CommodityCategory) {
    _uiState.value = _uiState.value.copy(selectedCategory = category)
  }

  fun setSearchQuery(query: String) {
    _uiState.value = _uiState.value.copy(searchQuery = query)
  }

  fun setSelectedMarket(marketId: String?) {
    _uiState.value = _uiState.value.copy(selectedMarketId = marketId)
  }

  fun selectCommodity(commodity: Commodity?) {
    _uiState.value = _uiState.value.copy(selectedCommodity = commodity)
  }

  fun toggleLanguage() {
    repository.toggleLanguage()
    _uiState.value = _uiState.value.copy(language = repository.currentLanguage.value)
  }

  fun openReportDialog(preselectedCommodity: Commodity? = null) {
    _uiState.value = _uiState.value.copy(
      isReportingOpen = true,
      selectedCommodity = preselectedCommodity ?: _uiState.value.selectedCommodity
    )
  }

  fun closeReportDialog() {
    _uiState.value = _uiState.value.copy(isReportingOpen = false)
  }

  fun openAlertDialog(commodity: Commodity) {
    _uiState.value = _uiState.value.copy(
      isAlertDialogOpen = true,
      alertTargetCommodity = commodity
    )
  }

  fun closeAlertDialog() {
    _uiState.value = _uiState.value.copy(
      isAlertDialogOpen = false,
      alertTargetCommodity = null
    )
  }

  fun openForecastDialog() {
    _uiState.value = _uiState.value.copy(isForecastOpen = true)
  }

  fun closeForecastDialog() {
    _uiState.value = _uiState.value.copy(isForecastOpen = false)
  }

  fun openSponsorDialog() {
    _uiState.value = _uiState.value.copy(isSponsorInfoOpen = true)
  }

  fun closeSponsorDialog() {
    _uiState.value = _uiState.value.copy(isSponsorInfoOpen = false)
  }

  fun submitPriceReport(
    commodityId: String,
    marketId: String,
    price: Int,
    reporterName: String,
    phoneNumber: String?,
    notes: String?,
    photoUri: String?
  ) {
    viewModelScope.launch {
      val report = repository.reportPrice(
        commodityId = commodityId,
        marketId = marketId,
        price = price,
        reporterName = reporterName,
        phoneNumber = phoneNumber,
        notes = notes,
        photoUri = photoUri
      )
      val msg = if (repository.currentLanguage.value == AppLanguage.PIDGIN) {
        "Oshey! You don get +10 Market Points! Price don update sharp-sharp! 🎉"
      } else {
        "Report submitted! +10 Points awarded to you. Live price updated! 🎉"
      }
      _uiState.value = _uiState.value.copy(
        isReportingOpen = false,
        snackbarMessage = msg
      )
    }
  }

  fun createPriceAlert(commodityId: String, targetPrice: Int) {
    viewModelScope.launch {
      repository.addPriceAlert(commodityId, targetPrice)
      val msg = if (repository.currentLanguage.value == AppLanguage.PIDGIN) {
        "Alert don set! We go ping you once price fall below ${MarketRepository.formatNaira(targetPrice)} 🔔"
      } else {
        "Alert set! You'll be alerted once price drops below ${MarketRepository.formatNaira(targetPrice)} 🔔"
      }
      _uiState.value = _uiState.value.copy(
        isAlertDialogOpen = false,
        snackbarMessage = msg
      )
    }
  }

  fun removePriceAlert(alertId: String) {
    repository.removePriceAlert(alertId)
    _uiState.value = _uiState.value.copy(
      snackbarMessage = if (repository.currentLanguage.value == AppLanguage.PIDGIN) "Alert deleted!" else "Price alert removed"
    )
  }

  fun subscribePremiumMock() {
    val msg = if (repository.currentLanguage.value == AppLanguage.PIDGIN) {
      "Premium Forecast unlocked! You dey see all next week price secrets now! 🔮"
    } else {
      "Premium Forecast unlocked! You now have access to weekly price trends. 🔮"
    }
    _uiState.value = _uiState.value.copy(
      isForecastOpen = false,
      snackbarMessage = msg
    )
  }

  fun dismissSnackbar() {
    _uiState.value = _uiState.value.copy(snackbarMessage = null)
  }
}
