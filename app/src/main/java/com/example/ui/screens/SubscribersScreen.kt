package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Subscriber
import com.example.data.model.SubscriberStatus
import com.example.ui.components.StatusBadge
import com.example.ui.theme.*
import com.example.ui.viewmodel.SaaSViewModel

@Composable
fun SubscribersScreen(
    viewModel: SaaSViewModel,
    modifier: Modifier = Modifier
) {
    val subscribers by viewModel.filteredSubscribers.collectAsState()
    val allSubscribers by viewModel.subscribers.collectAsState()
    val activeFilter by viewModel.subscriberFilter.collectAsState()
    val searchQuery by viewModel.subscriberSearchQuery.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Subscribers & Clients",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                        Text(
                            text = "${allSubscribers.size} total accounts • $${allSubscribers.filter { it.status == SubscriberStatus.ACTIVE }.sumOf { it.mrrAmount }.toInt()} recurring MRR",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondaryDark
                        )
                    }

                    Button(
                        onClick = { viewModel.openAddSubscriberDialog() },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryCyan),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("btn_add_subscriber_header")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color(0xFF002236),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Add",
                            color = Color(0xFF002236),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSubscriberSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_search_subscribers"),
                    placeholder = { Text("Search subscriber, company, or email...", color = TextMutedDark) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = TextSecondaryDark
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSubscriberSearchQuery("") }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear",
                                    tint = TextSecondaryDark
                                )
                            }
                        }
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryCyan,
                        unfocusedBorderColor = BorderSubtle,
                        focusedContainerColor = SurfaceDark,
                        unfocusedContainerColor = SurfaceDark,
                        focusedTextColor = TextPrimaryDark,
                        unfocusedTextColor = TextPrimaryDark
                    ),
                    shape = RoundedCornerShape(14.dp)
                )
            }

            // Status Filter Tabs
            item {
                val filterOptions = listOf(
                    "ALL" to "All (${allSubscribers.size})",
                    "ACTIVE" to "Active (${allSubscribers.count { it.status == SubscriberStatus.ACTIVE }})",
                    "TRIALING" to "Trial (${allSubscribers.count { it.status == SubscriberStatus.TRIALING }})",
                    "PAST_DUE" to "Past Due (${allSubscribers.count { it.status == SubscriberStatus.PAST_DUE }})",
                    "CANCELED" to "Canceled (${allSubscribers.count { it.status == SubscriberStatus.CANCELED }})"
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filterOptions.forEach { (key, label) ->
                        val isSelected = activeFilter == key
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.setSubscriberFilter(key) },
                            label = {
                                Text(
                                    text = label,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryCyan.copy(alpha = 0.2f),
                                selectedLabelColor = PrimaryCyan,
                                containerColor = SurfaceDark,
                                labelColor = TextSecondaryDark
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) PrimaryCyan else BorderSubtle
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }

            // Subscribers List
            if (subscribers.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = null,
                                tint = TextMutedDark,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No subscribers found",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimaryDark,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Try adjusting your filter or search query.",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondaryDark
                            )
                        }
                    }
                }
            } else {
                items(subscribers, key = { it.id }) { subscriber ->
                    SubscriberCard(
                        subscriber = subscriber,
                        onStatusChange = { newStatus ->
                            viewModel.toggleSubscriberStatus(subscriber.id, newStatus)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SubscriberCard(
    subscriber: Subscriber,
    onStatusChange: (SubscriberStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandedMenu by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
            .testTag("subscriber_card_${subscriber.id}"),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Initials Avatar
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(subscriber.avatarColorHex)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = subscriber.avatarInitials,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = subscriber.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimaryDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = subscriber.company,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondaryDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = subscriber.email,
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMutedDark,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "$${subscriber.mrrAmount.toInt()}",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryCyan
                    )
                    Text(
                        text = "/month",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMutedDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Plan & Status Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = SurfaceVariantDark,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = subscriber.planName,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextPrimaryDark,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    StatusBadge(status = subscriber.status)
                }

                // Status Change Action Dropdown
                Box {
                    IconButton(
                        onClick = { expandedMenu = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Actions",
                            tint = TextSecondaryDark
                        )
                    }

                    DropdownMenu(
                        expanded = expandedMenu,
                        onDismissRequest = { expandedMenu = false },
                        modifier = Modifier.background(SurfaceDark)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Mark Active", color = SuccessGreen) },
                            onClick = {
                                onStatusChange(SubscriberStatus.ACTIVE)
                                expandedMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.CheckCircle, null, tint = SuccessGreen)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Mark In Trial", color = WarningAmber) },
                            onClick = {
                                onStatusChange(SubscriberStatus.TRIALING)
                                expandedMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.HourglassEmpty, null, tint = WarningAmber)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Mark Past Due", color = ErrorRed) },
                            onClick = {
                                onStatusChange(SubscriberStatus.PAST_DUE)
                                expandedMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Warning, null, tint = ErrorRed)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Cancel Subscription", color = TextMutedDark) },
                            onClick = {
                                onStatusChange(SubscriberStatus.CANCELED)
                                expandedMenu = false
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Cancel, null, tint = TextMutedDark)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Next Billing: ${subscriber.nextBillingDate} • Joined: ${subscriber.joinDate}",
                style = MaterialTheme.typography.labelSmall,
                color = TextMutedDark
            )
        }
    }
}
