package com.example.vkr.presentation.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentMapBinding
import com.example.vkr.App
import com.example.vkr.domain.models.MapMarker
import com.example.vkr.domain.models.StationCoordinates
import com.example.vkr.domain.models.StationData
import com.example.vkr.presentation.viewmodels.MapViewModel
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



class MapFragment : Fragment(R.layout.fragment_map) {
    private var _binding: FragmentMapBinding? = null
    private  val viewModel: MapViewModel by viewModels { MapViewModel.Factory(requireContext(),(requireActivity().application as App).getDb().cellDao() )}
    private lateinit var mapView : MapView
    private lateinit var mapObjects : MapObjectCollection

    private val binding: FragmentMapBinding
        get() = _binding ?: throw RuntimeException()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(context)
        _binding = FragmentMapBinding.bind(view)

        mapView = binding.mapview
        mapObjects =mapView.mapWindow.map.mapObjects
        requestPermission()
        initMap()
        displayMap()
        displayCurrentLocation()
        initLocationLiveData()

        //  TODO    Добавить слушатель по нажатию на станцию, для появления pop-up с краткой инфой о станции

    }

    private fun displayCurrentLocation(){
        viewModel.fetchCurrentLocation()
    }

    private fun initLocationLiveData() {
        viewModel.resultLive.observe(viewLifecycleOwner,{coordinates ->
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
        })

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
        val styleJson = "[{\"elements\":[\"label\"],\"stylers\":{\"visibility\":\"off\"}}]"
        mapView.getMapWindow().getMap().setMapStyle(styleJson)
    }

    private fun displayMap(){
        val textStyle =TextStyle()
        val iconStyle = IconStyle()
        val redImageProvider = ImageProvider.fromResource(context, R.drawable.red_branch_logo)
        val grayImageProvider = ImageProvider.fromResource(context, R.drawable.gray_branch_logo)
        val blueImageProvider = ImageProvider.fromResource(context, R.drawable.blue_branch_logo)
        val brownImageProvider = ImageProvider.fromResource(context, R.drawable.brown_branch_logo)

        textStyle.placement = TextStyle.Placement.BOTTOM
        iconStyle.scale=0.60f
        viewModel.markers.forEach { marker ->
            val icon = when (marker.branchNumber) {
                1 -> redImageProvider
                9 -> grayImageProvider
                3 -> blueImageProvider
                5 -> brownImageProvider
                else -> null
            }
            if (icon!=null) {
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
    private val placemarkTapListener = MapObjectTapListener { mapObject,_ ->
        val marker = mapObject.userData as? MapMarker ?: MapMarker(StationCoordinates(0.0,0.0),"Без названия",0)
        val stationData = StationData(title=marker.title, branchNumber = marker.branchNumber)
        val action= MapFragmentDirections.actionScreenMapToScreenStation(
            STATION = stationData
        )
        findNavController().navigate(action)
        true
    }
    private fun requestPermission() {
                if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                2000
            )
        }
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                2001
            )
        }
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_BACKGROUND_LOCATION),
                    2002


                )
            }
        }
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