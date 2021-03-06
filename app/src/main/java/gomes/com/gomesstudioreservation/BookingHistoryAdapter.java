package gomes.com.gomesstudioreservation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import gomes.com.gomesstudioreservation.models.HistoryBooking;
import gomes.com.gomesstudioreservation.utilities.RupiahCurrencyFormat;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.ViewHolder> {

    final private BookingHistoryAdapterOnClickHandler mClickHandler;

    public interface BookingHistoryAdapterOnClickHandler {
        void onClick(int reservasiId, String nomorBooking, String namaBand, String tagihan,
                     String status, String waktuBooking, String reserveTanggal, String reserveBatas,
                     String reservasiRefund, String refundAt, String refundStatus, String roomNama,
                     String studioNama, String jadwal);
    }

    private Context mContext;
    private List<HistoryBooking> historyBookingList;

    public BookingHistoryAdapter(Context mContext, List<HistoryBooking> historyBookingList,
                                 BookingHistoryAdapterOnClickHandler clickHandler) {
        this.mContext = mContext;
        this.historyBookingList = historyBookingList;
        this.mClickHandler = clickHandler;
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
        holder.tanggalBooking.setText(historyBookingList.get(position).getReserveTanggal());
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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
            kodeBooking = (TextView) itemView.findViewById(R.id.tvKodeBooking);
            bandName = (TextView) itemView.findViewById(R.id.tvBandName);
            studioName = (TextView) itemView.findViewById(R.id.tvStudioName);
            tagihan = (TextView) itemView.findViewById(R.id.tvTotalTagihan);
            statusBooked = (Button) itemView.findViewById(R.id.btn_status_booked);
            statusConfirmed = (Button) itemView.findViewById(R.id.btn_status_confirmed);
            statusCanceled = (Button) itemView.findViewById(R.id.btn_status_canceled);
            statusFailed = (Button) itemView.findViewById(R.id.btn_status_failed);
            tanggalBooking = (TextView) itemView.findViewById(R.id.tvTanggalBooking);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(historyBookingList.get(adapterPosition).getReservasiId(),
                    historyBookingList.get(adapterPosition).getNomorBooking(),
                    historyBookingList.get(adapterPosition).getNamaBand(),
                    historyBookingList.get(adapterPosition).getTagihan(),
                    historyBookingList.get(adapterPosition).getStatus(),
                    historyBookingList.get(adapterPosition).getWaktuBooking(),
                    historyBookingList.get(adapterPosition).getReserveTanggal(),
                    historyBookingList.get(adapterPosition).getReserveBatas(),
                    historyBookingList.get(adapterPosition).getReservasiRefund(),
                    historyBookingList.get(adapterPosition).getRefundAt(),
                    historyBookingList.get(adapterPosition).getRefundStatus(),
                    historyBookingList.get(adapterPosition).getRoomNama(),
                    historyBookingList.get(adapterPosition).getStudioNama(),
                    historyBookingList.get(adapterPosition).getJadwal());
        }

    }

    /* Within the RecyclerView.Adapter class */
    // Clean all elements of the recycler
    public void clear() {
        historyBookingList.clear();
        notifyDataSetChanged();
    }
}
