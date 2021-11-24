package com.reactnativecarousel

import android.content.Context
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.PixelUtil
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.views.view.ReactViewGroup
import com.facebook.react.uimanager.annotations.ReactProp

class CarouselPageWrapperManager : ViewGroupManager<CarouselPageWrapper>() {
  override fun getName() = "CarouselPageWrapper"

  override fun createViewInstance(reactContext: ThemedReactContext): CarouselPageWrapper {
    return CarouselPageWrapper(reactContext)
  }

  @ReactProp(name = "pageMaxWidth")
  fun pageMaxWidth(view: CarouselPageWrapper, value: Float) {
    view.pageMaxWidth = PixelUtil.toPixelFromDIP(value)
  }
}

class CarouselPageWrapper(context: Context?) : ReactViewGroup(context) {
  var pageMaxWidth: Float? = null

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    if (childCount == 0) return
    val tag = getChildAt(0).id
    (context as ReactContext).catalystInstance.reactQueueConfiguration.nativeModulesQueueThread.runOnQueue {
      var width = measuredWidth
      pageMaxWidth?.let {
        if (it < width) width = it.toInt()
      }
      ReactUpdateHelper.getUiManager(context as ReactContext).updateNodeSize(tag, width, measuredHeight)
    }
  }
}
