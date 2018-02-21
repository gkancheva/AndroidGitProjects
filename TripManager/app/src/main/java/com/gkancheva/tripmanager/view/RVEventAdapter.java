package com.gkancheva.tripmanager.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gkancheva.tripmanager.R;
import com.gkancheva.tripmanager.model.expense.Accommodation;
import com.gkancheva.tripmanager.model.expense.Event;
import com.gkancheva.tripmanager.model.expense.ExpenseCategory;
import com.gkancheva.tripmanager.model.expense.RentACar;
import com.gkancheva.tripmanager.model.expense.Transportation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RVEventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> mObjects;
    private List<Event> mEvents;
    private Event mEvent;
    private final int TRANSPORT = 0, EVENT = 1, DATE = 2;
    private SimpleDateFormat mFormatTime = new SimpleDateFormat("HH:mm");
    private IOnEventClickListener mListener;
    private Date mDate;
    private SimpleDateFormat f = new SimpleDateFormat("EEE, dd MMM yyyy");

    public interface IOnEventClickListener {
        public void onEventSelected(View v, int i);
        public void onEventLongClicked(View v, int i);
    }
    public RVEventAdapter(List<Event> mEvents, IOnEventClickListener mListener) {
        List<Object> objs = new ArrayList<>();
        mDate = mEvents.get(0).getStartDate();
        objs.add(mDate);
        objs.add(mEvents.get(0));
        for(int i = 1; i < mEvents.size(); i++) {
            if(mDate.getDate() != mEvents.get(i).getStartDate().getDate()) {
                mDate = mEvents.get(i).getStartDate();
                objs.add(mDate);
            }
            objs.add(mEvents.get(i));
        }
        this.mObjects = objs;
        this.mEvents = mEvents;
        this.mListener = mListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case TRANSPORT:
                View vTransport = inflater.inflate(R.layout.rv_row_event_transport, parent, false);
                holder = new TransportViewHolder(vTransport);
                break;
            case EVENT:
                View vEvent = inflater.inflate(R.layout.rv_row_event_accomm, parent, false);
                holder = new AccRentViewHolder(vEvent);
                break;
            case DATE:
                View mDate = inflater.inflate(R.layout.rv_row_date, parent, false);
                holder = new DateViewHolder(mDate);
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TRANSPORT:
                TransportViewHolder tHolder = (TransportViewHolder)holder;
                configureViewHolderTransport(tHolder, position);
                break;
            case EVENT:
                AccRentViewHolder aHolder = (AccRentViewHolder)holder;
                configureViewHolderAccommodation(aHolder, position);
                break;
            case DATE:
                DateViewHolder dHolder = (DateViewHolder)holder;
                dHolder.mTxtDate.setText(f.format(mObjects.get(position)));
            default:
                break;
        }
    }

    private void configureViewHolderAccommodation(AccRentViewHolder h, int i) {
        mEvent = (Event)mObjects.get(i);
        h.mImageView.setImageResource(getIconByCategory(mEvent.getExpenseCategory()));
        h.mTxtProvider.setText(mEvent.getProvider());
        h.mTxtStartTime.setText(mFormatTime.format(mEvent.getStartDate()));
        h.mTxtConfirmation.setText(mEvent.getConfirmation());
        h.mTxtConfDesc.setText(R.string.confirmation_number);
        switch (mEvent.getExpenseCategory()) {
            case ACCOMMODATION:
                if(((Accommodation) mEvent).getType().equals("check-in")) {
                    h.mTxtStartDesc.setText(R.string.check_in_time);
                } else {
                    h.mTxtStartDesc.setText(R.string.check_out_time);
                }
                break;
            case RENT_A_CAR:
                if(((RentACar) mEvent).getType().equals("pick-up")) {
                    h.mTxtStartDesc.setText(R.string.pick_up_time);
                } else {
                    h.mTxtStartDesc.setText(R.string.drop_off_time);
                }
                break;
            default:
                break;
        }
    }

    private void configureViewHolderTransport(TransportViewHolder h, int i) {
        mEvent = (Event)mObjects.get(i);
        h.mImageView.setImageResource(getIconByCategory(mEvent.getExpenseCategory()));
        h.mTxtTitle.setText(mEvent.getTitle());
        h.mTxtStartTime.setText(mFormatTime.format(mEvent.getStartDate()));
        h.mTxtStartPoint.setText(mEvent.getStartPoint());
        h.mTxtEndTime.setText(mFormatTime.format(mEvent.getEndDate()));
        h.mTxtEndPoint.setText(mEvent.getEndPoint());
        h.mTxtConfirmation.setText(mEvent.getConfirmation());
        h.mTxtProvider.setText(mEvent.getProvider());
    }

    @Override
    public int getItemViewType(int pos) {
        if((mObjects.get(pos) instanceof Accommodation) ||
                (mObjects.get(pos) instanceof RentACar)) {
            return EVENT;
        } else if (mObjects.get(pos) instanceof Transportation){
            return TRANSPORT;
        } else if(mObjects.get(pos) instanceof Date){
            return DATE;
        } else {
            return -1;
        }
    }

    private int getIconByCategory(ExpenseCategory expCat) {
        int imageIndex = 0;
        switch (expCat) {
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
            case OTHER_TRANSPORT:
                imageIndex =  R.mipmap.ic_direction_24dp;
                break;
            case RENT_A_CAR:
                imageIndex =  R.mipmap.ic_road_24dp;
                break;
            case TRAIN:
                imageIndex =  R.mipmap.ic_train_24dp;
                break;
            default:
                break;
        }
        return imageIndex;
    }

    @Override
    public int getItemCount() {
        return this.mObjects.size();
    }

    public class TransportViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        protected ImageView mImageView;
        protected TextView mTxtTitle;
        protected TextView mTxtStartTime;
        protected TextView mTxtStartPoint;
        protected TextView mTxtEndTime;
        protected TextView mTxtEndPoint;
        protected TextView mTxtConfirmation;
        protected TextView mTxtProvider;

        public TransportViewHolder(View v) {
            super(v);
            mImageView = (ImageView)v.findViewById(R.id.icon_event);
            mTxtTitle = (TextView)v.findViewById(R.id.event_title);
            mTxtStartTime = (TextView)v.findViewById(R.id.event_start_time);
            mTxtStartPoint = (TextView)v.findViewById(R.id.event_start_point);
            mTxtEndTime = (TextView)v.findViewById(R.id.event_end_time);
            mTxtEndPoint = (TextView)v.findViewById(R.id.event_end_point);
            mTxtConfirmation = (TextView)v.findViewById(R.id.event_confirmation);
            mTxtProvider = (TextView)v.findViewById(R.id.event_provider);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getPositionInEvents(this.getLayoutPosition());
            mListener.onEventSelected(v, position);
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getPositionInEvents(this.getLayoutPosition());
            mListener.onEventLongClicked(v, position);
            return true;
        }
    }

    public class DateViewHolder extends RecyclerView.ViewHolder {
        protected TextView mTxtDate;

        public DateViewHolder(View v) {
            super(v);
            mTxtDate = (TextView)v.findViewById(R.id.date_row);
        }
    }

    //This is the holder to be invoked if category is instanceOf Accommodation or RentACar
    public class AccRentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        protected ImageView mImageView;
        protected TextView mTxtProvider;
        protected TextView mTxtStartTime;
        protected TextView mTxtStartDesc;
        protected TextView mTxtConfirmation;
        protected TextView mTxtConfDesc;

        public AccRentViewHolder(View v) {
            super(v);
            mImageView = (ImageView)v.findViewById(R.id.icon_event);
            mTxtProvider = (TextView)v.findViewById(R.id.event_provider);
            mTxtStartTime = (TextView)v.findViewById(R.id.event_time);
            mTxtStartDesc = (TextView)v.findViewById(R.id.event_start_desc);
            mTxtConfirmation = (TextView)v.findViewById(R.id.event_confirmation);
            mTxtConfDesc = (TextView)v.findViewById(R.id.event_conf_desc);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getPositionInEvents(this.getLayoutPosition());
            mListener.onEventSelected(v, position);
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getPositionInEvents(this.getLayoutPosition());
            mListener.onEventLongClicked(v, position);
            return true;
        }
    }

    private int getPositionInEvents(int layoutPosition) {
        int pos = 0;
        Event ev = (Event) mObjects.get(layoutPosition);
        for(int i = 0; i < mEvents.size(); i++){
            if(mEvents.get(i).getId().equals(ev.getId())) {
                pos = i;
                break;
            }
        }
        return pos;
    }
}
