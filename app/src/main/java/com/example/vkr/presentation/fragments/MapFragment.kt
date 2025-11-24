package com.example.vkr.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMapBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider


class MapFragment : Fragment(R.layout.fragment_map) {
    private var _binding: FragmentMapBinding? = null
    private lateinit var mapView : MapView
    private val binding: FragmentMapBinding
        get() = _binding ?: throw RuntimeException()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(context)

        _binding = FragmentMapBinding.bind(view)
        mapView = binding.mapview
        mapView.getMapWindow().getMap().move(
            CameraPosition(
                Point(55.753875, 37.622443),
                /* zoom = */ 12.00f,
                /* azimuth = */ 0.0f,
                /* tilt = */ 0.0f
            ),
        )
    // TODO: Убрать ненужные детали на карте (подписи к зданиям, назавания районов названия улиц и т.д.; перенести код отрисовки mapView во ViewModel и во фрагменте вызывать метод модели;
        //  TODO    Добавить слушатель по нажатию на станцию, для появления pop-up с краткой инфой о станции
        val redImageProvider = ImageProvider.fromResource(context, R.drawable.red_branch_logo)
        val textStyle =TextStyle()
        val iconStyle = IconStyle()
        val grayImageProvider = ImageProvider.fromResource(context, R.drawable.gray_branch_logo)
        val blueImageProvider = ImageProvider.fromResource(context, R.drawable.blue_branch_logo)
        val brownImageProvider = ImageProvider.fromResource(context, R.drawable.brown_branch_logo)

        textStyle.placement = TextStyle.Placement.BOTTOM
        iconStyle.scale=0.7f
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.736137, 37.594925)
            setIcon(redImageProvider)
             setText("Парк Культуры")
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)
             addTapListener(placemarkTapListener)
        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.745073, 37.603488)
            setIcon(redImageProvider)
            setText("Кропоткинская")
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.751344, 37.610225)
            setIcon(redImageProvider)
            setText("Библиотека им. Ленина")
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.756969, 37.615526)
            setIcon(redImageProvider)
            setText("Охотный Ряд")
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.759617, 37.626763)
            setIcon(redImageProvider)
            setText("Охотный Ряд")
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.764796, 37.638691)
            setIcon(redImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Чистые Пруды")
            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.768952, 37.648969)
            setIcon(redImageProvider)
            setText("Красные Ворота")
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.774367, 37.653878)
            setIcon(redImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Комсомольская")
            addTapListener(placemarkTapListener)

        }


        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.727293, 37.625098)
            setIcon(grayImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Серпуховская")
            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.736903, 37.618697)
            setIcon(grayImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Полянка")
            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.750574, 37.608649)
            setIcon(grayImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Боровицкая")
            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.765845, 37.608171)
            setIcon(grayImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Чеховская")
            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.771677, 37.620597)
            setIcon(grayImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Цветной Бульвар")
            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.781821, 37.598696)
            setIcon(grayImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Менделеевская")
            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.744701, 37.566815)
            setIcon(blueImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Киевская")
            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.747751, 37.583834)
            setIcon(blueImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Смоленская")
            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.752536, 37.604196)
            setIcon(blueImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Арбатская")
            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.756806, 37.622727)
            setIcon(blueImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Площадь Революции")
            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.758467, 37.658763)
            setIcon(blueImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Курская")
            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.760214, 37.577219)
            setIcon(brownImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Краснопресненская")
            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.779566, 37.601422)
            setIcon(brownImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Новослободская")
            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.779610, 37.633298)
            setIcon(brownImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Проспект Мира")
            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.774397, 37.655634)
            setIcon(brownImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Комсомольская")
            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.757450, 37.659938)
            setIcon(brownImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Курская")
            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.742313, 37.653094)
            setIcon(brownImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Таганская")
            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.731820, 37.637005)
            setIcon(brownImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Павелецкая")
            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.728995, 37.622745)
            setIcon(brownImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Добрынинская")
            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.729218, 37.611187)
            setIcon(brownImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Октябрьская")
            addTapListener(placemarkTapListener)

        }
        mapView.mapWindow.map.mapObjects.addPlacemark().apply {
            geometry = Point(55.743927, 37.568106)
            setIcon(brownImageProvider)
            setTextStyle(textStyle.setPlacement(TextStyle.Placement.BOTTOM))
            setIconStyle(iconStyle)

            setText("Киевская")
            addTapListener(placemarkTapListener)

        }







    }
    private val placemarkTapListener = MapObjectTapListener { _, point ->
        Toast.makeText(
            context,
            "Tapped the point (${point.longitude}, ${point.latitude})",
            Toast.LENGTH_SHORT
        ).show()
        true
    }
    override fun onStart() {

        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }


}