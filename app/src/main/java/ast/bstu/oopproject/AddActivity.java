package ast.bstu.oopproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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
    private final int Pick_image = 1;
    ImageView imageView;
    String imguri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageView= findViewById(R.id.image);
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

        Button PickImage = (Button) findViewById(R.id.pick);
        PickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, Pick_image);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
         configure_fields();
    }

    @Override
    protected void onResume() {
        super.onResume();
        configure_fields();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case Pick_image:
                if (resultCode == RESULT_OK) {

                    // compare the resultCode with the
                    // SELECT_PICTURE constant
                        // Get the url of the image from data
                        Uri selectedImageUri = imageReturnedIntent.getData();
                        if (null != selectedImageUri) {
                            // update the preview image in the layout
                            imguri= selectedImageUri.toString();
                        }
                }
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
    private void configure_fields()
    {
        try {
            Bundle arg = getIntent().getExtras();
            id_event = arg.getInt("id_event");
            mode = arg.getInt("mode");
            Toast.makeText(this, id_event + "", Toast.LENGTH_SHORT).show();
            if (mode == 0) {
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.fragment_map);
                mapFragment.getMapAsync(this);
                title.setEnabled(false);
                category.setEnabled(false);
                lay_adress.setVisibility(View.GONE);
                price.setEnabled(false);
                date.setEnabled(false);
                mapfragment.setVisibility(View.VISIBLE);
                Button b = findViewById(R.id.buttonrgr);
                b.setVisibility(View.VISIBLE);
                SQLiteDatabase db = new DbHelper(getApplicationContext()).getReadableDatabase();
                Cursor cursor = DbEvent.findbyid(db, id_event);
                while (cursor.moveToNext()) {
                    title.setText(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                    category.setText(cursor.getString(cursor.getColumnIndexOrThrow("category")));
                    latitude = cursor.getDouble(cursor.getColumnIndexOrThrow("latitude"));
                    longitude = cursor.getDouble(cursor.getColumnIndexOrThrow("longitude"));
                    Log.d("e", "" + longitude);
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

        EventModel event = new EventModel(Objects.requireNonNull(title.getText()).toString(),imguri, Objects.requireNonNull(category.getText()).toString(),
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
    public void del(View view){
        try{
            AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                    // set message, title, and icon
                    .setTitle("Delete")
                    .setMessage("Do you want to Delete")

                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            SQLiteDatabase db = new DbHelper(getApplicationContext()).getWritableDatabase();;
                            DbEvent.deleteById(db,id_event);
                            Toast.makeText(AddActivity.this, String.valueOf(pr.getProgress())+ "Изменено", Toast.LENGTH_SHORT).show();
                            Intent intent =new Intent(AddActivity.this, MainActivity.class);
                            startActivity(intent);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.dismiss();

                        }
                    })
                    .show();
        }
        catch (SQLException ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    //todo: а и сортировки на кнопочки

}