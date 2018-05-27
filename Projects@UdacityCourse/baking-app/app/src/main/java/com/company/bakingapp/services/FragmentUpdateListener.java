package com.company.bakingapp.services;

import com.company.bakingapp.models.Recipe;

public interface FragmentUpdateListener {
    void onFragmentUpdateRequested(Recipe recipe);
}