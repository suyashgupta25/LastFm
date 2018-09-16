package de.appsfactory.lastfm.ui.common.viewmodels

import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import de.appsfactory.lastfm.data.webservice.NetworkState
import de.appsfactory.lastfm.data.webservice.Status
import de.appsfactory.lastfm.utils.AppConstants.Companion.EMPTY

class ItemNetworkStateViewModel(networkState: NetworkState) {

    val showProgress = ObservableBoolean(false)
    val showError = ObservableBoolean(false)
    val errorMsg = ObservableField<String>(EMPTY)

    init {
        if (networkState.status === Status.RUNNING) { showProgress.set(true) }
            else { showProgress.set(false) }
        if (networkState.status === Status.FAILED) {
            showError.set(true)
            errorMsg.set(networkState.msg)
        } else {
            showError.set(false)
            errorMsg.set(EMPTY)
        }
    }

}