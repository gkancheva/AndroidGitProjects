package com.company.bakingapp.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.company.bakingapp.R;
import com.company.bakingapp.models.Recipe;
import com.company.bakingapp.services.RecipeClickListener;
import com.company.bakingapp.utils.RecipeImageUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesRVAdapter extends RecyclerView.Adapter<RecipesRVAdapter.RecipeViewHolder> {

    private List<Recipe> mRecipes;
    private final Context mContext;
    private RecipeClickListener mClickListener;

    public RecipesRVAdapter(Context context, RecipeClickListener clickListener) {
        this.mRecipes = new ArrayList<>();
        this.mContext = context;
        this.mClickListener = clickListener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_card_recipe, parent, false);
        view.setFocusable(true);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return this.mRecipes.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_recipe) ImageView mIvRecipeImage;
        @BindView(R.id.tv_recipe_name) TextView mRecipeName;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bind(int position) {
            Recipe recipe = mRecipes.get(position);
            this.mRecipeName.setText(recipe.getName());
            if(recipe.getImage().isEmpty()) {
                this.mIvRecipeImage.setImageResource(RecipeImageUtils.getImageResource(recipe.getName()));
                return;
            }
            Picasso.with(mContext)
                    .load(recipe.getImage())
                    .placeholder(R.drawable.dessert_vector)
                    .error(R.drawable.dessert_vector)
                    .into(this.mIvRecipeImage);
        }

        @Override
        public void onClick(View v) {
            Recipe recipe = mRecipes.get(getAdapterPosition());
            mClickListener.onRecipeSelected(recipe);
        }
    }

    public void updateRecipeList(List<Recipe> recipes) {
        int itemCount = this.getItemCount();
        this.mRecipes.clear();
        this.notifyItemRangeRemoved(0, itemCount);
        this.mRecipes.addAll(recipes);
        this.notifyItemRangeInserted(0, recipes.size());
    }
}