package de.appsfactory.lastfm.ui.home.myalbumsscreen

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import androidx.navigation.fragment.findNavController
import dagger.android.support.AndroidSupportInjection
import de.appsfactory.lastfm.R
import de.appsfactory.lastfm.databinding.FragmentMyAlbumsBinding
import de.appsfactory.lastfm.databinding.FragmentSearchBinding
import de.appsfactory.lastfm.ui.common.listeners.ListItemClickListener
import javax.inject.Inject

class MyAlbumsFragment : Fragment(), ListItemClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var mAdapter: MyAlbumsAdapter? = null

    val viewModel: MyAlbumsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MyAlbumsViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_albums, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinding(view)
    }

    private fun initBinding(view: View) {
        val binding = DataBindingUtil.bind<FragmentMyAlbumsBinding>(view)
        lifecycle.addObserver(viewModel)
        binding.let {
            it!!.viewModel = viewModel
            it.setLifecycleOwner(this)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        addObservers()
    }

    private fun addObservers() {
        val binding = view?.let { DataBindingUtil.bind<FragmentMyAlbumsBinding>(it) }
        viewModel.albums.observe(this, Observer {
            if (mAdapter == null) {
                mAdapter = MyAlbumsAdapter(this)
                binding?.rvMyAlbums?.adapter = mAdapter
            }
            if (it != null) {
                mAdapter!!.setData(it)
                mAdapter!!.notifyDataSetChanged()
                if (it.isEmpty()) {
                    viewModel.hasAlbums.value = false
                } else {
                    viewModel.hasAlbums.value = true
                }
            } else {
                viewModel.hasAlbums.value = false
            }
        })
    }

    override fun onClick(view: View, position: Int) {
        val album = viewModel.albums.value?.get(position)
        album?.let {
            val direction = MyAlbumsFragmentDirections
                    .ActionMyalbumsListFragmentToDetailsFragment(it)

            findNavController().navigate(direction)
        }
    }

    override fun onRetryClick(position: Int) {

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_my_albums, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                val direction = MyAlbumsFragmentDirections.ActionMyalbumsListFragmentToSearchFragment()
                findNavController().navigate(direction)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}