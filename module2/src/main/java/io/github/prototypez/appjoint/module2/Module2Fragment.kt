package io.github.prototypez.appjoint.module2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class Module2Fragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_module2, container, false)
  }

  companion object {
    fun newInstance(): Module2Fragment {
      return Module2Fragment();
    }
  }
}
