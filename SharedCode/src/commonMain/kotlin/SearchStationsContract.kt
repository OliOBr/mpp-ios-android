package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface SearchStationsContract {
    interface View {
        fun listStationsInListView(stationData: List<Station>)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun getAndListStationsData(view: SearchStationsContract.View)
    }
}
