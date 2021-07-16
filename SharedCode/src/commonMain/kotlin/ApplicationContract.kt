package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    interface MainView {
        fun displayJourneysInRecyclerView(journeysData: List<Journey>)
    }

    interface SearchStationsView {
        fun listStationsInListView(view: List<Station>)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun getAndDisplayJourneysData(view: ApplicationContract.MainView, arrivalStation: String, departureStation: String)
        abstract fun getAndListStationsData(view: ApplicationContract.SearchStationsView, url: String)
    }
}
