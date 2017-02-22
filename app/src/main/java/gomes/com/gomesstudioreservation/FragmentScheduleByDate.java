package gomes.com.gomesstudioreservation;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import gomes.com.gomesstudioreservation.data.ReservationContract;

public class FragmentScheduleByDate extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        ScheduleAdapter.AdapterCallback {

    private final String TAG = FragmentScheduleByDate.class.getSimpleName();

    public static final String[] MAIN_SCHEDULE_PROJECTION = {
            ReservationContract.JadwalEntry.COLUMN_TANGGAL,
            ReservationContract.JadwalEntry.COLUMN_ROOM_ID,
            ReservationContract.JadwalEntry.COLUMN_JADWAL_ID,
            ReservationContract.JadwalEntry.COLUMN_JADWAL_START,
            ReservationContract.JadwalEntry.COLUMN_JADWAL_END,
            ReservationContract.JadwalEntry.COLUMN_HARGA
    };

    public static final int INDEX_JADWAL_TANGGAL = 0;
    public static final int INDEX_JADWAL_ROOM_ID = 1;
    public static final int INDEX_JADWAL_ID = 2;
    public static final int INDEX_JADWAL_START = 3;
    public static final int INDEX_JADWAL_END = 4;
    public static final int INDEX_JADWAL_HARGA = 5;

    private static final int ID_JADWAL_LOADER = 50;

    private ScheduleAdapter mScheduleAdapter;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;
    private String bundleTanggal;

    private ProgressBar mLoadingIndicator;

    public FragmentScheduleByDate() {

    }

    public static FragmentScheduleByDate newInstance(int position, String tanggal) {
        Log.d("Fragment newInstance", "masuk sini");
        FragmentScheduleByDate fragment = new FragmentScheduleByDate();
        Bundle args = new Bundle();
        args.putString("tanggal", tanggal);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onMethodCallback() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_jadwal_by_date, container, false);
        Bundle bundle = getArguments();
        if(bundle != null) {
            bundleTanggal = bundle.getString("tanggal");
        }
        mRecyclerView = (RecyclerView)layout.findViewById(R.id.rvJadwalByDate);
        mLoadingIndicator = (ProgressBar)layout.findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.hasFixedSize();

        mScheduleAdapter = new ScheduleAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mScheduleAdapter);

        showLoading();

        getLoaderManager().initLoader(ID_JADWAL_LOADER, null, this);

        return layout;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle args) {
        switch (loaderId) {
            case ID_JADWAL_LOADER:
                Log.d("From Fragment", "masuk sini");
                Uri jadwalQueryUri = ReservationContract.JadwalEntry.CONTENT_URI;
                String sortOrder = ReservationContract.JadwalEntry.COLUMN_ROOM_ID + " ASC";
                String selection = ReservationContract.JadwalEntry.COLUMN_TANGGAL + "=?";
                String selectionArgs = bundleTanggal;

                return new CursorLoader(getActivity(),
                        jadwalQueryUri,
                        MAIN_SCHEDULE_PROJECTION,
                        selection,
                        new String[]{selectionArgs},
                        sortOrder);
            default:
                throw new RuntimeException("Loader Not Implemented:" + loaderId);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mScheduleAdapter.swapCursor(data);
        if(mPosition == RecyclerView.NO_POSITION)
            mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        if(data.getCount() != 0) {
            showJadwalDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mScheduleAdapter.swapCursor(null);
    }

    private void showJadwalDataView() {
        /* First, hide the loading indicator */
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        /* Finally, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }
    private void showLoading() {
        /* Then, hide the weather data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Finally, show the loading indicator */
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }
}
