package com.reactnativecarousel

import com.facebook.react.bridge.ReactContext
import com.facebook.react.uimanager.LayoutShadowNode
import com.facebook.react.uimanager.UIImplementation
import com.facebook.react.uimanager.UIManagerModule
import com.facebook.react.uimanager.UIViewOperationQueue
import java.lang.reflect.Method

object ReactUpdateHelper {
  private var _uiManager: UIManagerModule? = null
  private var _uiImplementation: UIImplementation? = null
  private var _operationsQueue: UIViewOperationQueue? = null
  private var _dispatchViewUpdateMethod: Method? = null

  fun getUiManager(c: ReactContext): UIManagerModule {
    _uiManager?.let { return it }
    val manager = c.getNativeModule<UIManagerModule>(UIManagerModule::class.java)!!
    _uiManager = manager
    return manager
  }

  fun getUiImplementation(c: ReactContext): UIImplementation {
    _uiImplementation?.let { return it }
    val uiManager = getUiManager(c)
    val obj = getPrivateField<UIImplementation>(uiManager, "mUIImplementation")
    _uiImplementation = obj
    return obj
  }

  fun getOperationsQueue(c: ReactContext): UIViewOperationQueue {
    _operationsQueue?.let { return it }
    val uiImplementation = getUiImplementation(c)
    val obj = getPrivateField<UIViewOperationQueue>(uiImplementation, "mOperationsQueue")
    _operationsQueue = obj
    return obj
  }

  fun dispatchViewUpdate(c: ReactContext) {
    val uiImplementation = getUiImplementation(c)
    if (_dispatchViewUpdateMethod == null) {
      val obj = getPrivateMethod(uiImplementation, "dispatchViewUpdates", Int::class.java)
      _dispatchViewUpdateMethod = obj
    }
    _dispatchViewUpdateMethod?.also {
      c.runOnNativeModulesQueueThread {
        it.invoke(uiImplementation, -1)
      }
    }
  }

  @Suppress("UNCHECKED_CAST")
  fun <R> getPrivateField(source: Any, fieldName: String): R =
    source::class.java.getDeclaredField(fieldName).let { f ->
      f.isAccessible = true
      f.get(source) as R
    }

  fun getPrivateMethod(source: Any, methodName: String, vararg parameterTypes: Class<*>): Method =
    source::class.java.getDeclaredMethod(methodName, *parameterTypes).also { f ->
      f.isAccessible = true
    }
}

fun LayoutShadowNode.updateReactLayout(index: Int, height: Int) {
  try {
    // Check view is exists
    ReactUpdateHelper.getUiManager(themedContext).resolveView(reactTag)

    ReactUpdateHelper
      .getOperationsQueue(themedContext)
//            .enqueueUpdateLayout(parent!!.reactTag, reactTag, 0, 0, 0, 0)
      .enqueueUpdateLayout(rootTag, reactTag, 0, 0, 0, height)
    ReactUpdateHelper.dispatchViewUpdate(themedContext)
    println("🔦 ${index} updateReactLayout success")
  } catch (e: Exception) {
    println("🔦 ${index} updateReactLayout ERROR" + e.message)
    /* no-op */
  }
}
