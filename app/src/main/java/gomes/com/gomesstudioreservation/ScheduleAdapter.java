package gomes.com.gomesstudioreservation;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import gomes.com.gomesstudioreservation.models.Schedule;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleAdapterViewHolder> {

    private final Context mContext;

    final private ScheduleAdapterOnClickHandler mClickHandler;

    public interface ScheduleAdapterOnClickHandler {
        void onClick(long date);
    }

    private Cursor mCursor;

    private ArrayList<Schedule> mListSchedule = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public ScheduleAdapter(Context context, ScheduleAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public ScheduleAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.jadwal_item, viewGroup, false);
        view.setFocusable(true);
        return new ScheduleAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ScheduleAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String roomId = mCursor.getString(FragmentScheduleByDate.INDEX_JADWAL_ROOM_ID);
        holder.scheduleStudioRoom.setText();
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

    class ScheduleAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView scheduleStudio;
        final TextView scheduleStudioRoom;
        final TextView scheduleStudioStartTime;
        final TextView scheduleStudioEndTime;

        public ScheduleAdapterViewHolder(View view) {
            super(view);
            scheduleStudio = (TextView) itemView.findViewById(R.id.tv_studio_name);
            scheduleStudioRoom = (TextView) itemView.findViewById(R.id.tv_room);
            scheduleStudioStartTime = (TextView) itemView.findViewById(R.id.tv_waktu_mulai);
            scheduleStudioEndTime = (TextView) itemView.findViewById(R.id.tv_waktu_selesai);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
        }
    }
}
