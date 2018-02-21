package com.gkancheva.tripmanager.controller.expense;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gkancheva.tripmanager.MainActivity;
import com.gkancheva.tripmanager.R;
import com.gkancheva.tripmanager.model.expense.Accommodation;
import com.gkancheva.tripmanager.model.expense.Expense;
import com.gkancheva.tripmanager.model.expense.ExpenseCategory;
import com.gkancheva.tripmanager.model.expense.PaymentMethod;
import com.gkancheva.tripmanager.model.expense.RentACar;
import com.gkancheva.tripmanager.model.expense.Transportation;
import com.gkancheva.tripmanager.model.trip.Trip;
import com.gkancheva.tripmanager.repositories.EventExpenseManager;
import com.gkancheva.tripmanager.repositories.TripManager;
import com.gkancheva.tripmanager.view.DatePickerFragment;
import com.gkancheva.tripmanager.view.DoneOnEditorActionListener;
import com.gkancheva.tripmanager.view.TimePickerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FragmentAddNewExp extends Fragment implements View.OnClickListener{

    private static final String TRIP_INDEX = "Trip index", CATEGORY = "Category";
    private static final String START_POINT = "start_point", END_POINT = "end_point", START_DATE = "start_date",
            END_DATE = "end_date", START_TIME = "start_time", END_TIME = "end_time", PROVIDER = "provider";
    private static final String TRANSPORTATION = "Transportation", EXPENSE = "Expense", RENT_A_CAR = "Rent a car", ACCOMMODATION = "Accommodation";

    private long mTripIndex;
    private String mCategory;
    private TripManager mTripManager;
    private EventExpenseManager mEventExpenseManager;
    private Trip mTrip;
    private ImageView mImgProvider;
    private EditText mTxtCategory, mTxtNumber, mTxtProvider, mTxtStartPoint, mTxtEndPoint, mTxtStartDate, mTxtEndDate, mTxtStartTime, mTxtEndTime;
    private EditText mTxtTitle, mTxtCurrency, mTxtAmount, mTxtDate, mTxtPaymentMethod;
    private Button mBtnSave;
    private String mNumber, mProvider, mStartPoint, mEndPoint, mStrDate, mStrStartDate, mStrEndDate, mStrStartTime, mStrEndTime, mTitle, mCurrency, mPayment;
    private Double mPrice;
    private ExpenseCategory mCat;
    private PaymentMethod mPaymentMethod;
    private Date mDate, mStartDateTime, mEndDateTime;

    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat formatDateTime = new SimpleDateFormat("dd/MM/yyyy,HH:mm");

    public FragmentAddNewExp(){}

    public static FragmentAddNewExp newInstance(long tripIndex, String category) {
        FragmentAddNewExp mFragment = new FragmentAddNewExp();
        Bundle mArgs = new Bundle();
        mArgs.putLong(TRIP_INDEX, tripIndex);
        mArgs.putString(CATEGORY, category);
        mFragment.setArguments(mArgs);
        return mFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        mTripIndex = args.getLong(TRIP_INDEX);
        mCategory = args.getString(CATEGORY);
        View view = null;
        switch (mCategory) {
            case TRANSPORTATION:
                view = inflater.inflate(R.layout.fragment_new_transport, container, false);
                break;
            case EXPENSE:
                view = inflater.inflate(R.layout.fragment_new_expense, container, false);
                break;
            case ACCOMMODATION:
            case RENT_A_CAR:
                view = inflater.inflate(R.layout.fragment_new_accomm, container, false);
                break;
            default:
                break;
        }
        setViewsByCategory(mCategory, view);

        mTripManager = new TripManager();
        mTrip = mTripManager.findTripById(mTripIndex);

        ((MainActivity)getActivity()).updateToolbar(R.string.adding_new_plan, "",R.string.to_, mTrip.getName());
        return view;
    }

    private void setViewsByCategory(String category, View v) {
        switch (category) {
            case TRANSPORTATION:
                settingTransportationViews(v);
                break;
            case EXPENSE:
                settingExpenseViews(v);
                break;
            case ACCOMMODATION:
            case RENT_A_CAR:
                settingAccommRentViews(category, v);
                break;
            default:
                break;
        }
    }

    private void settingAccommRentViews(String cat, View v) {
        mImgProvider = (ImageView)v.findViewById(R.id.ic_provider);
        mImgProvider.setImageResource(setIcon(cat));
        mTxtProvider = (EditText)v.findViewById(R.id.new_accomm_provider);
        mTxtProvider.setHint(getHintByCat(cat, PROVIDER));
        mTxtProvider.setOnEditorActionListener(new DoneOnEditorActionListener());
        mTxtNumber = (EditText)v.findViewById(R.id.new_accomm_conf_nb);
        mTxtNumber.setHint(R.string.confirmation_number);
        mTxtNumber.setOnEditorActionListener(new DoneOnEditorActionListener());
        mTxtStartPoint = (EditText)v.findViewById(R.id.new_accomm_start_location);
        mTxtStartPoint.setHint(getHintByCat(cat, START_POINT));
        mTxtStartPoint.setOnEditorActionListener(new DoneOnEditorActionListener());
        mTxtEndPoint = (EditText)v.findViewById(R.id.new_accomm_end_point);
        mTxtEndPoint.setHint(getHintByCat(cat, END_POINT));
        mTxtEndPoint.setOnEditorActionListener(new DoneOnEditorActionListener());
        mTxtStartDate = (EditText)v.findViewById(R.id.new_accomm_start_date);
        mTxtStartDate.setHint(getHintByCat(cat, START_DATE));
        mTxtStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    DialogFragment fr = new DatePickerFragment(v);
                    fr.show(getActivity().getFragmentManager(), "date_picker");
                }
            }
        });
        mTxtStartDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStrStartDate = mTxtStartDate.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        mTxtEndDate = (EditText)v.findViewById(R.id.new_accomm_end_date);
        mTxtEndDate.setHint(getHintByCat(cat, END_DATE));
        mTxtEndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    DialogFragment fr = new DatePickerFragment(v);
                    fr.show(getActivity().getFragmentManager(), "date_picker");
                }
            }
        });
        mTxtEndDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStrEndDate = mTxtEndDate.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        mTxtStartTime = (EditText)v.findViewById(R.id.new_accomm_start_time);
        mTxtStartTime.setHint(getHintByCat(cat, START_TIME));
        mTxtStartTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    DialogFragment fr = new TimePickerFragment(v);
                    fr.show(getActivity().getFragmentManager(), "time_picker");
                }
            }
        });
        mTxtStartTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStrStartTime = mTxtStartTime.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        mTxtEndTime = (EditText)v.findViewById(R.id.new_accomm_end_time);
        mTxtEndTime.setHint(getHintByCat(cat, END_TIME));
        mTxtEndTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    DialogFragment fr = new TimePickerFragment(v);
                    fr.show(getActivity().getFragmentManager(), "time_picker");
                }
            }
        });
        mTxtEndTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStrEndTime = mTxtEndTime.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        setViewsByCategory(EXPENSE, v);
    }

    private void settingExpenseViews(View v) {
        mTxtTitle = (EditText)v.findViewById(R.id.new_exp_title);
        mTxtTitle.setOnEditorActionListener(new DoneOnEditorActionListener());
        mTxtCategory = (EditText)v.findViewById(R.id.new_exp_category);
        mTxtCategory.setText(mCategory);
        mTxtCategory.setOnClickListener(this);
        mTxtCurrency = (EditText)v.findViewById(R.id.new_exp_currency);
        mTxtCurrency.setOnClickListener(this);
        mTxtAmount = (EditText)v.findViewById(R.id.new_exp_amount);
        mTxtAmount.setOnEditorActionListener(new DoneOnEditorActionListener());
        mTxtPaymentMethod = (EditText)v.findViewById(R.id.new_exp_payment);
        mTxtPaymentMethod.setOnClickListener(this);
        mTxtDate = (EditText)v.findViewById(R.id.new_exp_date);
        mTxtDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    DialogFragment fr = new DatePickerFragment(v);
                    fr.show(getActivity().getFragmentManager(), "date_picker");
                }
            }
        });
        mTxtDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStrDate = mTxtDate.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        mBtnSave = (Button)v.findViewById(R.id.btn_add_new_exp);
        mBtnSave.setOnClickListener(this);
    }

    private void settingTransportationViews(View v) {
        mTxtNumber = (EditText)v.findViewById(R.id.new_tr_number);
        mTxtNumber.setHint(R.string.confirmation_number);
        mTxtNumber.setOnEditorActionListener(new DoneOnEditorActionListener());
        mTxtProvider = (EditText)v.findViewById(R.id.new_tr_company);
        mTxtProvider.setHint(R.string.company);
        mTxtProvider.setOnEditorActionListener(new DoneOnEditorActionListener());
        mTxtStartPoint = (EditText)v.findViewById(R.id.new_tr_dep_point);
        mTxtStartPoint.setHint(R.string.dep_point);
        mTxtStartPoint.setOnEditorActionListener(new DoneOnEditorActionListener());
        mTxtEndPoint = (EditText)v.findViewById(R.id.new_tr_arr_point);
        mTxtEndPoint.setHint(R.string.arr_point);
        mTxtEndPoint.setOnEditorActionListener(new DoneOnEditorActionListener());
        mTxtStartDate = (EditText)v.findViewById(R.id.new_tr_dep_date);
        mTxtStartDate.setHint(R.string.dep_date);
        mTxtStartDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    DialogFragment fr = new DatePickerFragment(v);
                    fr.show(getActivity().getFragmentManager(), "date_picker");
                }
            }
        });
        mTxtStartDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStrStartDate = mTxtStartDate.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        mTxtEndDate = (EditText)v.findViewById(R.id.new_tr_arr_date);
        mTxtEndDate.setHint(R.string.arr_date);
        mTxtEndDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    DialogFragment fr = new DatePickerFragment(v);
                    fr.show(getActivity().getFragmentManager(), "date_picker");
                }
            }
        });
        mTxtEndDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStrEndDate = mTxtEndDate.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        mTxtStartTime = (EditText)v.findViewById(R.id.new_tr_dep_time);
        mTxtStartTime.setHint(R.string.dep_time);
        mTxtStartTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    DialogFragment fr = new TimePickerFragment(v);
                    fr.show(getActivity().getFragmentManager(), "time_picker");
                }
            }
        });
        mTxtStartTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStrStartTime = mTxtStartTime.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        mTxtEndTime = (EditText)v.findViewById(R.id.ic_new_tr_arr_time);
        mTxtEndTime.setHint(R.string.arr_time);
        mTxtEndTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    DialogFragment fr = new TimePickerFragment(v);
                    fr.show(getActivity().getFragmentManager(), "time_picker");
                }
            }
        });
        mTxtEndTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mStrEndTime = mTxtEndTime.getText().toString();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        setViewsByCategory(EXPENSE, v);
    }

    private int getHintByCat(String category, String args) {
        if(category.equals(ACCOMMODATION)) {
            switch (args) {
                case PROVIDER:
                    return R.string.hotel_name;
                case START_POINT:
                    return R.string.hotel_address;
                case END_POINT:
                    return R.string.check_out;
                case START_DATE:
                    return R.string.check_in_date;
                case END_DATE:
                    return R.string.check_out_date;
                case START_TIME:
                    return R.string.check_in_time;
                case END_TIME:
                    return R.string.check_out_time;
                default:
                    return -1;
            }
        } else {
            switch (args) {
                case PROVIDER:
                    return R.string.company;
                case START_POINT:
                    return R.string.pick_up_location;
                case END_POINT:
                    return R.string.drop_off_location;
                case START_DATE:
                    return R.string.pick_up_date;
                case END_DATE:
                    return R.string.drop_off_date;
                case START_TIME:
                    return R.string.pick_up_time;
                case END_TIME:
                    return R.string.drop_off_time;
                default:
                    return -1;
            }
        }
    }

    private int setIcon(String category) {
        if(category.equals(ACCOMMODATION)) {
            return R.mipmap.ic_hotel_black_24dp;
        }
        return R.mipmap.ic_road_24dp;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_add_new_exp) {
            if(validateInputs(mCategory)) {
                mEventExpenseManager = new EventExpenseManager();
                switch (mCategory) {
                    case TRANSPORTATION:
                        Transportation tr = new Transportation(mCat, mTrip, mStartPoint, mEndPoint,
                                mStartDateTime, mEndDateTime, mProvider, mNumber, mTitle,mPrice, mDate, mCurrency, mPaymentMethod);
                        mEventExpenseManager.saveNewExpense(tr);
                        ((MainActivity)getActivity()).scheduleNotificationForEvent(mStartDateTime, mCat.toString(), mTitle);
                        break;
                    case ACCOMMODATION:
                        Accommodation acc1 = new Accommodation(mCat, mTrip, mProvider, mStartPoint, mNumber,
                                mStartDateTime, mTitle,mPrice, mDate, mCurrency, mPaymentMethod, "check-in");
                        Accommodation acc2 = new Accommodation(mCat, mTrip, mProvider, mStartPoint, mNumber,
                                mTitle, mEndDateTime, "check-out");
                        mEventExpenseManager.saveNewExpense(acc1);
                        mEventExpenseManager.saveNewExpense(acc2);
                        ((MainActivity)getActivity()).scheduleNotificationForEvent(mStartDateTime, mCat.toString() + " - " + acc1.getType(), mTitle);
                        ((MainActivity)getActivity()).scheduleNotificationForEvent(mStartDateTime, mCat.toString() + " - " + acc2.getType(), mTitle);
                        break;
                    case RENT_A_CAR:
                        RentACar rent1 = new RentACar(mCat, mTrip, mProvider, mNumber, mStartPoint,
                                mStartDateTime, mTitle,mPrice, mDate, mCurrency, mPaymentMethod, "pick-up");
                        RentACar rent2 = new RentACar(mCat, mTrip, mProvider, mNumber, mEndPoint,
                                mTitle, mEndDateTime, "drop-off");
                        mEventExpenseManager.saveNewExpense(rent1);
                        mEventExpenseManager.saveNewExpense(rent2);
                        ((MainActivity)getActivity()).scheduleNotificationForEvent(mStartDateTime, mCat.toString() + " - " + rent1.getType(), mTitle);
                        ((MainActivity)getActivity()).scheduleNotificationForEvent(mStartDateTime, mCat.toString() + " - " + rent2.getType(), mTitle);
                        break;
                    case EXPENSE:
                        Expense exp = new Expense(mTrip, mCat, mTitle, mPrice, mDate, mCurrency, mPaymentMethod);
                        mEventExpenseManager.saveNewExpense(exp);
                        break;
                }
                ((MainActivity)getActivity()).removeAllFragmentsFromBackStack();
                ((MainActivity)getActivity()).showSingleTripInfo(mTrip.getId());
                mTripManager = new TripManager();
                Toast.makeText(getContext(), R.string.successfully_saved_expense, Toast.LENGTH_SHORT).show();
            }
        }
        if(v.getId() == R.id.new_exp_currency){
            showCurrencyDialog(mTxtCurrency);
        }
        if(v.getId() == R.id.new_exp_payment) {
            showPaymentDialog(mTxtPaymentMethod);
        }
        if(v.getId() == R.id.new_exp_category) {
            if(mCategory.equals(TRANSPORTATION)) {
                showCategoryTypeDialog(mTxtCategory, TRANSPORTATION);
            }
            if(mCategory.equals(EXPENSE)) {
                showCategoryTypeDialog(mTxtCategory, EXPENSE);
            }
        }
    }

    private boolean validateInputs(String cat) {
        boolean hasNoErrors = true;
        switch (cat) {
            case TRANSPORTATION:
                hasNoErrors = validateTransportationInput();
                break;
            case ACCOMMODATION:
            case RENT_A_CAR:
                hasNoErrors = validateAccommInputs(cat);
                break;
            default:
                break;
        }
        if(hasNoErrors) {
            hasNoErrors = validateExpenseInput();
        }
        return hasNoErrors;
    }

    private boolean validateAccommInputs(String cat) {
        if(mTxtNumber != null) {
            mNumber = mTxtNumber.getText().toString();
        } else {
            mNumber = "";
        }
        if(mTxtProvider != null && !mTxtProvider.getText().toString().isEmpty()) {
            mProvider = mTxtProvider.getText().toString();
            if(mTxtStartPoint != null && !mTxtStartPoint.getText().toString().isEmpty()) {
                mStartPoint = mTxtStartPoint.getText().toString();
                if(mTxtEndPoint != null && mTxtEndPoint.getText().toString().isEmpty()) {
                    if(cat.equals(RENT_A_CAR)) {
                        mEndPoint = mTxtEndPoint.getText().toString();
                    }
                    if(mStrStartDate != null && mStrStartDate.length() > 0 &&
                            mStrStartTime != null && mStrStartTime.length() > 0 &&
                            mStrEndDate != null && mStrEndDate.length() > 0 &&
                            mStrEndTime != null && mStrEndTime.length() > 0) {
                        try {
                            mStartDateTime = formatDateTime.parse(mStrStartDate + "," + mStrStartTime);
                            mEndDateTime = formatDateTime.parse(mStrEndDate + "," + mStrEndTime);
                        } catch (ParseException e) {
                            mTxtStartDate.setError(getResources().getString(R.string.valid_date));
                            return false;
                        }
                    } else {
                        if(mTxtStartDate.getText().toString().isEmpty()) {
                            mTxtStartDate.setError(getResources().getString(R.string.no_empty_date));
                        } else if(mTxtEndDate.getText().toString().isEmpty()){
                            mTxtStartDate.setError(getResources().getString(R.string.no_empty_date));
                        } else if(mTxtStartTime.getText().toString().isEmpty()) {
                            mTxtStartTime.setError(getResources().getString(R.string.no_empty_time));
                        } else {
                            mTxtEndTime.setError(getResources().getString(R.string.no_empty_time));
                        }
                        return false;
                    }
                } else {
                    mTxtEndPoint.setError(getResources().getString(R.string.provide_location));
                    return false;
                }
            } else {
                mTxtStartPoint.setError(getResources().getString(R.string.provide_location));
                return false;
            }
        } else {
            mTxtProvider.setError(getResources().getString(R.string.provide_provider));
            return false;
        }
        return true;
    }

    private boolean validateExpenseInput() {
        if(mTxtCategory != null && !mTxtCategory.getText().toString().isEmpty()) {
            mCat = getExpenseCategoryType(mTxtCategory.getText().toString());
        }
        if(mCurrency == null) {
            mTxtCurrency.setError(getResources().getString(R.string.select_currency));
            return false;
        } else {
            if(mTxtTitle != null && !mTxtTitle.getText().toString().isEmpty()) {
                mTitle = mTxtTitle.getText().toString();
                if(mTxtAmount != null && !mTxtAmount.getText().toString().isEmpty()) {
                    mPrice = Double.parseDouble(mTxtAmount.getText().toString());
                    if(mPayment != null) {
                        mPaymentMethod = getPaymentType(mPayment);
                        if(mStrDate != null && mStrDate.length() > 0) {
                            try {
                                mDate = format.parse(mStrDate);
                            } catch (ParseException e) {
                                mTxtDate.setError(getResources().getString(R.string.valid_date));
                                return false;
                            }
                        } else {
                            mTxtDate.setError(getResources().getString(R.string.no_empty_date));
                            return false;
                        }
                    } else {
                        mTxtPaymentMethod.setError(getResources().getString(R.string.select_payment_method));
                        return false;
                    }
                } else {
                    mTxtAmount.setError(getResources().getString(R.string.provide_amount));
                    return false;
                }
            } else {
                mTxtTitle.setError(getResources().getString(R.string.provide_title));
                return false;
            }
        }
        return true;
    }

    private ExpenseCategory getExpenseCategoryType(String s) {
        if(s.equals("Airfare")) {
            return ExpenseCategory.AIRFARE;
        } else if(s.equals("Boat")) {
            return ExpenseCategory.BOAT;
        } else if (s.equals("Bus")) {
            return ExpenseCategory.BUS;
        } else if(s.equals("Train")) {
            return ExpenseCategory.TRAIN;
        } else if(s.equals("Other transport")) {
            return ExpenseCategory.OTHER_TRANSPORT;
        } else if(s.equals("Accommodation")) {
            return ExpenseCategory.ACCOMMODATION;
        } else if(s.equals("Rent a car")) {
            return ExpenseCategory.RENT_A_CAR;
        } else if(s.equals("Fuel")) {
            return ExpenseCategory.FUEL;
        } else if(s.equals("Gifts")) {
            return ExpenseCategory.GIFTS;
        } else if(s.equals("Meal")) {
            return ExpenseCategory.MEAL;
        } else if(s.equals("Shopping")) {
            return ExpenseCategory.SHOPPING;
        } else if(s.equals("Travel insurance")) {
            return ExpenseCategory.TRAVEL_INSURANCE;
        } else if(s.equals("Public transport")) {
            return ExpenseCategory.PUBLIC_TRANSPORTATION;
        } else {
            return  ExpenseCategory.OTHER_SIMPLE_EXP;
        }
    }

    private PaymentMethod getPaymentType(String pay) {
        PaymentMethod pm = null;
        switch (pay) {
            case "Credit card":
                pm = PaymentMethod.CREDIT_CARD;
                break;
            case "Cash":
                pm = PaymentMethod.CASH;
                break;
            case "Bank transfer":
                pm = PaymentMethod.BANK_TRANSFER;
                break;
            default:
                break;
        }
        return pm;
    }

    private boolean validateTransportationInput() {
        if(mTxtNumber != null) {
            mNumber = mTxtNumber.getText().toString();
        } else {
            mNumber = "";
        }
        if(mTxtProvider != null) {
            mProvider = mTxtProvider.getText().toString();
        } else {
            mProvider = "";
        }
        if(mTxtStartPoint != null && !mTxtStartPoint.getText().toString().isEmpty()) {
            mStartPoint = mTxtStartPoint.getText().toString();
            if(mTxtEndPoint != null && !mTxtEndPoint.getText().toString().isEmpty()) {
                mEndPoint = mTxtEndPoint.getText().toString();
                if(mStrStartDate != null && mStrStartDate.length() > 0 &&
                        mStrStartTime != null && mStrStartTime.length() > 0) {
                    try {
                        mStartDateTime = formatDateTime.parse(mStrStartDate + "," + mStrStartTime);
                    } catch (ParseException e){
                        mTxtStartDate.setError(String.valueOf(R.string.valid_date));
                        mTxtStartTime.setError(String.valueOf(R.string.valid_time));
                        return false;
                    }
                    if(mStrEndDate != null && mStrStartTime.length() > 0 &&
                            mStrEndTime != null && mStrEndTime.length() > 0) {
                        try {
                            mEndDateTime = formatDateTime.parse(mStrEndDate + "," + mStrEndTime);
                        } catch (ParseException e){
                            mTxtEndDate.setError(String.valueOf(R.string.valid_date));
                            mTxtEndTime.setError(String.valueOf(R.string.valid_time));
                            return false;
                        }
                    }
                } else {
                    if(mTxtStartDate.getText().toString().isEmpty()) {
                        mTxtStartDate.setError(getResources().getString(R.string.no_empty_date));
                    } else if(mTxtStartTime.getText().toString().isEmpty()) {
                        mTxtStartTime.setError(getResources().getString(R.string.no_empty_time));
                    } else if(mTxtEndDate.getText().toString().isEmpty()) {
                        mTxtEndDate.setError(getResources().getString(R.string.no_empty_date));
                    } else {
                        mTxtEndTime.setError(getResources().getString(R.string.no_empty_time));
                    }
                    return false;
                }
            } else {
                mTxtEndPoint.setError(getResources().getString(R.string.valid_arrival_point));
                return false;
            }
        } else {
            mTxtStartPoint.setError(getResources().getString(R.string.valid_departure_point));
            return false;
        }
        return true;
    }

    private void showCategoryTypeDialog(final EditText mTxtView, String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        switch (type) {
            case TRANSPORTATION:
                builder.setTitle(R.string.transportation);
                builder.setItems(R.array.tr_categories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Resources r = getActivity().getResources();
                        mTxtView.setText(r.getStringArray(R.array.tr_categories)[i]);
                        dialog.dismiss();
                    }
                });
                break;
            case EXPENSE:
                builder.setTitle(R.string.category);
                builder.setItems(R.array.exp_categories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Resources r = getActivity().getResources();
                        mTxtView.setText(r.getStringArray(R.array.exp_categories)[i]);
                        dialog.dismiss();
                    }
                });
                break;
            default:
                mTxtView.setText(type);
        }
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showCurrencyDialog(final TextView mTxtView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.currency);
        builder.setItems(R.array.currencies, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Resources res = getActivity().getResources();
                mCurrency = (res.getStringArray(R.array.currencies))[i];
                mTxtView.setText(mCurrency);
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showPaymentDialog(final EditText mTxtPaymentMethod) {
        AlertDialog.Builder b = new AlertDialog.Builder(getActivity());
        b.setTitle("Payment type");
        b.setItems(R.array.payment_methods, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Resources res = getActivity().getResources();
                mPayment = (res.getStringArray(R.array.payment_methods))[i];
                mTxtPaymentMethod.setText(mPayment);
                dialog.dismiss();
            }
        });
        AlertDialog alert = b.create();
        alert.show();
    }
}
