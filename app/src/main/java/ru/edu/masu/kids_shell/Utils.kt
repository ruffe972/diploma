package ru.edu.masu.kids_shell

import android.content.Intent
import android.view.View
import android.view.ViewTreeObserver

fun View.callOnceOnGlobalLayout(callback: () -> Unit) {
    if (viewTreeObserver.isAlive) {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                callback()
            }
        })
    }
}

inline fun <reified T : Enum<T>> Intent.putExtra(victim: T): Intent =
        putExtra(T::class.qualifiedName, victim.ordinal)

inline fun <reified T: Enum<T>> Intent.getEnumExtra(): T? =
        getIntExtra(T::class.qualifiedName, -1)
                .takeUnless { it == -1 }
                ?.let { T::class.java.enumConstants[it] }