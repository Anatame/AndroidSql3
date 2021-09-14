package com.example.sql3;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //references to controls
    Button btnAdd, btnViewAll;
    EditText et_name, et_age;
    Switch sw_activeCustomer;
    ListView lvCustomerList;
    ArrayAdapter customerArrayAdapter;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btn_add);
        btnViewAll = findViewById(R.id.btn_viewAll);
        et_name = findViewById(R.id.et_name);
        et_age = findViewById(R.id.et_age);
        sw_activeCustomer = findViewById(R.id.sw_active);
        lvCustomerList = findViewById(R.id.lv_customerList);

        databaseHelper = new DatabaseHelper(MainActivity.this);
        ShowCustomersOnListView(databaseHelper.getEveryone());

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomerModel customerModel;

                try{
                    customerModel = new CustomerModel(-1,
                            et_name.getText().toString(),
                            Integer.parseInt(et_age.getText().toString()),
                            sw_activeCustomer.isChecked());

                    Toast.makeText(MainActivity.this,
                            customerModel.toString(),
                            Toast.LENGTH_SHORT)
                            .show();


                } catch (Exception e){
                    customerModel = new CustomerModel(-1,
                            "Error",
                            0,
                            false);

                    Toast.makeText(MainActivity.this,
                            "Error creating customer",
                            Toast.LENGTH_SHORT)
                            .show();
                }

                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);

                boolean success = databaseHelper.addOne(customerModel);

                Toast.makeText(MainActivity.this, "Success= " + success, Toast.LENGTH_SHORT).show();
                ShowCustomersOnListView(databaseHelper.getEveryone());




            }
        });

        btnViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper = new DatabaseHelper(MainActivity.this);
                List<CustomerModel> everyone = databaseHelper.getEveryone();
                ShowCustomersOnListView(everyone);


                Toast.makeText(MainActivity.this, everyone.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        lvCustomerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CustomerModel clickedCustomer = (CustomerModel) parent.getItemAtPosition(position);
                databaseHelper.deleteOne(clickedCustomer);
                ShowCustomersOnListView(databaseHelper.getEveryone());
                Toast.makeText(MainActivity.this, "Deleted " + clickedCustomer.toString(), Toast.LENGTH_SHORT).show();


            }
        });



    }

    private void ShowCustomersOnListView(List<CustomerModel> everyone2) {
        customerArrayAdapter = new ArrayAdapter<CustomerModel>(MainActivity.this, android.R.layout.simple_list_item_1, everyone2);
        lvCustomerList.setAdapter(customerArrayAdapter);
    }
}