package com.bove.martin.pexel.presentation.utils

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Created by Martín Bove on 20/6/2022.
 * E-mail: mbove77@gmail.com
 */
internal class NpaStaggeredGridLayoutManager : StaggeredGridLayoutManager {
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
    }

    constructor(spanCount: Int, orientation: Int) : super(spanCount, orientation) {}

    /**
     * Disable predictive animations. There is a bug in RecyclerView which causes views that
     * are being reloaded to pull invalid ViewHolders from the internal recycler stack if the
     * adapter size has decreased since the ViewHolder was recycled.
     */
    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}