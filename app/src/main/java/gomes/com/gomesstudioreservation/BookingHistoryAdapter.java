package gomes.com.gomesstudioreservation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import gomes.com.gomesstudioreservation.models.HistoryBooking;
import gomes.com.gomesstudioreservation.utilities.RupiahCurrencyFormat;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.ViewHolder> {

    private Context mContext;
    private List<HistoryBooking> historyBookingList;

    public BookingHistoryAdapter(Context mContext, List<HistoryBooking> historyBookingList) {
        this.mContext = mContext;
        this.historyBookingList = historyBookingList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_history_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        RupiahCurrencyFormat formatter = new RupiahCurrencyFormat();

        holder.kodeBooking.setText(historyBookingList.get(position).getNomorBooking());
        holder.bandName.setText(historyBookingList.get(position).getNamaBand());
        holder.studioName.setText(historyBookingList.get(position).getStudioNama());
        holder.tagihan.setText(formatter.toRupiahFormat(historyBookingList.get(position).getTagihan()));
        holder.tanggalBooking.setText(historyBookingList.get(position).getTanggalBooking());
        switch(historyBookingList.get(position).getStatus()) {
            case "0":
                holder.statusBooked.setVisibility(View.VISIBLE);
                holder.statusConfirmed.setVisibility(View.INVISIBLE);
                holder.statusCanceled.setVisibility(View.INVISIBLE);
                holder.statusFailed.setVisibility(View.INVISIBLE);
                break;
            case "1":
                holder.statusConfirmed.setVisibility(View.VISIBLE);
                holder.statusBooked.setVisibility(View.INVISIBLE);
                holder.statusCanceled.setVisibility(View.INVISIBLE);
                holder.statusFailed.setVisibility(View.INVISIBLE);
                break;
            case "2":
                holder.statusCanceled.setVisibility(View.VISIBLE);
                holder.statusBooked.setVisibility(View.INVISIBLE);
                holder.statusConfirmed.setVisibility(View.INVISIBLE);
                holder.statusFailed.setVisibility(View.INVISIBLE);
                break;
            case "3":
                holder.statusFailed.setVisibility(View.VISIBLE);
                holder.statusBooked.setVisibility(View.INVISIBLE);
                holder.statusConfirmed.setVisibility(View.INVISIBLE);
                holder.statusCanceled.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return historyBookingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView kodeBooking;
        public TextView bandName;
        public TextView studioName;
        public TextView tagihan;
        public Button statusBooked;
        public Button statusConfirmed;
        public Button statusCanceled;
        public Button statusFailed;
        public TextView tanggalBooking;

        public ViewHolder(View itemView) {
            super(itemView);
            kodeBooking = (TextView)itemView.findViewById(R.id.tvKodeBooking);
            bandName = (TextView)itemView.findViewById(R.id.tvBandName);
            studioName = (TextView)itemView.findViewById(R.id.tvStudioName);
            tagihan = (TextView)itemView.findViewById(R.id.tvTotalTagihan);
            statusBooked = (Button)itemView.findViewById(R.id.btn_status_booked);
            statusConfirmed = (Button)itemView.findViewById(R.id.btn_status_confirmed);
            statusCanceled = (Button)itemView.findViewById(R.id.btn_status_canceled);
            statusFailed = (Button)itemView.findViewById(R.id.btn_status_failed);
            tanggalBooking = (TextView)itemView.findViewById(R.id.tvTanggalBooking);
        }
    }
}
