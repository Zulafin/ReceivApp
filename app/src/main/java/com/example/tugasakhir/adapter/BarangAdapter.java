package com.example.tugasakhir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugasakhir.R;
import com.example.tugasakhir.model.ItemData;

import java.util.ArrayList;
import java.util.List;

public class BarangAdapter extends RecyclerView.Adapter<BarangAdapter.ViewHolder> {

    private List<ItemData> itemList;
    private List<ItemData> originalList;
    private Context context;
    private static BarangAdapterListener mListener;

    public BarangAdapter(Context context, List<ItemData> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.originalList = new ArrayList<>(itemList);
    }

    public void setOnItemClickListener(BarangAdapterListener listener) {
        mListener = listener;
    }

    public interface BarangAdapterListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemData item = itemList.get(position);

        holder.textNoResi.setText(item.getNoResi());
        holder.textPilihanBarang.setText(item.getPilihanBarang());
        holder.textTanggalMasuk.setText(item.getTanggalMasuk());
        holder.textNamaPenerima.setText(item.getNamaPenerima());
        holder.textNamaPengirim.setText(item.getNamaPengirim());
        holder.status.setText(item.getStatus());
        holder.tanggalTerima.setText(item.getReceivedDate());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textNoResi, textPilihanBarang, textTanggalMasuk, textNamaPenerima,
                textNamaPengirim, status, tanggalTerima;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textNoResi = itemView.findViewById(R.id.textNoResi);
            textPilihanBarang = itemView.findViewById(R.id.textPilihanBarang);
            textTanggalMasuk = itemView.findViewById(R.id.textTanggalMasuk);
            textNamaPenerima = itemView.findViewById(R.id.textNamaPenerima);
            textNamaPengirim = itemView.findViewById(R.id.textNamaPengirim);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            status = itemView.findViewById(R.id.text_status);
            tanggalTerima = itemView.findViewById(R.id.text_received_date);

            btnEdit.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onEditClick(position);
                }
            });

            btnDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onDeleteClick(position);
                }
            });
        }
    }

}