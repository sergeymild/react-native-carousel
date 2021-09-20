package com.reactnativecarousel

import com.facebook.react.uimanager.PixelUtil
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp


class CarouselViewManager : ViewGroupManager<CarouselView>() {
  override fun getName() = "CarouselView"

  override fun createViewInstance(reactContext: ThemedReactContext): CarouselView {
    return CarouselView(reactContext)
  }

  @ReactProp(name = "nextItemVisible")
  fun nextItemVisible(view: CarouselView, value: Int) {
    view.nextItemVisiblePx = PixelUtil.toPixelFromDIP(value.toFloat())
  }

  @ReactProp(name = "currentItemHorizontalMargin")
  fun currentItemHorizontalMargin(view: CarouselView, value: Int) {
    view.currentItemHorizontalMarginPx = PixelUtil.toPixelFromDIP(value.toFloat())
  }
}
