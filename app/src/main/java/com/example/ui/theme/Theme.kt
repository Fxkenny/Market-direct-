package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
  primary = MarketGreenVibrant,
  onPrimary = Color.White,
  primaryContainer = MarketGreenContainer,
  onPrimaryContainer = MarketOnGreenContainer,
  secondary = MarketGold,
  onSecondary = Color.White,
  secondaryContainer = MarketGoldContainer,
  onSecondaryContainer = MarketOnGoldContainer,
  tertiary = MarketGreenDark,
  background = SurfaceBackground,
  onBackground = NeutralDark,
  surface = SurfaceCard,
  onSurface = NeutralDark,
  surfaceVariant = Color(0xFFF0F5F2),
  onSurfaceVariant = NeutralMedium,
  outline = SurfaceBorder,
  error = PriceHikeRed,
  onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
  primary = MarketGreenVibrant,
  onPrimary = Color(0xFF003820),
  primaryContainer = Color(0xFF005232),
  onPrimaryContainer = MarketGreenContainer,
  secondary = MarketGold,
  onSecondary = Color(0xFF422300),
  secondaryContainer = Color(0xFF5E3400),
  onSecondaryContainer = MarketGoldContainer,
  background = Color(0xFF111714),
  onBackground = Color(0xFFE8EDE9),
  surface = Color(0xFF19221D),
  onSurface = Color(0xFFE8EDE9),
  surfaceVariant = Color(0xFF233029),
  onSurfaceVariant = Color(0xFFB4C4BC),
  outline = Color(0xFF33443B),
  error = Color(0xFFFFB4AB),
  onError = Color(0xFF690005)
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Disable dynamic color so the app stays authentically Opay/PalmPay emerald green
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
