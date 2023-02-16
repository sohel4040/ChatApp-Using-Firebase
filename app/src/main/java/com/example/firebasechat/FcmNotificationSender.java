package com.example.firebasechat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import android.app.Activity;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
public class FcmNotificationSender {

        String userFcmToken;
        String title;
        String body;
        Context mContext;
        Activity mActivity;


        private RequestQueue requestQueue;
        private final String postUrl = "https://fcm.googleapis.com/fcm/send";
        private final String fcmServerKey ="AAAABaffckk:APA91bEc-64BNTIF_Cxzd612qBOtKKSMVsbMTiY7c1EjqSG7RxiQuTIQZ5LSC2XWAWucQ28kCZrXpiyUTGLuax14YjGiVdKN9m0ZXk2ONNfMoNNkwh2gZsQjywPjl0IxUo3rZUisw3Y0";

        public FcmNotificationSender(String userFcmToken, String title, String body, Context mContext, Activity mActivity) {
            this.userFcmToken = userFcmToken;
            this.title = title;
            this.body = body;
            this.mContext = mContext;
            this.mActivity = mActivity;


        }

        public void SendNotifications() {

            requestQueue = Volley.newRequestQueue(mActivity);
            JSONObject mainObj = new JSONObject();
            try {
                mainObj.put("to", userFcmToken);
                JSONObject notiObject = new JSONObject();
                notiObject.put("title", title);
                notiObject.put("body", body);
                notiObject.put("icon", "icon"); // enter icon that exists in drawable only



                mainObj.put("notification", notiObject);


                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // code run is got error

                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {


                        Map<String, String> header = new HashMap<>();
                        header.put("content-type", "application/json");
                        header.put("authorization", "key=" + fcmServerKey);
                        return header;


                    }
                };
                requestQueue.add(request);


            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
    }

