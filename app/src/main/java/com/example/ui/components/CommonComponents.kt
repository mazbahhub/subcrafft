package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.*
import com.example.ui.theme.*

@Composable
fun NicheSelectorChipRow(
    selectedNiche: NicheType,
    onSelectNiche: (NicheType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        NicheType.values().forEach { niche ->
            val isSelected = niche == selectedNiche
            val bg = if (isSelected) PrimaryCyan else SurfaceVariantDark
            val contentColor = if (isSelected) Color(0xFF002236) else TextPrimaryDark
            val borderModifier = if (isSelected) Modifier else Modifier.border(1.dp, BorderSubtle, RoundedCornerShape(20.dp))

            Surface(
                modifier = borderModifier
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onSelectNiche(niche) }
                    .testTag("niche_chip_${niche.name}"),
                color = bg,
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(text = niche.iconEmoji, fontSize = 15.sp)
                    Text(
                        text = niche.title,
                        style = MaterialTheme.typography.labelLarge,
                        color = contentColor,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier,
    trendBadge: String? = null
) {
    Card(
        modifier = modifier
            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
            .testTag("metric_card_${title.lowercase().replace(" ", "_")}"),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(iconTint.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(20.dp)
                    )
                }

                if (trendBadge != null) {
                    Surface(
                        color = SuccessGreen.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.TrendingUp,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = trendBadge,
                                style = MaterialTheme.typography.labelSmall,
                                color = SuccessGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimaryDark
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondaryDark,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = TextMutedDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun StatusBadge(
    status: SubscriberStatus,
    modifier: Modifier = Modifier
) {
    val bgColor = when (status) {
        SubscriberStatus.ACTIVE -> SuccessGreen.copy(alpha = 0.18f)
        SubscriberStatus.TRIALING -> WarningAmber.copy(alpha = 0.18f)
        SubscriberStatus.PAST_DUE -> ErrorRed.copy(alpha = 0.18f)
        SubscriberStatus.CANCELED -> TextMutedDark.copy(alpha = 0.18f)
    }

    val textColor = when (status) {
        SubscriberStatus.ACTIVE -> SuccessGreen
        SubscriberStatus.TRIALING -> WarningAmber
        SubscriberStatus.PAST_DUE -> ErrorRed
        SubscriberStatus.CANCELED -> TextSecondaryDark
    }

    Surface(
        modifier = modifier,
        color = bgColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(CircleShape)
                    .background(textColor)
            )
            Text(
                text = status.label,
                style = MaterialTheme.typography.labelSmall,
                color = textColor,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun RevenueTrendChart(
    data: List<RevenueMonthlyData>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) return

    val maxMrr = data.maxOfOrNull { it.mrr } ?: 1.0

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp)),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "MRR Growth Trajectory",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark
                    )
                    Text(
                        text = "6-Month verified recurring revenue run-rate",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondaryDark
                    )
                }

                Surface(
                    color = PrimaryCyan.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "+92% H2 Run Rate",
                        style = MaterialTheme.typography.labelSmall,
                        color = PrimaryCyan,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Canvas Bar & Area Chart
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                val barWidth = size.width / (data.size * 2f)
                val spacing = size.width / data.size
                val chartHeight = size.height - 24.dp.toPx()

                // Draw subtle grid lines
                for (i in 1..3) {
                    val y = (chartHeight / 4) * i
                    drawLine(
                        color = BorderSubtle,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = 1f
                    )
                }

                // Draw bars and connecting line
                val points = mutableListOf<Offset>()

                data.forEachIndexed { index, item ->
                    val barHeight = ((item.mrr / maxMrr) * chartHeight).toFloat().coerceAtLeast(10f)
                    val x = index * spacing + spacing / 2
                    val y = chartHeight - barHeight

                    points.add(Offset(x, y))

                    // Draw Bar
                    drawRoundRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(PrimaryCyan, PrimaryBlue.copy(alpha = 0.6f))
                        ),
                        topLeft = Offset(x - barWidth / 2, y),
                        size = Size(barWidth, barHeight),
                        cornerRadius = CornerRadius(6f, 6f)
                    )
                }

                // Draw smooth line connecting peaks
                if (points.size > 1) {
                    val path = Path()
                    path.moveTo(points.first().x, points.first().y)
                    for (i in 1 until points.size) {
                        val prev = points[i - 1]
                        val curr = points[i]
                        val midX = (prev.x + curr.x) / 2
                        path.cubicTo(midX, prev.y, midX, curr.y, curr.x, curr.y)
                    }

                    drawPath(
                        path = path,
                        color = PrimaryCyan,
                        style = Stroke(width = 3.dp.toPx())
                    )

                    // Draw dots on points
                    points.forEach { point ->
                        drawCircle(
                            color = BackgroundDark,
                            radius = 5.dp.toPx(),
                            center = point
                        )
                        drawCircle(
                            color = PrimaryCyan,
                            radius = 3.dp.toPx(),
                            center = point
                        )
                    }
                }
            }

            // Month labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                data.forEach { item ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = item.monthName,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondaryDark,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = "$${item.mrr.toInt()}",
                            style = MaterialTheme.typography.labelSmall,
                            color = PrimaryLightBlue,
                            fontSize = 10.sp
                        )
                    }
                }
            }
        }
    }
}
