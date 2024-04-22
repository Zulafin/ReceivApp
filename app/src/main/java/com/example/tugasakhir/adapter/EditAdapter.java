package com.example.tugasakhir.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugasakhir.R;
import com.example.tugasakhir.model.ItemData;

import java.util.List;

public class EditAdapter extends RecyclerView.Adapter<EditAdapter.ViewHolder> {

    private final List<ItemData> editList;
    private EditAdapterListener mListener;
    public EditAdapter(List<ItemData> editList) {
        this.editList = editList;
    }
    public void setOnItemClickListener(EditAdapterListener listener) {
        this.mListener = listener;
    }
    public interface EditAdapterListener {
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
        ItemData itemData = editList.get(position);
        holder.textPilihanBarang.setText(itemData.getPilihanBarang());
        holder.textNoResi.setText(itemData.getNoResi());
        holder.textTanggalMasuk.setText(itemData.getTanggalMasuk());
        holder.textNamaPenerima.setText(itemData.getNamaPenerima());
        holder.textNamaPengirim.setText(itemData.getNamaPengirim());
    }

    @Override
    public int getItemCount() {
        return editList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textPilihanBarang;
        TextView textNoResi;
        TextView textTanggalMasuk;
        TextView textNamaPenerima;
        TextView textNamaPengirim;
        ImageButton btnEdit;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textPilihanBarang = itemView.findViewById(R.id.textPilihanBarang);
            textNoResi = itemView.findViewById(R.id.textNoResi);
            textTanggalMasuk = itemView.findViewById(R.id.textTanggalMasuk);
            textNamaPenerima = itemView.findViewById(R.id.textNamaPenerima);
            textNamaPengirim = itemView.findViewById(R.id.textNamaPengirim);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnEdit.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onEditClick(position);
                }
            });

            btnDelete.setOnClickListener(v -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onDeleteClick(position);
                }
            });
        }
    }
}