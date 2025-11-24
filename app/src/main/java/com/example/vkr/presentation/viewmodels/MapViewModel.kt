package com.example.vkr.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vkr.domain.data.BranchColor
import com.example.vkr.domain.data.MapMarker
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.TextStyle

class MapViewModel : ViewModel() {
    val markers: List<MapMarker> = listOf(
        MapMarker(55.736137, 37.594925, "Парк Культуры", BranchColor.RED),
        MapMarker(55.745073, 37.603488, "Кропоткинская", BranchColor.RED),
        MapMarker(55.751344, 37.610225, "Библиотека им. Ленина", BranchColor.RED),
        MapMarker(55.756969, 37.615526, "Охотный ряд", BranchColor.RED),
        MapMarker(55.759617, 37.626763, "Лубянка", BranchColor.RED),
        MapMarker(55.764796, 37.638691, "Чистые Пруды", BranchColor.RED),
        MapMarker(55.768952, 37.648969, "Красные Ворота", BranchColor.RED),
        MapMarker(55.774367, 37.653878, "Комсомольская", BranchColor.RED),
        MapMarker(55.727293, 37.625098, "Серпуховская", BranchColor.GRAY),
        MapMarker(55.736903, 37.618697, "Полянка", BranchColor.GRAY),
        MapMarker(55.750574, 37.608649, "Боровицкая", BranchColor.GRAY),
        MapMarker(55.765845, 37.608171, "Чеховская", BranchColor.GRAY),
        MapMarker(55.771677, 37.620597, "Цветной Бульвар", BranchColor.GRAY),
        MapMarker(55.781821, 37.598696, "Менделеевская", BranchColor.GRAY),
        MapMarker(55.744701, 37.566815, "Киевская", BranchColor.BLUE),
        MapMarker(55.747751, 37.583834, "Смоленская", BranchColor.BLUE),
        MapMarker(55.752536, 37.604196, "Арбатская", BranchColor.BLUE),
        MapMarker(55.756806, 37.622727, "Площадь Революции", BranchColor.BLUE),
        MapMarker(55.758467, 37.658763, "Курская", BranchColor.BLUE),
        MapMarker(55.760214, 37.577219, "Краснопресненская", BranchColor.BROWN),
        MapMarker(55.779566, 37.601422, "Новослободская", BranchColor.BROWN),
        MapMarker(55.779610, 37.633298, "Краснопресненская", BranchColor.BROWN),
        MapMarker(55.760214, 37.577219, "Проспект Мира", BranchColor.BROWN),
        MapMarker(55.774397, 37.655634, "Комсомольская", BranchColor.BROWN),
        MapMarker(55.757450, 37.659938, "Курская", BranchColor.BROWN),
        MapMarker(55.742313, 37.653094, "Таганская", BranchColor.BROWN),
        MapMarker(55.731820, 37.637005, "Павелецкая", BranchColor.BROWN),
        MapMarker(55.728995, 37.622745, "Добрынинская", BranchColor.BROWN),
        MapMarker(55.729218, 37.611187, "Киевская", BranchColor.BROWN),








    )



}