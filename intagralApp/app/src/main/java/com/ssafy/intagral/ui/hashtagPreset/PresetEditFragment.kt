package com.ssafy.intagral.ui.hashtagPreset

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.ssafy.intagral.R
import com.ssafy.intagral.data.PresetItem
import com.ssafy.intagral.data.source.PresetRepository
import com.ssafy.intagral.databinding.FragmentPresetEditBinding
import com.ssafy.intagral.databinding.ItemPresetEditRecyclerBinding
import com.ssafy.intagral.databinding.ItemPresetViewRecyclerBinding
import com.ssafy.intagral.ui.upload.ResultTagListFragment

/**
 * TODO
 *  - presetRepository viewmodel로 만들기
 *  - 삭제 버튼
 *  - 추가 버튼
 */
class PresetEditFragment : Fragment() {

    // TODO : viewmodel로 만들기
    private val presetRepository : PresetRepository = PresetRepository()

    private lateinit var binding: FragmentPresetEditBinding

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
        binding = FragmentPresetEditBinding.inflate(inflater, container, false)

        binding.presetCompleteButton.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.menu_frame_layout,
                    PresetViewFragment.newInstance()
                ).commit()
        }

        // create  layoutManager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())

        // pass it to rvLists layoutManager
        binding.presetEditRecyclerList.layoutManager = layoutManager

        // TODO : viewmodel 작성하기
        val presetItemList = presetRepository.getPreset().tagMap.map {
            PresetItem(it.key, it.value)
        }

        binding.presetEditRecyclerList.adapter = PresetEditAdapter(presetItemList)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PresetEditFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    inner class PresetEditAdapter(var presetItemList: List<PresetItem>): RecyclerView.Adapter<PresetEditAdapter.ViewHolder>(){
        inner class ViewHolder(val binding: ItemPresetEditRecyclerBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemPresetEditRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            with(holder){
                with(presetItemList[position]){
                    binding.className.text = this.className
                    for(tag in this.tagList){
                        val chip = layoutInflater.inflate(R.layout.view_preset_edit_chip, binding.presetEditTagChipGroup, false) as Chip
                        chip.text = tag
                        binding.presetEditTagChipGroup.addView(chip)
                    }
                }
            }
        }

        override fun getItemCount(): Int {
            return presetItemList.size
        }

    }
}