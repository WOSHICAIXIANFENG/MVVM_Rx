package com.cai.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cai.demo.ui.MainFragment
import kotlinx.android.synthetic.main.activity_main.fragment_container

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
        .replace(R.id.fragment_container, MainFragment.newInstance()).commitNow()
    }
  }
}
