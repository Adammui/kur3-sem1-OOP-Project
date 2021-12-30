package ast.bstu.oopproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;

    public ArrayList<String> titleList = new ArrayList<>();
    public ArrayList<String> idList = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(this);


        mListView = findViewById(R.id.list_view);

        titleList.clear();idList.clear();

        SQLiteDatabase db = new DbHelper(getApplicationContext()).getReadableDatabase();
        Cursor cursor = DbEvent.getAll(db);
        while (cursor.moveToNext()) {
            titleList.add(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            titleList.add(cursor.getString(cursor.getColumnIndexOrThrow("id")));
        }
        adapter = new ArrayAdapter<>(this, R.layout.list_item, R.id.text_item, titleList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Object o = mListView.getItemAtPosition(position);

                Intent intent1 =new Intent(MainActivity.this, AddActivity.class);
                intent1.putExtra("id_event", (Integer) o);
                startActivity(intent1);
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

    }
    public void minsk (View view)
    {
        mMap.clear();
        LatLng sydney = new LatLng(1, 1);
        MarkerOptions marko= new MarkerOptions();
        marko.position(sydney);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));
        mMap.addMarker(marko);
    }
    public void add(View view)
    {
        /*
        sur=findViewById(R.id.surname);
        name=findViewById(R.id.name);
        gr=findViewById(R.id.radio);
        M=findViewById(R.id.radioM); B=findViewById(R.id.radioB);G=findViewById(R.id.radioG);

        if(gr.getCheckedRadioButtonId()!=-1 && !sur.getText().toString().isEmpty() && !name.getText().toString().isEmpty()) {
            RadioButton r = findViewById(gr.getCheckedRadioButtonId());
            town = r.getText().toString();*/
            Intent intent = new Intent(MainActivity.this, ast.bstu.oopproject.AddActivity.class);
            /*intent.putExtra("town", town);
            intent.putExtra("surname", sur.getText().toString());
            intent.putExtra("name", name.getText().toString());

            intent.putExtra("doc", doc );
            intent.putExtra("docname", docname );
            intent.putExtra("time", time );
            intent.putExtra("analysis", analysis );*/

            startActivity(intent);
            /*
        }
        else {
            TextView r = findViewById(R.id.warn);
            r.setVisibility(View.VISIBLE);
        }*/
    }
}