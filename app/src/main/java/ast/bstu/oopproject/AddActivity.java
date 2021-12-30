package ast.bstu.oopproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;

public class AddActivity extends AppCompatActivity implements OnMapReadyCallback {

    private com.google.android.material.textfield.TextInputEditText title, category,address, price, note, date;
    private com.google.android.material.textfield.TextInputLayout lay_adress;
    private double latitude;
    private double longitude;
    //private double date;
    SeekBar pr;
    private int priority, id_event;
    private SQLiteDatabase db;
    int mode=1; //1= add mode, 0= edit mode
    GoogleMap mMap;
    LinearLayout mapfragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        title= findViewById(R.id.title);
        category= findViewById(R.id.category);
        address= findViewById(R.id.address);
        lay_adress=findViewById(R.id.address_lay);
        price= findViewById(R.id.price);
        note= findViewById(R.id.note);
        date= findViewById(R.id.date);
        pr= findViewById(R.id.priority);
        mapfragment = findViewById(R.id.fragment_lay);
        db = new DbHelper(getApplicationContext()).getReadableDatabase();


    }

    @Override
    protected void onStart() {
        super.onStart();

        try
        {
            Bundle arg = getIntent().getExtras();
            id_event = arg.getInt("id_event");
            mode = arg.getInt("mode");
            Toast.makeText(this, id_event+"", Toast.LENGTH_SHORT).show();
            if (mode==0){
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_map);
                mapFragment.getMapAsync(this);
                title.setEnabled(false);
                category.setEnabled(false);
                lay_adress.setVisibility(View.GONE);
                price.setEnabled(false);
                date.setEnabled(false);
                mapfragment.setVisibility(View.VISIBLE);
                SQLiteDatabase db = new DbHelper(getApplicationContext()).getReadableDatabase();
                Cursor cursor = DbEvent.findbyid(db, id_event);
                while (cursor.moveToNext()) {
                    title.setText(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                    category.setText(cursor.getString(cursor.getColumnIndexOrThrow("category")));
                    latitude=cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                    longitude=cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
                    Log.d("e",""+longitude);
                    price.setText(cursor.getString(cursor.getColumnIndexOrThrow("price")));
                    date.setText(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                    note.setText(cursor.getString(cursor.getColumnIndexOrThrow("note")));
                    pr.setProgress(cursor.getInt(cursor.getColumnIndexOrThrow("priority")));
                }
            }
        }
        catch (Exception ex)
        {
           // Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        searchplace(latitude, longitude);

    }
    public void searchplace (double latitude,double longitude)
    {
        mMap.clear();
        LatLng sydney = new LatLng(latitude, longitude);
        MarkerOptions marko= new MarkerOptions();
        marko.position(sydney);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17));
        mMap.addMarker(marko);
    }
    public void save(View view)
    {
        if (mode==1)
            newevent();
        else
            save();
    }
    private void newevent(){
        Geocoder geocoder = new Geocoder(this);
        List<Address> addr;
        LatLng p1 = null;

        try {
            addr = geocoder.getFromLocationName(Objects.requireNonNull(address.getText()).toString(), 5);
            if (addr == null) {
                return;
            }
            Address location = addr.get(0);
            location.getLatitude();
            location.getLongitude();
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        EventModel event = new EventModel(Objects.requireNonNull(title.getText()).toString(), Objects.requireNonNull(category.getText()).toString(),
                Objects.requireNonNull(address.getText()).toString(),p1.latitude,p1.longitude, Objects.requireNonNull(price.getText()).toString(),
                Objects.requireNonNull(date.getText()).toString(), Objects.requireNonNull(note.getText()).toString(),pr.getProgress());
        if(DbEvent.add(db, event) != -1) {
            Toast.makeText(this, "Талон заказан", Toast.LENGTH_SHORT).show();

        } else Toast.makeText(this, "Ошибка добавления", Toast.LENGTH_SHORT).show();

        Intent intent =new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void save(){
        try{
        SQLiteDatabase db = new DbHelper(getApplicationContext()).getWritableDatabase();;
        DbEvent.editNote(db,id_event, Objects.requireNonNull(note.getText()).toString(),String.valueOf(pr.getProgress()));
            Toast.makeText(this, String.valueOf(pr.getProgress())+ "Изменено", Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        catch (SQLException ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    //todo: меню с сортировкой и туда засунуть кнопку добавить, потом еще геокодер и мб картинки. а и сортировки на кнопочки

}