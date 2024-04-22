package com.example.tugasakhir.model;

public class DataEdit {
    private String pilihanBarang;
    private String noResi;
    private String tanggalMasuk;
    private String namaPenerima;
    private String namaPengirim;
    private String receivedDate;
    private String status;
    private String itemId;

    public DataEdit(String pilihanBarang, String noResi, String tanggalMasuk, String namaPenerima, String namaPengirim, String receivedDate, String status) {
        this.pilihanBarang = pilihanBarang;
        this.noResi = noResi;
        this.tanggalMasuk = tanggalMasuk;
        this.namaPenerima = namaPenerima;
        this.namaPengirim = namaPengirim;
        this.receivedDate = receivedDate;
        this.status = status;
    }

    public String getPilihanBarang() {
        return pilihanBarang;
    }

    public String getNoResi() {
        return noResi;
    }

    public String getTanggalMasuk() {
        return tanggalMasuk;
    }

    public String getNamaPenerima() {
        return namaPenerima;
    }

    public String getNamaPengirim() {
        return namaPengirim;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        this.receivedDate = receivedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getItemId() {
        return itemId;
    }
}

