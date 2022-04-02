package io.github.prototypez.appjoint.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class LegacyFragment : Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_legacy, container, false)
  }

  companion object {
    fun newInstance(): LegacyFragment {
      return LegacyFragment();
    }
  }
}
