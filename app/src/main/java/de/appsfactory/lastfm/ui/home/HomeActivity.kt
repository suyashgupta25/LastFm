package de.appsfactory.lastfm.ui.home

import android.databinding.DataBindingUtil
import android.os.Bundle
import de.appsfactory.lastfm.R
import de.appsfactory.lastfm.databinding.ActivityHomeBinding
import de.appsfactory.lastfm.ui.common.base.BaseActivity
import de.appsfactory.lastfm.ui.home.myalbumsscreen.MyAlbumsFragment
import de.appsfactory.lastfm.utils.ext.replaceFragment

class HomeActivity : BaseActivity() {

    val binding: ActivityHomeBinding by lazy {
        DataBindingUtil.setContentView<ActivityHomeBinding>(this, R.layout.activity_home)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentContainerId = binding.flHomeContent.id
        replaceFragment(fragmentContainerId, ::MyAlbumsFragment)
    }
}