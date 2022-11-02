package com.ssafy.intagral.ui.upload

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.chip.Chip
import com.ssafy.intagral.R
import com.ssafy.intagral.data.source.PresetRepository
import com.ssafy.intagral.databinding.FragmentResultTagListBinding
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ResultTagListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultTagListFragment : Fragment() {

    private val presetRepository : PresetRepository = PresetRepository()

    private lateinit var binding: FragmentResultTagListBinding

    private var detectedClassList: ArrayList<String>? = null
    private var detectedImageBitmap: Bitmap? = null
    private var resultTagSet: HashSet<String> = HashSet()
    private var selectedTagList: List<String> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.also {
            detectedClassList = it.getStringArrayList(ARG_PARAM1)
            detectedImageBitmap = it.getParcelable(ARG_PARAM2)
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

        binding.resultTagChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val tagList = arrayListOf<String>()
            for(id in checkedIds){
                val chip : Chip = requireActivity().findViewById<Chip?>(id)
                tagList.add(chip.text.toString())
            }
            selectedTagList = tagList.toList()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        detectedImageBitmap?.also {
            binding.resultTagImage.setImageBitmap(it)
        }

        detectedClassList?.also {
            val preset = presetRepository.getPresetList()
            for(cls in it){
                if(cls != "default"){
                    resultTagSet.add(cls)
                }
                preset.tagMap[cls]?.also {
                    resultTagSet.addAll(it.toList())
                }
            }
        }

        for(tag in resultTagSet.toList()){
            val chip = layoutInflater.inflate(R.layout.view_tag_choice_chip, binding.resultTagChipGroup, false) as Chip
            chip.text = (tag)
            binding.resultTagChipGroup.addView(chip)
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
                    if(!resultTagSet.contains(tag)){
                        val chip = layoutInflater.inflate(R.layout.view_tag_choice_chip, binding.resultTagChipGroup, false) as Chip
                        chip.text = (tag)
                        binding.resultTagChipGroup.addView(chip)
                        binding.resultTagChipGroup.check(chip.id)
                        resultTagSet.add(tag)
                        Toast.makeText(requireContext(), "${tag} 태그가 추가되었습니다.", Toast.LENGTH_SHORT).show()
                        binding.inputTagText.setText("")
                    }else{
                        Toast.makeText(requireContext(), "이미 추가된 태그입니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.publish_preview_button -> {
//                    Toast.makeText(requireContext(), selectedTagList.toString(), Toast.LENGTH_SHORT).show()
                    requireActivity()
                        .supportFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.menu_frame_layout,
                            UploadPreviewFragment.newInstance()
                        ).commit()
                }
                R.id.tag_result_publish_button -> {
                    Toast.makeText(requireContext(), selectedTagList.toString(), Toast.LENGTH_SHORT).show()
                }
                else -> {

                }
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ResultTagListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: ArrayList<String> = arrayListOf("default"), param2: BitmapDrawable) =
            ResultTagListFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(ARG_PARAM1, param1)
                    putParcelable(ARG_PARAM2, param2.bitmap)
                }
            }
    }
}