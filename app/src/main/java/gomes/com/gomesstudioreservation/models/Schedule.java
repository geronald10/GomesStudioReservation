package gomes.com.gomesstudioreservation.models;

import java.io.Serializable;

public class Schedule implements Serializable {

    private String tanggal;
    private String roomId;
    private String jadwalId;
    private String jadwalStart;
    private String jadwalEnd;
    private String harga;
    private boolean isSelected;

    public Schedule() {

    }

    public Schedule(String tanggal, String roomId, String jadwalId, String jadwalStart, String jadwalEnd, String harga, boolean isSelected) {
        this.tanggal = tanggal;
        this.roomId = roomId;
        this.jadwalId = jadwalId;
        this.jadwalStart = jadwalStart;
        this.jadwalEnd = jadwalEnd;
        this.harga = harga;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
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

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }
}
