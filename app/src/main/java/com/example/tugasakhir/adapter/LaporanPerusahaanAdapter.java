package com.example.tugasakhir.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugasakhir.R;
import com.example.tugasakhir.model.ItemData;

import java.util.List;

public class LaporanPerusahaanAdapter extends RecyclerView.Adapter<LaporanPerusahaanAdapter.ViewHolder> {

    private final List<ItemData> itemList;
    private  LaporanPerusahaanAdapterListener mListener;

    public LaporanPerusahaanAdapter(List<ItemData> itemList) {
        this.itemList = itemList;
    }

    public void setOnItemClickListener(LaporanPerusahaanAdapterListener listener) {
        this.mListener = listener;
    }

    public interface LaporanPerusahaanAdapterListener {
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
        ItemData itemData = itemList.get(position);

        holder.textNoResi.setText(itemData.getNoResi());
        holder.textPilihanBarang.setText(itemData.getPilihanBarang());
        holder.textTanggalMasuk.setText(itemData.getTanggalMasuk());
        holder.textNamaPenerima.setText(itemData.getNamaPenerima());
        holder.textNamaPengirim.setText(itemData.getNamaPengirim());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textPilihanBarang, status, tanggalTerima, statusTitle, tanggalTerimaTitle;
        TextView textNoResi;
        TextView textTanggalMasuk;
        TextView textNamaPenerima;
        TextView textNamaPengirim;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textPilihanBarang = itemView.findViewById(R.id.textPilihanBarang);
            textNoResi = itemView.findViewById(R.id.textNoResi);
            textTanggalMasuk = itemView.findViewById(R.id.textTanggalMasuk);
            textNamaPenerima = itemView.findViewById(R.id.textNamaPenerima);
            textNamaPengirim = itemView.findViewById(R.id.textNamaPengirim);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            status = itemView.findViewById(R.id.text_status);
            statusTitle = itemView.findViewById(R.id.title_status);
            tanggalTerima = itemView.findViewById(R.id.text_received_date);
            tanggalTerimaTitle = itemView.findViewById(R.id.title_received_date);

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

            status.setVisibility(View.GONE);
            statusTitle.setVisibility(View.GONE);
            tanggalTerima.setVisibility(View.GONE);
            tanggalTerimaTitle.setVisibility(View.GONE);
        }
    }
}