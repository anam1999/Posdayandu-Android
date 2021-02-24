package com.posdayandu.posdayandu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Imun {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("nama_vaksin")
    @Expose
    private String nama_vaksin;
    @SerializedName("jadwal")
    @Expose
    private String jadwal;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;

    public Imun(int id, String nama, String nama_vaksin, String jadwal, String keterangan) {
        this.id = id;
        this.nama = nama;
        this.nama_vaksin = nama_vaksin;
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

    public String getNama_vaksin() {
        return nama_vaksin;
    }

    public void setNama_vaksin(String nama_vaksin) {
        this.nama_vaksin = nama_vaksin;
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
