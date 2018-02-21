package com.udacity.sandwichclub.model;

import java.util.List;

public class Sandwich {

    private String mMainName;
    private List<String> mAlsoKnowns = null;
    private String mPlaceOfOrigin;
    private String mDescription;
    private String mImage;
    private List<String> mIngredients = null;

    /**
     * No args constructor for use in serialization
     */
    public Sandwich() {
    }

    public Sandwich(String mainName, List<String> alsoKnownAs, String placeOfOrigin, String description, String image, List<String> ingredients) {
        this.mMainName = mainName;
        this.mAlsoKnowns = alsoKnownAs;
        this.mPlaceOfOrigin = placeOfOrigin;
        this.mDescription = description;
        this.mImage = image;
        this.mIngredients = ingredients;
    }

    public String getMainName() {
        return this.mMainName;
    }

    public void setMainName(String mainName) {
        this.mMainName = mainName;
    }

    public List<String> getAlsoKnownAs() {
        return this.mAlsoKnowns;
    }

    public void setAlsoKnownAs(List<String> alsoKnownAs) {
        this.mAlsoKnowns = alsoKnownAs;
    }

    public String getPlaceOfOrigin() {
        return this.mPlaceOfOrigin;
    }

    public void setPlaceOfOrigin(String placeOfOrigin) {
        this.mPlaceOfOrigin = placeOfOrigin;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getImage() {
        return this.mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public List<String> getIngredients() {
        return this.mIngredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.mIngredients = ingredients;
    }
}
