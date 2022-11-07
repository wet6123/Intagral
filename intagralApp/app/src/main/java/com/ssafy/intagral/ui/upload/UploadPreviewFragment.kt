package com.ssafy.intagral.ui.upload

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.ssafy.intagral.R
import com.ssafy.intagral.databinding.FragmentUploadPreviewBinding
import com.ssafy.intagral.viewmodel.UploadViewModel

/**
 * TODO
 *  - 업로드 요청
 */
class UploadPreviewFragment : Fragment() {

    private lateinit var binding: FragmentUploadPreviewBinding

    private val uploadViewModel: UploadViewModel by activityViewModels()

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
        binding = FragmentUploadPreviewBinding.inflate(inflater)
        binding.backButton.setOnClickListener(UploadPreviewButtonListener())
        binding.publishButton.setOnClickListener(UploadPreviewButtonListener())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uploadViewModel.getImageBitmap().value?.also {
            binding.postDetail.postImage.setImageBitmap(it)
        }

        var selectedTagList: List<String> = listOf()
        uploadViewModel.getTagMap().value?.also{
            selectedTagList = it.filterValues {
                it
            }.keys.toList()
        }

        val content = selectedTagList.joinToString(
            " #",
            "#"
        )

        binding.postDetail.postContent.text = content
    }

    inner class UploadPreviewButtonListener: View.OnClickListener {
        override fun onClick(p0: View?) {
            when(p0?.id){
                R.id.back_button -> {
                    requireActivity()
                        .supportFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.menu_frame_layout,
                            ResultTagListFragment.newInstance()
                        ).commit()
                }
                R.id.publish_button -> {
                    requireActivity()
                        .supportFragmentManager
                        .beginTransaction()
                        .replace(
                            R.id.menu_frame_layout,
                            UploadCompleteFragment.newInstance()
                        ).commit()
                }
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            UploadPreviewFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}