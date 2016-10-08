package me.mwongela.sampleapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import me.mwongela.sampleapp.util.Constants;

public class MainActivity extends AppCompatActivity {

    Button btnSearch;
    EditText edSearchQuery;
    Spinner spSearchType;

    private int searchType = Constants.SEARCH_TYPE_ID_NO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = (Button) findViewById(R.id.btnSearch);
        edSearchQuery = (EditText) findViewById(R.id.edSearchQuery);
        spSearchType = (Spinner) findViewById(R.id.spSearchType);

        spSearchType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = spSearchType.getSelectedItem().toString();

                String searchOptions[] = getResources().getStringArray(R.array.search_options);

                if(searchOptions[0].equals(type)) {
                    searchType = Constants.SEARCH_TYPE_ID_NO;
                } else if(searchOptions[1].equals(type)) {
                    searchType = Constants.SEARCH_TYPE_REG_NO;
                } else if(searchOptions[2].equals(type)) {
                    searchType = Constants.SEARCH_TYPE_OFFENCE_ID;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String query = edSearchQuery.getText().toString().trim();

                if(query.equals("")) {
                    Toast.makeText(MainActivity.this, "Please type something in order to search", Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                if(searchType == Constants.SEARCH_TYPE_OFFENCE_ID) {
                    // Start OffenceActivity Activity and pass on the query (OffenceActivity ID)
                    Intent i = new Intent(MainActivity.this, OffenceActivity.class);
                    i.putExtra(OffenceActivity.KEY_OFFENCE_ID, query);
                    startActivity(i);
                } else {

                    // Start search results activity and pass the query
                    Intent i = new Intent(MainActivity.this, SearchResultsActivity.class);
                    i.putExtra(SearchResultsActivity.KEY_SEARCH_QUERY, query);
                    startActivity(i);
                }

            }
        });
    }

}
