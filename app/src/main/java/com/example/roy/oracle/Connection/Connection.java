package com.example.roy.oracle.Connection;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.example.roy.oracle.R;

import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Roy on 3/27/2018.
 */

public class Connection extends AsyncTask<Void,Void,Void> {

    private FormBody.Builder parametrs;
    private String url;
    private MessageParse parseInterface;
    private String response;

    private Activity activity;
    private Dialog main_dialog;
    private String requestGetOrPost;
    private long startTimeForRequest, endTimeForRequest;
    private boolean isJsonPost = false;
    private JSONObject jsonParams;
    private int response_code;
    private String request_method = null;
    private int flag;

    public Connection(FormBody.Builder parametrs, String url, MessageParse parseInterface, Activity activity, String requestGetOrPost, int flag) {
        this.parametrs = parametrs;
        this.url = url;
        this.parseInterface = parseInterface;
        this.activity = activity;
        this.requestGetOrPost = requestGetOrPost;
        this.flag = flag;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getResponse(String isPost) {
        final SSLContext sslContext;
        RequestBody requestBody;
        OkHttpClient okHttpClient = new OkHttpClient();
        SSLSocketFactory sslSocketFactory;
        final TrustManager[] trustAllCerts = getTrustManager();
        requestBody = parametrs.build();

        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
            if(flag==1) {
                okHttpClient = new OkHttpClient.Builder()
                        .sslSocketFactory(sslSocketFactory)
                        .hostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String s, SSLSession sslSession) {
                                return true;
                            }
                        }).connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(600, TimeUnit.SECONDS)
                        .build();
            } else {
                okHttpClient = new OkHttpClient.Builder()
                        .sslSocketFactory(sslSocketFactory)
                        .hostnameVerifier(new HostnameVerifier() {
                            @Override
                            public boolean verify(String s, SSLSession sslSession) {
                                return true;
                            }
                        }).connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(2, TimeUnit.SECONDS)
                        .build();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        SharedPreferences prefs = activity.getSharedPreferences("roy", MODE_PRIVATE);
        Request request;

            if (isPost.equals("GET")) {
                Log.e("HERE", "---");
                request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();
            } else {
                request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();
            }

        Log.e("Url",url+parametrs);

        try (Response response = okHttpClient.newCall(request).execute()) {

            String message = response.body().string();
            Log.d("getResponse","getResponseMessage: "+ message);
            return message;

        } catch (IOException e) {
            e.printStackTrace();
            return "Error !!!";
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected Void doInBackground(Void... voids) {
        if (isJsonPost) {

        } else {
            response = getResponse(requestGetOrPost);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if (activity== null) return;
        parseInterface.parseResponse(response);
    }


    private static TrustManager[] getTrustManager() {
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }
        };
        return trustAllCerts;
    }
}
