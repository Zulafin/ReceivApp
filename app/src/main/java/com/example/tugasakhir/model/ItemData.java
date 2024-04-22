package com.example.tugasakhir.model;

public class ItemData {
    private String itemId;
    private String pilihanBarang;
    private String noResi;
    private String tanggalMasuk;
    private String namaPenerima;
    private String namaPengirim;
    private String imageUrl;
    private String receivedDate;
    private String status;
    private String barangType;

    public ItemData() {
    }

    public ItemData(
            String itemId,
            String pilihanBarang,
            String noResi,
            String tanggalMasuk,
            String namaPenerima,
            String namaPengirim,
            String imageUrl,
            String receivedDate,
            String status,
            String barangType
    ) {
        this.itemId = itemId;
        this.pilihanBarang = pilihanBarang;
        this.noResi = noResi;
        this.tanggalMasuk = tanggalMasuk;
        this.namaPenerima = namaPenerima;
        this.namaPengirim = namaPengirim;
        this.imageUrl = imageUrl;
        this.receivedDate = receivedDate;
        this.status = status;
        this.barangType = barangType;
    }

    public String getItemId() {
        return itemId;
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

    public String getImageUrl() {
        return imageUrl;
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

    public String getBarangType() {
        return barangType;
    }

    public void setBarangType(String barangType) {
        this.barangType = barangType;
    }
}