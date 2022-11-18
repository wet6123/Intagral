package com.ssafy.intagral.ui.common.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.ssafy.intagral.IntagralApplication
import com.ssafy.intagral.R
import com.ssafy.intagral.data.model.ProfileSimpleItem
import com.ssafy.intagral.databinding.FragmentUserProfileBinding
import com.ssafy.intagral.util.ImageUtil
import com.ssafy.intagral.viewmodel.ProfileDetailViewModel
import com.ssafy.intagral.viewmodel.ProfileSimpleViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
class UserProfileDetailFragment: Fragment() {

    private lateinit var binding: FragmentUserProfileBinding

    private val profileSimpleViewModel: ProfileSimpleViewModel by activityViewModels()
    private val profileDetailViewModel: ProfileDetailViewModel by activityViewModels()

    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraLauncher : ActivityResultLauncher<Intent>

    private var newPhotoFile: File? = null
    private lateinit var newPhotoBitmap: Bitmap


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false).apply {
            if (profileDetailViewModel.getProfileDetail().value?.name == IntagralApplication.prefs.nickname) {
                profileDetailBtn.apply {
                    text = "프로필 수정"
                    setOnClickListener {
                        if (this.text == "프로필 수정") {
                            profileDetailViewModel.getEditStatus().value = ProfileDetailViewModel.EditStatus.ACTIVE
                        } else if (this.text == "수정 완료") {
                            val regex = """\s""".toRegex()
                            var name = inputNickname.editText!!.text.toString()
                            name = regex.replace(name, "")
                            val intro = inputIntro.editText!!.text.toString().trim()

                            if (name != "") {
                                profileDetailViewModel.editProfileData(name, intro)
                            }
                        }
                    }
                }
                registerForContextMenu(userProfileImg)
                userProfileImg.setOnClickListener {
                    it.showContextMenu()
                }
            } else {
                profileDetailBtn.apply {
                    text = if (profileDetailViewModel.getProfileDetail().value?.isFollow ?:false) "UNFOLLOW" else "FOLLOW"
                    setOnClickListener {
                        CoroutineScope(Main).launch {
                            profileDetailViewModel.getProfileDetail().value?.let {
                                var toggleResult = profileSimpleViewModel.toggleFollow(
                                    ProfileSimpleItem(
                                        it.type,
                                        it.name,
                                        it.isFollow,
                                        it.profileImg
                                    )
                                )
                                if(toggleResult){
                                    text = "Unfollow"
                                    binding.followerCnt.text = (Integer.parseInt(binding.followerCnt.text.toString()) + 1).toString()
                                }else{
                                    text = "Follow"
                                    binding.followerCnt.text = (Integer.parseInt(binding.followerCnt.text.toString()) - 1).toString()
                                }
                            }

                        }
                    }
                }
            }
            context?.let {
                Glide.with(it).load(
                    profileDetailViewModel.getProfileDetail().value?.profileImg
                        ?: "https://intagral-file-upload-bucket.s3.ap-northeast-2.amazonaws.com/car-967387__480.webp"
                ).into(this.userProfileImg)
                //TODO: follow 목록 페이지로 이동하는 리스너 등록
            }

            followerCnt.text =
                profileDetailViewModel.getProfileDetail().value?.follower?.toString() ?: "0"
            followingCnt.text =
                profileDetailViewModel.getProfileDetail().value?.following?.toString() ?: "0"
            hashtagCnt.text =
                profileDetailViewModel.getProfileDetail().value?.hashtag?.toString() ?: "0"

            followerBtn.apply {
                setOnClickListener {
                    profileDetailViewModel.getProfileDetail().value?.name.let {
                        // observe from MainMenuActivity
                        // TODO: create and manage status ex. init, create, ..
                        profileSimpleViewModel.getOnesFollowList(
                            "follower",
                            profileDetailViewModel.getProfileDetail().value!!.name
                        )
                    }
                }
            }

            followingBtn.apply {
                setOnClickListener {
                    profileDetailViewModel.getProfileDetail().value?.name.let {
                        profileSimpleViewModel.getOnesFollowList(
                            "following",
                            profileDetailViewModel.getProfileDetail().value!!.name
                        )
                    }
                }
            }

            hashtagBtn.apply {
                setOnClickListener {
                    profileDetailViewModel.getProfileDetail().value?.name.let {
                        profileSimpleViewModel.getOnesFollowList(
                            "hashtag",
                            profileDetailViewModel.getProfileDetail().value!!.name
                        )
                    }
                }
            }

            profileDetailNickname.text = profileDetailViewModel.getProfileDetail().value?.name
            profileDetailIntro.text = profileDetailViewModel.getProfileDetail().value?.intro

            profileDetailViewModel.getEditStatus().observe(
                viewLifecycleOwner
            ) {
                when (it) {
                    ProfileDetailViewModel.EditStatus.DUPLICATED_NAME -> {
                        val textLayout = inputNickname
                        textLayout.error = null
                        textLayout.isErrorEnabled = false
                        textLayout.error = "닉네임 중복"
                    }
                    ProfileDetailViewModel.EditStatus.INAVTIVE -> {
                        profileDetailBtn.text = "프로필 수정"
                        profileInfoViewContainer.visibility = View.VISIBLE
                        profileInfoEditContainer.visibility = View.GONE
                    }
                    ProfileDetailViewModel.EditStatus.ERROR -> {
                        profileDetailViewModel.getEditStatus().value =
                            ProfileDetailViewModel.EditStatus.INAVTIVE
                    }
                    ProfileDetailViewModel.EditStatus.ACTIVE -> {
                        profileDetailBtn.text = "수정 완료"
                        profileInfoViewContainer.visibility = View.GONE
                        profileInfoEditContainer.visibility = View.VISIBLE

                        inputNickname.editText!!.setText(
                            profileDetailViewModel.getProfileDetail().value?.name ?: ""
                        )
                        inputIntro.editText!!.setText(
                            profileDetailViewModel.getProfileDetail().value?.intro ?: ""
                        )
                    }
                    else -> {

                    }
                }
            }
        }

        cameraLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                newPhotoFile?.also {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        val source = ImageDecoder.createSource(requireActivity().contentResolver, Uri.fromFile(it))
                        ImageDecoder.decodeBitmap(source)?.let {
                            newPhotoBitmap = ImageUtil.resizeBitmap(it, 900f, 0f)
                        }
                    } else {
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, Uri.fromFile(it))?.let {
                            newPhotoBitmap = ImageUtil.resizeBitmap(it, 900f, 0f)
                        }
                    }
                    profileDetailViewModel.editProfileImage(requireContext().filesDir.path, newPhotoBitmap)
                }
            }
        }
        galleryLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            if(it.resultCode == Activity.RESULT_OK){
                val photoUri : Uri? = it.data?.data
                photoUri?.also{
                    if(Build.VERSION.SDK_INT < 28) {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            requireActivity().contentResolver,
                            photoUri
                        )
                        newPhotoBitmap = bitmap
                    } else {
                        val source = ImageDecoder.createSource(requireActivity().contentResolver, photoUri)
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        newPhotoBitmap = bitmap
                    }
                    profileDetailViewModel.editProfileImage(requireContext().filesDir.path, newPhotoBitmap)
                }
            }
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        profileDetailViewModel.getEditStatus().value = null
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        activity?.menuInflater?.inflate(R.menu.photo_picker_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.photo_picker_capture -> {
                capture()
            }
            R.id.photo_picker_album -> {
                getImageFromGallery()
            }
            else -> {

            }
        }
        return super.onContextItemSelected(item)
    }

    // 카메라 실행
    private fun capture(){
        context?.also{
            val file = ImageUtil.createImageFile(it)
            newPhotoFile = file
            val photoUri : Uri = FileProvider.getUriForFile(
                it,
                "com.ssafy.intagral.provider",
                file
            )
            val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            cameraLauncher.launch(intent)
        }
    }

    // 갤러리 실행
    private fun getImageFromGallery(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
    }
}