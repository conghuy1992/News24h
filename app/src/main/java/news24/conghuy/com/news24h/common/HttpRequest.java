package news24.conghuy.com.news24h.common;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import news24.conghuy.com.news24h.common.interfaces.XmlDataCallBack;
import news24.conghuy.com.news24h.model.XmlDto;

/**
 * Created by maidinh on 7/25/2017.
 */

public class HttpRequest {
    private String TAG = "HttpRequest";
    private Context context;

    public HttpRequest(Context context) {
        this.context = context;
    }

    public class processParseXml extends AsyncTask<String, String, List<XmlDto>> {
        String response;
        XmlDataCallBack callBack;

        public processParseXml(String response, XmlDataCallBack callBack) {
            this.response = response;
            this.callBack = callBack;
        }

        @Override
        protected List<XmlDto> doInBackground(String... strings) {
            return Const.parseXml(response);
        }

        @Override
        protected void onPostExecute(List<XmlDto> xmlDtoList) {
            super.onPostExecute(xmlDtoList);
            if (xmlDtoList != null && xmlDtoList.size() > 0) {
                for (int i = 0; i < xmlDtoList.size(); i++) {
                    if (xmlDtoList.get(i).link == null || xmlDtoList.get(i).link.length() == 0) {
                        xmlDtoList.remove(i);
                        i--;
                    }
                }
                if(callBack!=null)  callBack.onSuccess(xmlDtoList);
            } else {
                if(callBack!=null)   callBack.onFail();
            }
        }
    }

    public void getData(final String URL, final XmlDataCallBack callBack) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response == null) response = "";
//                        Log.d(TAG, "report response:" + response);
                        try {
                            response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        response = response.replaceAll(" & ", " &amp; ");
//                        response = response.replaceAll("\\?","&#63;");

                        if (callBack != null) new processParseXml(response, callBack).execute();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse:" + error.toString());
                        if (callBack != null) callBack.onFail();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Apis.TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        stringRequest.setShouldCache(false);

        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }
}
