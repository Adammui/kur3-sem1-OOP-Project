package ast.bstu.oopproject;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.content.ContextCompat;

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
    public ArrayList<String> imgList = new ArrayList<>();
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            finish();
        }
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

        SQLiteDatabase db = new DbHelper(getApplicationContext()).getWritableDatabase();
        Cursor cursor = DbEvent.getAllByPriority(db);
        while (cursor.moveToNext()) {
            titleList.add(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            idList.add(cursor.getString(cursor.getColumnIndexOrThrow("id_event")));
            imgList.add(cursor.getString(cursor.getColumnIndexOrThrow("imguri")));
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
        start();
    }
    @Override
    protected void onResume() {
        super.onResume();
        start();
    }
private void start()
{
    MyAdapter adapter = new MyAdapter(this, titleList, idList, imgList);
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
            Intent intent = new Intent(MainActivity.this, ast.bstu.oopproject.AddActivity.class);
            startActivity(intent);
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