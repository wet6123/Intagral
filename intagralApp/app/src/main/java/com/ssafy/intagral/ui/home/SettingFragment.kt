package com.ssafy.intagral.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ssafy.intagral.IntagralApplication
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

        binding.termsOfServiceButton.setOnClickListener(SettingClickListener())
        binding.logoutButton.setOnClickListener(SettingClickListener())
        binding.questionButton.setOnClickListener(SettingClickListener())
        return binding.root
    }

    inner class SettingClickListener : View.OnClickListener{
        override fun onClick(p0: View?) {
            when(p0?.id){
                R.id.terms_of_service_button -> {
                    val browserIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://k7a304.p.ssafy.io/intagral_policy.html")
                    )
                    startActivity(browserIntent)
                }
                R.id.question_button -> {
                    val address = arrayOf("intagralofficial@gmail.com")
                    val email = Intent(Intent.ACTION_SEND)
                    email.type = "plain/text"
                    email.putExtra(Intent.EXTRA_EMAIL, address)
                    email.putExtra(Intent.EXTRA_SUBJECT, "${IntagralApplication.prefs.nickname} 님의 문의")
                    email.putExtra(Intent.EXTRA_TEXT, "문의 내용 : ")
                    startActivity(email)
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