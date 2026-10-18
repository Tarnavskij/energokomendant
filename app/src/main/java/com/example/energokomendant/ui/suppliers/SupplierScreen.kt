package com.example.energokomendant.ui.suppliers

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.energokomendant.data.Catalog
import com.example.energokomendant.data.MetaState
import com.example.energokomendant.data.ShopItem

@Composable
fun SupplierScreen(
    meta: MetaState,
    onBuy: (ShopItem) -> Unit,
    onBack: () -> Unit
) {
    BackHandler { onBack() }
    val haptic = LocalHapticFeedback.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) { Text("← Назад") }
            Text("🏪 Поставщики", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)) {
                Text(
                    "⚡ ${meta.coins}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text("Постоянные улучшения — покупаются один раз.", fontSize = 12.sp)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(Catalog.items) { item ->
                val owned = meta.ownedItems.contains(item.id)
                ShopItemCard(
                    item = item,
                    owned = owned,
                    canAfford = meta.coins >= item.cost,
                    onBuy = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onBuy(item)
                    }
                )
            }
        }
    }
}

@Composable
private fun ShopItemCard(
    item: ShopItem,
    owned: Boolean,
    canAfford: Boolean,
    onBuy: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (owned) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(item.emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(item.description, fontSize = 11.sp)
                Text("${item.cost} ⚡", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            if (owned) {
                Text("✓", fontSize = 24.sp, color = MaterialTheme.colorScheme.primary)
            } else {
                Button(onClick = onBuy, enabled = canAfford) {
                    Text("Купить")
                }
            }
        }
    }
}