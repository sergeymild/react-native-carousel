package com.reactnativecarousel

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.facebook.react.uimanager.PixelUtil
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.views.view.ReactViewGroup

class HorizontalMarginItemDecoration(var horizontalMarginInDp: Int) : RecyclerView.ItemDecoration() {

  override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
    outRect.right = horizontalMarginInDp
    outRect.left = horizontalMarginInDp
  }

}

class CarouselView(context: Context?) : ReactViewGroup(context) {
  private val itemDecoration = HorizontalMarginItemDecoration(0)
  var nextItemVisiblePx = PixelUtil.toPixelFromDIP(0f)

  var currentItemHorizontalMarginPx = PixelUtil.toPixelFromDIP(0f)
    set(value) {
      field = value
      itemDecoration.horizontalMarginInDp = value.toInt()
    }

  override fun addView(child: View?, index: Int) {
    super.addView(child, index)

    if (child is FrameLayout) {
      val pager = child.getChildAt(0) as ViewPager2
      // Add a PageTransformer that translates the next and previous items horizontally
      // towards the center of the screen, which makes them visible
      pager.offscreenPageLimit = 1
      val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
      val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
        page.translationX = -pageTranslationX * position
      }
      pager.setPageTransformer(pageTransformer)
      pager.addItemDecoration(itemDecoration)
    }
  }
}
