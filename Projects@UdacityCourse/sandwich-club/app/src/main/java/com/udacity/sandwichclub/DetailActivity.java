package com.udacity.sandwichclub;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @BindView(R.id.image_iv) ImageView mIngredientsView;
    @BindView(R.id.origin_tv) TextView mTVOrigin;
    @BindView(R.id.also_known_tv) TextView mTVKnownAs;
    @BindView(R.id.ingredients_tv) TextView mTVIngredients;
    @BindView(R.id.description_tv) TextView mTVDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() == null) {
            closeOnError();
        }

        int position = getIntent().getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = null;
        try {
            sandwich = JsonUtils.parseSandwichJson(json, this);
        } catch (JSONException e) {
            closeOnError();
        }

        if (sandwich == null) {
            closeOnError();
            return;
        }

        this.populateUI(sandwich);
        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        Picasso.with(this)
                .load(sandwich.getImage())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(this.mIngredientsView);
        this.mTVOrigin.setText(sandwich.getPlaceOfOrigin());
        this.mTVDescription.setText(sandwich.getDescription());
        this.mTVIngredients.setText(TextUtils.join(", ", sandwich.getIngredients()));
        this.mTVKnownAs.setText(TextUtils.join(", ", sandwich.getAlsoKnownAs()));
    }

}
