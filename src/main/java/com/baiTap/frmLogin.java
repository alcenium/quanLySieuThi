package com.baiTap;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class frmLogin extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit;
    
    // Database connection
    private Connection conn;
    private final String DB_URL = "jdbc:mysql://localhost:3306/QLBanHangSieuThi";
    private final String USER = "root";
    private final String PASS = "";
    
    // Thông tin user đã đăng nhập
    public static int maTK;
    public static String tenTK;
    public static String loaiTK; // "nhanvien" hoặc "khachhang"

    public frmLogin() {
        super("Đăng nhập hệ thống");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Kết nối database
        connectDatabase();
        
        // Thiết lập giao diện
        initComponents();
        
        setSize(400, 300);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    
    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Title
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setForeground(new Color(41, 128, 185));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 5, 20, 5);
        panel.add(lblTitle, gbc);
        
        // Username
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        panel.add(lblUsername, gbc);
        
        gbc.gridx = 1;
        txtUsername = new JTextField(20);
        panel.add(txtUsername, gbc);
        
        // Password
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel lblPassword = new JLabel("Mật khẩu:");
        panel.add(lblPassword, gbc);
        
        gbc.gridx = 1;
        txtPassword = new JPasswordField(20);
        panel.add(txtPassword, gbc);
        
        // Buttons panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        btnLogin = new JButton("Đăng nhập");
        btnLogin.setPreferredSize(new Dimension(120, 35));
        btnLogin.setBackground(new Color(41, 128, 185));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        
        btnExit = new JButton("Thoát");
        btnExit.setPreferredSize(new Dimension(120, 35));
        btnExit.setBackground(new Color(149, 165, 166));
        btnExit.setForeground(Color.WHITE);
        btnExit.setFocusPainted(false);
        
        btnPanel.add(btnLogin);
        btnPanel.add(btnExit);
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 5, 5, 5);
        panel.add(btnPanel, gbc);
        
        add(panel);
        
        // Event handlers
        btnLogin.addActionListener(e -> Login());
        btnExit.addActionListener(e -> System.exit(0));
        
        // Enter key login
        txtPassword.addActionListener(e -> Login());
        txtUsername.addActionListener(e -> txtPassword.requestFocus());
    }
    
    // Kết nối database
    private void connectDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Kết nối database thành công!");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy driver MySQL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(0);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối database!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            System.exit(0);
        }
    }
    
    // Xử lý đăng nhập
    private void Login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String sql = "SELECT maTK, tenTK FROM taiKhoan WHERE tenTK = ? AND matKhau = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                maTK = rs.getInt("maTK");
                tenTK = rs.getString("tenTK");
                
                // Kiểm tra loại tài khoản
                String sqlCheckNV = "SELECT maNV FROM nhanVien WHERE maNV = ?";
                PreparedStatement pstmtNV = conn.prepareStatement(sqlCheckNV);
                pstmtNV.setInt(1, maTK);
                ResultSet rsNV = pstmtNV.executeQuery();
                
                if (rsNV.next()) {
                    loaiTK = "nhanvien";
                } else {
                    loaiTK = "khachhang";
                }
                
                JOptionPane.showMessageDialog(this, 
                    "Đăng nhập thành công!\nXin chào " + tenTK, 
                    "Thành công", JOptionPane.INFORMATION_MESSAGE);
                
                // Mở form chính (bạn có thể thay bằng form menu chính của mình)
                this.dispose();
                openMainForm();
                
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Tên đăng nhập hoặc mật khẩu không đúng!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtPassword.setText("");
                txtUsername.requestFocus();
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi đăng nhập: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    // Mở form chính sau khi đăng nhập
    private void openMainForm() {
        // Mở form Trang chủ
        frmTrangChu trangChu = new frmTrangChu();
        trangChu.setVisible(true);
    }
    
    // Đóng kết nối khi đóng form
    @Override
    public void dispose() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Đã đóng kết nối database!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        super.dispose();
    }
    
    public static void main(String[] args) {
            new frmLogin();
    }
}
