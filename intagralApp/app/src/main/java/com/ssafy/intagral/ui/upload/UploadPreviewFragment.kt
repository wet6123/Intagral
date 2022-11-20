package com.ssafy.intagral.ui.upload

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.ssafy.intagral.R
import com.ssafy.intagral.databinding.FragmentUploadPreviewBinding
import com.ssafy.intagral.viewmodel.UploadViewModel
import com.ssafy.intagral.viewmodel.UserViewModel

class UploadPreviewFragment : Fragment() {

    private lateinit var binding: FragmentUploadPreviewBinding

    private val uploadViewModel: UploadViewModel by activityViewModels()
    private val uploadUser: UserViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }

        uploadViewModel.getuploadStep().value = UploadViewModel.UploadStep.PREVIEW
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

        if(selectedTagList.isNotEmpty()){
            val content = selectedTagList.map { "#${it}" }.reduce { acc, s -> "$acc $s" }
            binding.postDetail.postContent.text = content
        } else {
            binding.postDetail.postContent.visibility = View.GONE
        }

        uploadUser.getMyInfo().value?.let {
            binding.postDetail.include.profileSimpleNickname.text = it.nickname
            Glide.with(requireContext()).load(
                it.imgPath
            ).into(binding.postDetail.include.profileSimpleImg)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        uploadViewModel.getuploadStep().value = UploadViewModel.UploadStep.TAG_RESULT
    }

    inner class UploadPreviewButtonListener: View.OnClickListener {
        override fun onClick(p0: View?) {
            when(p0?.id){
                R.id.back_button -> {
                    requireActivity()
                        .supportFragmentManager
                        .popBackStack()
                }
                R.id.publish_button -> {
                    uploadViewModel.publishPost(requireContext().filesDir.path)
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