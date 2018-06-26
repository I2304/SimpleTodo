package com.ikarmarkar.simpletodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get a reference to the ListView created in the layout
        lvItems = (ListView) findViewById(R.id.lvItems);
        // initialize the list called items
        readItems();
        // initialize the adapter using items
        itemsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, items);
        // wire the adapter to the view
        lvItems.setAdapter(itemsAdapter);

        // setup the listener upon creation
        setupListViewListener();
    }

    public void onAddItem(View v) {
        // get a reference to the EditText created in the layout
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        // get the EditText's content as a String
        String itemText = etNewItem.getText().toString();
        // add the item to the list via the adapter
        itemsAdapter.add(itemText);
        // store the list called updated
        writeItems();
        // clear the EditText (set it to an empty String)
        etNewItem.setText("");
        // notify the user
        Toast.makeText(getApplicationContext(),
                "Item added to list", Toast.LENGTH_SHORT).show();
    }

    private void setupListViewListener() {
        Log.i("MainActivity", "Setting up listener on list view");
        // set the ListView's itemLongClickListener
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                           long id) {
                Log.i("MainActivity", "Item removed from list: " + position);
                // remove the item in the list at a specific position
                items.remove(position);
                // notify the adapter that the dataset has been changed
                itemsAdapter.notifyDataSetChanged();
                // store the updated list
                writeItems();
                // return true (tell the framework that the long click was consumed)
                return true;
            }
        });
    }

    // return the file which stores the data
    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    // read our items from the file system
    private void readItems() {
        try {
            // create the array (based on the content in the file)
            items = new ArrayList<String>(FileUtils.readLines(getDataFile(),
                    Charset.defaultCharset()));
        } catch (IOException e) {
            // print the error
            e.printStackTrace();
            // load an empty list
            items = new ArrayList<>();
        }
    }

    // write our items to the filesystem
    private void writeItems() {
        try {
            // save the item list (line-delimited text file)
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            // print the error
            e.printStackTrace();
        }
    }
}