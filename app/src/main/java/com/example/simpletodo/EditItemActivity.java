package com.example.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.simpletodo.MainActivity.ITEM_POSITION;
import static com.example.simpletodo.MainActivity.ITEM_TEXT;

public class EditItemActivity extends AppCompatActivity {

    EditText et_item_text;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        et_item_text = findViewById(R.id.et_edit_item);
        et_item_text.setText(getIntent().getStringExtra(ITEM_TEXT));

        index = getIntent().getIntExtra(ITEM_POSITION, 0);

        getSupportActionBar().setTitle("Edit Item");
    }

    public void onSaveItem(View v) {
        Intent i = new Intent();
        i.putExtra(ITEM_TEXT, et_item_text.getText().toString());
        i.putExtra(ITEM_POSITION, index);
        setResult(RESULT_OK, i);
        finish();
    }
}
