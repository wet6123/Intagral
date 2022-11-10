package com.ssafy.intagral.ui.common.post

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.intagral.R
import com.ssafy.intagral.data.model.PostItem
import com.ssafy.intagral.databinding.FragmentPostListBinding
import com.ssafy.intagral.ui.home.HomeFragment
import com.ssafy.intagral.viewmodel.PostListViewModel

class PostListFragment: Fragment() {

    lateinit var binding: FragmentPostListBinding

    //post list
    private lateinit var postRecyclerView: RecyclerView
    private lateinit var postAdapter: PostAdapter
    private var postList = ArrayList<PostItem>()

    private val postListViewModel: PostListViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)

        binding = FragmentPostListBinding.inflate(inflater, container, false).apply {
            context?.also {
                postAdapter = PostAdapter(it, postList)
                postListRecyclerView.apply {
                    adapter = postAdapter
                    layoutManager = GridLayoutManager(it, 3)
                }
            }
        }

        postListViewModel.getPostList().observe(viewLifecycleOwner){
            it?.also{
                postList.addAll(it)
                postAdapter.notifyDataSetChanged()
            }
        }

        return binding.root
    }

    inner class PostAdapter(context: Context, val p: MutableList<PostItem>)
        : RecyclerView.Adapter<PostViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(R.layout.fragment_postitem, parent, false)

            return PostViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
            //한 행에 데이터를 넣어준다
            holder.apply {
                bindPost(this@PostListFragment.postList[position])
                itemView.setOnClickListener{
                    Toast.makeText(context, "post detail로 이동 $position", Toast.LENGTH_SHORT).show()
                }
            }

        }

        override fun getItemCount(): Int {
            return postList.size
        }
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindPost(post: PostItem) {
            //TODO: Glide Error 찾아보기
            Glide.with(itemView.context).load(post.imgPath).into(itemView.findViewById(R.id.dummyPostItem)) //R.id.~
        }
    }
}