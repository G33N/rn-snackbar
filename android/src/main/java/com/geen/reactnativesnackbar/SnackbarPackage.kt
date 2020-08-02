package com.geen.rnsnackbar

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.JavaScriptModule
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager

class SnackbarPackage : ReactPackage {
    override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
        return listOf<NativeModule>(SnackbarModule(reactContext))
    }

    // This "Override" method is being kept for backwards compatibilty with React Native
    // releases earlier than https://github.com/facebook/react-native/releases/tag/v0.47.0
    fun createJSModules(): List<Class<out JavaScriptModule?>> {
        return emptyList()
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
        return emptyList()
    }
}