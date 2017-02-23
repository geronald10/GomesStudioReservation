package gomes.com.gomesstudioreservation.models;

import java.io.Serializable;

public class HistoryBooking implements Serializable {

    private int reservasiId;
    private int studioId;
    private int roomId;
    private String nomorBooking;
    private String namaBand;
    private String tagihan;
    private String status;
    private String waktuBooking;
    private String tanggalBooking;
    private String reservasiRefund;
    private String roomNama;
    private String studioNama;
    private String jadwal;

    public HistoryBooking() {

    }

    public HistoryBooking(int reservasiId, int studioId, int roomId, String nomorBooking,
                          String namaBand, String tagihan, String status, String waktuBooking,
                          String tanggalBooking, String reservasiRefund, String roomNama,
                          String studioNama, String jadwal) {
        this.reservasiId = reservasiId;
        this.studioId = studioId;
        this.roomId = roomId;
        this.nomorBooking = nomorBooking;
        this.namaBand = namaBand;
        this.tagihan = tagihan;
        this.status = status;
        this.waktuBooking = waktuBooking;
        this.tanggalBooking = tanggalBooking;
        this.reservasiRefund = reservasiRefund;
        this.roomNama = roomNama;
        this.studioNama = studioNama;
        this.jadwal = jadwal;
    }

    public int getReservasiId() {
        return reservasiId;
    }

    public void setReservasiId(int reservasiId) {
        this.reservasiId = reservasiId;
    }

    public int getStudioId() {
        return studioId;
    }

    public void setStudioId(int studioId) {
        this.studioId = studioId;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public String getNomorBooking() {
        return nomorBooking;
    }

    public void setNomorBooking(String nomorBooking) {
        this.nomorBooking = nomorBooking;
    }

    public String getNamaBand() {
        return namaBand;
    }

    public void setNamaBand(String namaBand) {
        this.namaBand = namaBand;
    }

    public String getTagihan() {
        return tagihan;
    }

    public void setTagihan(String tagihan) {
        this.tagihan = tagihan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getWaktuBooking() {
        return waktuBooking;
    }

    public void setWaktuBooking(String waktuBooking) {
        this.waktuBooking = waktuBooking;
    }

    public String getTanggalBooking() {
        return tanggalBooking;
    }

    public void setTanggalBooking(String tanggalBooking) {
        this.tanggalBooking = tanggalBooking;
    }

    public String getReservasiRefund() {
        return reservasiRefund;
    }

    public void setReservasiRefund(String reservasiRefund) {
        this.reservasiRefund = reservasiRefund;
    }

    public String getRoomNama() {
        return roomNama;
    }

    public void setRoomNama(String roomNama) {
        this.roomNama = roomNama;
    }

    public String getStudioNama() {
        return studioNama;
    }

    public void setStudioNama(String studioNama) {
        this.studioNama = studioNama;
    }

    public String getJadwal() {
        return jadwal;
    }

    public void setJadwal(String jadwal) {
        this.jadwal = jadwal;
    }
}
