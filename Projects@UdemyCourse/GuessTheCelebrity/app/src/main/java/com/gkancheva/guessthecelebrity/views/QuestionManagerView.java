package com.gkancheva.guessthecelebrity.views;

import android.app.Activity;
import android.graphics.Bitmap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gkancheva.guessthecelebrity.R;

import java.util.List;

public class QuestionManagerView {
    private ImageView imageView;
    private Button btn0, btn1, btn2, btn3;
    private TextView pointsTxtView;

    public QuestionManagerView(Activity activity) {
        this.imageView = (ImageView)activity.findViewById(R.id.imageView);
        this.btn0 = (Button)activity.findViewById(R.id.btnOption0);
        this.btn1 = (Button)activity.findViewById(R.id.btnOption1);
        this.btn2 = (Button)activity.findViewById(R.id.btnOption2);
        this.btn3 = (Button)activity.findViewById(R.id.btnOption3);
        this.pointsTxtView = (TextView)activity.findViewById(R.id.points);
    }

    public void updateButtons(List<String> options) {
        this.btn0.setText(options.get(0));
        this.btn1.setText(options.get(1));
        this.btn2.setText(options.get(2));
        this.btn3.setText(options.get(3));
    }

    public void updateImage(Bitmap bitmap) {
        this.imageView.setImageBitmap(bitmap);
    }

    public void updatePointsTxtView(String text) {
        this.pointsTxtView.setText(text);
    }
}