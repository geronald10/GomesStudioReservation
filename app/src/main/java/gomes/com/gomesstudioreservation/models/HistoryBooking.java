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
    private String reserveTanggal;
    private String reserveBatas;
    private String reservasiRefund;
    private String refundAt;
    private String refundStatus;
    private String roomNama;
    private String studioNama;
    private String jadwal;

    public HistoryBooking() {

    }

    public HistoryBooking(int reservasiId, int studioId, int roomId, String nomorBooking,
                          String namaBand, String tagihan, String status, String waktuBooking,
                          String reserveTanggal, String reserveBatas, String reservasiRefund,
                          String refundAt, String refundStatus, String roomNama, String studioNama,
                          String jadwal) {
        this.reservasiId = reservasiId;
        this.studioId = studioId;
        this.roomId = roomId;
        this.nomorBooking = nomorBooking;
        this.namaBand = namaBand;
        this.tagihan = tagihan;
        this.status = status;
        this.waktuBooking = waktuBooking;
        this.reserveTanggal = reserveTanggal;
        this.reserveBatas = reserveBatas;
        this.reservasiRefund = reservasiRefund;
        this.refundAt = refundAt;
        this.refundStatus = refundStatus;
        this.roomNama = roomNama;
        this.studioNama = studioNama;
        this.jadwal = jadwal;
    }

    public String getReserveTanggal() {
        return reserveTanggal;
    }

    public void setReserveTanggal(String reserveTanggal) {
        this.reserveTanggal = reserveTanggal;
    }

    public String getReserveBatas() {
        return reserveBatas;
    }

    public void setReserveBatas(String reserveBatas) {
        this.reserveBatas = reserveBatas;
    }

    public String getRefundAt() {
        return refundAt;
    }

    public void setRefundAt(String refundAt) {
        this.refundAt = refundAt;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
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
