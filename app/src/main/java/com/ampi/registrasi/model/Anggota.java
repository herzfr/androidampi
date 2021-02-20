package com.ampi.registrasi.model;

import java.util.Arrays;

public class Anggota {

    private int id;
    private String name;
    private String noreg;
    private byte[] image;
    private String status;
    private int time;

    public Anggota(int id, String name, String noreg, byte[] image, String status, int time) {
        this.id = id;
        this.name = name;
        this.noreg = noreg;
        this.image = image;
        this.status = status;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
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
                ", image=" + Arrays.toString(image) +
                ", status='" + status + '\'' +
                ", time=" + time +
                '}';
    }
}
