package gomes.com.gomesstudioreservation.models;

import java.io.Serializable;

public class Schedule implements Serializable {

    private String tanggal;
    private String roomId;
    private String jadwalId;
    private int jadwal;
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

    public Schedule(int jadwal, String jadwalStart, String jadwalEnd, boolean isSelected) {
        this.jadwal = jadwal;
        this.jadwalStart = jadwalStart;
        this.jadwalEnd = jadwalEnd;
        this.isSelected = isSelected;
    }

    public int getJadwal() {
        return jadwal;
    }

    public void setJadwal(int jadwal) {
        this.jadwal = jadwal;
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

    @Override
    public boolean equals(Object obj) {
        return (this.tanggal.equals(((Schedule) obj).tanggal) &&
                (this.roomId.equals(((Schedule) obj).roomId) &&
                        (this.jadwalId.equals(((Schedule) obj).jadwalId) &&
                                (this.jadwalStart.equals(((Schedule) obj).jadwalStart) &&
                                        (this.jadwalEnd.equals(((Schedule) obj).jadwalEnd) &&
                                                (this.harga.equals(((Schedule) obj).harga) &&
                                                        (this.isSelected == (((Schedule) obj).isSelected))))))));
    }
}
