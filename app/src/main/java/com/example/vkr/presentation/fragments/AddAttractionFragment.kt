package com.example.vkr.presentation.fragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.myapplication.databinding.FragmentAddAttractionBinding
import com.example.myapplication.databinding.ItemAddedStationBinding

import com.example.vkr.network.dto.StationAttractionRequest
import com.example.vkr.presentation.viewmodel.AddAttractionViewModel

class AddAttractionFragment : Fragment() {

    private var _binding: FragmentAddAttractionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddAttractionViewModel by viewModels {
        AddAttractionViewModel.factory(requireContext())
    }

    private val pickPhotos = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris -> onPicked(uris, viewModel.photoUris, binding.addAttractionPhotoCount, "фото") }

    private val pickVideos = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris -> onPicked(uris, viewModel.videoUris, binding.addAttractionVideoCount, "видео") }

    private val pickAudios = registerForActivityResult(
        ActivityResultContracts.GetMultipleContents()
    ) { uris -> onPicked(uris, viewModel.audioUris, binding.addAttractionAudioCount, "аудио") }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAttractionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListeners() = with(binding) {
        addAttractionUploadPhotoBtn.setOnClickListener { pickPhotos.launch("image/*") }
        addAttractionUploadVideoBtn.setOnClickListener { pickVideos.launch("video/*") }
        addAttractionUploadAudioBtn.setOnClickListener { pickAudios.launch("audio/*") }

        addAttractionAddCellBtn.setOnClickListener { addStationFromInputs() }
        addAttractionBtn.setOnClickListener { onSaveClick() }
    }

    private fun addStationFromInputs() = with(binding) {
        val name     = addAttractionStationNameEdit.text.toString().trim()
        val branch   = addAttractionStationBranchEdit.text.toString().trim()
        val distance = addAttractionStationDistanceEdit.text.toString().trim().toIntOrNull()

        if (name.isEmpty() || branch.isEmpty() || distance == null) {
            toast("Заполните все поля станции")
            return@with
        }

        viewModel.addStation(StationAttractionRequest(name, branch, distance))
        addAttractionStationNameEdit.text.clear()
        addAttractionStationBranchEdit.text.clear()
        addAttractionStationDistanceEdit.text.clear()
    }

    private fun onSaveClick() = with(binding) {
        val name = addAttractionNameEdit.text.toString().trim()
        if (name.isEmpty()) {
            toast("Введите название")
            return@with
        }
        viewModel.submit(
            name = name,
            description = addAttractionDescriptionEdit.text.toString().trim(),
            address = addAttractionAddressEdit.text.toString().trim(),
            workingHours = addAttractionWorkingHoursEdit.text.toString().trim(),
            phone = addAttractionPhoneEdit.text.toString().trim(),
            email = addAttractionEmailEdit.text.toString().trim(),
            website = addAttractionWebsiteEdit.text.toString().trim()
        )
    }

    private fun onPicked(
        picked: List<Uri>,
        target: MutableList<Uri>,
        countView: TextView,
        label: String
    ) {
        if (picked.isEmpty()) return
        target.clear()
        target.addAll(picked)
        countView.text = "Выбрано $label: ${picked.size}"
        countView.isVisible = true
    }

    private fun observeViewModel() {
        viewModel.stations.observe(viewLifecycleOwner) { renderStations(it) }

        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                AddAttractionViewModel.UiState.Idle -> setLoading(false)
                AddAttractionViewModel.UiState.Loading -> setLoading(true)
                is AddAttractionViewModel.UiState.Success -> {
                    setLoading(false)
                    toast("Сохранено, id=${state.id}")
                    // findNavController().popBackStack()
                }
                is AddAttractionViewModel.UiState.Error -> {
                    setLoading(false)
                    toast("Ошибка: ${state.message}")
                }
            }
        }
    }

    private fun renderStations(items: List<StationAttractionRequest>) {
        val container = binding.addedStationsContainer
        container.removeAllViews()
        val inflater = LayoutInflater.from(requireContext())
        items.forEachIndexed { index, item ->
            val rowBinding = ItemAddedStationBinding.inflate(inflater, container, false)
            rowBinding.addedStationText.text =
                "${item.stationName} · ${item.branch} · ${item.distance} м"
            rowBinding.addedStationRemove.setOnClickListener {
                viewModel.removeStation(index)
            }
            container.addView(rowBinding.root)
        }
    }

    private fun setLoading(loading: Boolean) = with(binding) {
        addAttractionBtn.isEnabled = !loading
        addAttractionAddCellBtn.isEnabled = !loading
        addAttractionUploadPhotoBtn.isEnabled = !loading
        addAttractionUploadVideoBtn.isEnabled = !loading
        addAttractionUploadAudioBtn.isEnabled = !loading
        addAttractionBtn.text = if (loading) "Сохранение..." else "Сохранить"
    }

    private fun toast(msg: String) =
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
}