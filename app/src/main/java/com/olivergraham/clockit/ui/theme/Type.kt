package com.olivergraham.clockit.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.olivergraham.clockit.R


fun Typography.getFontSize() = LARGE_FONT_SIZE

private val LARGE_FONT_SIZE = 18.sp
private val SMALL_FONT_SIZE = 11.sp
private val FONT_FAMILY = FontFamily(Font(R.font.stylish_regular))
private val CARD_TITLE_FONT = FontFamily(Font(R.font.stylish_regular))

// TODO: Make better

val LightTypography = Typography(

    // used in Text() by default
    bodyLarge = TextStyle(
        fontFamily = FONT_FAMILY,
        fontWeight = FontWeight.ExtraBold,
        fontSize = LARGE_FONT_SIZE,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    // overridden by Text() in LargeButton()
    bodyMedium = TextStyle(
        fontFamily = FONT_FAMILY,
        fontWeight = FontWeight.Bold,
        fontSize = LARGE_FONT_SIZE * 1.1,
        lineHeight = 20.sp,
        letterSpacing = 0.sp
    ),

    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.rubikdirt_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = LARGE_FONT_SIZE * 2,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FONT_FAMILY,
        fontWeight = FontWeight.Medium,
        fontSize = SMALL_FONT_SIZE,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),

    // overridden by Text() in CardHeaders
    headlineSmall = TextStyle(
        fontFamily = CARD_TITLE_FONT,
        fontWeight = FontWeight.Bold,
        fontSize = LARGE_FONT_SIZE * 2,
        lineHeight = 40.sp,
        letterSpacing = 0.5.sp
    )
)

private val darkTextColor = Color.Black

val DarkTypography = Typography(

    // used in Text() by default
    bodyLarge = TextStyle(
        fontFamily = FONT_FAMILY,
        fontWeight = FontWeight.ExtraBold,
        fontSize = LARGE_FONT_SIZE,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
        color = darkTextColor
    ),

    // overridden by Text() in LargeButton()
    bodyMedium = TextStyle(
        fontFamily = FONT_FAMILY,
        fontWeight = FontWeight.Bold,
        fontSize = LARGE_FONT_SIZE * 1.1,
        lineHeight = 20.sp,
        letterSpacing = 0.sp,
        color = Color.White
    ),

    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.rubikdirt_regular)),
        fontWeight = FontWeight.Normal,
        fontSize = LARGE_FONT_SIZE * 2,
        lineHeight = 28.sp,
        letterSpacing = 0.sp,
        color = Color.White
    ),
    labelSmall = TextStyle(
        fontFamily = FONT_FAMILY,
        fontWeight = FontWeight.Medium,
        fontSize = SMALL_FONT_SIZE,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        color = darkTextColor
    ),

    // overridden by Text() in CardHeaders
    headlineSmall = TextStyle(
        fontFamily = CARD_TITLE_FONT,
        fontWeight = FontWeight.Bold,
        fontSize = LARGE_FONT_SIZE * 2,
        lineHeight = 40.sp,
        letterSpacing = 0.5.sp,
        color = Color.White
    )

)
