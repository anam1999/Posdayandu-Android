package com.posdayandu.posdayandu.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Video implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("nama_video")
    @Expose
    private String nama_video;
    @SerializedName("judul")
    @Expose
    private String judul;
    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;
    @SerializedName("kategori")
    @Expose
    private String kategori;
    @SerializedName("pemeran")
    @Expose
    private String pemeran;
    @SerializedName("created_at")
    @Expose
    private String created_at;

    public Video(int id, String nama_video, String deskripsi, String judul, String kategori, String pemeran, String created_at) {
        this.id = id;
        this.nama_video = nama_video;
        this.deskripsi = deskripsi;
        this.judul = judul;
        this.kategori = kategori;
        this.pemeran = pemeran;
        this.created_at = created_at;
    }

    protected Video(Parcel in) {
        id = in.readInt();
        nama_video = in.readString();
        judul = in.readString();
        deskripsi = in.readString();
        kategori = in.readString();
        pemeran = in.readString();
        created_at = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama_video() {
        return nama_video;
    }

    public void setNama_video(String nama_video) {
        this.nama_video = nama_video;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi(){
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi){
        this.deskripsi = deskripsi;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getPemeran() {
        return pemeran;
    }

    public void setPemeran(String pemeran) {
        this.pemeran = pemeran;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nama_video);
        dest.writeString(judul);
        dest.writeString(deskripsi);
        dest.writeString(pemeran);
        dest.writeString(kategori);
    }
}
