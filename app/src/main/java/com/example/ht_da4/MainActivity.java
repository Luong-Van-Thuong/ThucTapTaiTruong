package com.example.ht_da4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class MainActivity extends AppCompatActivity {


    TextView txtTrangThaiMayBom, txtDoAmCamBien, txtDoAmCaiDat, txtTocDo, txtMucNuoc;
    Button btnBatMayBom, btnTatMayBom, btnLichSu, btnCDHD;
    ImageView imageMayBom;
    SeekBar seekBTocDo;
    private int a = 0, b = 1;
    private boolean trangThaiTDOrTT = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvents();
        xuLyKiemTraData();
        xuLyEventsFirebase();
    }

    private void xuLyKiemTraData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("TrangThaiMayBom");

        txtDoAmCaiDat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần thực hiện gì ở đây
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Kiểm tra xem dữ liệu nhập vào có phải là số hay không
                if (!TextUtils.isEmpty(s)) {
                    String input = s.toString();
                    if (!input.matches("^\\d{2}$") || Integer.parseInt(input) > 99) {
                        // Nếu dữ liệu nhập vào không phải là số từ 00 đến 99, hiển thị thông báo lỗi
                        txtDoAmCaiDat.setError("Vui lòng chỉ nhập số từ 00 đến 99");
                    } else {
                        // Nếu dữ liệu nhập vào là số,  thông báo lỗi
                        DatabaseReference DA = database.getReference("DoAmCaiDat");
                        DA.setValue(Integer.parseInt(input));
                        txtDoAmCaiDat.setError(null);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
                // Không cần thực hiện gì ở đây
            }
        });

    }

    private void xuLyEventsFirebase() {
        DatabaseReference itemsRef = FirebaseDatabase.getInstance().getReference();

        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String trangThai = dataSnapshot.child("TrangThaiMayBom").getValue().toString();
                if(Integer.parseInt(trangThai) == 0 ){
                    txtTrangThaiMayBom.setText("May Bom Khong Hoat Dong");
                    imageMayBom.setImageResource(R.drawable.maybomoff);
                }else if(Integer.parseInt(trangThai) == 1) {
                    txtTrangThaiMayBom.setText("May Bom Dang Hoat Dong");
                    imageMayBom.setImageResource(R.drawable.maybomonl);
                }
                String dAmCaiDat = dataSnapshot.child("DoAmCaiDat").getValue().toString();
                txtDoAmCaiDat.setText(dAmCaiDat);

                String dAmCamBien = dataSnapshot.child("DoAmCamBien").getValue().toString();
                txtDoAmCamBien.setText(dAmCamBien);

                Integer MucNuoc = dataSnapshot.child("MucNuoc").getValue(Integer.class);
                if(MucNuoc == 0)
                    txtMucNuoc.setText("Hết Nước");
                if(MucNuoc ==1)
                    txtMucNuoc.setText("Còn Nước");

                String tD = dataSnapshot.child("TocDo").getValue().toString();
                if(Integer.parseInt(tD) < 10){
                    txtTocDo.setText("0"+tD);
                } else if(Integer.parseInt(tD) == 10){
                    txtTocDo.setText("10");
                }else {
                    txtTocDo.setText(tD);
                }
                seekBTocDo.setProgress(Integer.parseInt(tD));

                Integer tthd = dataSnapshot.child("TrangThaiHoatDong").getValue(Integer.class);

                if(tthd%2==0){
                    btnCDHD.setText("Chế Độ Tự Động");
                    btnTatMayBom.setEnabled(false);
                    btnBatMayBom.setEnabled(false);

                }
                if(tthd%2 == 1){
                    btnCDHD.setText("Chế Độ Thủ Công");
                    trangThaiTDOrTT = true;
                    btnTatMayBom.setEnabled(true);
                    btnBatMayBom.setEnabled(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi khi không thể đọc dữ liệu từ cơ sở dữ liệu Firebase
            }
        });
    }

    private void addEvents() {
        btnBatMayBom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase myData = FirebaseDatabase.getInstance();
                DatabaseReference myData1 = myData.getReference("TrangThaiMayBom");
                myData1.setValue(1);
            }
        });
        btnTatMayBom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase myData = FirebaseDatabase.getInstance();
                DatabaseReference myData1 = myData.getReference("TrangThaiMayBom");
                myData1.setValue(0);
            }
        });



        btnLichSu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });
        btnCDHD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a++;
                FirebaseDatabase myData2 = FirebaseDatabase.getInstance();
                DatabaseReference myData3 = myData2.getReference("TrangThaiHoatDong");
                myData3.setValue(a);
            }
        });
    }

    private void addControls() {
        //Khai báo địa chỉ các biến
        txtTrangThaiMayBom = findViewById(R.id.textView); // Địa chỉ biến hiển thị trạng thái hoạt động máy bơm dạng text
        txtDoAmCaiDat      = findViewById(R.id.editTextTextPersonName);//Địa chỉ biến hiển thị và nhập độ ẩm cài đặt máy bơm
        txtDoAmCamBien     = findViewById(R.id.textView4);//Địa chỉ biến hiển thị độ ẩm đất đọc từ cảm biến
        txtTocDo           = findViewById(R.id.textView6); //Địa chỉ biến hiển thị tốc độ động cơ
        txtMucNuoc         = findViewById(R.id.txtMucNuoc);

        btnBatMayBom = findViewById(R.id.button);
        btnTatMayBom = findViewById(R.id.button2);
        btnLichSu    = findViewById(R.id.button3);
        btnCDHD      = findViewById(R.id.btnCDHD);
        imageMayBom  = findViewById(R.id.imageView2);

        seekBTocDo   = findViewById(R.id.seekBar);
        // Đọc giá trị tốc độ
        seekBTocDo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int bienPhu = 1;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                bienPhu = i;
                if(i < 10) {
                    seekBTocDo.setProgress(i);
                }
//                else if(i == 10){
//                    seekBTocDo.setProgress();
//                }
                else {
                    seekBTocDo.setProgress(i);
                }
                FirebaseDatabase database2 = FirebaseDatabase.getInstance();
                DatabaseReference myRef2 = database2.getReference("TocDo");
                myRef2.setValue(bienPhu);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }
}