package me.mwongela.sampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import me.mwongela.sampleapp.adapter.SearchResultAdapter;
import me.mwongela.sampleapp.model.Offence;
import me.mwongela.sampleapp.util.Constants;
import me.mwongela.sampleapp.util.XMLParser;

public class SearchResultsActivity extends AppCompatActivity {


    public static final String KEY_SEARCH_QUERY = "search_query";

    private SearchResultAdapter mAdapter;
    private Handler mHandler;

    String query = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        mHandler = new Handler(Looper.getMainLooper());

        ArrayList<Offence> list = new ArrayList<>();
        mAdapter = new SearchResultAdapter(this, list);

        ListView lvResults = (ListView) findViewById(R.id.lvResults);
        lvResults.setAdapter(mAdapter);
        lvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Offence offence = mAdapter.data.get(position);

                Intent i = new Intent(SearchResultsActivity.this, OffenceActivity.class);
                i.putExtra(OffenceActivity.KEY_OFFENCE_ID, offence.getId() + "");
                startActivity(i);
            }
        });

        Bundle extras = getIntent().getExtras();
        query = extras.getString(KEY_SEARCH_QUERY);

        TextView tvSearchingFor = (TextView) findViewById(R.id.searching_for);
        tvSearchingFor.setText("Searching for: " + query);

        TextView tvNoResults = (TextView) findViewById(R.id.no_results);
        tvNoResults.setText("No records found for: " + query);

        search();
    }

    private void search() {

        if(query.equals("")) {
            Toast.makeText(this, "Invalid search query", Toast.LENGTH_LONG).show();
            return;
        }

        mAdapter.clear();

        final LinearLayout viewNoData = (LinearLayout) findViewById(R.id.layout_no_data);
        viewNoData.setVisibility(View.GONE);

        final LinearLayout viewLoading = (LinearLayout) findViewById(R.id.layout_loading);
        viewLoading.setVisibility(View.VISIBLE);

        StringRequest request = new StringRequest(Request.Method.GET, Constants.ABS_URL + "offences.xml",
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    viewLoading.setVisibility(View.GONE);

                    ArrayList<Offence> data = new ArrayList<>();

                    XMLParser parser = new XMLParser();
                    Document doc = parser.getDomElement(response);
                    NodeList nl = doc.getElementsByTagName(Constants.KEY_OFFENCE);

                    for(int i = 0; i < nl.getLength(); i++) {

                        Offence offence = new Offence();

                        Element e = (Element) nl.item(i);
                        offence.setId(Integer.parseInt(parser.getValue(e, Constants.KEY_OFFENCE_ID)));
                        offence.setName(parser.getValue(e, Constants.KEY_NAME));
                        offence.setIdNumber(parser.getValue(e, Constants.KEY_ID_NO));
                        offence.setRegNo(parser.getValue(e, Constants.KEY_REG_NO));
                        offence.setLocation(parser.getValue(e, Constants.KEY_LOCATION));
                        offence.setFine(Double.parseDouble(parser.getValue(e, Constants.KEY_FINE)));
                        offence.setStatus(parser.getValue(e, Constants.KEY_PAYMENT_STATUS));

                        data.add(offence);

                    }

                    if(data.size() == 0) {
                        viewNoData.setVisibility(View.VISIBLE);
                    }

                    mAdapter.addAll(data);
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    viewLoading.setVisibility(View.GONE);
                    Toast.makeText(SearchResultsActivity.this, "An error occured. Please try again later",
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
