package com.example.getfood

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Keep
import androidx.coordinatorlayout.widget.CoordinatorLayout

@Keep
class MoveUpwardsbehavior : CoordinatorLayout.Behavior<View?> {
    constructor() : super() {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    /*public override fun layoutDependsOn(parent: CoordinatorLayout, child: View?, dependency: View): Boolean {
        return dependency is SnackbarLayout
    }

    public override fun onDependentViewChanged(parent: CoordinatorLayout, child: View?, dependency: View): Boolean {
        val translationY: Float = Math.min(0f, ViewCompat.getTranslationY(dependency) - dependency.getHeight())
        ViewCompat.setTranslationY(child, translationY)
        return true
    }

    //you need this when you swipe the snackbar(thanx to ubuntudroid's comment)
    public override fun onDependentViewRemoved(parent: CoordinatorLayout, child: View?, dependency: View) {
        ViewCompat.animate((child)!!).translationY(0f).start()
    }*/
}