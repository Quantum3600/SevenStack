package com.trishit.sevenstack.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ButtonGroupDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.trishit.sevenstack.App
import com.trishit.sevenstack.ui.AppFonts
import com.trishit.sevenstack.ui.models.AppFont
import com.trishit.sevenstack.ui.models.AppTheme
import com.trishit.sevenstack.ui.models.ColorPalette
import com.trishit.sevenstack.ui.viewmodel.SettingsViewModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import sevenstack.app.shared.generated.resources.Res
import sevenstack.app.shared.generated.resources.back_icon
import sevenstack.app.shared.generated.resources.outline_mobile_24
import sevenstack.app.shared.generated.resources.rounded_moon_stars_24
import sevenstack.app.shared.generated.resources.rounded_wb_sunny_24
import kotlin.collections.forEachIndexed

class SettingsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        SettingsContent(onBackClick = { navigator.pop() })
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SettingsContent(
    onBackClick: () -> Unit
) {
    val viewModel = koinViewModel<SettingsViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "Settings",
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-1).sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(Res.drawable.back_icon),
                            contentDescription = "Back"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
//            item {
//                Column {
//                    Text("ACCOUNT", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
//                    Spacer(modifier = Modifier.height(12.dp))
//                    OutlinedCard(
//                        modifier = Modifier.fillMaxWidth(),
//                        shape = RoundedCornerShape(8.dp)
//                    ) {
//                        Column(modifier = Modifier.padding(16.dp)) {
//                            Text("Offline Mode Active", fontWeight = FontWeight.Bold, fontSize = 16.sp)
//                            Text("Cloud synchronization and backup strategies are managed in Phase 2.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
//                        }
//                    }
//                }
//            }
            item {
                Column {
                    Text(
                        "SYSTEM INTEGRATION",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(ButtonGroupDefaults.ConnectedSpaceBetween)
                    ) {
                        AppTheme.entries.forEachIndexed { index, theme ->
                            ToggleButton(
                                shapes = when (index) {
                                    0 -> ButtonGroupDefaults.connectedLeadingButtonShapes()
                                    1 -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                                    2 -> ButtonGroupDefaults.connectedTrailingButtonShapes()
                                    else -> ButtonGroupDefaults.connectedMiddleButtonShapes()
                                },
                                checked = uiState.theme == theme,
                                onCheckedChange = { viewModel.onThemeSelected(theme) },
                            ) {
                                Icon(
                                    painter = painterResource(
                                        when (theme.name) {
                                            "SYSTEM" -> Res.drawable.outline_mobile_24
                                            "DARK" -> Res.drawable.rounded_moon_stars_24
                                            "LIGHT" -> Res.drawable.rounded_wb_sunny_24
                                            else -> {}
                                        } as DrawableResource
                                    ),
                                    contentDescription = "Theme Icon"
                                )
                                Spacer(Modifier.size(ToggleButtonDefaults.IconSpacing))
                                Text(theme.name, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
            item {
                Column {
                    Text("Palette", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(12.dp))

                    val palettes = ColorPalette.entries
                    val carouselState = rememberCarouselState { palettes.size }

                    HorizontalMultiBrowseCarousel(
                        state = carouselState,
                        preferredItemWidth = 130.dp,
                        itemSpacing = 12.dp,
                        modifier = Modifier.fillMaxWidth().height(100.dp)
                    ) { index ->
                        val currentPalette = palettes[index]

                        // Map illustrative color boxes for the carousel items
                        val previewColor = when (currentPalette) {
                            ColorPalette.MONOCHROME -> Color.DarkGray
                            ColorPalette.RED -> Color(0xFFFF3B30)
                            ColorPalette.YELLOW -> Color(0xFFFFCC00)
                            ColorPalette.GREEN -> Color(0xFF34C759)
                            ColorPalette.BLUE -> Color(0xFF007AFF)
                        }

                        val isSelected = uiState.palette == currentPalette

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp))
                                .background(previewColor)
                                .border(
                                    width = if (isSelected) 4.dp else 0.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.onBackground else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { viewModel.onPaletteSelected(currentPalette) }
                                .padding(12.dp),
                            contentAlignment = Alignment.BottomStart
                        ) {
                            Text(
                                text = currentPalette.name,
                                color = if (currentPalette == ColorPalette.YELLOW) Color.Black else Color.White,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
            item {
                Column {
                    Text(
                        "Typography",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    val fonts = AppFont.entries
                    Column {
                        fonts.forEachIndexed { index, font ->
                            val shapes = ListItemDefaults.segmentedShapes(index = index, count = AppFont.entries.count())
                            SegmentedListItem(
                                selected = uiState.font = font,
                                onClick = { viewModel.onFontSelected(font) },
                                shapes = shapes, // Renders distinct rounded corners for top/middle/bottom
                                headlineContent = { Text(font.name) },
                                leadingContent = { RadioButton(selected = uiState.font = font, onClick = null) }
                            )

                            // Add a small divider between items to emphasize the grouping
                            if (index < count - 1) {
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}

