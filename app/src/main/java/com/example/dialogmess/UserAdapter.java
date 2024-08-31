package com.example.dialogmess;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserAdapter extends BaseAdapter {
    private MainActivity context;
    private ArrayList<User> list;
    private int layout;
    private Database database;

    public UserAdapter(MainActivity context, ArrayList<User> list, int layout) {
        this.context = context;
        this.list = list;
        this.layout = layout;
        this.database = new Database(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(layout, parent, false); // tạo 1 item mới
            viewHolder = new ViewHolder();
            // anh xa ViewHolder
            anhXaview(viewHolder, convertView);
            convertView.setTag(viewHolder);// taí sd view (trong trường hợp view tạo rồi)
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // set dữ liệu item
        User user = list.get(position);
        viewHolder.textView.setText(user.getName());
        Picasso.get().load(user.getImage()).into(viewHolder.avt);

        // set sự kiện cho các dialog
        // Menu
        viewHolder.chon.setOnClickListener(v ->   {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.layout_menu);
            Window window = dialog.getWindow();
            if (window != null) {
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams windowAttributes = window.getAttributes();
                windowAttributes.gravity = Gravity.CENTER;
                window.setAttributes(windowAttributes);
            } else {
                Toast.makeText(context.getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }


            // dialog sua
            Button sua;
            Button xoa;
            sua = dialog.findViewById(R.id.sua);
            xoa = dialog.findViewById(R.id.xoa);
            sua.setOnClickListener(v1 -> {
                context.dialogUpDate(user);
                dialog.dismiss();
            });
            // xoa
            xoa.setOnClickListener(v12 -> {
                dialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("XOÁ")
                        .setMessage("Bạn chắc chắn muốn xoá" + user.getName() + " khỏi danh sách")
                        .setPositiveButton("OK", (dialogsua, id) ->{
                            context.dialogDelete(user);
                        })
                        .setNegativeButton("Huỷ", (dialogsua, id) -> dialogsua.dismiss());
                AlertDialog dialogsua = builder.create();
                dialogsua.show();
            });
            dialog.show();
        });
        return convertView;
    }


    private void anhXaview(ViewHolder viewHolder, View convertView) {
        viewHolder.textView = convertView.findViewById(R.id.tv_user);
        viewHolder.avt = convertView.findViewById(R.id.img_avt);
        viewHolder.chon = convertView.findViewById(R.id.bt_chon);
    }


    public static class ViewHolder{
        Button chon;
        private TextView textView;
        private ImageView avt;
        //cac thuoc tinh trong row item
    }
}
