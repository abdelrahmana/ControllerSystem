package com.smartangle.util

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView

abstract class EndlessRecyclerOnScrollListener : RecyclerView.OnScrollListener() {
    private var previousTotal =
        0 // The total number of items in the dataset after the last load
    private var loading =
        true // True if we are still waiting for the last set of data to load.

    //    private int visibleThreshold = 1; // The minimum amount of items to have below your current scroll position before loading more.
    private val visibleThreshold =
        1 // The minimum amount of items to have below your current scroll position before loading more.
    var firstVisibleItem = 0
    var visibleItemCount = 0
    var totalItemCount = 0
    var currentVisiableItem = 0
    var findLastCompeletlyVisiblePosition = 0
    var currentPage = 0 // lets make it public to access it anywhere
    private var isLastItemDisplaying = false
    var mRecyclerViewHelper: RecyclerViewPositionHelper? = null
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        mRecyclerViewHelper = RecyclerViewPositionHelper(recyclerView)
        findLastCompeletlyVisiblePosition =
            mRecyclerViewHelper!!.findLastCompletelyVisibleItemPosition()
        visibleItemCount = recyclerView.childCount
        totalItemCount = mRecyclerViewHelper!!.itemCount
        firstVisibleItem = mRecyclerViewHelper!!.findFirstVisibleItemPosition()
        currentVisiableItem = mRecyclerViewHelper!!.findLastVisibleItemPosition()
        isLastItemDisplaying = mRecyclerViewHelper!!.isLastItemDisplaying(recyclerView)
        positionChange(currentVisiableItem)
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
            // End has been reached
            // Do something
            currentPage++
            onLoadMore(currentPage, currentVisiableItem)
            loading = true
        }
    }

    //Start loading
    abstract fun onLoadMore(currentPage: Int, visibleItemPosition: Int)
    abstract fun positionChange(lastVisibleItem: Int)
    inner class RecyclerViewPositionHelper internal constructor(val recyclerView: RecyclerView) {
        val layoutManager: RecyclerView.LayoutManager?

        /**
         * Returns the adapter item count.
         *
         * @return The total number on items in a layout manager
         */
        val itemCount: Int
            get() = layoutManager?.itemCount ?: 0

        /**
         * Returns the adapter position of the first visible view. This position does not include
         * adapter changes that were dispatched after the last layout pass.
         *
         * @return The adapter position of the first visible item or [RecyclerView.NO_POSITION] if
         * there aren't any visible items.
         */
        fun findFirstVisibleItemPosition(): Int {
            val child =
                findOneVisibleChild(0, layoutManager!!.childCount, false, true)
            return if (child == null) RecyclerView.NO_POSITION else recyclerView.getChildAdapterPosition(
                child
            )
        }

        /**
         * Returns the adapter position of the first fully visible view. This position does not include
         * adapter changes that were dispatched after the last layout pass.
         *
         * @return The adapter position of the first fully visible item or
         * [RecyclerView.NO_POSITION] if there aren't any visible items.
         */
        fun findFirstCompletelyVisibleItemPosition(): Int {
            val child =
                findOneVisibleChild(0, layoutManager!!.childCount, true, false)
            return if (child == null) RecyclerView.NO_POSITION else recyclerView.getChildAdapterPosition(
                child
            )
        }

        /**
         * Returns the adapter position of the last visible view. This position does not include
         * adapter changes that were dispatched after the last layout pass.
         *
         * @return The adapter position of the last visible view or [RecyclerView.NO_POSITION] if
         * there aren't any visible items
         */
        fun findLastVisibleItemPosition(): Int {
            val child =
                findOneVisibleChild(layoutManager!!.childCount - 1, -1, false, true)
            return if (child == null) RecyclerView.NO_POSITION else recyclerView.getChildAdapterPosition(
                child
            )
        }

        fun isLastItemDisplaying(recyclerView: RecyclerView): Boolean {
            if (recyclerView.adapter!!.itemCount != 0) {
                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.adapter!!
                        .getItemCount() - 1
                ) return true
            }
            return false
        }

        /**
         * Returns the adapter position of the last fully visible view. This position does not include
         * adapter changes that were dispatched after the last layout pass.
         *
         * @return The adapter position of the last fully visible view or
         * [RecyclerView.NO_POSITION] if there aren't any visible items.
         */
        fun findLastCompletelyVisibleItemPosition(): Int {
            val child =
                findOneVisibleChild(layoutManager!!.childCount - 1, -1, true, false)
            return if (child == null) RecyclerView.NO_POSITION else recyclerView.getChildAdapterPosition(
                child
            )
        }

        fun findOneVisibleChild(
            fromIndex: Int, toIndex: Int, completelyVisible: Boolean,
            acceptPartiallyVisible: Boolean
        ): View? {
            val helper: OrientationHelper
            helper = if (layoutManager!!.canScrollVertically()) {
                OrientationHelper.createVerticalHelper(layoutManager)
            } else {
                OrientationHelper.createHorizontalHelper(layoutManager)
            }
            val start = helper.startAfterPadding
            val end = helper.endAfterPadding
            val next = if (toIndex > fromIndex) 1 else -1
            var partiallyVisible: View? = null
            var i = fromIndex
            while (i != toIndex) {
                val child = layoutManager.getChildAt(i)
                val childStart = helper.getDecoratedStart(child)
                val childEnd = helper.getDecoratedEnd(child)
                if (childStart < end && childEnd > start) {
                    if (completelyVisible) {
                        if (childStart >= start && childEnd <= end) {
                            return child
                        } else if (acceptPartiallyVisible && partiallyVisible == null) {
                            partiallyVisible = child
                        }
                    } else {
                        return child
                    }
                }
                i += next
            }
            return partiallyVisible
        }

        init {
            layoutManager = recyclerView.layoutManager
        }
    }

    companion object {
        var TAG = "EndlessScrollListener"
    }
}