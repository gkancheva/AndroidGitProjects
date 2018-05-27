package com.company.bakingapp.services;

import com.company.bakingapp.models.Step;

public interface StepClickListener {
    void onStepSelected(Step step);
}