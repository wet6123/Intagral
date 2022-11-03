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
import com.ssafy.intagral.databinding.FragmentPresetViewBinding
import com.ssafy.intagral.databinding.ItemPresetViewRecyclerBinding

/**
 * TODO
 *  - presetRepository를 viewmodel로 빼기
 *  - floating 메뉴? 편집 버튼 만들기
 */
class PresetViewFragment : Fragment() {

    private val presetRepository : PresetRepository = PresetRepository()

    private lateinit var binding: FragmentPresetViewBinding

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

        // create  layoutManager
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(requireContext())
        // pass it to rvLists layoutManager
        binding.presetViewRecyclerList.layoutManager = layoutManager

        val presetItemList = presetRepository.getPreset().tagMap.map {
            PresetItem(it.key, it.value)
        }
        binding.presetViewRecyclerList.adapter = PresetViewAdapter(presetItemList)

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


    inner class PresetViewAdapter(var presetItemList: List<PresetItem>): RecyclerView.Adapter<PresetViewAdapter.ViewHolder>(){
        inner class ViewHolder(val binding: ItemPresetViewRecyclerBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemPresetViewRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            with(holder){
                with(presetItemList[position]){
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
            return presetItemList.size
        }

    }
}