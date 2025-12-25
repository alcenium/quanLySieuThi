package com.baiTap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class frmNhanVien extends JFrame {
    private JTable nhanVienTable;
    private DefaultTableModel tableModel;
    private JTextField txtMaNV, txtTenNV, txtSDT, txtDiaChi, txtNgaySinh;
    private JComboBox<String> cboGioiTinh;
    private JTextField txtTenTK, txtMatKhau;
    private JList<String> listChucVu;
    private DefaultListModel<String> chucVuListModel;
    
    // Database connection
    private Connection conn;
    private final String DB_URL = "jdbc:mysql://localhost:3306/QLBanHangSieuThi";
    private final String USER = "root";
    private final String PASS = "";
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public frmNhanVien() {
        super("Quản lý nhân viên");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Kết nối database
        connectDatabase();

        JLabel lbTitle = new JLabel("QUẢN LÝ NHÂN VIÊN", JLabel.CENTER);
        lbTitle.setForeground(Color.blue);
        lbTitle.setFont(new Font("Arial", Font.BOLD, 25));
        add(lbTitle, BorderLayout.PAGE_START);

        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

        //Panel Left
        JPanel pnLeft = new JPanel(new BorderLayout());
        chucVuListModel = new DefaultListModel<>();
        chucVuListModel.addElement("Tất cả nhân viên");
        chucVuListModel.addElement("Nam");
        chucVuListModel.addElement("Nữ");
        
        listChucVu = new JList<>(chucVuListModel);
        JScrollPane scrollPane = new JScrollPane(listChucVu);
        scrollPane.setBorder(new TitledBorder(border, "Lọc theo giới tính"));

        JPanel pnBtn1 = new JPanel();
        JButton btnRefresh = new JButton("Làm mới");
        JButton btnSearch = new JButton("Tìm kiếm");
        
        pnBtn1.add(btnRefresh);
        pnBtn1.add(btnSearch);

        pnLeft.add(scrollPane, BorderLayout.CENTER);
        pnLeft.add(pnBtn1, BorderLayout.PAGE_END);

        //Panel Right
        JPanel pnRight = new JPanel(new BorderLayout());

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết"));

        String[] columns = {"Mã NV", "Tên nhân viên", "Ngày sinh", "Giới tính", "SĐT", "Địa chỉ"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        nhanVienTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(nhanVienTable);
        tablePanel.add(tableScrollPane);
        pnRight.add(tablePanel, BorderLayout.PAGE_START);
        tableScrollPane.setPreferredSize(new Dimension(tableScrollPane.getWidth(), 200));

        JPanel pnTT = new JPanel(new GridLayout(6, 2, 5, 3));
        pnTT.setBorder(new TitledBorder(border, "Thông tin nhân viên"));
        
        JLabel lbMaNV = new JLabel("Mã nhân viên:");
        JLabel lbTenNV = new JLabel("Tên nhân viên:");
        JLabel lbNgaySinh = new JLabel("Ngày sinh (yyyy-MM-dd):");
        JLabel lbGioiTinh = new JLabel("Giới tính:");
        JLabel lbSDT = new JLabel("Số điện thoại:");
        JLabel lbDiaChi = new JLabel("Địa chỉ:");

        txtMaNV = new JTextField(30);
        txtMaNV.setEditable(false); // Auto increment
        txtTenTK = new JTextField(30);
        txtMatKhau = new JPasswordField(30);
        txtTenNV = new JTextField(30);
        txtNgaySinh = new JTextField(30);
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        txtSDT = new JTextField(30);
        txtDiaChi = new JTextField(30);

        pnTT.add(lbMaNV);
        pnTT.add(txtMaNV);
        pnTT.add(lbTenNV);
        pnTT.add(txtTenNV);
        pnTT.add(lbNgaySinh);
        pnTT.add(txtNgaySinh);
        pnTT.add(lbGioiTinh);
        pnTT.add(cboGioiTinh);
        pnTT.add(lbSDT);
        pnTT.add(txtSDT);
        pnTT.add(lbDiaChi);
        pnTT.add(txtDiaChi);

        pnRight.add(pnTT, BorderLayout.CENTER);

        //Panel Btn2
        JPanel pnBtn2 = new JPanel();
        JButton btAdd = new JButton("Thêm");
        JButton btUpdate = new JButton("Sửa");
        JButton btDelete = new JButton("Xóa");
        JButton btClear = new JButton("Xóa trắng");

        pnBtn2.add(btAdd);
        pnBtn2.add(btUpdate);
        pnBtn2.add(btDelete);
        pnBtn2.add(btClear);

        pnRight.add(pnBtn2, BorderLayout.PAGE_END);

        //JSplitPane
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnLeft, pnRight);
        add(jSplitPane);

        setSize(900, 500);
        setLocationRelativeTo(null);

        // Load dữ liệu ban đầu
        loadAllNhanVien();

        // Event handlers
        btnRefresh.addActionListener(e -> loadAllNhanVien());
        btnSearch.addActionListener(e -> TimKiem());
        
        btAdd.addActionListener(e -> ThemNhanVien());
        btUpdate.addActionListener(e -> SuaNhanVien());
        btDelete.addActionListener(e -> XoaNhanVien());
        btClear.addActionListener(e -> ClearForm());
        
        // Event khi click vào table
        nhanVienTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = nhanVienTable.getSelectedRow();
                if (row >= 0) {
                    txtMaNV.setText(tableModel.getValueAt(row, 0).toString());
                    txtTenNV.setText(tableModel.getValueAt(row, 1).toString());
                    txtNgaySinh.setText(tableModel.getValueAt(row, 2).toString());
                    cboGioiTinh.setSelectedItem(tableModel.getValueAt(row, 3).toString());
                    txtSDT.setText(tableModel.getValueAt(row, 4).toString());
                    txtDiaChi.setText(tableModel.getValueAt(row, 5).toString());
                    
                    // Load thông tin tài khoản
                    loadThongTinTaiKhoan(tableModel.getValueAt(row, 0).toString());
                }
            }
        });
        
        // Event khi chọn filter
        listChucVu.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = listChucVu.getSelectedValue();
                if (selected != null) {
                    if (selected.equals("Tất cả nhân viên")) {
                        loadAllNhanVien();
                    } else {
                        loadNhanVienByGioiTinh(selected);
                    }
                }
            }
        });
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
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối database!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Load tất cả nhân viên
    private void loadAllNhanVien() {
        tableModel.setRowCount(0);
        String sql = "SELECT maNV, ten, ngaySinh, gioiTinh, sdt, diaChi FROM nhanVien";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("maNV"),
                    rs.getString("ten"),
                    rs.getDate("ngaySinh"),
                    rs.getString("gioiTinh"),
                    rs.getString("sdt"),
                    rs.getString("diaChi")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi load nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Load nhân viên theo giới tính
    private void loadNhanVienByGioiTinh(String gioiTinh) {
        tableModel.setRowCount(0);
        String sql = "SELECT maNV, ten, ngaySinh, gioiTinh, sdt, diaChi FROM nhanVien WHERE gioiTinh = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, gioiTinh);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("maNV"),
                    rs.getString("ten"),
                    rs.getDate("ngaySinh"),
                    rs.getString("gioiTinh"),
                    rs.getString("sdt"),
                    rs.getString("diaChi")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi load nhân viên theo giới tính!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Load thông tin tài khoản
    private void loadThongTinTaiKhoan(String maNV) {
        String sql = "SELECT tenTK, matKhau FROM taiKhoan WHERE maTK = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(maNV));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                txtTenTK.setText(rs.getString("tenTK"));
                txtMatKhau.setText(rs.getString("matKhau"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tìm kiếm nhân viên
    private void TimKiem() {
        String keyword = JOptionPane.showInputDialog(this, "Nhập tên nhân viên cần tìm:");
        if (keyword != null && !keyword.trim().isEmpty()) {
            tableModel.setRowCount(0);
            String sql = "SELECT maNV, ten, ngaySinh, gioiTinh, sdt, diaChi FROM nhanVien WHERE ten LIKE ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, "%" + keyword + "%");
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("maNV"),
                        rs.getString("ten"),
                        rs.getDate("ngaySinh"),
                        rs.getString("gioiTinh"),
                        rs.getString("sdt"),
                        rs.getString("diaChi")
                    };
                    tableModel.addRow(row);
                }
                
                if (tableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên nào!");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // ============ CRUD NHÂN VIÊN ============
    private void ThemNhanVien() {
        // Validate input
        if (txtTenTK.getText().trim().isEmpty() ||
            txtMatKhau.getText().trim().isEmpty() ||
            txtTenNV.getText().trim().isEmpty() || 
            txtNgaySinh.getText().trim().isEmpty() ||
            txtSDT.getText().trim().isEmpty() ||
            txtDiaChi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        
        try {
            // Validate ngày sinh
            java.util.Date ngaySinh = dateFormat.parse(txtNgaySinh.getText().trim());
            java.sql.Date sqlDate = new java.sql.Date(ngaySinh.getTime());
            
            // Bắt đầu transaction
            conn.setAutoCommit(false);
            
            try {
                // Thêm tài khoản trước
                String sqlTK = "INSERT INTO taiKhoan (tenTK, matKhau) VALUES (?, ?)";
                PreparedStatement pstmtTK = conn.prepareStatement(sqlTK, Statement.RETURN_GENERATED_KEYS);
                pstmtTK.setString(1, txtTenTK.getText().trim());
                pstmtTK.setString(2, txtMatKhau.getText().trim());
                pstmtTK.executeUpdate();
                
                // Lấy maTK vừa tạo
                ResultSet rs = pstmtTK.getGeneratedKeys();
                int maTK = 0;
                if (rs.next()) {
                    maTK = rs.getInt(1);
                }
                
                // Thêm nhân viên
                String sqlNV = "INSERT INTO nhanVien (maNV, ten, ngaySinh, gioiTinh, sdt, diaChi) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmtNV = conn.prepareStatement(sqlNV);
                pstmtNV.setInt(1, maTK);
                pstmtNV.setString(2, txtTenNV.getText().trim());
                pstmtNV.setDate(3, sqlDate);
                pstmtNV.setString(4, (String) cboGioiTinh.getSelectedItem());
                pstmtNV.setString(5, txtSDT.getText().trim());
                pstmtNV.setString(6, txtDiaChi.getText().trim());
                
                int result = pstmtNV.executeUpdate();
                
                // Commit transaction
                conn.commit();
                
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                    loadAllNhanVien();
                    ClearForm();
                }
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
            
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày sinh không hợp lệ! (yyyy-MM-dd)", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void SuaNhanVien() {
        if (txtMaNV.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!");
            return;
        }
        
        try {
            int maNV = Integer.parseInt(txtMaNV.getText().trim());
            
            // Validate ngày sinh
            java.util.Date ngaySinh = dateFormat.parse(txtNgaySinh.getText().trim());
            java.sql.Date sqlDate = new java.sql.Date(ngaySinh.getTime());
            
            // Bắt đầu transaction
            conn.setAutoCommit(false);
            
            try {
                // Update tài khoản
                String sqlTK = "UPDATE taiKhoan SET tenTK=?, matKhau=? WHERE maTK=?";
                PreparedStatement pstmtTK = conn.prepareStatement(sqlTK);
                pstmtTK.setString(1, txtTenTK.getText().trim());
                pstmtTK.setString(2, txtMatKhau.getText().trim());
                pstmtTK.setInt(3, maNV);
                pstmtTK.executeUpdate();
                
                // Update nhân viên
                String sqlNV = "UPDATE nhanVien SET ten=?, ngaySinh=?, gioiTinh=?, sdt=?, diaChi=? WHERE maNV=?";
                PreparedStatement pstmtNV = conn.prepareStatement(sqlNV);
                pstmtNV.setString(1, txtTenNV.getText().trim());
                pstmtNV.setDate(2, sqlDate);
                pstmtNV.setString(3, (String) cboGioiTinh.getSelectedItem());
                pstmtNV.setString(4, txtSDT.getText().trim());
                pstmtNV.setString(5, txtDiaChi.getText().trim());
                pstmtNV.setInt(6, maNV);
                
                int result = pstmtNV.executeUpdate();
                
                // Commit transaction
                conn.commit();
                
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Sửa nhân viên thành công!");
                    loadAllNhanVien();
                    ClearForm();
                }
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
            
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày sinh không hợp lệ! (yyyy-MM-dd)", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi sửa nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void XoaNhanVien() {
        if (txtMaNV.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa nhân viên này?\n(Sẽ xóa cả tài khoản liên quan)", 
            "Xác nhận", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int maNV = Integer.parseInt(txtMaNV.getText().trim());
                
                // Xóa tài khoản (sẽ tự động xóa nhân viên do ON DELETE CASCADE)
                String sql = "DELETE FROM taiKhoan WHERE maTK = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, maNV);
                    int result = pstmt.executeUpdate();
                    if (result > 0) {
                        JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!");
                        loadAllNhanVien();
                        ClearForm();
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Lỗi xóa nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Mã nhân viên không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void ClearForm() {
        txtMaNV.setText("");
        txtTenTK.setText("");
        txtMatKhau.setText("");
        txtTenNV.setText("");
        txtNgaySinh.setText("");
        cboGioiTinh.setSelectedIndex(0);
        txtSDT.setText("");
        txtDiaChi.setText("");
        nhanVienTable.clearSelection();
        txtTenTK.requestFocus();
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
    
    // Test chạy form
    public static void main(String[] args) {
        frmNhanVien frmNhanVien = new frmNhanVien();
        frmNhanVien.setVisible(true);
    }
}
