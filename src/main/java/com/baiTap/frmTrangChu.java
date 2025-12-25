package com.baiTap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class frmTrangChu extends JFrame {
    public frmTrangChu()
    {
        super("Trang chu");
        setDefaultCloseOperation(frmLogin.DISPOSE_ON_CLOSE);

        JLabel lbTitle = new JLabel("TRANG CHỦ", JLabel.CENTER);
        lbTitle.setForeground(Color.RED);
        lbTitle.setFont(new Font("Arial", Font.BOLD, 25));
        add(lbTitle, BorderLayout.PAGE_START);

        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("Menu");

        JMenuItem mSanPham = new JMenuItem("Sản phẩm");
        JMenuItem mNhanVien = new JMenuItem("Nhân viên");
        JMenuItem mHoaDon = new JMenuItem("Hóa đơn");
        JMenuItem mKhachHang = new JMenuItem("Khách hàng");
        JMenuItem mThongKe = new JMenuItem("Thống kê");
        JMenuItem mExit = new JMenuItem("Thoat");

        JMenuItem mGioHang = new JMenuItem("Giỏ hàng");
        JMenuItem mTaiKhoan = new JMenuItem("Tài khoản");

        menuFile.add(mSanPham);
        menuFile.add(mNhanVien);
        menuFile.add(mKhachHang);
        menuFile.add(mHoaDon);
        menuFile.add(mThongKe);
        menuFile.add(mTaiKhoan);
        menuFile.add(mGioHang);
        menuFile.addSeparator();
        menuFile.add(mExit);

        menuBar.add(menuFile);
        setJMenuBar(menuBar);

        pack();
        setSize(900, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        mSanPham.addActionListener(e ->
        {
            frmSanPham frmSanPham = new frmSanPham();
            frmSanPham.setVisible(true);
        });

        mGioHang.addActionListener(e -> {
            frmGioHang frmGioHang = new frmGioHang();
            frmGioHang.setVisible(true);
        });

        mNhanVien.addActionListener(e ->
        {
            frmNhanVien frmNhanVien = new frmNhanVien();
            frmNhanVien.setVisible(true);
        });

        mHoaDon.addActionListener(e -> {
            frmHoaDon frmHoaDon = new frmHoaDon();
            frmHoaDon.setVisible(true);
        });

        mKhachHang.addActionListener(e -> {
            frmKhachHang frmKhachHang = new frmKhachHang();
            frmKhachHang.setVisible(true);
        });

        mThongKe.addActionListener(e -> {
            frmThongKe frmThongKe = new frmThongKe();
            frmThongKe.setVisible(true);
        });

        mExit.addActionListener(e ->
        {
            int result = JOptionPane.showConfirmDialog(null, "Ban co muon thoat", "Thoat", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION)
            {
                this.dispose();
                new frmLogin();
            }

        });
    }
}
