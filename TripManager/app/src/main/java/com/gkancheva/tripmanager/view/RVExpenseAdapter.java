package com.gkancheva.tripmanager.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gkancheva.tripmanager.R;
import com.gkancheva.tripmanager.model.expense.Expense;
import com.gkancheva.tripmanager.model.expense.ExpenseCategory;
import com.gkancheva.tripmanager.model.expense.PaymentMethod;

import java.text.SimpleDateFormat;
import java.util.List;

public class RVExpenseAdapter extends RecyclerView.Adapter<RVExpenseAdapter.ExpenseVHolder> {

    private List<Expense> mExpenses;
    private SimpleDateFormat mFormat = new SimpleDateFormat("EEE, MMM dd, yyyy");
    private int mPayment;
    private Expense mExpense;
    private IOnExpenseClickListener mListener;

    public interface IOnExpenseClickListener {
        public void onExpenseSelected(View v, int i);
    }

    public RVExpenseAdapter(List<Expense> mExpenses, IOnExpenseClickListener mListener) {
        this.mExpenses = mExpenses;
        this.mListener = mListener;
    }

    @Override
    public ExpenseVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_row_expense, parent, false);
        return new ExpenseVHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpenseVHolder holder, int position) {
        mExpense = mExpenses.get(position);
        ExpenseCategory mExpCategory = mExpense.getExpenseCategory();
        holder.mImageView.setImageResource(getIconByCategory(position));
        holder.mTxtTitle.setText(mExpense.getTitle());
        holder.mTxtCatExp.setText(getExpenseCategory(mExpCategory));
        holder.mTxtPaymentMethod.setText(getPaymentMethod(mExpense.getPaymentMethod()));
        holder.mTxtDateOfPayment.setText(mFormat.format(mExpense.getDate()));
        holder.mTxtAmount.setText(String.format("%.2f", mExpense.getPrice()));
    }

    @Override
    public int getItemCount() {
        return this.mExpenses.size();
    }

    public class ExpenseVHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected ImageView mImageView;
        protected TextView mTxtTitle;
        protected TextView mTxtCatExp;
        protected TextView mTxtPaymentMethod;
        protected TextView mTxtDateOfPayment;
        protected TextView mTxtAmount;

        public ExpenseVHolder(View v) {
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.icon_exp);
            mTxtTitle = (TextView) v.findViewById(R.id.exp_title);
            mTxtCatExp = (TextView)v.findViewById(R.id.exp_category);
            mTxtPaymentMethod = (TextView)v.findViewById(R.id.exp_payment_method);
            mTxtDateOfPayment = (TextView)v.findViewById(R.id.exp_date_payment);
            mTxtAmount = (TextView)v.findViewById(R.id.exp_expense_amount);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onExpenseSelected(v, this.getLayoutPosition());
        }
    }

    private int getPaymentMethod(PaymentMethod paymentMethod) {
        mPayment = 0;
        if(paymentMethod == PaymentMethod.BANK_TRANSFER) {
            mPayment = R.string.bank_transfer;
        } else if(paymentMethod == PaymentMethod.CASH) {
            mPayment = R.string.cash;
        } else {
            mPayment = R.string.cc;
        }
        return mPayment;
    }

    private int getExpenseCategory(ExpenseCategory expenseCategory) {
        int category = 0;
        switch (expenseCategory) {
            case ACCOMMODATION:
                category = R.string.accommodation;
                break;
            case AIRFARE:
                category = R.string.airfare;
                break;
            case BOAT:
                category = R.string.boat;
                break;
            case BUS:
                category = R.string.bus;
                break;
            case FUEL:
                category = R.string.fuel;
                break;
            case GIFTS:
                category = R.string.gifts;
                break;
            case MEAL:
                category = R.string.meal;
                break;
            case OTHER_SIMPLE_EXP:
                category = R.string.other;
                break;
            case OTHER_TRANSPORT:
                category = R.string.other_transport;
                break;
            case PUBLIC_TRANSPORTATION:
                category = R.string.public_transport;
                break;
            case RENT_A_CAR:
                category = R.string.rent_a_car;
                break;
            case SHOPPING:
                category = R.string.shopping;
                break;
            case TRAIN:
                category = R.string.train;
                break;
            case TRAVEL_INSURANCE:
                category = R.string.travel_insurance;
                break;
            default:
                break;
        }
        return category;
    }

    private int getIconByCategory(int position) {
        int imageIndex = 0;
        switch (mExpenses.get(position).getExpenseCategory()) {
            case ACCOMMODATION:
                imageIndex =  R.mipmap.ic_hotel_black_24dp;
                break;
            case AIRFARE:
                imageIndex =  R.mipmap.ic_airplane_24dp;
                break;
            case BOAT:
                imageIndex =  R.mipmap.ic_boat_24dp;
                break;
            case BUS:
                imageIndex =  R.mipmap.ic_bus_24dp;
                break;
            case FUEL:
                imageIndex =  R.mipmap.ic_petrol_24dp;
                break;
            case GIFTS:
                imageIndex =  R.mipmap.ic_card_giftcard_black_24dp;
                break;
            case MEAL:
                imageIndex =  R.mipmap.ic_dining;
                break;
            case OTHER_SIMPLE_EXP:
                imageIndex =  R.mipmap.ic_edit;
                break;
            case OTHER_TRANSPORT:
                imageIndex =  R.mipmap.ic_direction_24dp;
                break;
            case PUBLIC_TRANSPORTATION:
                imageIndex =  R.mipmap.ic_bus_24dp;
                break;
            case RENT_A_CAR:
                imageIndex =  R.mipmap.ic_road_24dp;
                break;
            case SHOPPING:
                imageIndex =  R.mipmap.ic_shopping_cart_black_24dp;
                break;
            case TRAIN:
                imageIndex =  R.mipmap.ic_train_24dp;
                break;
            case TRAVEL_INSURANCE:
                imageIndex =  R.mipmap.ic_luggage_24dp;
                break;
            default:
                break;
        }
        return imageIndex;
    }
}
