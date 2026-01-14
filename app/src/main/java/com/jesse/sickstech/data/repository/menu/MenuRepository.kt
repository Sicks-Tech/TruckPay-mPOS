package com.jesse.sickstech.data.repository.menu

import com.jesse.sickstech.R
import com.jesse.sickstech.features.menu.Menu

class MenuRepository {
    fun getMenu(): List<Menu> {
        return listOf(
            Menu(R.drawable.hamburguer, "Hamburguer Super Truck", "R$ 30,00"),
            Menu(R.drawable.hamburguer, "Hamburguer Super Super Truck", "R$ 40,00"),
            Menu(R.drawable.hamburguer, "Hamburguer Smash", "R$ 20,00"),
            Menu(R.drawable.hamburguer, "Hamburguer Smash", "R$ 20,00"),
            Menu(R.drawable.hamburguer, "Hamburguer Smash", "R$ 20,00"),
            Menu(R.drawable.hamburguer, "Hamburguer Smash", "R$ 20,00"),
            Menu(R.drawable.hamburguer, "Hamburguer Smash", "R$ 20,00"),
            Menu(R.drawable.hamburguer, "Hamburguer Smash", "R$ 20,00"),
        )
    }

    fun setMenu(menu: List<Menu>) {}

}