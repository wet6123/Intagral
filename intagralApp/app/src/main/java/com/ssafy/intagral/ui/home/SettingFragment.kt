package com.ssafy.intagral.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ssafy.intagral.MainMenuActivity
import com.ssafy.intagral.R
import com.ssafy.intagral.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding

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
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        binding.termsOfServiceButton.setOnClickListener(LogoutClickListener())
        binding.logoutButton.setOnClickListener(LogoutClickListener())
        return binding.root
    }

    inner class LogoutClickListener : View.OnClickListener{
        override fun onClick(p0: View?) {
            when(p0?.id){
                R.id.terms_of_service_button -> {

                }
                R.id.logout_button -> {
                    val activity = requireActivity() as MainMenuActivity
                    activity.logout()
                }
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}