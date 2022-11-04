package com.ssafy.intagral.ui.hashtagPreset

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.ssafy.intagral.R
import com.ssafy.intagral.data.PresetClassItem
import com.ssafy.intagral.databinding.FragmentPresetViewBinding
import com.ssafy.intagral.databinding.ItemPresetViewRecyclerBinding
import com.ssafy.intagral.viewmodel.PresetViewModel

class PresetViewFragment : Fragment() {

    private lateinit var binding: FragmentPresetViewBinding
    private val presetViewModel: PresetViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPresetViewBinding.inflate(inflater, container, false)

        binding.presetEditButton.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.menu_frame_layout,
                    PresetEditFragment.newInstance()
                ).commit()
        }

        // create  layoutManager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        // pass it to rvLists layoutManager
        binding.presetViewRecyclerList.layoutManager = layoutManager

        binding.presetViewRecyclerList.adapter = PresetViewAdapter(presetViewModel.getPresetList().value ?: listOf())

        presetViewModel.getPresetList().observe(
                viewLifecycleOwner
        ){
            it?.also {
                binding.presetViewRecyclerList.adapter = PresetViewAdapter(it)
            }
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PresetViewFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }


    inner class PresetViewAdapter(var presetClassItemList: List<PresetClassItem>): RecyclerView.Adapter<PresetViewAdapter.ViewHolder>(){
        inner class ViewHolder(val binding: ItemPresetViewRecyclerBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemPresetViewRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            with(holder){
                with(presetClassItemList[position]){
                    binding.className.text = this.className
                    for(tag in this.tagList){
                        val chip = layoutInflater.inflate(R.layout.view_preset_view_chip, binding.presetViewTagChipGroup, false) as Chip
                        chip.text = "#$tag"
                        binding.presetViewTagChipGroup.addView(chip)
                    }
                }
            }
        }

        override fun getItemCount(): Int {
            return presetClassItemList.size
        }

    }
}