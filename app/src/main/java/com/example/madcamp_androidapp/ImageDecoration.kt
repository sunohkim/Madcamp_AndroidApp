package com.example.madcamp_androidapp

import android.content.Context
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ImageDecoration(private val divHeight: Int, private val spanCount: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val count = state.itemCount

        /*if (position < spanCount) {
            outRect.top = divHeight
        } else if (position == count-1) {
            outRect.bottom = divHeight
        } else {
            outRect.top = divHeight
            outRect.bottom = divHeight
        }*/
        if (position < spanCount) {
            outRect.top = divHeight
        }
        /*if ((position)/spanCount == count/spanCount) {
            outRect.bottom = 300
        }*/
        if (position == count-1) {
            //outRect.bottom = 400
        }
        else {
            outRect.bottom = divHeight
        }
        if (position % spanCount == 0) {
            outRect.right = divHeight
        }
        else if (position % spanCount == spanCount-1) {
            outRect.left = divHeight
        }
    }
}