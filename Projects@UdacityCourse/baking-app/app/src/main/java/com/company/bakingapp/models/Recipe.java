package com.company.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Recipe implements Parcelable {
    private long mId;
    private String mName;
    private int mServings;
    private String mImage;
    private List<Ingredient> mIngredients;
    private List<Step> mSteps;

    public Recipe(long id, String name, int servings, String image, List<Ingredient> ingredients, List<Step> steps) {
        this.mId = id;
        this.mName = name;
        this.mServings = servings;
        this.mImage = image;
        this.mIngredients = ingredients;
        this.mSteps = steps;
    }

    public long getId() {
        return this.mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getServings() {
        return this.mServings;
    }

    public void setServings(int servings) {
        this.mServings = servings;
    }

    public String getImage() {
        return this.mImage;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public List<Ingredient> getIngredients() {
        return this.mIngredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.mIngredients = ingredients;
    }

    public List<Step> getSteps() {
        return this.mSteps;
    }

    public void setSteps(List<Step> steps) {
        this.mSteps = steps;
    }

    public String getIngredientsAsText() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.getIngredients().size(); i++) {
            sb.append(i + 1).append(". ")
                    .append(this.getIngredients().get(i).toString());
            if(i != this.getIngredients().size() -1) {
                sb.append(System.lineSeparator());
            }
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Recipe)) {
            return false;
        }
        Recipe r = (Recipe)obj;
        return r.mId == this.getId() &&
                r.getName().equals(this.getName());
    }

    protected Recipe(Parcel in) {
        mId = in.readLong();
        mName = in.readString();
        mServings = in.readInt();
        mImage = in.readString();
        if (in.readByte() == 0x01) {
            mIngredients = new ArrayList<Ingredient>();
            in.readList(mIngredients, Ingredient.class.getClassLoader());
        } else {
            mIngredients = null;
        }
        if (in.readByte() == 0x01) {
            mSteps = new ArrayList<Step>();
            in.readList(mSteps, Step.class.getClassLoader());
        } else {
            mSteps = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mName);
        dest.writeInt(mServings);
        dest.writeString(mImage);
        if (mIngredients == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mIngredients);
        }
        if (mSteps == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(mSteps);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };
}