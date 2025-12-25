package com.baiTap.model;

import java.util.ArrayList;
import java.util.List;

public class DanhMuc {
    private String tenDanhMuc;
    private List<SanPham> danhSachSanPham;

    public DanhMuc() {
        danhSachSanPham = new ArrayList<>();
    }

    public DanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
        danhSachSanPham = new ArrayList<>();
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public List<SanPham> getDanhSachSanPham() {
        return danhSachSanPham;
    }

    public void setDanhSachSanPham(List<SanPham> danhSachSanPham) {
        this.danhSachSanPham = danhSachSanPham;
    }

    public void themSanPham(SanPham sp) {
        danhSachSanPham.add(sp);
    }

    public boolean xoaSanPham(String productID) {
        return danhSachSanPham.removeIf(sp -> sp.getMaSP().equals(productID));
    }

    public SanPham timSanPham(String productID) {
        for (SanPham sp : danhSachSanPham) {
            if (sp.getMaSP().equals(productID)) {
                return sp;
            }
        }
        return null;
    }

    public boolean capNhatSanPham(SanPham spMoi) {
        for (int i = 0; i < danhSachSanPham.size(); i++) {
            if (danhSachSanPham.get(i).getMaSP().equals(spMoi.getMaSP())) {
                danhSachSanPham.set(i, spMoi);
                return true;
            }
        }
        return false;
    }

    public List<SanPham> timTheoCategory(String category) {
        List<SanPham> result = new ArrayList<>();
        for (SanPham sp : danhSachSanPham) {
            if (sp.getDanhMuc().equals(category)) {
                result.add(sp);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Danh mục: ").append(tenDanhMuc).append("\n");
        for (SanPham sp : danhSachSanPham) {
            sb.append(sp.toString()).append("\n");
        }
        return sb.toString();
    }
}