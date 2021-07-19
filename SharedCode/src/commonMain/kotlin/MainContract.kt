package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface MainContract {
    interface View {
        fun displayJourneysInRecyclerView(journeysData: List<Journey>)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun getAndDisplayJourneysData(view: MainContract.View, originStationCRS: String, destStationCRS: String)
    }
}
