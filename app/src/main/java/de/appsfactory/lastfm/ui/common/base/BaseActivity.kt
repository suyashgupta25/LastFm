package de.appsfactory.lastfm.ui.common.base

import dagger.android.support.DaggerAppCompatActivity

/**
 * Created by suyashg
 *
 * Base class for activity extensibility and reusability
 */
abstract class BaseActivity : DaggerAppCompatActivity() {
    //common for all activities
    var fragmentContainerId: Int = 0

}