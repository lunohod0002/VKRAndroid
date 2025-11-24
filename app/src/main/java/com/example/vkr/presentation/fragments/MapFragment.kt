package com.example.vkr.presentation.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider

import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMapBinding
import com.example.vkr.domain.data.BranchColor
import com.example.vkr.presentation.viewmodels.MapViewModel
import com.google.android.play.core.integrity.b
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
    private val viewModel: MapViewModel by viewModels()
    private lateinit var mapView : MapView
    private val binding: FragmentMapBinding
        get() = _binding ?: throw RuntimeException()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(context)

        _binding = FragmentMapBinding.bind(view)

        mapView = binding.mapview
        val mapObjects =mapView.mapWindow.map.mapObjects
        // TODO: Убрать ненужные детали на карте (подписи к зданиям, назавания районов названия улиц и т.д.;
        //  TODO    Добавить слушатель по нажатию на станцию, для появления pop-up с краткой инфой о станции
        initMap()
        val textStyle =TextStyle()
        val iconStyle = IconStyle()
        val redImageProvider = ImageProvider.fromResource(context, R.drawable.red_branch_logo)
        val grayImageProvider = ImageProvider.fromResource(context, R.drawable.gray_branch_logo)
        val blueImageProvider = ImageProvider.fromResource(context, R.drawable.blue_branch_logo)
        val brownImageProvider = ImageProvider.fromResource(context, R.drawable.brown_branch_logo)

        textStyle.placement = TextStyle.Placement.BOTTOM
        iconStyle.scale=0.7f
        viewModel.markers.forEach { marker ->
            val icon = when (marker.branchColor) {
                BranchColor.RED -> redImageProvider
                BranchColor.GRAY -> grayImageProvider
                BranchColor.BLUE -> blueImageProvider
                BranchColor.BROWN -> brownImageProvider
            }

            mapObjects.addPlacemark().apply {
                geometry = Point(marker.latitude, marker.longitude)
                setIcon(icon)
                setText(marker.title)
                setTextStyle(textStyle)
                setIconStyle(iconStyle)
                userData = marker.title
                addTapListener(placemarkTapListener)
            }
        }
    }


    private fun initMap() {
        mapView.getMapWindow().getMap().move(
            CameraPosition(
                Point(55.753875, 37.622443),
                /* zoom = */ 12.00f,
                /* azimuth = */ 0.0f,
                /* tilt = */ 0.0f
            ),
        )
    }
    private val placemarkTapListener = MapObjectTapListener { mapObject,_ ->
        val title = mapObject.userData as? String ?: "Без названия"

        Toast.makeText(
            context,
            "Выбран: $title",
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