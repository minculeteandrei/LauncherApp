package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends ListActivity implements SearchView.OnQueryTextListener, AdapterView.OnItemSelectedListener {
    CustomAdapter adapterList;
    Spinner sortBy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sortBy = findViewById(R.id.spinnerSort);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sort_options_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBy.setAdapter(adapter);
        adapterList = new CustomAdapter(this, getApplicationContext());
        setListAdapter(adapterList);

        SearchView search = findViewById(R.id.searchView);
        search.setOnQueryTextListener(this);
        sortBy.setOnItemSelectedListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(sortBy.getSelectedItem().toString().equals("Sort by recently used"))
            adapterList.sortByRecentlyUsed();
        setSelection(0);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapterList.filter(newText);
        return false;

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if(item.equals("Sort by name"))
            adapterList.sortByName();
        else if(item.equals("Sort by recently used"))
            adapterList.sortByRecentlyUsed();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}