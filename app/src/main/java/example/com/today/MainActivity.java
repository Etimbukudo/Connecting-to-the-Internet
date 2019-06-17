package example.com.today;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

import example.com.today.utilities.NetworkUtils;


public class MainActivity extends AppCompatActivity {

    //COMPLETED - Create a EditText variable to store a reference to the Search EditText
    private EditText SearchBoxEditText;

    // COMPLETED - Create a TextView variable to store a reference to the Display TextView
    private TextView mUrlDisplayTextView;

    // COMPLETED - Create a TextView variable to store a reference to the ResultsTextView
    private TextView mSearchResultsTextView;

    // COMPLETED (12) Create a TextView variable to store a reference to the error message TextView
    private TextView mErrorMessageDisplay;

    // COMPLETED (24) Create a ProgressBar variable to store a reference to the ProgressBar
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //COMPLETED - Get a reference to the EditText of SearchBox EditText using findViewById
        SearchBoxEditText = (EditText) findViewById(R.id.et_search_box);

        //COMPLETED - Get a reference to the TextView of Display TextView using findViewById
        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);

        //COMPLETED - Get a reference to the TextView of SearchResult TextView using findViewById
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_github_search_results_json);

        // COMPLETED (13) Get a reference to the error TextView using findViewById
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        // COMPLETED (25) Get a reference to the ProgressBar using findViewById
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
    }


    /**
     * This method retrieves the search text from the EditText, constructs the

     * URL (using {@link NetworkUtils}) for the github repository you'd like to find, displays

     * that URL in a TextView, and finally fires off an AsyncTask to perform the GET request using

     * our {@link GithubQueryTask}

     */
    private void makeGithubSearchQuery() {
        String githubQuery = SearchBoxEditText.getText().toString();

        URL githubSearchUrl = NetworkUtils.buildUrl(githubQuery);

        mUrlDisplayTextView.setText(githubSearchUrl.toString());

        new GithubQueryTask().execute(githubSearchUrl);
    }


// COMPLETED (14) Create a method called showJsonDataView to show the data and hide the error
    /**

     * This method will make the View for the JSON data visible and
     * hide the error message.
     * <p>

     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.

     */
    private void showJsonDataView() {
        // First, make sure the error is invisible
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

// Then, make sure the JSON data is visible
        mSearchResultsTextView.setVisibility(View.VISIBLE);
    }


// COMPLETED (15) Create a method called showErrorMessage to show the error and hide the data

    /**

     * This method will make the error message visible and hide the JSON
     * View.
     * <p>

     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.

     */

    private void showErrorMessage() {
        // First, hide the currently visible data
        mSearchResultsTextView.setVisibility(View.INVISIBLE);

        // Then, show the error
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    public class GithubQueryTask extends AsyncTask<URL, Void, String> {


        // COMPLETED (26) Override onPreExecute to set the loading indicator to visible

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }


        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String githubSearchResults = null;

            try {
                githubSearchResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return githubSearchResults;
        }

        @Override
        protected void onPostExecute(String githubSearchResults) {

            // COMPLETED (27) As soon as the loading is complete, hide the loading indicator
            mLoadingIndicator.setVisibility(View.INVISIBLE);

            if (githubSearchResults != null && !githubSearchResults.equals("")) {

                // COMPLETED (17) Call showJsonDataView if we have valid, non-null results
                showJsonDataView();

                mSearchResultsTextView.setText(githubSearchResults);
            } else {

                // COMPLETED (16) Call showErrorMessage if the result is null in onPostExecute
                showErrorMessage();
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemThatWasClickedId = item.getItemId();

        if (itemThatWasClickedId == R.id.action_search) {
            makeGithubSearchQuery();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
