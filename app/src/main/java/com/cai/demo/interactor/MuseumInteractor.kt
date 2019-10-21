package com.cai.demo.interactor

import android.os.SystemClock
import com.cai.demo.model.Museum
import com.cai.demo.network.MuseumEntity
import com.cai.demo.network.MuseumService
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable

interface MuseumInteractor {
  fun getMuseumList(): Single<List<Museum>>
}

class MuseumInteractorImpl(val museumService: MuseumService) : MuseumInteractor {

  override fun getMuseumList(): Single<List<Museum>> {
    return museumService.museums()
      .subscribeOn(Schedulers.newThread())
      .observeOn(AndroidSchedulers.mainThread())
      .flatMap { response ->
        if (response.isSuccess()) {
          Single.just(response.data?.map { it.toClientModel() })
        } else {
          Single.just(emptyList())
        }
      }
  }

  private fun MuseumEntity.toClientModel(): Museum {
    return Museum(
      id = this.id,
      name = this.name,
      imgUrl = this.img
    )
  }

  // https://www.vogella.com/tutorials/RxJava/article.html#why-doing-asynchronous-programming

  val downloadObservable = Observable.create<String> { emitter ->
    // download data from other source
    SystemClock.sleep(10000)
    emitter.onNext("Result")
    emitter.onComplete()
  }

  val myCallable = Callable<String> {
    // long time
    "xianfeng"
  }

  private fun loadAddressList() {
    val disposable = downloadObservable
      .subscribeOn(Schedulers.newThread())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe { result ->
        // update UI
      }

    //

//    Observable.fromCallable(myCallable)
//      .subscribeOn(Schedulers.newThread())
//      .observeOn(AndroidSchedulers.mainThread())
//      .subscribe {  }
  }
}