package com.example.ht_da4;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity_HienThiLichSu extends ArrayAdapter {
    //Độ ẩm cài đặt, Thời gian, Trạng thái máy bơm, Tốc Độ, Độ ẩm
    Activity context;
    int resource;
    List<Integer> DoAmCaiDat, TrangThaiMayBom, TocDo, DoAm, CDHD, MucNuoc;
    List<String> ThoiGian;

    public MainActivity_HienThiLichSu(@NonNull Activity context, int resource,@NonNull List<Integer> DoAmCaiDat,@NonNull List<String> ThoiGian,@NonNull List<Integer> TrangThaiMayBom,@NonNull List<Integer> TocDo, @NonNull List<Integer> DoAm, @NonNull List<Integer> CDHD, @NonNull List<Integer> MucNuoc) {
        super(context, resource, DoAm);
        this.context = context;
        this.resource = resource;
        this.DoAmCaiDat = DoAmCaiDat;
        this.ThoiGian = ThoiGian;
        this.TrangThaiMayBom = TrangThaiMayBom;
        this.TocDo = TocDo;
        this.DoAm = DoAm;
        this.CDHD = CDHD;
        this.MucNuoc = MucNuoc;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View row = inflater.inflate(this.resource, null);
        //Thời gian
        TextView txtThoiGian = row.findViewById(R.id.txtLSTG);
        txtThoiGian.setText(ThoiGian.get(position));
        //Độ ẩm cảm biến
        TextView txtDoAmCaiDat = row.findViewById(R.id.txtLSDACD);
        txtDoAmCaiDat.setText(DoAmCaiDat.get(position)+"%");
        //Trạng thái máy bơm
        ImageView imageViewMayBom = row.findViewById(R.id.imageViewMayBom);
        if(TrangThaiMayBom.get(position) == 0){
            imageViewMayBom.setImageResource(R.drawable.maybomoff);
        }
        if(TrangThaiMayBom.get(position) == 1){
            imageViewMayBom.setImageResource(R.drawable.maybomonl);
        }
        //Tốc độ
        TextView txtTocDo = row.findViewById(R.id.txtLSTD);
       // txtTocDo.setText(TocDo.get(position));
        txtTocDo.setText(String.valueOf(TocDo.get(position)));
        //Độ ẩm
        TextView txtDoAmCamBien = row.findViewById(R.id.txtLSDA);
        txtDoAmCamBien.setText(DoAm.get(position)+"%");
        //Che Do hoat dong
        TextView txtCDHD = row.findViewById(R.id.txtLSTTHD);
        if(CDHD.get(position)%2==0){
            txtCDHD.setText("Tự Động");
        }
        if(CDHD.get(position)%2==1){
            txtCDHD.setText("Thủ Công");
        }
        //Lượng nước
        TextView txtLuongNuoc = row.findViewById(R.id.txtLSLN);
        if(MucNuoc.get(position) == 0){
            txtLuongNuoc.setText("Hết Nước");
        }
        if(MucNuoc.get(position) ==1){
            txtLuongNuoc.setText("Còn Nước");
        }
        return row;
    }
}
