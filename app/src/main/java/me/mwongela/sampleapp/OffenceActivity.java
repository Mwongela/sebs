package me.mwongela.sampleapp;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import me.mwongela.sampleapp.util.Constants;
import me.mwongela.sampleapp.util.XMLParser;

public class OffenceActivity extends AppCompatActivity {

    public static final String KEY_OFFENCE_ID = "offence_id";

    private String offenceId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offence);

        Bundle extras = getIntent().getExtras();
        offenceId = extras.getString(KEY_OFFENCE_ID);

        getOffenceDetails();
    }

    private void getOffenceDetails() {

        if(offenceId.equals("")) {
            Toast.makeText(this, "Data cannot be loaded for an empty offence ID", Toast.LENGTH_LONG).show();
            return;
        }

        final ScrollView svData = (ScrollView) findViewById(R.id.data);
        svData.setVisibility(View.GONE);

        final LinearLayout viewNoData = (LinearLayout) findViewById(R.id.layout_no_data);
        viewNoData.setVisibility(View.GONE);

        final LinearLayout viewLoading = (LinearLayout) findViewById(R.id.layout_loading);
        viewLoading.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, Constants.ABS_URL + "offence.xml",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        viewLoading.setVisibility(View.GONE);

                        XMLParser parser = new XMLParser();
                        Document doc = parser.getDomElement(response);
                        NodeList nl = doc.getElementsByTagName(Constants.KEY_OFFENCE);

                        if(nl.getLength() > 0) {
                            Element e = (Element) nl.item(0);

                            ((TextView) findViewById(R.id.offence_id)).setText(parser.getValue(e, Constants.KEY_OFFENCE_ID));
                            ((TextView) findViewById(R.id.name)).setText(parser.getValue(e, Constants.KEY_NAME));
                            ((TextView) findViewById(R.id.id_number)).setText(parser.getValue(e, Constants.KEY_ID_NO));
                            ((TextView) findViewById(R.id.reg_no)).setText(parser.getValue(e, Constants.KEY_REG_NO));
                            ((TextView) findViewById(R.id.location)).setText(parser.getValue(e, Constants.KEY_LOCATION));
                            ((TextView) findViewById(R.id.fine)).setText(parser.getValue(e, Constants.KEY_FINE));

                            TextView tvStatus = (TextView) findViewById(R.id.status);
                            String status = parser.getValue(e, Constants.KEY_PAYMENT_STATUS);
                            tvStatus.setText(status);

                            if(status.equals("PAID")) {
                                tvStatus.setTextColor(Color.GREEN);
                            } else {
                                tvStatus.setTextColor(Color.RED);
                            }

                            svData.setVisibility(View.VISIBLE);

                        } else {
                            viewNoData.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        viewLoading.setVisibility(View.GONE);
                        Toast.makeText(OffenceActivity.this, "An error occured. Please try again later",
                                Toast.LENGTH_LONG).show();

                        viewNoData.setVisibility(View.VISIBLE);
                    }
                })/*{

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", "Androidhive");
                params.put("email", "abc@androidhive.info");
                params.put("password", "password123");

                return params;
            }

        }*/;

        AppController.getInstance().addToRequestQueue(request);

    }
}
