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
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class frmSanPham extends JFrame {
    private JTable productTable;
    private DefaultListModel<String> categoryListModel;
    private DefaultTableModel tableModel;
    private JTextField txtMaSP, txtTenSP, txtGia, txtSL;
    private JComboBox<String> DanhMucComboBox;
    private DefaultComboBoxModel<String> comboBoxModel;
    private JList<String> list;
    
    // Database connection
    private Connection conn;
    private final String DB_URL = "jdbc:mysql://localhost:3306/QLBanHangSieuThi";
    private final String USER = "root";
    private final String PASS = "";
    private Map<String, Integer> danhMucMap = new HashMap<>();

    public frmSanPham() {
        super("Quản lý sản phẩm");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Kết nối database
        connectDatabase();

        JLabel lbTitle = new JLabel("QUẢN LÝ SẢN PHẨM", JLabel.CENTER);
        lbTitle.setForeground(Color.blue);
        lbTitle.setFont(new Font("Arial", Font.BOLD, 25));
        add(lbTitle, BorderLayout.PAGE_START);

        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

        //Panel Left
        JPanel pnLeft = new JPanel(new BorderLayout());
        categoryListModel = new DefaultListModel<>();
        
        list = new JList<>(categoryListModel);
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setBorder(new TitledBorder(border, "Danh mục sản phẩm"));

        JPanel pnBtn1 = new JPanel();
        JButton btnAdd = new JButton("Thêm");
        JButton btnUpdate = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");

        pnBtn1.add(btnAdd);
        pnBtn1.add(btnUpdate);
        pnBtn1.add(btnDelete);

        pnLeft.add(scrollPane, BorderLayout.CENTER);
        pnLeft.add(pnBtn1, BorderLayout.PAGE_END);

        //Panel Right
        JPanel pnRight = new JPanel(new BorderLayout());

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Thông tin chi tiết"));

        comboBoxModel = new DefaultComboBoxModel<>();
        DanhMucComboBox = new JComboBox<>(comboBoxModel);

        String[] columns = {"Mã SP", "Tên sản phẩm", "Giá", "Số lượng"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(productTable);
        tablePanel.add(tableScrollPane);
        pnRight.add(tablePanel, BorderLayout.PAGE_START);
        tableScrollPane.setPreferredSize(new Dimension(tableScrollPane.getWidth(), 200));

        JPanel pnTT = new JPanel(new GridLayout(5, 2, 5, 3));
        pnTT.setBorder(new TitledBorder(border, "Thông tin sản phẩm"));
        
        JLabel lbDanhMuc = new JLabel("Danh mục:");
        JLabel lbMaSP = new JLabel("Mã sản phẩm:");
        JLabel lbTenSP = new JLabel("Tên sản phẩm:");
        JLabel lbGia = new JLabel("Giá:");
        JLabel lbSL = new JLabel("Số lượng:");

        txtMaSP = new JTextField(30);
        txtMaSP.setEditable(false); // Auto increment
        txtTenSP = new JTextField(30);
        txtGia = new JTextField(30);
        txtSL = new JTextField(30);

        pnTT.add(lbDanhMuc);
        pnTT.add(DanhMucComboBox);
        pnTT.add(lbMaSP);
        pnTT.add(txtMaSP);
        pnTT.add(lbTenSP);
        pnTT.add(txtTenSP);
        pnTT.add(lbGia);
        pnTT.add(txtGia);
        pnTT.add(lbSL);
        pnTT.add(txtSL);

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
        loadDanhMuc();
        loadAllSanPham();

        // Event handlers cho Danh mục
        btnAdd.addActionListener(e -> ThemDanhMuc());
        btnUpdate.addActionListener(e -> SuaDanhMuc());
        btnDelete.addActionListener(e -> XoaDanhMuc());

        // Event handlers cho Sản phẩm
        btAdd.addActionListener(e -> ThemSP());
        btUpdate.addActionListener(e -> SuaSP());
        btDelete.addActionListener(e -> XoaSP());
        btClear.addActionListener(e -> ClearSP());
        
        // Event khi click vào table
        productTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = productTable.getSelectedRow();
                if (row >= 0) {
                    txtMaSP.setText(tableModel.getValueAt(row, 0).toString());
                    txtTenSP.setText(tableModel.getValueAt(row, 1).toString());
                    txtGia.setText(tableModel.getValueAt(row, 2).toString());
                    txtSL.setText(tableModel.getValueAt(row, 3).toString());
                }
            }
        });
        
        // Event khi chọn danh mục
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedCategory = list.getSelectedValue();
                if (selectedCategory != null) {
                    DanhMucComboBox.setSelectedItem(selectedCategory);
                    loadSanPhamByDanhMuc(selectedCategory);
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

    // Load danh mục từ database
    private void loadDanhMuc() {
        categoryListModel.clear();
        comboBoxModel.removeAllElements();
        danhMucMap.clear();
        
        String sql = "SELECT maDM, tenDM FROM danhMuc";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int maDM = rs.getInt("maDM");
                String tenDM = rs.getString("tenDM");
                categoryListModel.addElement(tenDM);
                comboBoxModel.addElement(tenDM);
                danhMucMap.put(tenDM, maDM);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi load danh mục!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Load tất cả sản phẩm
    private void loadAllSanPham() {
        tableModel.setRowCount(0);
        String sql = "SELECT maSP, tenSP, gia, soLuong FROM sanPham";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("maSP"),
                    rs.getString("tenSP"),
                    rs.getInt("gia"),
                    rs.getInt("soLuong")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi load sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Load sản phẩm theo danh mục
    private void loadSanPhamByDanhMuc(String tenDM) {
        tableModel.setRowCount(0);
        Integer maDM = danhMucMap.get(tenDM);
        if (maDM == null) return;
        
        String sql = "SELECT maSP, tenSP, gia, soLuong FROM sanPham WHERE maDM = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maDM);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Object[] row = {
                    rs.getInt("maSP"),
                    rs.getString("tenSP"),
                    rs.getInt("gia"),
                    rs.getInt("soLuong")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi load sản phẩm theo danh mục!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // ============ CRUD DANH MỤC ============
    private void ThemDanhMuc() {
        String tenDM = JOptionPane.showInputDialog(this, "Nhập tên danh mục:");
        if (tenDM != null && !tenDM.trim().isEmpty()) {
            String sql = "INSERT INTO danhMuc (tenDM) VALUES (?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, tenDM);
                int result = pstmt.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Thêm danh mục thành công!");
                    loadDanhMuc();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi thêm danh mục!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void SuaDanhMuc() {
        String selectedDM = list.getSelectedValue();
        if (selectedDM == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục cần sửa!");
            return;
        }
        
        String newName = JOptionPane.showInputDialog(this, "Nhập tên danh mục mới:", selectedDM);
        if (newName != null && !newName.trim().isEmpty()) {
            Integer maDM = danhMucMap.get(selectedDM);
            String sql = "UPDATE danhMuc SET tenDM = ? WHERE maDM = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, newName);
                pstmt.setInt(2, maDM);
                int result = pstmt.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Sửa danh mục thành công!");
                    loadDanhMuc();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi sửa danh mục!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private void XoaDanhMuc() {
        String selectedDM = list.getSelectedValue();
        if (selectedDM == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn danh mục cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa danh mục này?\n(Sẽ xóa cả các sản phẩm thuộc danh mục)", 
            "Xác nhận", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            Integer maDM = danhMucMap.get(selectedDM);
            String sql = "DELETE FROM danhMuc WHERE maDM = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, maDM);
                int result = pstmt.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Xóa danh mục thành công!");
                    loadDanhMuc();
                    loadAllSanPham();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi xóa danh mục!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // ============ CRUD SẢN PHẨM ============
    private void ThemSP() {
        // Validate input
        if (txtTenSP.getText().trim().isEmpty() || 
            txtGia.getText().trim().isEmpty() || 
            txtSL.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        
        try {
            String tenDM = (String) DanhMucComboBox.getSelectedItem();
            Integer maDM = danhMucMap.get(tenDM);
            String tenSP = txtTenSP.getText().trim();
            int gia = Integer.parseInt(txtGia.getText().trim());
            int soLuong = Integer.parseInt(txtSL.getText().trim());
            
            String sql = "INSERT INTO sanPham (maDM, tenSP, gia, soLuong) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, maDM);
                pstmt.setString(2, tenSP);
                pstmt.setInt(3, gia);
                pstmt.setInt(4, soLuong);
                
                int result = pstmt.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
                    loadAllSanPham();
                    ClearSP();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi thêm sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá và số lượng phải là số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void SuaSP() {
        if (txtMaSP.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa!");
            return;
        }
        
        try {
            int maSP = Integer.parseInt(txtMaSP.getText().trim());
            String tenDM = (String) DanhMucComboBox.getSelectedItem();
            Integer maDM = danhMucMap.get(tenDM);
            String tenSP = txtTenSP.getText().trim();
            int gia = Integer.parseInt(txtGia.getText().trim());
            int soLuong = Integer.parseInt(txtSL.getText().trim());
            
            String sql = "UPDATE sanPham SET maDM=?, tenSP=?, gia=?, soLuong=? WHERE maSP=?";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, maDM);
                pstmt.setString(2, tenSP);
                pstmt.setInt(3, gia);
                pstmt.setInt(4, soLuong);
                pstmt.setInt(5, maSP);
                
                int result = pstmt.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Sửa sản phẩm thành công!");
                    loadAllSanPham();
                    ClearSP();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi sửa sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void XoaSP() {
        if (txtMaSP.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa sản phẩm này?", 
            "Xác nhận", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int maSP = Integer.parseInt(txtMaSP.getText().trim());
                String sql = "DELETE FROM sanPham WHERE maSP = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, maSP);
                    int result = pstmt.executeUpdate();
                    if (result > 0) {
                        JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
                        loadAllSanPham();
                        ClearSP();
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Lỗi xóa sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Mã sản phẩm không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void ClearSP() {
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtGia.setText("");
        txtSL.setText("");
        productTable.clearSelection();
        txtTenSP.requestFocus();
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
        frmSanPham frmSanPham = new frmSanPham();
        frmSanPham.setVisible(true);
    }
}
