package com.example.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // code to identify the edit activity
    public static final int EDIT_REQUEST_CODE = 20;
    // keys to pass data between activities
    public static final String ITEM_TEXT = "itemText";
    public static final String ITEM_POSITION = "itemPosition";

    ArrayList<String> items;
    ArrayAdapter<String> items_adapter;
    ListView lv_items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems();

        items_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lv_items = findViewById(R.id.lv_items);
        lv_items.setAdapter(items_adapter);


        setupListViewListener();

    }

    public void onAddItem(View v) {
        EditText et_new_item = findViewById(R.id.et_new_item);
        String item_name = et_new_item.getText().toString();
        if(item_name.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Item name cannot be empty!", Toast.LENGTH_LONG).show();
        } else {
            items_adapter.add(item_name);
            et_new_item.setText("");
            Toast.makeText(getApplicationContext(), "Item added to list", Toast.LENGTH_SHORT).show();
            writeItems();
        }
    }

    private void setupListViewListener() {
        Log.i("MainActivity", "setting up listener on list view");
        lv_items.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long id) {
                Log.i("MainActivity", "item " + index + " removed from list");
                items.remove(index);
                items_adapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        // Setup edit listener
        lv_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                // create activity
                Intent i = new Intent(MainActivity.this, EditItemActivity.class);

                // pass data
                i.putExtra(ITEM_TEXT, items.get(index));
                i.putExtra(ITEM_POSITION, index);

                // display activity
                startActivityForResult(i, EDIT_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE) {
            // get data from edit activity
            String updatedItem = data.getStringExtra(ITEM_TEXT);
            int index = data.getIntExtra(ITEM_POSITION, 0);

            // update the item
            items.set(index, updatedItem);
            items_adapter.notifyDataSetChanged();

            // Write to persistent storage
            writeItems();

            // Notify user
            Toast.makeText(getApplicationContext(), "Item updated successfully", Toast.LENGTH_SHORT).show();
        }
    }

    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems() {
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "error reading file", e);
            items = new ArrayList<>();
        }
    }

    private void writeItems() {
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity", "error writing file", e);
        }
    }
}
