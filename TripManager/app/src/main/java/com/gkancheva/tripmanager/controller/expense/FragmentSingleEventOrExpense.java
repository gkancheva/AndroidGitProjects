package com.gkancheva.tripmanager.controller.expense;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gkancheva.tripmanager.R;
import com.gkancheva.tripmanager.model.expense.Accommodation;
import com.gkancheva.tripmanager.model.expense.Event;
import com.gkancheva.tripmanager.model.expense.Expense;
import com.gkancheva.tripmanager.model.expense.ExpenseCategory;
import com.gkancheva.tripmanager.model.expense.RentACar;
import com.gkancheva.tripmanager.repositories.EventExpenseManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentSingleEventOrExpense extends Fragment implements View.OnClickListener{

    private static final String ID = "id", CATEGORY = "category";
    private static final String ACCOMMODATION = "ACCOMMODATION", RENT_A_CAR = "RENT_A_CAR",
            AIRFARE = "AIRFARE", BOAT = "BOAT", BUS = "BUS", TRAIN = "TRAIN", OTHER_TRANSPORT = "OTHER_TRANSPORT",
            FUEL = "FUEL", GIFTS = "GIFTS", MEAL = "MEAL", OTHER_SIMPLE_EXP = "OTHER_SIMPLE_EXP", PUBLIC_TRANSPORTATION = "PUBLIC_TRANSPORTATION",
            SHOPPING = "SHOPPING", TRAVEL_INSURANCE = "TRAVEL_INSURANCE";
    private static final String CHECK_IN = "check-in", PICK_UP = "pick-up";
    private String mCurrentPicturePath;
    private EventExpenseManager mExpenseEventManager;
    private Expense mExpense;
    private Event mEvent, mEvent1;
    private long mId;
    private String mCategory;
    private ImageView mImgProvider, mImageView;
    private Button mButton;
    private EditText mTxtCategory, mTxtNumber, mTxtProvider, mTxtStartPoint, mTxtEndPoint, mTxtStartDate, mTxtEndDate, mTxtStartTime, mTxtEndTime;
    private EditText mTxtTitle, mTxtCurrency, mTxtAmount, mTxtDate, mTxtPaymentMethod;
    private SimpleDateFormat f = new SimpleDateFormat("dd MMM yyyy");
    private SimpleDateFormat tf = new SimpleDateFormat("HH:MM");

    public FragmentSingleEventOrExpense(){}

    public static FragmentSingleEventOrExpense newInstance(String category, long id) {
        FragmentSingleEventOrExpense mFragment = new FragmentSingleEventOrExpense();
        Bundle mArgs = new Bundle();
        mArgs.putLong(ID, id);
        mArgs.putString(CATEGORY, category);
        mFragment.setArguments(mArgs);
        return mFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        mId = args.getLong(ID);
        mCategory = args.getString(CATEGORY);
        mExpenseEventManager = new EventExpenseManager();

        View view = null;
        switch (mCategory) {
            case AIRFARE:
            case BOAT:
            case BUS:
            case OTHER_TRANSPORT:
            case TRAIN:
                view = inflater.inflate(R.layout.fragment_new_transport, container, false);
                mEvent = mExpenseEventManager.findTrById(mId);
                setTransportationViews(mEvent, view);
                break;
            case ACCOMMODATION:
                view = inflater.inflate(R.layout.fragment_new_accomm, container, false);
                mEvent = mExpenseEventManager.findAccommodationById(mId);
                if(((Accommodation)mEvent).getType().equals(CHECK_IN)) {
                    mEvent1 = mExpenseEventManager.findAccommodationById(mEvent.getId() + 1);
                    setAccommRentViews(mEvent, mEvent1, view);
                } else {
                    mEvent1 = mExpenseEventManager.findAccommodationById(mEvent.getId() - 1);
                    setAccommRentViews(mEvent1, mEvent, view);
                }
                break;
            case RENT_A_CAR:
                view = inflater.inflate(R.layout.fragment_new_accomm, container, false);
                mEvent = mExpenseEventManager.findRentACarById(mId);
                if(((RentACar)mEvent).getType().equals(PICK_UP)) {
                    mEvent1 = mExpenseEventManager.findRentACarById(mEvent.getId() + 1);
                    setAccommRentViews(mEvent, mEvent1, view);
                } else {
                    mEvent1 = mExpenseEventManager.findRentACarById(mEvent.getId() - 1);
                    setAccommRentViews(mEvent1, mEvent, view);
                }
                break;
            case FUEL:
            case GIFTS:
            case MEAL:
            case OTHER_SIMPLE_EXP:
            case PUBLIC_TRANSPORTATION:
            case SHOPPING:
            case TRAVEL_INSURANCE:
                view = inflater.inflate(R.layout.fragment_new_expense, container, false);
                mExpense = mExpenseEventManager.findExpenseById(mId);
                setExpenseViews(mExpense, view);
                break;
            default:
                break;
        }
        return view;
    }

    private void setExpenseViews(Expense expense, View v) {
        mTxtTitle = (EditText)v.findViewById(R.id.new_exp_title);
        mTxtTitle.setTextColor(getResources().getColor(R.color.colorTextIcons));
        mTxtTitle.setText(expense.getTitle());
        mTxtTitle.setEnabled(false);
        mTxtCategory = (EditText)v.findViewById(R.id.new_exp_category);
        mTxtCategory.setText(expense.getExpenseCategory().toString());
        mTxtCategory.setEnabled(false);
        mTxtCurrency = (EditText)v.findViewById(R.id.new_exp_currency);
        mTxtCurrency.setText(expense.getCurrency());
        mTxtCurrency.setEnabled(false);
        mTxtAmount = (EditText)v.findViewById(R.id.new_exp_amount);
        mTxtAmount.setText(String.format("%.2f", expense.getPrice()));
        mTxtAmount.setEnabled(false);
        mTxtPaymentMethod = (EditText)v.findViewById(R.id.new_exp_payment);
        mTxtPaymentMethod.setText(expense.getPaymentMethod().toString());
        mTxtPaymentMethod.setEnabled(false);
        mTxtDate = (EditText)v.findViewById(R.id.new_exp_date);
        mTxtDate.setText(f.format(expense.getDate()));
        mTxtDate.setEnabled(false);
        mButton = (Button)v.findViewById(R.id.btn_add_new_exp);
        mImageView = (ImageView)v.findViewById(R.id.image_view);
        if(expense.getReceiptPath() != null) {
            Bitmap receipt = BitmapFactory.decodeFile(expense.getReceiptPath());
            mImageView.setImageBitmap(receipt);
            mButton.setText("Change receipt");
        } else {
            mButton.setText("Add receipt");
        }
        mButton.setOnClickListener(this);
    }

    private void setAccommRentViews(Event startEvent, Event endEvent, View v) {
        mImgProvider = (ImageView)v.findViewById(R.id.ic_provider);
        mImgProvider.setImageResource(setIcon(startEvent.getExpenseCategory()));
        mTxtProvider = (EditText)v.findViewById(R.id.new_accomm_provider);
        mTxtProvider.setText(startEvent.getProvider());
        mTxtProvider.setEnabled(false);
        mTxtNumber = (EditText)v.findViewById(R.id.new_accomm_conf_nb);
        mTxtNumber.setText(startEvent.getConfirmation());
        mTxtNumber.setEnabled(false);
        mTxtStartPoint = (EditText)v.findViewById(R.id.new_accomm_start_location);
        mTxtStartPoint.setText(startEvent.getStartPoint());
        mTxtStartPoint.setEnabled(false);
        mTxtEndPoint = (EditText)v.findViewById(R.id.new_accomm_end_point);
        if(startEvent.getExpenseCategory().equals(ExpenseCategory.ACCOMMODATION)) {
            mTxtEndPoint.setText(R.string.check_out);
        } else {
            mTxtEndPoint.setText(endEvent.getEndPoint());
        }
        mTxtEndPoint.setEnabled(false);
        mTxtStartDate = (EditText)v.findViewById(R.id.new_accomm_start_date);
        mTxtStartDate.setText(f.format(startEvent.getStartDate()));
        mTxtStartDate.setEnabled(false);
        mTxtEndDate = (EditText)v.findViewById(R.id.new_accomm_end_date);
        mTxtEndDate.setText(f.format(endEvent.getStartDate()));
        mTxtEndDate.setEnabled(false);
        mTxtStartTime = (EditText)v.findViewById(R.id.new_accomm_start_time);
        mTxtStartTime.setText(tf.format(startEvent.getStartDate()));
        mTxtStartTime.setEnabled(false);
        mTxtEndTime = (EditText)v.findViewById(R.id.new_accomm_end_time);
        mTxtEndTime.setText(tf.format(endEvent.getStartDate()));
        mTxtEndTime.setEnabled(false);
        setExpenseViews(startEvent, v);
    }

    private void setTransportationViews(Event event, View v) {
        mImgProvider = (ImageView)v.findViewById(R.id.ic_new_tr_company);
        mImgProvider.setImageResource(setIcon(event.getExpenseCategory()));
        mTxtNumber = (EditText)v.findViewById(R.id.new_tr_number);
        mTxtNumber.setText(event.getConfirmation());
        mTxtNumber.setEnabled(false);
        mTxtProvider = (EditText)v.findViewById(R.id.new_tr_company);
        mTxtProvider.setText(event.getProvider());
        mTxtProvider.setEnabled(false);
        mTxtStartPoint = (EditText)v.findViewById(R.id.new_tr_dep_point);
        mTxtStartPoint.setText(event.getStartPoint());
        mTxtStartPoint.setEnabled(false);
        mTxtEndPoint = (EditText)v.findViewById(R.id.new_tr_arr_point);
        mTxtEndPoint.setText(event.getEndPoint());
        mTxtEndPoint.setEnabled(false);
        mTxtStartDate = (EditText)v.findViewById(R.id.new_tr_dep_date);
        mTxtStartDate.setText(f.format(event.getStartDate()));
        mTxtStartDate.setEnabled(false);
        mTxtEndDate = (EditText)v.findViewById(R.id.new_tr_arr_date);
        mTxtEndDate.setText(f.format(event.getEndDate()));
        mTxtEndDate.setEnabled(false);
        mTxtStartTime = (EditText)v.findViewById(R.id.new_tr_dep_time);
        mTxtStartTime.setText(tf.format(event.getStartDate()));
        mTxtStartTime.setEnabled(false);
        mTxtEndTime = (EditText)v.findViewById(R.id.ic_new_tr_arr_time);
        mTxtEndTime.setText(tf.format(event.getEndDate()));
        mTxtEndTime.setEnabled(false);
        setExpenseViews(event, v);
    }

    private int setIcon(ExpenseCategory expenseCategory) {
        switch (expenseCategory) {
            case ACCOMMODATION:
                return R.mipmap.ic_hotel_black_24dp;
            case AIRFARE:
                return R.mipmap.ic_airplane_24dp;
            case BOAT:
                return R.mipmap.ic_boat_24dp;
            case BUS:
                return R.mipmap.ic_bus_24dp;
            case FUEL:
                return R.mipmap.ic_petrol_24dp;
            case GIFTS:
                return R.mipmap.ic_card_giftcard_black_24dp;
            case MEAL:
                return R.mipmap.ic_dining;
            case OTHER_SIMPLE_EXP:
                return R.mipmap.ic_local_offer_black_24dp;
            case OTHER_TRANSPORT:
                return R.mipmap.ic_direction_24dp;
            case PUBLIC_TRANSPORTATION:
                return R.mipmap.ic_bus_24dp;
            case RENT_A_CAR:
                return R.mipmap.ic_road_24dp;
            case SHOPPING:
                return R.mipmap.ic_shopping_cart_black_24dp;
            case TRAIN:
                return R.mipmap.ic_train_24dp;
            case TRAVEL_INSURANCE:
                return R.mipmap.ic_content_paste_black_24dp;
            default:
                return -1;
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_add_new_exp) {
            if(mEvent != null) {
                mEvent.setReceiptPath(capturePhoto());
                mExpenseEventManager.editExpense(mEvent.getId(), mEvent);
            } else {
                mExpense.setReceiptPath(capturePhoto());
                mExpenseEventManager.editExpense(mExpense.getId(), mExpense);
            }
        }
    }

    private String capturePhoto() {
        Intent intentTakePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intentTakePic.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if(photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(getContext(),
                        "com.gkancheva.tripmanager", photoFile);
                intentTakePic.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intentTakePic, 1);
            }
        }
        return mCurrentPicturePath;
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "ReceiptPicture_" + timeStamp + "_";
        File storageDirectory = getActivity().getExternalFilesDir(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, ".jpeg", storageDirectory);
        mCurrentPicturePath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK) {
            galleryAddPic();
            Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPicturePath);
            mImageView.setImageBitmap(bitmap);
        }
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPicturePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }
}
