package de.appsfactory.lastfm.utils.ext

import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity

/**
 * Created by suyashg
 *
 * Utiliy class for adding, replacing fragments
 */
@Suppress("UNCHECKED_CAST")
fun <T> FragmentActivity.findFragmentById(@IdRes id: Int): T = supportFragmentManager.findFragmentById(id) as T

inline fun FragmentActivity.setFragment(containerViewId: Int, f: () -> Fragment): Fragment? {
    val manager = supportFragmentManager
    val fragment = manager?.findFragmentById(containerViewId)
    fragment?.let { return it }
    return f().apply { manager?.beginTransaction()?.add(containerViewId, this)?.commit() }
}

inline fun FragmentActivity.replaceFragment(containerViewId: Int, f: () -> Fragment): Fragment? {
    return f().apply { supportFragmentManager?.beginTransaction()?.replace(containerViewId, this)?.commit() }
}

inline fun FragmentActivity.addFragment(containerViewId: Int, f: () -> Fragment, bundle: Bundle): Fragment? {
    val manager = supportFragmentManager
    return f().apply {
        this.arguments = bundle
        manager?.beginTransaction()?.add(containerViewId, this)?.addToBackStack(null)?.commit()
    }
}