package com.posdayandu.posdayandu.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MessageKeluhan implements Parcelable {

    String id, dari,ke,pesan,dibaca,tanggal;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getDari() {
        return dari;
    }
    public void setDari(String dari) {
        this.dari = dari;
    }

    public void setKe(String ke) {
        this.ke = ke;
    }
    public String getKe() {
        return ke;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }
    public String getPesan() {
        return pesan;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
    public String getTanggal() {
        return tanggal;
    }

    public MessageKeluhan() {
    }

    protected MessageKeluhan(Parcel in) {
        id = in.readString();
        dari = in.readString();
        ke = in.readString();
        pesan = in.readString();
        tanggal= in.readString();

    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(dari);
        dest.writeString(ke);
        dest.writeString(pesan);
        dest.writeString(tanggal);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MessageKeluhan> CREATOR = new Creator<MessageKeluhan>() {
        @Override
        public MessageKeluhan createFromParcel(Parcel in) {
            return new MessageKeluhan(in);
        }

        @Override
        public MessageKeluhan[] newArray(int size) {
            return new MessageKeluhan[size];
        }
    };
}