package com.example.dialogmess;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //Khai báo và khởi tạo
    Database database;
    ListView listView;
    EditText nhapTen;
    Button bt_them;
    ArrayList<User> list;
    UserAdapter adapter;

    // data
//    String[] name = {"Trần Thanh Xuân", "Trần Nguyễn Sĩ Đạt"};
//    String[] image = {"https://robohash.org/Xuan", "https://robohash.org/dat"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Ánh xạ
        listView = findViewById(R.id.ListView);
        nhapTen = findViewById(R.id.edit_nhapTen);
        bt_them = findViewById(R.id.Bt_them);

        list = new ArrayList<>(); //
        adapter = new UserAdapter(this, list, R.layout.layout_user);
        listView.setAdapter(adapter);

        database = new Database(this);
//        database.queryData("DROP TABLE Noidung");
        database.queryData("CREATE TABLE IF NOT EXISTS Users (Name VARCHAR(20), Avt VARCHAR(50))");

        themDuLieu();
//
//        for (int i = 0; i < name.length; i++) {
//            list.add(new User(name[i], image[i]));
//        }

        adapter.notifyDataSetChanged();
        //
        bt_them.setOnClickListener(v -> {
            String name = nhapTen.getText().toString();
            String avtName = "https://robohash.org/" + name;
            if (name.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhâp tên ", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                database.queryData("INSERT INTO Users VALUES ( ' "+ name +"', '"+ avtName +"')");
                list.add(new User(name, avtName));
                adapter.notifyDataSetChanged();
            }
            nhapTen.setText("");
        });
    }
    public void dialogUpDate(User user) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog);


        EditText edSua = dialog.findViewById(R.id.edit_nhap);
        Button btOK = dialog.findViewById(R.id.bt_OK);
        Button btHuy = dialog.findViewById(R.id.bt_huy);

        edSua.setText(user.getName());

        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenMoi = edSua.getText().toString().trim();
                String avtTen = "https://robohash.org/" + tenMoi;
                if(TextUtils.isEmpty(tenMoi)) {
                    Toast.makeText(MainActivity.this, "Vui long nhap ten", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();;
                    return;
                } else {
                    // Cập nhật thông tin người dùng cơ sở dữ liệu
                    database.queryData("UPDATE Users SET Name = '"+ tenMoi +"'  WHERE Name = '" + user.getName()+"'");
                    database.queryData("UPDATE Users SET Image = '"+ avtTen +"'  WHERE Image = '" + user.getImage()+"'");
                    themDuLieu();
                    // Cập nhật danh sách người dùng
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();

                }
            }
        });
        btHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void dialogDelete(User user) {
         AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setMessage("Bạn có muốn xoá ?");
         builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 database.queryData("DELETE FROM Users WHERE Name =  '" + user.getName() + "'");
                 list.remove(user);
                 adapter.notifyDataSetChanged();
             }
         });
         builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {

             }
         });
         builder.show();
     }

    private void themDuLieu() {
        Cursor data = database.getData("SELECT * FROM Users");
        list.clear();
        while (data.moveToNext()) {
            String name = data.getString(0);
            String image = data.getString(1);
            list.add(new User(name, image));
        }
        adapter.notifyDataSetChanged();
    }
}