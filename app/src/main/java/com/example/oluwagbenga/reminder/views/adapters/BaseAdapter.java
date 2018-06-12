package com.example.oluwagbenga.reminder.views.adapters;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.gesture.Gesture;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.oluwagbenga.reminder.models.Reminder;
import com.example.oluwagbenga.reminder.views.utils.OnSwipeListener;
import com.example.oluwagbenga.talentbaseitemreminder.R;
import com.example.oluwagbenga.talentbaseitemreminder.databinding.ReminderItemsBinding;

import java.util.List;

public class BaseAdapter  extends RecyclerView.Adapter<BaseAdapter.ReminderViewHolder>{

    private List<Reminder> remList;
    //private ReminderItemsBinding binding;
    private OnItemClickListener mCallback;
    private OnLongPressedListener mLongPressed;
    private Context context;
    private OnDeleteReminderListener mDeleteListener;
    private GestureDetector mGestureDetector;

    public interface OnDeleteReminderListener{
        /**
         *
         * @param reminder is the data we want to delete
         */
        void onDeleteReminder(Reminder reminder, int position);
    }

    public void setOnDeleteReminderListener(OnDeleteReminderListener mCallback){
        this.mDeleteListener = mCallback;
    }

    public interface OnItemClickListener{
        void onItemClick(Reminder reminder);
    }

    public interface OnLongPressedListener{
        void onLongPressed(Reminder reminder);
    }

    public void setOnLongPressedListener(OnLongPressedListener mCallback){
        this.mLongPressed = mCallback;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mCallback = listener;
    }

    public BaseAdapter(List<Reminder> reminders){
        this.remList = reminders;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
       // mGestureDetector = new
        context = parent.getContext();
        ReminderItemsBinding remBinding = DataBindingUtil.inflate(inflater, R.layout.reminder_items, parent, false);
        return new ReminderViewHolder(remBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull final ReminderViewHolder holder, int position) {
        final Reminder reminder =remList.get(position);
        holder.mBinding.setReminder(reminder);

        final LinearLayout layout = holder.mBinding.backG;
        layout.animate().translationX(layout.getWidth()).translationX(0).setDuration(400);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onItemClick(reminder);
            }
        });

        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mLongPressed.onLongPressed(reminder);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return remList == null? 0: remList.size();
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder{

        ReminderItemsBinding mBinding;

        public ReminderViewHolder(ReminderItemsBinding binding) {
            super(binding.getRoot());
            this.mBinding = binding;
        }
    }
}
