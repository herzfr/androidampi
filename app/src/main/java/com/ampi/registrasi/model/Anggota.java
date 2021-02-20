package com.ampi.registrasi.model;

public class Anggota {

    private int id;
    private String name;
    private String noreg;
    private String image;
    private String status;
    private String jabatan;
    private int time;

    public Anggota(int id, String name, String noreg, String image, String status, String jabatan, int time) {
        this.id = id;
        this.name = name;
        this.noreg = noreg;
        this.image = image;
        this.status = status;
        this.jabatan = jabatan;
        this.time = time;
    }

    public Anggota() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNoreg() {
        return noreg;
    }

    public void setNoreg(String noreg) {
        this.noreg = noreg;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Anggota{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", noreg='" + noreg + '\'' +
                ", image=" + image +
                ", status='" + status + '\'' +
                ", time=" + time +
                '}';
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }
}
