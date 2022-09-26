package com.javacat.easybudget.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object AndroidUtils {
    fun hideKeyboard(view: View) {
        getInputMethodManager(view).hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyboard(view: View){
        view.post{
            getInputMethodManager(view).showSoftInput(view,InputMethodManager.SHOW_IMPLICIT)
        }
    }
    private fun getInputMethodManager(view: View): InputMethodManager{
        val context = view.context
        return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }
}