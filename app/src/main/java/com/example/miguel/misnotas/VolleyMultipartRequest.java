package com.example.miguel.misnotas;

/**
 * Created by 79812 on 23/11/2017.
 */


import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.util.CharsetUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

public class VolleyMultipartRequest extends Request<String> {
    MultipartEntityBuilder entity = MultipartEntityBuilder.create();
    HttpEntity httpentity;
    private final Response.Listener<String> mListener;
    private final List<File> mFileParts;
    private final Map<String, String> mStringPart;
    private final Map<String, String> header;
    private String arrayName;
    public VolleyMultipartRequest(String url, Response.ErrorListener errorListener,
                                  Response.Listener<String> listener, List<File> files,
                                  Map<String, String> mStringPart, Map<String, String> header, String arrayName) {
        super(Method.POST, url, errorListener);
        this.mListener = listener;
        this.mFileParts = files;
        this.mStringPart = mStringPart;
        this.header = header;
        this.arrayName = arrayName;
        entity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        try {
            entity.setCharset(CharsetUtils.get("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        buildMultipartEntity();
        httpentity = entity.build();
    }

    private void buildMultipartEntity() {
        Log.d("Number", ""+mFileParts.size());

        if (mFileParts.size()>0) {
            for (File f : mFileParts) {
                entity.addPart(arrayName, new FileBody(f, ContentType.create("audio/*"), f.getName()));
            }
        }
        //entity.addPart(FILE_PART_NAME, new FileBody(mFilePart, ContentType.create("image/jpeg"), mFilePart.getName()));
        if (mStringPart != null) {
            for (Map.Entry<String, String> entry : mStringPart.entrySet()) {
                entity.addTextBody(entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    public String getBodyContentType() {
        return httpentity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            httpentity.writeTo(bos);
        } catch (IOException e) {
            VolleyLog.e("IOException writing to ByteArrayOutputStream");
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            System.out.println("Network Response " + new String(response.data, "UTF-8"));
            return Response.success(new String(response.data, "UTF-8"),
                    getCacheEntry());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Response.success(new String(response.data), getCacheEntry());
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (header == null) {
            return super.getHeaders();
        } else {
            return header;
        }
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}