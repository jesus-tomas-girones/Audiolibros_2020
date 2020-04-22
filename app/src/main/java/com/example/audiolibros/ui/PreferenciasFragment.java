package com.example.audiolibros.ui;

import android.os.Bundle;

import com.example.audiolibros.R;

import androidx.preference.PreferenceFragmentCompat;

public class PreferenciasFragment extends PreferenceFragmentCompat {
   @Override
   public void onCreatePreferences(Bundle savedInstanceState,
                                   String rootKey) {
      setPreferencesFromResource(R.xml.preferencias, rootKey);
   }
}

