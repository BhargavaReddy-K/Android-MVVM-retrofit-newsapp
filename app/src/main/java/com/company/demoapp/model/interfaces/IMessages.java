package com.company.demoapp.model.interfaces;


import android.text.SpannableString;
import android.widget.ImageView;


public interface IMessages {

    void showProgressDialog(String message);

    void hideDialog();

    void toast(String message);

    void toastInternet();

    String getCurrentDate(String format);

    void loadImagePicasso(ImageView imageView, String url);

    String changeDateFormat(String inputFormat, String outputFormat, String inputDate);

    SpannableString setTextBold(String string);

    void getChoice(CharSequence[] choices);

    void shareData(String url);

}
