package com.ssafy.intagral.ui.upload

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.ssafy.intagral.MainMenuActivity
import com.ssafy.intagral.R
import com.ssafy.intagral.databinding.FragmentUploadCompleteBinding
import com.ssafy.intagral.viewmodel.UploadViewModel

class UploadCompleteFragment : Fragment() {

    private lateinit var binding: FragmentUploadCompleteBinding

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
        binding = FragmentUploadCompleteBinding.inflate(inflater)
        uploadViewModel.getImageBitmap().value = null
        uploadViewModel.getDetectedClassList().value = null
        uploadViewModel.getTagMap().value = null

        binding.toMain.setOnClickListener(UploadCompleteButtonListener())
        return binding.root
    }

    inner class UploadCompleteButtonListener: OnClickListener{
        override fun onClick(p0: View?) {
            when(p0?.id){
                R.id.to_main -> {
                    (requireActivity() as MainMenuActivity).setHome()
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            UploadCompleteFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}