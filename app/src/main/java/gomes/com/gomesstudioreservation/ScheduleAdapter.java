package gomes.com.gomesstudioreservation;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gomes.com.gomesstudioreservation.models.Schedule;
import gomes.com.gomesstudioreservation.utilities.RupiahCurrencyFormat;


public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleAdapterViewHolder> {

    private AdapterCallback mAdapterCallback;

    public interface AdapterCallback {
        void onMethodCallback();
    }

    private LayoutInflater mLayoutInflater;
    public Context mContext;

    private Cursor mCursor;

    public List<Schedule> checkedSchedule = new ArrayList<>();

    public ScheduleAdapter(Context context, AdapterCallback adapterCallback) {
        mContext = context;
        mAdapterCallback = adapterCallback;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ScheduleAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.jadwal_item, viewGroup, false);
        view.setFocusable(true);
        return new ScheduleAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ScheduleAdapterViewHolder holder, final int position) {

        mCursor.moveToPosition(position);
        final String roomId = mCursor.getString(FragmentScheduleByDate.INDEX_JADWAL_ROOM_ID);
        final String tanggal = mCursor.getString(FragmentScheduleByDate.INDEX_JADWAL_TANGGAL);
        final String jadwalId = mCursor.getString(FragmentScheduleByDate.INDEX_JADWAL_ID);
        final String jadwalStart = mCursor.getString(FragmentScheduleByDate.INDEX_JADWAL_START);
        final String jadwalEnd = mCursor.getString(FragmentScheduleByDate.INDEX_JADWAL_END);
        final String hargaSewa = mCursor.getString(FragmentScheduleByDate.INDEX_JADWAL_HARGA);
        RupiahCurrencyFormat toRupiah = new RupiahCurrencyFormat();
        final String harga = toRupiah.toRupiahFormat(String.valueOf(hargaSewa));

        holder.scheduleStudioRoom.setText(roomId);
        holder.scheduleStudioStartTime.setText(jadwalStart);
        holder.scheduleStudioEndTime.setText(jadwalEnd);
        holder.scheduleStudioHarga.setText(harga);
        holder.checkBoxSelected.setChecked(false);
        holder.checkBoxSelected.setTag(mCursor.getPosition());

        holder.checkBoxSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    if (mContext instanceof ManageBookingActivity || holder.checkBoxSelected.isChecked()) {
                        Schedule schedule = new Schedule(tanggal, roomId, jadwalId, jadwalStart, jadwalEnd, hargaSewa, true);
                        ((ManageBookingActivity) mContext).saveToList(schedule);
                    }
                } else {
                    if (mContext instanceof ManageBookingActivity || holder.checkBoxSelected.isChecked()) {
                        Schedule schedule = new Schedule(tanggal, roomId, jadwalId, jadwalStart, jadwalEnd, hargaSewa, true);
                        ((ManageBookingActivity) mContext).removeFromList(schedule);
                    }
                }
            }
        });
        mAdapterCallback.onMethodCallback();
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public static class ScheduleAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView scheduleStudio;
        TextView scheduleStudioRoom;
        TextView scheduleStudioStartTime;
        TextView scheduleStudioEndTime;
        TextView scheduleStudioHarga;
        CheckBox checkBoxSelected;

        ScheduleAdapterViewHolder(View view) {
            super(view);
            scheduleStudio = (TextView) itemView.findViewById(R.id.tv_studio_name);
            scheduleStudioRoom = (TextView) itemView.findViewById(R.id.tv_room);
            scheduleStudioStartTime = (TextView) itemView.findViewById(R.id.tv_waktu_mulai);
            scheduleStudioEndTime = (TextView) itemView.findViewById(R.id.tv_waktu_selesai);
            scheduleStudioHarga = (TextView) itemView.findViewById(R.id.tv_harga);
            checkBoxSelected = (CheckBox) itemView.findViewById(R.id.cb_select_jadwal);
        }
    }
}
