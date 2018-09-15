package de.appsfactory.lastfm.ui.home.albumdetails

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import de.appsfactory.lastfm.R
import de.appsfactory.lastfm.databinding.FragmentAlbumDetailsBinding
import javax.inject.Inject

class AlbumDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    val viewModel: AlbumDetailsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(AlbumDetailsViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_album_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(view)
    }

    private fun initBinding(view: View) {
        val binding = DataBindingUtil.bind<FragmentAlbumDetailsBinding>(view)
        lifecycle.addObserver(viewModel)
        binding.let {
            it!!.viewModel = viewModel
            it.setLifecycleOwner(this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.prepareData(AlbumDetailsFragmentArgs.fromBundle(arguments).album)
    }
}