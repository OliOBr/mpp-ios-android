package com.jetbrains.handson.mpp.mobile

import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.*
import kotlinx.serialization.json.JsonArray
import kotlin.coroutines.CoroutineContext


class ApplicationPresenter: ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    private val scope = CoroutineScope(coroutineContext)

    override fun getAPIURLWithSelectedStationsPresenter(arrivalStation: String, departureStation: String): String{
        return getAPIURLWithSelectedStations(arrivalStation, departureStation)
    }

    override fun getAndDisplayJourneysData(view: ApplicationContract.View, url: String) {
        scope.launch { // launch a new coroutine and continue
            val journeysData: JsonArray = makeGetRequestForJourneysData(url)
            view.displayJourneysInRecyclerView(journeysData.map{parseJSONElementToJourney(it)})
        }
    }
}
