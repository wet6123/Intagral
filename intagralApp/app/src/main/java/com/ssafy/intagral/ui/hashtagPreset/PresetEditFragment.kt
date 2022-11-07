package com.ssafy.intagral.ui.hashtagPreset

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.ssafy.intagral.R
import com.ssafy.intagral.data.PresetClassItem
import com.ssafy.intagral.databinding.FragmentPresetEditBinding
import com.ssafy.intagral.databinding.ItemPresetEditRecyclerBinding
import com.ssafy.intagral.viewmodel.PresetViewModel

class PresetEditFragment : Fragment() {

    private lateinit var binding: FragmentPresetEditBinding
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

        binding.presetEditRecyclerList.adapter = PresetEditAdapter(presetViewModel.getPresetList().value ?: listOf())

        presetViewModel.getPresetList().observe(
            viewLifecycleOwner
        ){
            it?.also {
                binding.presetEditRecyclerList.adapter = PresetEditAdapter(it)
            }
        }

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

    inner class PresetEditAdapter(var presetClassItemList: List<PresetClassItem>): RecyclerView.Adapter<PresetEditAdapter.ViewHolder>(){
        inner class ViewHolder(val binding: ItemPresetEditRecyclerBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val binding = ItemPresetEditRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            with(holder){
                with(presetClassItemList[position]){
                    binding.className.text = this.className
                    for(tag in this.tagList){
                        val chip = layoutInflater.inflate(R.layout.view_preset_edit_chip, binding.presetEditTagChipGroup, false) as Chip
                        chip.text = tag
                        chip.setOnCloseIconClickListener{
                            it as Chip
                            presetViewModel.removeTag(this.className, it.text.toString())
                            Toast.makeText(requireContext(), "${it.text} remove!!", Toast.LENGTH_SHORT).show()
                        }
                        binding.presetEditTagChipGroup.addView(chip)
                    }
                    binding.tagAddButton.setOnClickListener {
                        val regex = """\s""".toRegex()
                        val tag : String = regex.replace(binding.tagInput.text.toString(), "")

                        if(!tag.equals("")){
                            presetViewModel.addTag(this.className, tag)
                            binding.tagInput.setText("")
                        }else{
                            Toast.makeText(requireContext(), "태그를 입력해주세요", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        override fun getItemCount(): Int {
            return presetClassItemList.size
        }

    }
}