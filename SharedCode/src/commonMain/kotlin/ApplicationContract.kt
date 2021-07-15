package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    interface View {
        fun getAndDisplayJourneysData(view: ApplicationContract.View)
        fun displayJourneysInRecyclerView(journeysData: List<Journey>)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun getAndDisplayJourneysData(view: ApplicationContract.View, url: String)
        abstract fun getAPIURLWithSelectedStationsPresenter(arrivalStation: String, departureStation: String): String
    }
}
