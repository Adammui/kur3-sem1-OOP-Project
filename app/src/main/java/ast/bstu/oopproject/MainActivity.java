package ast.bstu.oopproject;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public ArrayList<String> titleList = new ArrayList<>();
    public ArrayList<String> idList = new ArrayList<>();
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.list_view);
        registerForContextMenu(mListView);
    }

    @Override
    protected void onStart() {
        super.onStart();

        titleList.clear();idList.clear();

        SQLiteDatabase db = new DbHelper(getApplicationContext()).getReadableDatabase();
        Cursor cursor = DbEvent.getAll(db);
        while (cursor.moveToNext()) {
            titleList.add(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            idList.add(cursor.getString(cursor.getColumnIndexOrThrow("id_event")));
        }
        MyAdapter adapter = new MyAdapter(this, titleList, idList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {

                TextView t = view.findViewById(R.id.text_id);
                int strText = Integer.parseInt(t.getText().toString());
                Log.d("e", ""+ strText);
                Intent intent1 =new Intent(view.getContext(), AddActivity.class);
                intent1.putExtra("id_event", strText);
                intent1.putExtra("mode", 0);
                startActivity(intent1);
            }
        });
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