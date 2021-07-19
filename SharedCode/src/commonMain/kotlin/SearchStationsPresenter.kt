package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.*
import kotlinx.serialization.json.JsonArray
import kotlin.coroutines.CoroutineContext


class SearchStationsPresenter: SearchStationsContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    private val scope = CoroutineScope(coroutineContext)

    override fun getAndListStationsData(view: SearchStationsContract.View) {
        scope.launch { // launch a new coroutine and continue
            val stationsData: JsonArray = makeGetRequestForStationsData()
            view.listStationsInListView(stationsData.map{ parseJSONElementToStation(it) }
                    .filter{it.crs!="null"}.sortedBy { it.stationName })
        }
    }
}
