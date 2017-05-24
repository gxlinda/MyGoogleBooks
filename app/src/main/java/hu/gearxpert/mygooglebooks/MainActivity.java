package hu.gearxpert.mygooglebooks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void searchButtonClicked (View view) {
        EditText searchField = (EditText) findViewById(R.id.search_field);
        String searchValue = searchField.getText().toString();

        if (searchValue.length() == 0) {
            Toast.makeText(this, R.string.enter_keywords,Toast.LENGTH_SHORT).show();
        } else {
            searchValue = searchValue.replaceAll("[^\\p{L}\\p{Nd}]+", "+"); //changes all non-letters and non-numbers to +
            Intent intent = new Intent(this, BookListActivity.class);
            intent.putExtra("KEYWORDS", searchValue);
            startActivity(intent);
        }
    }
}
