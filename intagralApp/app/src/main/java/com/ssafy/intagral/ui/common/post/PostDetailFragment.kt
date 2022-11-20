package com.ssafy.intagral.ui.common.post

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.ssafy.intagral.IntagralApplication
import com.ssafy.intagral.R
import com.ssafy.intagral.data.model.ProfileSimpleItem
import com.ssafy.intagral.data.model.ProfileType
import com.ssafy.intagral.databinding.ViewPostDetailBinding
import com.ssafy.intagral.viewmodel.PostDeleteViewModel
import com.ssafy.intagral.viewmodel.PostDetailViewModel
import com.ssafy.intagral.viewmodel.ProfileDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

@AndroidEntryPoint
class PostDetailFragment: Fragment() {

    private var paramPostId: Int? = null

    private lateinit var binding: ViewPostDetailBinding
    private val postDetailViewModel: PostDetailViewModel by viewModels()
    private val postDeleteViewModel: PostDeleteViewModel by activityViewModels()
    private val profileDetailViewModel: ProfileDetailViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramPostId = it.getInt(PARAM_NAME)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ViewPostDetailBinding.inflate(inflater, container, false)

        binding.include.buttonLike.setOnClickListener{
            postDetailViewModel.togglePostLike(paramPostId!!)
        }

        registerForContextMenu(binding.include.postDetailMenuButton)
        binding.include.postDetailMenuButton.setOnClickListener{
            it.showContextMenu()
        }
        
        binding.include.profileSimpleImgAndName.setOnClickListener{
            profileDetailViewModel.changeProfileDetail(ProfileSimpleItem(
                ProfileType.user,
                postDetailViewModel.getPostDetail().value!!.writer,
                postDetailViewModel.getPostDetail().value!!.isFollow,
                postDetailViewModel.getPostDetail().value!!.imgPath
            ))
        }

        childFragmentManager.beginTransaction().replace(R.id.post_list_under_post_detail, PostListFragment.newInstance("recommend", paramPostId.toString())).commit()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPostDetail.visibility = View.GONE
        postDetailViewModel.getPostDetail().observe(
            viewLifecycleOwner
        ){
            it?.let {
                Glide.with(requireContext())
                    .load(it.writerImgPath)
                    .into(binding.include.profileSimpleImg)
                binding.include.profileSimpleNickname.text = it.writer


                Glide.with(requireContext())
                    .load(it.imgPath)
                    .into(binding.postImage)
                binding.include.postLikeCount.text = "${it.likeCount}"
                if(it.tags.isNotEmpty()){
                    binding.postContent.text = it.tags.map { "#${it}" }.reduce { acc, s -> "$acc $s" }
                    binding.postContent.visibility = View.VISIBLE
                }else{
                    binding.postContent.visibility = View.GONE
                }
                binding.include.buttonLike.isChecked = it.isLike


                binding.viewPostDetail.visibility = View.VISIBLE
            }
        }

        postDetailViewModel.fetchPostDetail(paramPostId!!)

    }

    override fun onDestroy() {
        super.onDestroy()
        postDetailViewModel.getPostDetail().value = null
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        requireActivity().menuInflater.inflate(R.menu.post_detail_menu, menu)

        menu.findItem(R.id.post_menu_item_delete).isVisible = postDetailViewModel.getPostDetail().value!!.writer.equals(IntagralApplication.prefs.nickname)
        menu.findItem(R.id.post_menu_item_report).isVisible = !postDetailViewModel.getPostDetail().value!!.writer.equals(IntagralApplication.prefs.nickname)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.post_menu_item_report -> {
                AlertDialog.Builder(requireContext())
                    .setTitle("신고하기")
                    .setMessage("신고하시겠습니까?")
                    .setPositiveButton("네"
                    ) { _, _ ->
                        val address = arrayOf("intagralofficial@gmail.com")
                        val email = Intent(Intent.ACTION_SEND)
                        email.type = "plain/text"
                        email.putExtra(Intent.EXTRA_EMAIL, address)
                        email.putExtra(Intent.EXTRA_SUBJECT, "INTAGRAL REPORT : POST NUMBER $paramPostId")
                        email.putExtra(Intent.EXTRA_TEXT, "${paramPostId}번 게시글 신고\n내용 : ")
                        startActivity(email)
                    }
                    .setNegativeButton("아니요"
                    ) { _, _ -> }.create().show()
            }
            R.id.post_menu_item_delete -> {
                AlertDialog.Builder(requireContext())
                    .setTitle("삭제하기")
                    .setMessage("정말로 삭제하시겠습니까?")
                    .setPositiveButton("네"
                    ) { _, _ ->
                        CoroutineScope(Main).launch{
                            var result = postDetailViewModel.deletePost(paramPostId!!)
                            if(result != -1){
                                postDeleteViewModel.getDeletedPostId().value = paramPostId
                            }
                            requireActivity().supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        }
                    }
                    .setNegativeButton("아니요"
                    ) { _, _ -> }.create().show()
            }
            else -> {}
        }
        return true
    }

    companion object {
        private const val PARAM_NAME = "paramPostId"
        @JvmStatic
        fun newInstance(postId: Int) =
            PostDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(PARAM_NAME, postId)
                }
            }
    }
}