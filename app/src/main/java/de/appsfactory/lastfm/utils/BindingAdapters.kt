package de.appsfactory.lastfm.utils

import android.databinding.BindingAdapter
import android.support.design.widget.FloatingActionButton
import android.support.v4.content.ContextCompat
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import de.appsfactory.lastfm.R

@BindingAdapter("app:glideProfileImageUri")
    fun setGlideProfileImageUri(imv: ImageView, url:String) {
        val dimension = imv.context.resources.getDimension(R.dimen.radius_card_corner)
        val transforms = RequestOptions()
                .transforms(CenterInside(), RoundedCorners(dimension.toInt()))
                .placeholder(R.mipmap.ic_lastfm_round)
                .error(R.mipmap.ic_lastfm_round)
        Glide.with(imv.context.applicationContext)
                .load(url)
                .apply(transforms)
                .into(imv)
    }

    @BindingAdapter("app:glideBannerImageUri")
    fun setGlideBannerImageUri(imv: ImageView, url:String) {
        val transforms = RequestOptions()
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)
        Glide.with(imv.context.applicationContext)
                .load(url)
                .apply(transforms)
                .into(imv)
    }

    @BindingAdapter("app:favbuttonImage")
    fun setFavbuttonImage(fab: FloatingActionButton, isFav:Boolean) {
        if(isFav) {
            fab.setImageDrawable(ContextCompat.getDrawable(fab.context, R.drawable.ic_favorite_filled))
        } else {
            fab.setImageDrawable(ContextCompat.getDrawable(fab.context, R.drawable.ic_favorite_empty))
        }
    }