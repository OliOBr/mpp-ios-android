package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    interface View {
        fun getAndDisplayJourneysData(view: ApplicationContract.View)
        fun displayJourneysInRecyclerView(newTrains: List<Train>)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)
        abstract fun getAndDisplayJourneysData(view: ApplicationContract.View, url: String)
        abstract fun getAPIURLWithSelectedStationsPresenter(arrivalStation: String, departureStation: String): String
    }
}
