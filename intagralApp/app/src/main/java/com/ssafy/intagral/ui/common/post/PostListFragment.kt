package com.ssafy.intagral.ui.common.post

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.squareup.picasso.Picasso
import com.ssafy.intagral.R
import com.ssafy.intagral.data.model.PostItem
import com.ssafy.intagral.databinding.FragmentPostListBinding
import com.ssafy.intagral.viewmodel.PostListViewModel

class PostListFragment: Fragment() {

    lateinit var binding: FragmentPostListBinding

    //post list
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
                    layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                        gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
                    }
                    addOnScrollListener(object : RecyclerView.OnScrollListener() {
                        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                            super.onScrolled(recyclerView, dx, dy)
                            if (!canScrollVertically(1) && postListViewModel.getPageInfo().isNext) {
                                postListViewModel.fetchPostList()
                            }
                        }
                    })

                    setItemViewCacheSize(300)
                    itemAnimator = null
                }
            }
        }
        postAdapter.notifyDataSetChanged()

        postListViewModel.getPostList().observe(viewLifecycleOwner){
            it?.also{
                if(postListViewModel.getPageInfo().state == PostListViewModel.StateInfo.INIT){ return@also }
                var start = postList.size
                var num = 0

                for(postItem in it){
                    if(postList.find {
                        it.postId == postItem.postId
                    } == null){
                        postList.add(postItem)
                        num++
                    }
                }
                postAdapter.notifyItemRangeInserted(start, num)
            }
        }

        postListViewModel.getDeletedPostId().observe(
            viewLifecycleOwner
        ){ deletedPostId ->
            var deletedItem = postList.find {
                it.postId == deletedPostId
            }
            if(deletedItem != null){
                var deletedIndex = postList.indexOf(deletedItem)
                postList.removeAt(deletedIndex)
                postAdapter.notifyItemRemoved(deletedIndex)
                postAdapter.notifyItemRangeChanged(deletedIndex, postList.size)
                postListViewModel.reloadRecentPage()
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
                    requireActivity()
                        .supportFragmentManager
                        .beginTransaction()
                        .addToBackStack(null)
                        .add(
                            R.id.menu_frame_layout,
                            PostDetailFragment.newInstance(postList[position].postId)
                        ).commit()
                }

            }

        }

        override fun getItemCount(): Int {
            return postList.size
        }
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindPost(post: PostItem) {
            Picasso.get().load(post.imgPath).placeholder(R.drawable.ic_transeparent_background).into(itemView.findViewById(R.id.dummyPostItem) as ImageView)

        }
    }
}