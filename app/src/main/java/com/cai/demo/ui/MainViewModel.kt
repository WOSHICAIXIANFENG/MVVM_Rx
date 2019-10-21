package com.cai.demo.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cai.demo.interactor.MuseumInteractor
import com.cai.demo.model.Museum
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainViewModel : ViewModel(), KoinComponent {
  private val compositeDisposable: CompositeDisposable = CompositeDisposable()
  private val interactor: MuseumInteractor by inject()

  private val _museums = MutableLiveData<List<Museum>>()
  val museums: MutableLiveData<List<Museum>>
    get() = _museums

  private val _showLoading = MutableLiveData<Boolean>()
  val showLoading = _showLoading

  private val _showErrorTip = MutableLiveData<Boolean>()
  val showErrorTip = _showErrorTip

  private val _showEmptyTip = MutableLiveData<Boolean>()
  val showEmptyTip = _showEmptyTip

//  init {
//    loadMuseums()
//  }

  fun loadMuseums() {
//    // todo
//    loadMockData()
    loadMuseumsFromNetwork()
  }

  private fun loadMuseumsFromNetwork() {
    addToDisposable(interactor.getMuseumList().doOnSubscribe {
      _showLoading.value = true
    }.doOnError {
      _showErrorTip.value = true
      _showLoading.value = false
    }.doOnSuccess {
      _showLoading.value = false
    }.subscribe { museums ->
      if (museums.isEmpty()) {
        _showEmptyTip.value = true
      } else {
        _showEmptyTip.value = false
        _museums.value = museums
      }
    })
  }

  private fun loadMockData() {
    _showLoading.value = true
    val mockData = listOf(
      Museum(
        id = 1,
        name = "Samuel 11",
        imgUrl = "http://museos.cultura.pe/sites/default/files/styles/museos_portada/public/museos/imagen/rnm_1493925752.jpg"
      )
      ,
      Museum(
        id = 2,
        name = "Cai Test 22",
        imgUrl = "http://museos.cultura.pe/sites/default/files/styles/museos_portada/public/museos/imagen/rnm_1493925752.jpg"
      )
    )

    addToDisposable(Observable.just(mockData)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .doOnError {
        _showLoading.value = false
        _showErrorTip.value = true
      }
      .subscribe {
        _showLoading.value = false
        if (it.isEmpty()) {
          _showEmptyTip.value = it.isEmpty()
        } else {
          _museums.value = it
        }
      })
  }

  private fun addToDisposable(disposable: Disposable) {
    compositeDisposable.add(disposable)
  }

  override fun onCleared() {
    compositeDisposable.clear()
    super.onCleared()
  }
}