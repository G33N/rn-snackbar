package com.geen.rnsnackbar

import android.R
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.facebook.react.bridge.*
import com.google.android.material.snackbar.Snackbar
import java.util.*

class SnackbarModule(reactContext: ReactApplicationContext?) : ReactContextBaseJavaModule(reactContext!!) {
    private val mActiveSnackbars: MutableList<Snackbar> = ArrayList()
    override fun getName(): String {
        return REACT_NAME
    }

    override fun getConstants(): Map<String, Any>? {
        val constants: MutableMap<String, Any> = HashMap()
        constants["LENGTH_LONG"] = Snackbar.LENGTH_LONG
        constants["LENGTH_SHORT"] = Snackbar.LENGTH_SHORT
        constants["LENGTH_INDEFINITE"] = Snackbar.LENGTH_INDEFINITE
        constants["POSITION_TOP"] = "top"
        constants["POSITION_BOTTOM"] = "bottom"
        return constants
    }

    @ReactMethod
    fun show(options: ReadableMap, callback: Callback) {
        val view: ViewGroup
        view = try {
            currentActivity!!.window.decorView.findViewById<View>(R.id.content) as ViewGroup
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
        if (view == null) return
        mActiveSnackbars.clear()

        // `hasWindowFocus`: Returns true if this activity's *main* window currently has window focus.
        // Note that this is not the same as the view itself having focus.
        if (!view.hasWindowFocus()) {
            // Get all modal views on the screen.
            val modals = recursiveLoopChildren(view, ArrayList())
            for (modal in modals) {
                if (modal == null) continue
                displaySnackbar(modal, options, callback)
                return
            }

            // No valid modals.
            if (view.visibility == View.VISIBLE) {
                displaySnackbar(view, options, callback)
            } else {
                Log.w(REACT_NAME, "Content view is not in focus or not visible")
            }
            return
        }
        displaySnackbar(view, options, callback)
    }

    @ReactMethod
    fun dismiss() {
        for (snackbar in mActiveSnackbars) {
            snackbar?.dismiss()
        }
        mActiveSnackbars.clear()
    }

    private fun displaySnackbar(view: View, options: ReadableMap, callback: Callback) {
        val constants = constants
        val text = getOptionValue(options, "text", "")
        val duration = getOptionValue(options, "duration", Snackbar.LENGTH_SHORT)
        val position = getOptionValue(options, "position", "bottom")
        val textColor = getOptionValue(options, "textColor", Color.WHITE)
        val rtl = getOptionValue(options, "rtl", false)
        val fontFamily = getOptionValue(options, "fontFamily", null)
        var font: Typeface? = null
        if (fontFamily != null) {
            font = try {
                Typeface.createFromAsset(view.context.assets, "fonts/$fontFamily.ttf")
            } catch (e: Exception) {
                e.printStackTrace()
                throw Error("Failed to load font $fontFamily.ttf, did you include it in your assets?")
            }
        }
        val snackbar: Snackbar
        try {
            snackbar = text?.let { Snackbar.make(view, it, duration) }!!;
        } catch (e: IllegalArgumentException) {
            // TODO: Fix root cause of "No suitable parent found from the given view. Please provide a valid view."
            e.printStackTrace()
            return
        }
        val layout = snackbar.view
        if (rtl && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layout.layoutDirection = View.LAYOUT_DIRECTION_RTL
            layout.textDirection = View.TEXT_DIRECTION_RTL
        }
        val snackbarText = layout.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        snackbarText.setTextColor(textColor)
        if (font != null) {
            snackbarText.typeface = font
        }
        mActiveSnackbars.add(snackbar)
        if (options.hasKey("backgroundColor")) {
            layout.setBackgroundColor(options.getInt("backgroundColor"))
        }
        if (options.hasKey("position")) {
            val params = layout.layoutParams as FrameLayout.LayoutParams
            //If the view is not covering the whole snackbar layout, add this line
            params.gravity = Gravity.TOP
            layout.layoutParams = params
        }
        if (options.hasKey("action")) {
            val actionOptions = options.getMap("action")
            val actionText = getOptionValue(actionOptions, "text", "")
            val actionTextColor = getOptionValue(actionOptions, "textColor", Color.WHITE)
            val onClickListener: View.OnClickListener = object : View.OnClickListener {
                // Prevent double-taps which can lead to a crash.
                var callbackWasCalled = false
                override fun onClick(v: View) {
                    if (callbackWasCalled) return
                    callbackWasCalled = true
                    callback.invoke()
                }
            }
            snackbar.setAction(actionText, onClickListener)
            snackbar.setActionTextColor(actionTextColor)
            if (font != null) {
                val snackbarActionText = layout.findViewById<TextView>(com.google.android.material.R.id.snackbar_action)
                snackbarActionText.typeface = font
            }
        }
        snackbar.setCallback(object : Snackbar.Callback() {
            override fun onShown(snackbar: Snackbar) {
                super.onShown(snackbar)
                snackbar.view.visibility = View.VISIBLE
            }
        })
        snackbar.show()
    }

    /**
     * Loop through all child modals and save references to them.
     */
    private fun recursiveLoopChildren(view: ViewGroup, modals: ArrayList<View>): ArrayList<View> {
        if (view.javaClass.simpleName.equals("ReactModalHostView", ignoreCase = true)) {
            modals.add(view.getChildAt(0))
        }
        for (i in view.childCount - 1 downTo 0) {
            val child = view.getChildAt(i)
            if (child is ViewGroup) {
                recursiveLoopChildren(child, modals)
            }
        }
        return modals
    }

    private fun getOptionValue(options: ReadableMap?, key: String, fallback: String?): String? {
        return if (options!!.hasKey(key)) options.getString(key) else fallback
    }

    private fun getOptionValue(options: ReadableMap?, key: String, fallback: Int): Int {
        return if (options!!.hasKey(key)) options.getInt(key) else fallback
    }

    private fun getOptionValue(options: ReadableMap, key: String, fallback: Boolean): Boolean {
        return if (options.hasKey(key)) options.getBoolean(key) else fallback
    }

    companion object {
        private const val REACT_NAME = "RNSnackbar"
    }
}