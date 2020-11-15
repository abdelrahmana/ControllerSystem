package com.example.controllersystemapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_view_images_gallery.*

class ViewImagesGalleryActivity : AppCompatActivity() {

    var position = 0
    var listimage = ArrayList<String>()
    var adapter: OnePhotoAdapter? = null
    lateinit var gridLayoutManager : GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_images_gallery)

        gridLayoutManager = GridLayoutManager(applicationContext, 1, RecyclerView.HORIZONTAL, true)

        getIntents()

        initRecycler()


        back_arrow_img?.setOnClickListener(View.OnClickListener { //arrowPosition = review_position - 1;
            scrollPosition(gridLayoutManager.findFirstVisibleItemPosition() - 1)
        })


        next_arrow_img?.setOnClickListener(View.OnClickListener { // arrowPosition = review_position + 1;
            scrollPosition(gridLayoutManager.findFirstVisibleItemPosition() + 1)
        })


    }

    private fun initRecycler() {

        // val gridLayoutManager = GridLayoutManager(applicationContext, 1, RecyclerView.HORIZONTAL, false)
        adapter = OnePhotoAdapter(listimage)
        one_photo_recycler?.adapter = adapter

        one_photo_recycler?.setHasFixedSize(true)
        one_photo_recycler?.layoutManager = gridLayoutManager
        attachMySnapToRecycler(one_photo_recycler)
        one_photo_recycler?.scrollToPosition(position)


    }

    private fun getIntents() {

        val intent = intent
        if (intent != null) {
            listimage = getIntent().getStringArrayListExtra("imagesList")
            position = getIntent().getIntExtra("position", 0)

            Log.d("sizee" , "${listimage.size}")



        }
    }


    private fun attachMySnapToRecycler(recyclerView: RecyclerView) {
        val snapHelper: LinearSnapHelper = object : LinearSnapHelper() {
            override fun findTargetSnapPosition(layoutManager: RecyclerView.LayoutManager, velocityX: Int, velocityY: Int): Int {
                val centerView = findSnapView(layoutManager) ?: return RecyclerView.NO_POSITION
                val position = layoutManager.getPosition(centerView)
                var targetPosition = -1
                if (layoutManager.canScrollHorizontally()) {
                    targetPosition = if (velocityX < 0) {
                        position - 1
                    } else {
                        position + 1
                    }
                }
                if (layoutManager.canScrollVertically()) {
                    targetPosition = if (velocityY < 0) {
                        position - 1
                    } else {
                        position + 1
                    }
                }
                val firstItem = 0
                val lastItem = layoutManager.itemCount - 1
                targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem))
                return targetPosition
            }
        }
        snapHelper.attachToRecyclerView(recyclerView)
    }


    private fun scrollPosition(arrowPosition: Int) {
        Log.d("position ", "" + arrowPosition)
        if (arrowPosition <= 0) {
            one_photo_recycler?.scrollToPosition(0)
        }
        if (arrowPosition >= listimage.size - 1) {
            one_photo_recycler?.scrollToPosition(listimage.size - 1)
        } else {
            one_photo_recycler?.scrollToPosition(arrowPosition)
        }
    }


}