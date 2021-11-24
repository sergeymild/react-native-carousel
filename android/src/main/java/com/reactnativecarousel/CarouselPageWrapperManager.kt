package com.reactnativecarousel

import android.content.Context
import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.views.view.ReactViewGroup

class CarouselPageWrapperManager : ViewGroupManager<CarouselPageWrapper>() {
  override fun getName() = "CarouselPageWrapper"

  override fun createViewInstance(reactContext: ThemedReactContext): CarouselPageWrapper {
    return CarouselPageWrapper(reactContext)
  }
}

class CarouselPageWrapper(context: Context?) : ReactViewGroup(context) {

  override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
    super.onLayout(changed, left, top, right, bottom)
    if (childCount == 0) return
    val tag = getChildAt(0).id
    (context as ReactContext).catalystInstance.reactQueueConfiguration.nativeModulesQueueThread.runOnQueue {
      ReactUpdateHelper.getUiManager(context as ReactContext).updateNodeSize(tag, measuredWidth, measuredHeight)
    }
  }
}
