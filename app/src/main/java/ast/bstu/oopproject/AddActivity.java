package ast.bstu.oopproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

public class AddActivity extends AppCompatActivity {

    private com.google.android.material.textfield.TextInputEditText title, category,address, price, note, date;
    private double latitude;
    private double longitude;
    //private double date;
    private int priority;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        title= findViewById(R.id.title);
        category= findViewById(R.id.category);
        address= findViewById(R.id.address);
        price= findViewById(R.id.price);
        note= findViewById(R.id.note);
        date= findViewById(R.id.date);
        db = new DbHelper(getApplicationContext()).getReadableDatabase();
        try
        {
            Bundle arg = getIntent().getExtras();
            int id = (int) arg.getInt("id_event");
            Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
        }
        catch (Exception ex)
        {
            Toast.makeText(this, "бандл не загружен", Toast.LENGTH_SHORT).show();
        }
    }

    public void newevent(View view)
    {

        EventModel event = new EventModel(title.getText().toString(),category.getText().toString(),
                address.getText().toString(),0,0,price.getText().toString(),
                0, note.getText().toString(),1);
         if(DbEvent.add(db, event) != -1) {
            Toast.makeText(this, "Талон заказан", Toast.LENGTH_SHORT).show();

        } else Toast.makeText(this, "Ошибка добавления", Toast.LENGTH_SHORT).show();

        Intent intent =new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    //todo: тут осталось добавить передачу какой элемент был выбран и вторую страничку с просмотром. потом еще геокодер и мб картинки. а и сортировки на кнопочки

}