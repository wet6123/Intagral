package com.ssafy.intagral.ui.upload

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import com.ssafy.intagral.R
import com.ssafy.intagral.data.source.PresetRepository
import com.ssafy.intagral.databinding.FragmentResultTagListBinding
import com.ssafy.intagral.viewmodel.UploadViewModel

/**
 * TODO
 *  - presetRepository를 viewmodel로 빼기
 */
class ResultTagListFragment : Fragment() {

    private val presetRepository : PresetRepository = PresetRepository()

    private lateinit var binding: FragmentResultTagListBinding

    private val uploadViewModel: UploadViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.also {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentResultTagListBinding.inflate(layoutInflater)

        binding.addTagButton.setOnClickListener(ResultTagListButtonClickListener())
        binding.publishPreviewButton.setOnClickListener(ResultTagListButtonClickListener())
        binding.tagResultPublishButton.setOnClickListener(ResultTagListButtonClickListener())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uploadViewModel.getImageBitmap().value?.also {
            binding.resultTagImage.setImageBitmap(it)
        }

        uploadViewModel.getTagMap().observe(
            viewLifecycleOwner
        ){
            binding.resultTagChipGroup.removeAllViews()
            it?.also {
                for((tag, isSelected) in it){
                    val chip = layoutInflater.inflate(R.layout.view_tag_choice_chip, binding.resultTagChipGroup, false) as Chip
                    chip.text = (tag)
                    chip.isSelected = isSelected
                    chip.setOnCheckedChangeListener { compoundButton, b ->
                        val tagMap = uploadViewModel.getTagMap().value ?: HashMap()
                        tagMap[compoundButton.text.toString()] = b
                        uploadViewModel.getTagMap().value = tagMap
                    }
                    binding.resultTagChipGroup.addView(chip)
                }
            }
        }

        uploadViewModel.getDetectedClassList().value?.also {
            // TODO : repository viewmodel로 빼기
            val preset = presetRepository.getPresetList()
            val tagMap = uploadViewModel.getTagMap().value ?: HashMap<String, Boolean>()
            for(cls in it){
                if(cls != "default"){
                    tagMap[cls] = tagMap[cls] ?: false
                }
                preset.tagMap[cls]?.also {
                    tagMap.putAll(it.map { it to (tagMap[it]?: false) }.toMap())
                }
            }
            uploadViewModel.getTagMap().value = tagMap
        }
    }

    inner class ResultTagListButtonClickListener: OnClickListener{
        override fun onClick(p0: View?) {
            when(p0?.id){
                R.id.add_tag_button -> {
                    val regex = """\s""".toRegex()
                    val tag : String = regex.replace(binding.inputTagText.text.toString(), "")
                    if(tag == ""){
                        binding.inputTagText.setText("")
                        Toast.makeText(requireContext(), "태그를 입력해주세요.", Toast.LENGTH_SHORT).show()
                        return
                    }
                    val tagMap = uploadViewModel.getTagMap().value ?: HashMap()
                    if(!tagMap.contains(tag)){
                        tagMap[tag] = true
                        uploadViewModel.getTagMap().value = tagMap
                        Toast.makeText(requireContext(), "${tag} 태그가 추가되었습니다.", Toast.LENGTH_SHORT).show()
                        binding.inputTagText.setText("")
                    }else{
                        Toast.makeText(requireContext(), "이미 추가된 태그입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.publish_preview_button -> {
                    requireActivity()
                        .supportFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.menu_frame_layout,
                            UploadPreviewFragment.newInstance()
                        ).commit()
                }
                R.id.tag_result_publish_button -> {
                    requireActivity()
                        .supportFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.menu_frame_layout,
                            UploadCompleteFragment.newInstance()
                        ).commit()
                }
                else -> {

                }
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ResultTagListFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}