package com.example.ht_da4;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class MainActivity2 extends AppCompatActivity {

    //Gồm 3 bước: B1:Day giu lieu, B2:Xml hiển thị, B3:class tạo hàm
    ArrayList<Integer> arrayDoAm = new ArrayList<Integer>(100); //Danh sach du lieu 1
    ArrayList<Integer> arrayTrangThaiMayBom = new ArrayList<>(100);//Danh sach du lieu 2
    ArrayList<String> arrayThoiGian = new ArrayList<>(100);//Danh sach du lieu 3
    ArrayList<Integer> arrayDoAmCaiDat = new ArrayList<>(100);
    ArrayList<Integer> arrayTocDo = new ArrayList<>(100);
    ArrayList<Integer> arrayCDHD = new ArrayList<>(100);
    ArrayList<Integer> arrayMucNuoc = new ArrayList<>(100);


    ArrayList<Integer> arrayDoAm2 = new ArrayList<Integer>(100); //Danh sach du lieu 1
    ArrayList<Integer> arrayTrangThaiMayBom2 = new ArrayList<>(100);//Danh sach du lieu 2
    ArrayList<String> arrayThoiGian2 = new ArrayList<>(100);//Danh sach du lieu 3
    ArrayList<Integer> arrayDoAmCaiDat2 = new ArrayList<>(100);
    ArrayList<Integer> arrayTocDo2 = new ArrayList<>(100);
    ArrayList<Integer> arrayCDHD2 = new ArrayList<>(100);
    ArrayList<Integer> arrayMucNuoc2 = new ArrayList<>(100);

    MainActivity_HienThiLichSu adapterLichSu1; //Class tao thêm hang khi co du lieu

    //Tao duong dan SQL
    String DATABASE_NAME = "ht.db";
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database = null;
    //Luu tru du lieu SQL
    ArrayList<String> dsLichSu;         //dsDanhBa
    ArrayAdapter<String> adapterLichSu; //adapterDanhba

    ListView lvLichSu;      //Xml Hien thi
    Button btnChuyenVeManHinhChinh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hienthi_lichsu);

        addConTrols();
        xyLySaoChep();
        addEvents();
        showAllContactOnListView();
        xuLySuKienFirebase();


    }
    private void xuLySuKienFirebase() {
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference();

        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Lấy giá trị độ ẩm từ cơ sở dữ liệu Firebase
                Integer humidity = dataSnapshot.child("DoAmCamBien").getValue(Integer.class);
                Integer trangThaiMayBom = dataSnapshot.child("TrangThaiMayBom").getValue(Integer.class);
                Integer doAmCaiDat = dataSnapshot.child("DoAmCaiDat").getValue(Integer.class);
                Integer tocDo = dataSnapshot.child("TocDo").getValue(Integer.class);
                Integer cdhd = dataSnapshot.child("TrangThaiHoatDong").getValue(Integer.class);
                Integer mucnuoc = dataSnapshot.child("MucNuoc").getValue(Integer.class);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                    LocalDateTime thoiGianThayDoi = LocalDateTime.now();
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
//                   // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                    ZonedDateTime vietnamTime = thoiGianThayDoi.atZone(ZoneId.of("Asia/Ho_Chi_Minh"));
//                    String formattedTime = vietnamTime.format(formatter);
                    Date thoiGianHienTai = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy (HH:mm)");
                    sdf.setTimeZone(TimeZone.getTimeZone("GMT+7")); // Đặt múi giờ mong muốn
                    String thoiGianString = sdf.format(thoiGianHienTai);
                    arrayThoiGian.add(thoiGianString);
                }

                // Lưu giá trị độ ẩm vào mảnG
                arrayDoAm.add(humidity);
                if (arrayDoAm.size() == 99) {
                    arrayDoAm.remove(0);
                    arrayDoAm.add(99, humidity);
                }
                //Luu gia tri trangthai may bom
                arrayTrangThaiMayBom.add(trangThaiMayBom);
                if (arrayTrangThaiMayBom.size() == 99) {
                    arrayTrangThaiMayBom.remove(0);
                    arrayTrangThaiMayBom.add(99, humidity);
                }
                //Luu gia tri DoAmCaiDat
                arrayDoAmCaiDat.add(doAmCaiDat);
                if (arrayDoAmCaiDat.size() == 99) {
                    arrayDoAmCaiDat.remove(0);
                    arrayDoAmCaiDat.add(99, humidity);
                }
                //Luu gia tri TocDo
                arrayTocDo.add(tocDo);
                if (arrayTocDo.size() == 99) {
                    arrayTocDo.remove(0);
                    arrayTocDo.add(99, humidity);
                }
                //Luu gia tri Trang Thai Hoat Dong
                arrayCDHD.add(cdhd);
                if (arrayCDHD.size() == 99) {
                    arrayCDHD.remove(0);
                    arrayCDHD.add(99, cdhd);
                }
                //Luu trang thai muc nuoc
                arrayMucNuoc.add(mucnuoc);
                if (arrayMucNuoc.size() == 99) {
                    arrayMucNuoc.remove(0);
                    arrayMucNuoc.add(99, mucnuoc);
                }
                xuLyThemData(arrayDoAm, arrayTrangThaiMayBom, arrayDoAmCaiDat, arrayThoiGian, arrayTocDo, arrayCDHD, arrayMucNuoc);
                //showAllContactOnListView();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi khi không thể đọc dữ liệu từ cơ sở dữ liệu Firebase
            }
        });
    }

    //Lam ve SQL Buoc 1,2,3 khong can chinh sua
    //B1 lay duong dan luu tru thu muc SQL
    private String layDuongDanLuuTru(){
        return getApplicationInfo().dataDir+DB_PATH_SUFFIX+DATABASE_NAME;
    }
    //B2 Sao chep du lieu vao file khac
    private void CopyDatabaseFromAsset() {
        try{
            InputStream myInput = getAssets().open(DATABASE_NAME);
            //Lay duong  dan luu tru du lieu
            String outFileName = layDuongDanLuuTru();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            //Kiem tra xem file toi tai hay chua
            if(!f.exists()){
                f.mkdir();
            }
            //Sao chep du lieu
            OutputStream myOutpuut = new FileOutputStream(outFileName);
            //
            byte[] buffer = new byte[1024];
            int length;
            while((length = myInput.read(buffer)) > 0){
                myOutpuut.write(buffer, 0, length);
            }
            //Dong cac file
            myOutpuut.flush();
            myOutpuut.close();
            myInput.close();

        }catch (Exception ex){
            Log.e("Loi_SaoChep", ex.toString());
        }
    }
    //B3
    private void xyLySaoChep() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if(!dbFile.exists()){
            try {
                CopyDatabaseFromAsset();
                //Toast.makeText(this, "Sao chep du lieu thanh cong", Toast.LENGTH_LONG).show();
            }catch (Exception e){
                //Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
    // Thêm Data vào SQLite
    private void xuLyThemData(ArrayList<Integer> arrayDoAm, ArrayList<Integer> arrayTrangThaiMayBom, ArrayList<Integer> arrayDoAmCaiDat, ArrayList<String> arrayThoiGian, ArrayList<Integer> arrayTocDo, ArrayList<Integer> arrayCDHD, ArrayList<Integer> arrayMucNuoc){
//        //Câu lệnh xóa toàn bộ dữ liệu cũ của bảng
 //       database.delete("ht", null, null);
        for(int i = 0; i < arrayDoAm.toArray().length; i++ ){
            ContentValues values = new ContentValues();
            values.put("doamcaidat", arrayDoAmCaiDat.get(i));
            values.put("thoigian", arrayThoiGian.get(i));
            values.put("trangthaimaybom", arrayTrangThaiMayBom.get(i));
            values.put("tocdo", arrayTocDo.get(i));
            values.put("doam", arrayDoAm.get(i));
            values.put("trangthaihoatdong", arrayCDHD.get(i));
            values.put("mucnuoc", arrayMucNuoc.get(i));
            database.insert("ht", null, values);
        }

    }
    //Hiển thị dữ liệu SQL trong thư mục Assest
    private void showAllContactOnListView() {
        //Buoc 1: Mo CSDL truoc
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
        Cursor cursor = database.query("ht", null, null, null, null, null,  null);
        //Cursor cursor = database.rawQuery("select * from dsls1", null);
        //Xóa dữ liệu trong các mảng cũ
        arrayDoAmCaiDat2.clear();
        arrayThoiGian2.clear();
        arrayTrangThaiMayBom2.clear();
        arrayTocDo2.clear();
        arrayDoAm2.clear();
        arrayCDHD2.clear();
        arrayMucNuoc2.clear();
        while (cursor.moveToNext()){
            int doamcaidat = cursor.getInt(0);
            String thoigian = cursor.getString(1);
            int trangthaimaybom = cursor.getInt(2);
            int tocdo = cursor.getInt(3);
            int doam = cursor.getInt(4);
            int trangthaihoatdong = cursor.getInt(5);
            int mucnuoc = cursor.getInt(6);
//            dsLichSu.add("Do Am Cai Dat: "+doamcaidat+"\n"+
//                    "Thoi gian: "+thoigian+"\n"+
//                    "Trang thai may bom: "+trangthaimaybom+"\n"+
//                    "Toc Do: "+tocdo+"\n"+
//                    "Do Am Cam Bien: "+doam);
            arrayDoAmCaiDat2.add(doamcaidat);
            arrayThoiGian2.add(thoigian);
            arrayTrangThaiMayBom2.add(trangthaimaybom);
            arrayTocDo2.add(tocdo);
            arrayDoAm2.add(doam);
            arrayCDHD2.add(trangthaihoatdong);
            arrayMucNuoc2.add(mucnuoc);
        }
        cursor.close();
        adapterLichSu1.notifyDataSetChanged();
    }

    private void addEvents() {
        btnChuyenVeManHinhChinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private void addConTrols() {
        btnChuyenVeManHinhChinh = findViewById(R.id.btnChuyenManHinh);
        lvLichSu = findViewById(R.id.lvLichSu);
//        dsLichSu = new ArrayList<>();
        //Thêm dữ lieu hien thị giao dien
        adapterLichSu1 = new MainActivity_HienThiLichSu (
                MainActivity2.this,
                R.layout.activity_lichsu2,
                arrayDoAmCaiDat2,
                arrayThoiGian2,
                arrayTrangThaiMayBom2,
                arrayTocDo2,
                arrayDoAm2,
                arrayCDHD2,
                arrayMucNuoc2);
        lvLichSu.setAdapter(adapterLichSu1);
//        dsLichSu = new ArrayList<>();
//        adapterLichSu = new ArrayAdapter<>(MainActivity2.this,
//                android.R.layout.simple_list_item_1,
//                dsLichSu);
//        lvLichSu.setAdapter(adapterLichSu);
    }
}
