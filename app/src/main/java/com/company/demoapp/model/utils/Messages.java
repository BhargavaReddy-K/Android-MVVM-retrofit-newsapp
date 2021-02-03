package com.company.demoapp.model.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.demoapp.R;
import com.company.demoapp.model.font.TypeFaceSpan;
import com.company.demoapp.model.interfaces.AlertOnClick;
import com.company.demoapp.model.interfaces.IMessages;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Messages implements IMessages {

    private final Context context;
    private Dialog progressDialog;
    private final String FONT_BOLD = "SourceSansPro-Black.ttf";
    private AlertOnClick alertOnClick;

    public Messages(Context context) {
        this.context = context;
    }

    public Messages(AlertOnClick alertOnClick, Context context) {
        this.alertOnClick = alertOnClick;
        this.context = context;
    }


    @Override
    public void showProgressDialog(String message) {
        try {
            progressDialog = new Dialog(context);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.setContentView(R.layout.layout_progress_dialog);
            TextView messageTextView = progressDialog.findViewById(R.id.text_viewMessage);
            messageTextView.setText(message);
            if (progressDialog.getWindow() != null)
                progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setCancelable(false);
            progressDialog.show();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }


    @Override
    public void hideDialog() {
        try {
            progressDialog.dismiss();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

    }


    @Override
    public void loadImagePicasso(final ImageView imageView, String url) {
        Picasso.get().load(url)
                .into(imageView, new Callback() {
                    @Override
                    public void onError(Exception e) {
                    }

                    @Override
                    public void onSuccess() {
                    }
                });
    }

    public String changeDateFormat(String inputFormat, String outputFormat, String inputDate) {
        String outputDate;
        Date parsed;
        inputDate = inputDate.replace("T", " ").replace("Z", "");
        SimpleDateFormat inputDF = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        SimpleDateFormat outputDF = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());
        try {
            parsed = inputDF.parse(inputDate);
            assert parsed != null;
            outputDate = outputDF.format(parsed);
        } catch (Exception e) {
            e.printStackTrace();
            outputDate = inputDate;
        }
        return outputDate;

    }

    public void shareData(String url) {
        try {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");
            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            share.putExtra(Intent.EXTRA_TEXT, url);
            context.startActivity(Intent.createChooser(share, context.getResources().getString(R.string.share)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public SpannableString setTextBold(String string) {
        SpannableString spannableString = new SpannableString(string);
        spannableString.setSpan(new TypeFaceSpan(context, FONT_BOLD), 0, spannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @Override
    public void getChoice(final CharSequence[] choices) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Select");
        alert.setItems(choices, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                alertOnClick.alertOnClick(choices[which].toString());
            }
        });
        alert.show();
    }


    @Override
    public void toastInternet() {
        Toast.makeText(context, R.string.no_network, Toast.LENGTH_LONG).show();

    }

    @Override
    public String getCurrentDate(String format) {
        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = new Date();
        return dateFormat.format(date);
//        return "2021-01-29";
    }

}

