package ast.bstu.oopproject;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
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
import androidx.core.app.NotificationCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import ast.bstu.oopproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    public ArrayList<String> titleList = new ArrayList<>();
    public ArrayList<String> idList = new ArrayList<>();
    private ListView mListView;
    private ActivityMainBinding binding;
    String date1;
    NotificationManager notificationManager;
    private static final int NOTIFY_ID = 1;
    private static final String CHANNEL_ID = "CHANNEL_ID";
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    LocalDateTime now = LocalDateTime.now();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.list_view);
        registerForContextMenu(mListView);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add(view);
            }
        });
        notificationManager =
                (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        titleList.clear();idList.clear();

        SQLiteDatabase db = new DbHelper(getApplicationContext()).getReadableDatabase();
        Cursor cursor = DbEvent.getAll(db);
        while (cursor.moveToNext()) {
            titleList.add(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            idList.add(cursor.getString(cursor.getColumnIndexOrThrow("id_event")));
            date1= cursor.getString(cursor.getColumnIndexOrThrow("date"));
            String date2= dtf.format(now);
            Log.d("",""+date2+ date1);
            if (date1.equals(date2))
                Notification(date1,cursor.getString(cursor.getColumnIndexOrThrow("title")) );
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

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
    public void Notification(String date, String info){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_light_normal_background)
                        .setWhen(System.currentTimeMillis())
                        .setContentIntent(pendingIntent)
                        .setContentTitle("У вас на сегодня запланировано мероприятие")
                        .setContentText(date+" У вас "+info);
        //.setPriority(PRIORITY_HIGH);
        createChannelIfNeeded(notificationManager);
        notificationManager.notify(NOTIFY_ID,notificationBuilder.build());

    }
    public static void createChannelIfNeeded(NotificationManager manager) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);

            manager.createNotificationChannel(notificationChannel);

        }

    }

}