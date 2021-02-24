package com.posdayandu.posdayandu.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Kms {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("usia")
    @Expose
    private String usia;
    @SerializedName("tanggal")
    @Expose
    private String tanggal;
    @SerializedName("bb")
    @Expose
    private String bb;
    @SerializedName("tinggi")
    @Expose
    private String tinggi;
    @SerializedName("suhu")
    @Expose
    private String suhu;
    @SerializedName("lingkar_kepala")
    @Expose
    private String lingkarkepala;
    @SerializedName("jadwal")
    @Expose
    private String jadwal;

    public Kms(int id, String nama, String tanggal, String usia, String bb, String tinggi, String suhu, String lingkarkepala, String jadwal) {
        this.id = id;
        this.nama = nama;
        this.tanggal = tanggal;
        this.usia = usia;
        this.bb = bb;
        this.tinggi = tinggi;
        this.suhu = suhu;
        this.lingkarkepala = lingkarkepala;
        this.jadwal = jadwal;
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

    public String getUsia() {
        return usia;
    }

    public void setUsia(String usia) {
        this.usia = usia;
    }

    public String getTanggal(){
        return tanggal;
    }

    public void setTanggal(String tanggal){
        this.tanggal = tanggal;
    }

    public String getBb() {
        return bb;
    }

    public void setBb(String bb) {
        this.bb = bb;
    }

    public String getTinggi() {
        return tinggi;
    }

    public void setTinggi(String tinggi) {
        this.tinggi = tinggi;
    }

    public String getSuhu() {
        return suhu;
    }

    public void setSuhu(String suhu) {
        this.suhu = suhu;
    }

    public String getLingkarkepala() {
        return lingkarkepala;
    }

    public void setLingkarkepala(String lingkarkepala) {
        this.lingkarkepala = lingkarkepala;
    }

    public String getJadwal() {
        return jadwal;
    }

    public void setJadwal(String jadwal) {
        this.jadwal = jadwal;
    }
}
