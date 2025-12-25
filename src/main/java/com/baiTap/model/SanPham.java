package com.baiTap.model;

public class SanPham {
    private String MaSP;
    private String TenSP;
    private double GiaSP;
    private int SL;
    private String MoTa;
    private String DanhMuc;

    public SanPham()
    {

    }

    public SanPham(String maSP, String tenSP, double giaSP, int SL, String moTa, String danhMuc) {
        this.MaSP = maSP;
        this.TenSP = tenSP;
        this.GiaSP = giaSP;
        this.SL = SL;
        this.MoTa = moTa;
        this.DanhMuc = danhMuc;
    }

    public String getMaSP() {
        return MaSP;
    }

    public void setMaSP(String maSP) {
        MaSP = maSP;
    }

    public String getTenSP() {
        return TenSP;
    }

    public void setTenSP(String tenSP) {
        TenSP = tenSP;
    }

    public double getGiaSP() {
        return GiaSP;
    }

    public void setGiaSP(double giaSP) {
        GiaSP = giaSP;
    }

    public int getSL() {
        return SL;
    }

    public void setSL(int SL) {
        this.SL = SL;
    }

    public String getMoTa() {
        return MoTa;
    }

    public void setMoTa(String moTa) {
        MoTa = moTa;
    }

    public String getDanhMuc() {
        return DanhMuc;
    }

    public void setDanhMuc(String danhMuc) {
        DanhMuc = danhMuc;
    }

    @Override
    public String toString() {
        return "SanPham{" +
                "MaSP='" + MaSP + '\'' +
                ", TenSP='" + TenSP + '\'' +
                ", GiaSP=" + GiaSP +
                ", SL=" + SL +
                ", MoTa='" + MoTa + '\'' +
                ", DanhMuc='" + DanhMuc + '\'' +
                '}';
    }
}
