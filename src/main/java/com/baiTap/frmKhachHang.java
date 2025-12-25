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

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
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

public class frmKhachHang extends JFrame {
    private JTable khachHangTable;
    private DefaultTableModel tableModel;
    private JTextField txtMaKH, txtTenKH, txtSDT, txtDiaChi;
    private JTextField txtTenTK, txtMatKhau;
    private JList<String> listKhuVuc;
    private DefaultListModel<String> khuVucListModel;
    
    // Database connection
    private Connection conn;
    private DB db = new DB();

    public frmKhachHang() {
        super("Quản lý khách hàng");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Kết nối database
        connectDatabase();

        JLabel lbTitle = new JLabel("QUẢN LÝ KHÁCH HÀNG", JLabel.CENTER);
        lbTitle.setForeground(Color.blue);
        lbTitle.setFont(new Font("Arial", Font.BOLD, 25));
        add(lbTitle, BorderLayout.PAGE_START);

        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

        //Panel Left
        JPanel pnLeft = new JPanel(new BorderLayout());
        khuVucListModel = new DefaultListModel<>();
        khuVucListModel.addElement("Tất cả khách hàng");
        khuVucListModel.addElement("Quận 1");
        khuVucListModel.addElement("Quận 3");
        khuVucListModel.addElement("Quận 5");
        khuVucListModel.addElement("Quận 10");
        khuVucListModel.addElement("Quận Bình Thạnh");
        khuVucListModel.addElement("Quận Tân Bình");
        khuVucListModel.addElement("Quận 11");
        khuVucListModel.addElement("Quận Phú Nhuận");
        
        listKhuVuc = new JList<>(khuVucListModel);
        JScrollPane scrollPane = new JScrollPane(listKhuVuc);
        scrollPane.setBorder(new TitledBorder(border, "Lọc theo khu vực"));

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

        String[] columns = {"Mã KH", "Tên khách hàng", "SĐT", "Địa chỉ"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        khachHangTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(khachHangTable);
        tablePanel.add(tableScrollPane);
        pnRight.add(tablePanel, BorderLayout.PAGE_START);
        tableScrollPane.setPreferredSize(new Dimension(tableScrollPane.getWidth(), 200));

        JPanel pnTT = new JPanel(new GridLayout(4, 2, 5, 3));
        pnTT.setBorder(new TitledBorder(border, "Thông tin khách hàng"));
        
        JLabel lbMaKH = new JLabel("Mã khách hàng:");
        JLabel lbTenKH = new JLabel("Tên khách hàng:");
        JLabel lbSDT = new JLabel("Số điện thoại:");
        JLabel lbDiaChi = new JLabel("Địa chỉ:");

        txtMaKH = new JTextField(30);
        txtMaKH.setEditable(false); // Auto increment
        txtTenTK = new JTextField(30);
        txtMatKhau = new JPasswordField(30);
        txtTenKH = new JTextField(30);
        txtSDT = new JTextField(30);
        txtDiaChi = new JTextField(30);

        pnTT.add(lbMaKH);
        pnTT.add(txtMaKH);
        pnTT.add(lbTenKH);
        pnTT.add(txtTenKH);
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
        loadAllKhachHang();

        // Event handlers
        btnRefresh.addActionListener(e -> loadAllKhachHang());
        btnSearch.addActionListener(e -> TimKiem());
        
        btAdd.addActionListener(e -> ThemKhachHang());
        btUpdate.addActionListener(e -> SuaKhachHang());
        btDelete.addActionListener(e -> XoaKhachHang());
        btClear.addActionListener(e -> ClearForm());
        
        // Event khi click vào table
        khachHangTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = khachHangTable.getSelectedRow();
                if (row >= 0) {
                    txtMaKH.setText(tableModel.getValueAt(row, 0).toString());
                    txtTenKH.setText(tableModel.getValueAt(row, 1).toString());
                    txtSDT.setText(tableModel.getValueAt(row, 2).toString());
                    txtDiaChi.setText(tableModel.getValueAt(row, 3).toString());
                    
                    // Load thông tin tài khoản
                    loadThongTinTaiKhoan(tableModel.getValueAt(row, 0).toString());
                }
            }
        });
        
        // Event khi chọn filter
        listKhuVuc.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = listKhuVuc.getSelectedValue();
                if (selected != null) {
                    if (selected.equals("Tất cả khách hàng")) {
                        loadAllKhachHang();
                    } else {
                        loadKhachHangByKhuVuc(selected);
                    }
                }
            }
        });
    }

    // Kết nối database
    private void connectDatabase() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            conn = DriverManager.getConnection(db.get("url"), db.get("username"), db.get("password"));
            System.out.println("Kết nối database thành công!");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy driver MySQL!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi kết nối database!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Load tất cả khách hàng
    private void loadAllKhachHang() {
        tableModel.setRowCount(0);
        String sql = "SELECT maKH, ten, sdt, diachi FROM khachHang";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("maKH"),
                    rs.getString("ten"),
                    rs.getString("sdt"),
                    rs.getString("diachi")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi load khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Load khách hàng theo khu vực
    private void loadKhachHangByKhuVuc(String khuVuc) {
        tableModel.setRowCount(0);
        String sql = "SELECT maKH, ten, sdt, diachi FROM khachHang WHERE diachi LIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + khuVuc + "%");
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("maKH"),
                    rs.getString("ten"),
                    rs.getString("sdt"),
                    rs.getString("diachi")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi load khách hàng theo khu vực!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Load thông tin tài khoản
    private void loadThongTinTaiKhoan(String maKH) {
        String sql = "SELECT tenTK, matKhau FROM taiKhoan WHERE maTK = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(maKH));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                txtTenTK.setText(rs.getString("tenTK"));
                txtMatKhau.setText(rs.getString("matKhau"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tìm kiếm khách hàng
    private void TimKiem() {
        String keyword = JOptionPane.showInputDialog(this, "Nhập tên hoặc SĐT khách hàng:");
        if (keyword != null && !keyword.trim().isEmpty()) {
            tableModel.setRowCount(0);
            String sql = "SELECT maKH, ten, sdt, diachi FROM khachHang WHERE ten LIKE ? OR sdt LIKE ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, "%" + keyword + "%");
                pstmt.setString(2, "%" + keyword + "%");
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("maKH"),
                        rs.getString("ten"),
                        rs.getString("sdt"),
                        rs.getString("diachi")
                    };
                    tableModel.addRow(row);
                }
                
                if (tableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng nào!");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // ============ CRUD KHÁCH HÀNG ============
    private void ThemKhachHang() {
        // Validate input
        if (txtTenTK.getText().trim().isEmpty() ||
            txtMatKhau.getText().trim().isEmpty() ||
            txtTenKH.getText().trim().isEmpty() || 
            txtSDT.getText().trim().isEmpty() ||
            txtDiaChi.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        
        try {
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
                
                // Thêm khách hàng
                String sqlKH = "INSERT INTO khachHang (maKH, ten, sdt, diachi) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmtKH = conn.prepareStatement(sqlKH);
                pstmtKH.setInt(1, maTK);
                pstmtKH.setString(2, txtTenKH.getText().trim());
                pstmtKH.setString(3, txtSDT.getText().trim());
                pstmtKH.setString(4, txtDiaChi.getText().trim());
                
                int result = pstmtKH.executeUpdate();
                
                // Commit transaction
                conn.commit();
                
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                    loadAllKhachHang();
                    ClearForm();
                }
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void SuaKhachHang() {
        if (txtMaKH.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!");
            return;
        }
        
        try {
            int maKH = Integer.parseInt(txtMaKH.getText().trim());
            
            // Bắt đầu transaction
            conn.setAutoCommit(false);
            
            try {
                // Update tài khoản
                String sqlTK = "UPDATE taiKhoan SET tenTK=?, matKhau=? WHERE maTK=?";
                PreparedStatement pstmtTK = conn.prepareStatement(sqlTK);
                pstmtTK.setString(1, txtTenTK.getText().trim());
                pstmtTK.setString(2, txtMatKhau.getText().trim());
                pstmtTK.setInt(3, maKH);
                pstmtTK.executeUpdate();
                
                // Update khách hàng
                String sqlKH = "UPDATE khachHang SET ten=?, sdt=?, diachi=? WHERE maKH=?";
                PreparedStatement pstmtKH = conn.prepareStatement(sqlKH);
                pstmtKH.setString(1, txtTenKH.getText().trim());
                pstmtKH.setString(2, txtSDT.getText().trim());
                pstmtKH.setString(3, txtDiaChi.getText().trim());
                pstmtKH.setInt(4, maKH);
                
                int result = pstmtKH.executeUpdate();
                
                // Commit transaction
                conn.commit();
                
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Sửa khách hàng thành công!");
                    loadAllKhachHang();
                    ClearForm();
                }
                
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi sửa khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void XoaKhachHang() {
        if (txtMaKH.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa khách hàng này?\n(Sẽ xóa cả tài khoản và giỏ hàng liên quan)", 
            "Xác nhận", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int maKH = Integer.parseInt(txtMaKH.getText().trim());
                
                // Xóa tài khoản (sẽ tự động xóa khách hàng do ON DELETE CASCADE)
                String sql = "DELETE FROM taiKhoan WHERE maTK = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, maKH);
                    int result = pstmt.executeUpdate();
                    if (result > 0) {
                        JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!");
                        loadAllKhachHang();
                        ClearForm();
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Lỗi xóa khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Mã khách hàng không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void ClearForm() {
        txtMaKH.setText("");
        txtTenTK.setText("");
        txtMatKhau.setText("");
        txtTenKH.setText("");
        txtSDT.setText("");
        txtDiaChi.setText("");
        khachHangTable.clearSelection();
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

    public static void main(String[] args) {
        frmKhachHang frmKhachHang = new frmKhachHang();
        frmKhachHang.setVisible(true);
    }
}
