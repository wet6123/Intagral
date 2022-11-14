package com.ssafy.intagral.ui.common.post

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.ssafy.intagral.IntagralApplication
import com.ssafy.intagral.R
import com.ssafy.intagral.data.model.ProfileSimpleItem
import com.ssafy.intagral.data.model.ProfileType
import com.ssafy.intagral.databinding.ViewPostDetailBinding
import com.ssafy.intagral.viewmodel.PostDetailViewModel
import com.ssafy.intagral.viewmodel.PostListViewModel
import com.ssafy.intagral.viewmodel.ProfileDetailViewModel

class PostDetailFragment: Fragment() {

    private var paramPostId: Int? = null

    private lateinit var binding: ViewPostDetailBinding
    private val postDetailViewModel: PostDetailViewModel by activityViewModels()
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

        binding.include.profileSimpleFollowbtn.setOnClickListener {
            postDetailViewModel.toggleWriterFollow(paramPostId!!)
        }

        binding.buttonLike.setOnClickListener{
            postDetailViewModel.togglePostLike(paramPostId!!)
        }

        registerForContextMenu(binding.postDetailMenuButton)
        binding.postDetailMenuButton.setOnClickListener{
            it.showContextMenu()
        }
        
        // TODO : 유저페이지로 이동하는 클릭 이벤트리스너
        binding.include.profileSimpleImgAndName.setOnClickListener{
            profileDetailViewModel.changeProfileDetail(ProfileSimpleItem(
                ProfileType.user,
                postDetailViewModel.getPostDetail().value!!.writer,
                postDetailViewModel.getPostDetail().value!!.isFollow,
                postDetailViewModel.getPostDetail().value!!.imgPath
            ))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPostDetail.background =  requireContext().getDrawable(R.drawable.bg_gradation)
        binding.viewPostDetail.visibility = View.GONE
        postDetailViewModel.getPostDetail().observe(
            viewLifecycleOwner
        ){
            it?.let {
                Glide.with(requireContext())
                    .load(it.writerImgPath)
                    .into(binding.include.profileSimpleImg)
                binding.include.profileSimpleNickname.text = it.writer

                if(it.isFollow){
                    binding.include.profileSimpleFollowbtn.text = "UNFOLLOW"
                } else {
                    binding.include.profileSimpleFollowbtn.text = "FOLLOW"
                }

                Glide.with(requireContext())
                    .load(it.imgPath)
                    .into(binding.postImage)
                binding.postLikeCount.text = "${it.likeCount}"
                if(it.tags.isNotEmpty()){
                    binding.postContent.text = it.tags.map { "#${it}" }.reduce { acc, s -> "$acc $s" }
                }
                binding.buttonLike.isChecked = it.isLike


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
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.post_detail_menu, menu)

        // TODO : 내가 아닐때만 false
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
                        // TODO : delete 요청
                        postDetailViewModel.deletePost(paramPostId!!)
                        Toast.makeText(
                            requireContext(),
                            "$paramPostId 번 게시글 삭제",
                            Toast.LENGTH_SHORT
                        ).show()
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                    .setNegativeButton("아니요"
                    ) { _, _ -> }.create().show()
            }
            else -> {}
        }
        return super.onContextItemSelected(item)
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