package me.iwf.photopicker.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by donglua on 15/6/30.
 */
public class Photo implements Parcelable {

    private int id;
    private String path;
    private boolean local = true;

    public Photo(int id, String path) {
        this.id = id;
        this.path = path;
    }

    public void setLocal(boolean local) {
        this.local = local;
    }

    public boolean isLocal() {
        return local;
    }

    public Photo() {
    }

    protected Photo(Parcel in) {
        id = in.readInt();
        path = in.readString();
        local = in.readByte() != 0;
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Photo)) return false;

        Photo photo = (Photo) o;

        return id == photo.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(path);
        dest.writeByte((byte) (local ? 1 : 0));

    }
}
