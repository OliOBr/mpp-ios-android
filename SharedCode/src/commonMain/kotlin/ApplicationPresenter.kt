package com.jetbrains.handson.mpp.mobile

import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext


class ApplicationPresenter: ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private var view: ApplicationContract.View? = null
    private val job: Job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    private val scope = CoroutineScope(coroutineContext)

    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        view.setLabel(createApplicationScreenMessage())
    }

    override fun getData(url: String):Unit {
        scope.launch { // launch a new coroutine and continue
            makeGetRequestForData(url)
        }
    }
}
