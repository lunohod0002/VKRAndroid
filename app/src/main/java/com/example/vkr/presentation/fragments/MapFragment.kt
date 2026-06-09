package com.example.vkr.presentation.fragments

import com.example.vkr.logic.viewmodels.MapViewModel
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMapBinding
import com.example.vkr.network.dto.MapMarker
import com.example.vkr.network.dto.StationCoordinates
import com.example.vkr.network.dto.StationData
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Circle
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.TextStyle
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map) {
    private var _binding: FragmentMapBinding? = null
    private val viewModel: MapViewModel by viewModels()
    private lateinit var mapView: MapView
    private lateinit var mapObjects: MapObjectCollection

    private val binding: FragmentMapBinding
        get() = _binding ?: throw RuntimeException()

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.fetchCurrentLocation()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MapKitFactory.initialize(context)
        _binding = FragmentMapBinding.bind(view)

        mapView = binding.mapview
        mapObjects = mapView.mapWindow.map.mapObjects

        initMap()
        initLocationLiveData()
        observeStations() // Подписка на станции с сервера/кеша

        checkAndRequestLocation()
    }

    private fun observeStations() {
        viewModel.markers.observe(viewLifecycleOwner) { markersList ->
            displayMap(markersList)
        }
    }

    private fun checkAndRequestLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.fetchCurrentLocation()
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun initLocationLiveData() {
        viewModel.resultLive.observe(viewLifecycleOwner) { coordinates ->
            if (coordinates != null) {

            val circle = Circle(
                Point(coordinates.latitude, coordinates.longitude),
                250f
            )
            mapObjects.addCircle(circle).apply {

                strokeWidth = 0.5f
                strokeColor = ContextCompat.getColor(requireContext(), R.color.colorRed)
                fillColor = ContextCompat.getColor(requireContext(), R.color.colorRedSemiTransparent)
            }
            }
        }
    }

    private fun initMap() {
        mapView.getMapWindow().getMap().move(
            CameraPosition(
                Point(55.753875, 37.622443),
                12.00f,
                0.0f,
                0.0f
            ),
        )
        val styleJson = "[{\"elements\":[\"label\"],\"stylers\":{\"visibility\":\"off\"}}]"
        mapView.getMapWindow().getMap().setMapStyle(styleJson)
    }

    private fun displayMap(markers: List<MapMarker>) {
        // ОЧИЩАЕМ карту перед новой отрисовкой, чтобы не было дублей!
        mapObjects.clear()

        val textStyle = TextStyle()
        val iconStyle = IconStyle()
        val redImageProvider = ImageProvider.fromResource(context, R.drawable.red_branch_logo)
        val grayImageProvider = ImageProvider.fromResource(context, R.drawable.gray_branch_logo)
        val blueImageProvider = ImageProvider.fromResource(context, R.drawable.blue_branch_logo)
        val brownImageProvider = ImageProvider.fromResource(context, R.drawable.brown_branch_logo)

        textStyle.placement = TextStyle.Placement.BOTTOM
        iconStyle.scale = 0.60f

        markers.forEach { marker ->
            val icon = when (marker.branchNumber) {
                1 -> redImageProvider
                9 -> grayImageProvider
                3 -> blueImageProvider
                5 -> brownImageProvider
                else -> null // Если ветка не опознана, можно добавить дефолтную иконку
            }

            if (icon != null) {
                mapObjects.addPlacemark().apply {
                    geometry = Point(marker.coordinates.latitude, marker.coordinates.longitude)
                    setIcon(icon)
                    setText(marker.title)
                    setTextStyle(textStyle)
                    setIconStyle(iconStyle)
                    userData = marker
                    addTapListener(placemarkTapListener)
                }
            }
        }
    }

    private val placemarkTapListener = MapObjectTapListener { mapObject, _ ->
        val marker = mapObject.userData as? MapMarker
            ?: MapMarker(StationCoordinates(0.0, 0.0), "Без названия", 0)
        val stationData = StationData(title = marker.title, branchNumber = marker.branchNumber)
        viewModel.navigateToStation(stationData)
        true
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()
    }

    override fun onStop() {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}