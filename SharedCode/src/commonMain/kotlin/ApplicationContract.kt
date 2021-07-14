package com.jetbrains.handson.mpp.mobile

import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    interface View {
        fun setLabel(text: String)
        fun updateTrainsRecycleView(trains: List<Train>)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)
        abstract fun getData(view: ApplicationContract.View,url: String)
        abstract fun getAPIURLWithSelectedStationsPresenter(arrivalStation: String, departureStation: String): String
    }
}
