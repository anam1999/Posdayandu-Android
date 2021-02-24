package com.posdayandu.posdayandu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KontrolHamil {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("kondisi_kehamilan")
    @Expose
    private String kondisi_kehamilan;
    @SerializedName("jadwal")
    @Expose
    private String jadwal;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;

    public KontrolHamil(int id, String nama, String kondisi_kehamilan, String jadwal, String keterangan) {
        this.id = id;
        this.nama = nama;
        this.kondisi_kehamilan = kondisi_kehamilan;
        this.jadwal = jadwal;
        this.keterangan = keterangan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getKontrol_kehamilan() {
        return kondisi_kehamilan;
    }

    public void setKontrol_kehamilan(String kondisi_kehamilan) {
        this.kondisi_kehamilan = kondisi_kehamilan;
    }

    public String getJadwal() {
        return jadwal;
    }

    public void setJadwal(String jadwal) {
        this.jadwal = jadwal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan){
        this.keterangan = keterangan;
    }
}
