package ast.bstu.oopproject;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    Context ctx;
    LayoutInflater lInflater;
    ArrayList<String> objects;
    ArrayList<String> pics;

    MyAdapter(Context context, ArrayList<String> products, ArrayList<String> pic) {
        ctx = context;
        objects = products;
        pics=pic;
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.list_item, parent, false);
        }
        Log.d("",pics.get(position));
        Uri imageUri = Uri.parse(pics.get(position));
        Bitmap bitmap = null;
        ((TextView) view.findViewById(R.id.text)).setText(objects.get(position));
        ((ImageView) view.findViewById(R.id.img)).setImageURI(Uri.parse(pics.get(position)));

        LinearLayout l= view.findViewById(R.id.list_item);

       /*l.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                TextView t = view.findViewById(R.id.text);
                String strText = t.getText().toString();
                Intent intent = new Intent(view.getContext(), AddActivity.class);
                intent.putExtra("mode", "normal");
                intent.putExtra("item", strText);
                //сдесь нужно добавить остальные пункты
                ctx.startActivity(intent);

            }
        });*/
        return view;
    }

}