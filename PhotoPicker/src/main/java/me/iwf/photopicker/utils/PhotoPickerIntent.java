package me.iwf.photopicker.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import me.iwf.photopicker.PhotoPickerActivity;
import me.iwf.photopicker.entity.Photo;

/**
 * Created by donglua on 15/7/2.
 */
public class PhotoPickerIntent extends Intent {

  private PhotoPickerIntent() {
  }

  private PhotoPickerIntent(Intent o) {
    super(o);
  }

  private PhotoPickerIntent(String action) {
    super(action);
  }

  private PhotoPickerIntent(String action, Uri uri) {
    super(action, uri);
  }

  private PhotoPickerIntent(Context packageContext, Class<?> cls) {
    super(packageContext, cls);
  }

  public PhotoPickerIntent(Context packageContext) {
    super(packageContext, PhotoPickerActivity.class);
  }

  public void setPhotoCount(int photoCount) {
    this.putExtra(PhotoPickerActivity.EXTRA_MAX_COUNT, photoCount);
  }

  public void setShowCamera(boolean showCamera) {
    this.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, showCamera);
  }

  public void setShowGif(boolean showGif) {
    this.putExtra(PhotoPickerActivity.EXTRA_SHOW_GIF, showGif);
  }
  public void setSeletedPhotos(List<Photo> photos){
    this.putParcelableArrayListExtra(PhotoPickerActivity.KEY_ALREADY_SELECTED_PHOTOS, (ArrayList<? extends Parcelable>) photos);
  }

}
