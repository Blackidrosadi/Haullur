package com.rosadi.haullur.List.Model;

import java.util.List;

public class Baca {
    String id, idKeluarga, nama, rt, jumlahAlmarhum, dibaca;
    List<Almarhums> almarhumsList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdKeluarga() {
        return idKeluarga;
    }

    public void setIdKeluarga(String idKeluarga) {
        this.idKeluarga = idKeluarga;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getRt() {
        return rt;
    }

    public void setRt(String rt) {
        this.rt = rt;
    }

    public String getJumlahAlmarhum() {
        return jumlahAlmarhum;
    }

    public void setJumlahAlmarhum(String jumlahAlmarhum) {
        this.jumlahAlmarhum = jumlahAlmarhum;
    }

    public String getDibaca() {
        return dibaca;
    }

    public void setDibaca(String dibaca) {
        this.dibaca = dibaca;
    }

    public List<Almarhums> getAlmarhumsList() {
        return almarhumsList;
    }

    public void setAlmarhumsList(List<Almarhums> almarhumsList) {
        this.almarhumsList = almarhumsList;
    }
}
