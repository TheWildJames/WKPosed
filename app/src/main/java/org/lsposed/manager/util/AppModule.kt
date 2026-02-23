package org.lsposed.manager.util

import android.content.Context
import android.content.pm.PackageInfo
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import org.lsposed.manager.R

@GlideModule
class AppModule : AppGlideModule() {
    override fun isManifestParsingEnabled(): Boolean = false

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        val iconSize = context.resources.getDimensionPixelSize(R.dimen.app_icon_size)
        val factory = AppIconModelLoader.Factory(iconSize, false, context)
        registry.prepend(PackageInfo::class.java, Bitmap::class.java, factory)
    }
}