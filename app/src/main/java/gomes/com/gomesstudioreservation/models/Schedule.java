package gomes.com.gomesstudioreservation.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Schedule implements Parcelable {
    public static final Parcelable.Creator<Schedule> CREATOR
            = new Parcelable.Creator<Schedule>() {
        public Schedule createFromParcel(Parcel in) {
            Log.d("create form parcel", "Schedule");
            return new Schedule(in);
        }
        public Schedule[] newArray(int size) {
            return new Schedule[size];
        }
    };

    private String tanggal;
    private String code;
    private String status;
    private String roomId;
    private String jadwalId;
    private String jadwalStart;
    private String jadwalEnd;

    public Schedule() {

    }

    public Schedule(Parcel input) {
        tanggal = input.readString();
        code = input.readString();
        status = input.readString();
        roomId = input.readString();
        jadwalId = input.readString();
        jadwalStart = input.readString();
        jadwalEnd = input.readString();
    }

    public Schedule(String tanggal, String code, String status, String roomId, String jadwalId, String jadwalStart, String jadwalEnd) {
        this.tanggal = tanggal;
        this.code = code;
        this.status = status;
        this.roomId = roomId;
        this.jadwalId = jadwalId;
        this.jadwalStart = jadwalStart;
        this.jadwalEnd = jadwalEnd;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getJadwalId() {
        return jadwalId;
    }

    public void setJadwalId(String jadwalId) {
        this.jadwalId = jadwalId;
    }

    public String getJadwalStart() {
        return jadwalStart;
    }

    public void setJadwalStart(String jadwalStart) {
        this.jadwalStart = jadwalStart;
    }

    public String getJadwalEnd() {
        return jadwalEnd;
    }

    public void setJadwalEnd(String jadwalEnd) {
        this.jadwalEnd = jadwalEnd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tanggal);
        dest.writeString(code);
        dest.writeString(status);
        dest.writeString(roomId);
        dest.writeString(jadwalId);
        dest.writeString(jadwalStart);
        dest.writeString(jadwalEnd);
    }
}
