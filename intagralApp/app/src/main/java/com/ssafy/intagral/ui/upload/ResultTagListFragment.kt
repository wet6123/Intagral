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
import com.ssafy.intagral.databinding.FragmentResultTagListBinding
import com.ssafy.intagral.viewmodel.PresetViewModel
import com.ssafy.intagral.viewmodel.UploadViewModel

class ResultTagListFragment : Fragment() {

    private lateinit var binding: FragmentResultTagListBinding

    private val uploadViewModel: UploadViewModel by activityViewModels()
    private val presetViewModel: PresetViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.also {
        }

        uploadViewModel.getuploadStep().value = UploadViewModel.UploadStep.TAG_RESULT
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
            it?.also {
                binding.resultTagChipGroup.removeAllViews()
                for((tag, isSelected) in it){
                    val chip = layoutInflater.inflate(R.layout.view_tag_choice_chip, binding.resultTagChipGroup, false) as Chip
                    chip.text = (tag)
                    chip.isCheckable = true
                    chip.isChecked = isSelected
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
            val tagMap = uploadViewModel.getTagMap().value ?: HashMap()
            val presetItemList = presetViewModel.getPresetList().value
            for(cls in it){
                val presetItem = presetItemList?.find {
                    it.className == cls
                }
                
                presetItem?.let {
                    if(cls != "default"){
                        val isChecked = tagMap[it.classNameKor]
                        tagMap[it.classNameKor] = isChecked ?: false
                    }
                    tagMap.putAll(it.tagList.map { it to (tagMap[it]?: false) }.toMap())
                }
            }
            uploadViewModel.getTagMap().value = tagMap
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        uploadViewModel.getuploadStep().value = UploadViewModel.UploadStep.PHOTO_PICKER
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
                        .addToBackStack(null)
                        .add(
                            R.id.menu_frame_layout,
                            UploadPreviewFragment.newInstance()
                        ).commit()
                }
                R.id.tag_result_publish_button -> {
                    uploadViewModel.publishPost(requireContext().filesDir.path)
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