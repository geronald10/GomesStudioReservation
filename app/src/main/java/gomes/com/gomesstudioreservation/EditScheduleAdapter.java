package gomes.com.gomesstudioreservation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import gomes.com.gomesstudioreservation.models.Schedule;

public class EditScheduleAdapter extends RecyclerView.Adapter<EditScheduleAdapter.ViewHolder> {

    private Context mContext;
    private List<Schedule> scheduleList;
    private int limitCheck;
    private int numberOfCheckboxesChecked = 0;

    public EditScheduleAdapter(Context mContext, List<Schedule> scheduleList, int limitCheck) {
        this.mContext = mContext;
        this.scheduleList = scheduleList;
        this.limitCheck = limitCheck;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_jadwal_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.scheduleStudioStartTime.setText(scheduleList.get(position).getJadwalStart());
        holder.scheduleStudioEndTime.setText(scheduleList.get(position).getJadwalEnd());

        holder.checkBoxSelected.setOnCheckedChangeListener(null);
        holder.checkBoxSelected.setChecked(scheduleList.get(position).isSelected());

        holder.checkBoxSelected.setChecked(scheduleList.get(position).isSelected());
        if (scheduleList.get(position).isSelected()) {
            numberOfCheckboxesChecked++;
            if (mContext instanceof EditBookingActivity) {
                int scheduleId = scheduleList.get(position).getJadwal();
                ((EditBookingActivity) mContext).saveToList(scheduleId);
            }
        }
        holder.checkBoxSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Log.d("limitChecked", String.valueOf(limitCheck));
                if (isChecked && numberOfCheckboxesChecked >= limitCheck) {
                    holder.checkBoxSelected.setChecked(false);
                    holder.checkBoxSelected.setSelected(false);
                } else {
                    if (isChecked) {
                        numberOfCheckboxesChecked++;
                        if (mContext instanceof EditBookingActivity) {
                            int scheduleId = scheduleList.get(position).getJadwal();
                            ((EditBookingActivity) mContext).saveToList(scheduleId);
                        }
                    } else {
                        numberOfCheckboxesChecked--;
                        if (mContext instanceof EditBookingActivity) {
                            int scheduleId = scheduleList.get(position).getJadwal();
                            ((EditBookingActivity) mContext).removeFromList(scheduleId);
                        }
                    }
                }
                Log.d("numberChecked", String.valueOf(numberOfCheckboxesChecked));
            }
        });
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView scheduleStudioStartTime;
        TextView scheduleStudioEndTime;
        CheckBox checkBoxSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            scheduleStudioStartTime = (TextView) itemView.findViewById(R.id.tv_waktu_mulai);
            scheduleStudioEndTime = (TextView) itemView.findViewById(R.id.tv_waktu_selesai);
            checkBoxSelected = (CheckBox) itemView.findViewById(R.id.cb_select_jadwal);
        }
    }
}
