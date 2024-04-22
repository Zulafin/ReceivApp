package com.example.tugasakhir.model;

import com.google.firebase.database.PropertyName;

public class LaporanBarangItem {
    private String itemId;
    private String noResi;
    private String pilihanBarang;
    private String tanggalMasuk;
    private String namaPenerima;
    private String namaPengirim;
    private String imageUrl;


    public LaporanBarangItem() {
    }

    public LaporanBarangItem(String itemId, String noResi, String pilihanBarang, String tanggalMasuk, String namaPenerima, String namaPengirim, String imageUrl) {
        this.itemId = itemId;
        this.noResi = noResi;
        this.pilihanBarang = pilihanBarang;
        this.tanggalMasuk = tanggalMasuk;
        this.namaPenerima = namaPenerima;
        this.namaPengirim = namaPengirim;
        this.imageUrl = imageUrl;
    }

    public String getItemId() {
        return itemId;
    }

    @PropertyName("noResi")
    public String getNoResi() {
        return noResi;
    }

    @PropertyName("pilihanBarang")
    public String getPilihanBarang() {
        return pilihanBarang;
    }

    @PropertyName("tanggalMasuk")
    public String getTanggalMasuk() {
        return tanggalMasuk;
    }

    @PropertyName("namaPenerima")
    public String getNamaPenerima() {
        return namaPenerima;
    }

    @PropertyName("namaPengirim")
    public String getNamaPengirim() {
        return namaPengirim;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
