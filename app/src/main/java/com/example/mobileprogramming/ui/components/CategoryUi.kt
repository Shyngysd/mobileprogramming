package com.example.mobileprogramming.ui.components

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.mobileprogramming.R
import com.example.mobileprogramming.data.model.ServiceCategory

@StringRes
fun categoryLabel(category: ServiceCategory): Int = when (category) {
    ServiceCategory.HEALTH -> R.string.category_health
    ServiceCategory.BANKING -> R.string.category_banking
    ServiceCategory.PUBLIC_SERVICES -> R.string.category_public_services
    ServiceCategory.FOOD -> R.string.category_food
    ServiceCategory.BEAUTY -> R.string.category_beauty
    ServiceCategory.POST -> R.string.category_post
}

fun categoryIcon(category: ServiceCategory): ImageVector = when (category) {
    ServiceCategory.HEALTH -> Icons.Filled.Favorite
    ServiceCategory.BANKING -> Icons.Filled.Lock
    ServiceCategory.PUBLIC_SERVICES -> Icons.Filled.Home
    ServiceCategory.FOOD -> Icons.Filled.ShoppingCart
    ServiceCategory.BEAUTY -> Icons.Filled.Face
    ServiceCategory.POST -> Icons.Filled.Email
}