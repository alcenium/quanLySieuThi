package com.baiTap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class frmHoaDon extends JFrame {
    private JTable hoaDonTable, itemTable;
    private DefaultTableModel hoaDonTableModel, itemTableModel;
    private JTextField txtMaHoaDon, txtMaNV, txtTenNV, txtNgayThanhToan;
    private JTextField txtMaSP, txtTenSP, txtGia, txtSoLuong;
    private JLabel lblTongTien;
    private JList<String> listNhanVien;
    private DefaultListModel<String> nhanVienListModel;
    
    // Database connection
    private Connection conn;
    private final String DB_URL = "jdbc:mysql://localhost:3306/QLBanHangSieuThi";
    private final String USER = "root";
    private final String PASS = "";
    //private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public frmHoaDon() {
        super("Quản lý hóa đơn");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Kết nối database
        connectDatabase();

        JLabel lbTitle = new JLabel("QUẢN LÝ HÓA ĐỠN", JLabel.CENTER);
        lbTitle.setForeground(Color.blue);
        lbTitle.setFont(new Font("Arial", Font.BOLD, 25));
        add(lbTitle, BorderLayout.PAGE_START);

        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

        //Panel Left
        JPanel pnLeft = new JPanel(new BorderLayout());
        nhanVienListModel = new DefaultListModel<>();
        
        listNhanVien = new JList<>(nhanVienListModel);
        JScrollPane scrollPane = new JScrollPane(listNhanVien);
        scrollPane.setBorder(new TitledBorder(border, "Danh sách nhân viên"));

        JPanel pnBtn1 = new JPanel();
        JButton btnRefresh = new JButton("Làm mới");
        JButton btnSearch = new JButton("Tìm kiếm");
        
        pnBtn1.add(btnRefresh);
        pnBtn1.add(btnSearch);

        pnLeft.add(scrollPane, BorderLayout.CENTER);
        pnLeft.add(pnBtn1, BorderLayout.PAGE_END);

        //Panel Right
        JPanel pnRight = new JPanel(new BorderLayout());

        // Panel hóa đơn
        JPanel hoaDonPanel = new JPanel(new BorderLayout());
        hoaDonPanel.setBorder(BorderFactory.createTitledBorder("Danh sách hóa đơn"));

        String[] hoaDonColumns = {"Mã hóa đơn", "Mã NV", "Tên NV", "Ngày thanh toán", "Tổng tiền"};
        hoaDonTableModel = new DefaultTableModel(hoaDonColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        hoaDonTable = new JTable(hoaDonTableModel);
        JScrollPane hoaDonScrollPane = new JScrollPane(hoaDonTable);
        hoaDonPanel.add(hoaDonScrollPane);
        hoaDonScrollPane.setPreferredSize(new Dimension(hoaDonScrollPane.getWidth(), 120));

        pnRight.add(hoaDonPanel, BorderLayout.PAGE_START);

        // Panel items trong hóa đơn
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết sản phẩm"));

        String[] itemColumns = {"Mã item", "Mã SP", "Tên SP", "Giá", "Số lượng", "Thành tiền"};
        itemTableModel = new DefaultTableModel(itemColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        itemTable = new JTable(itemTableModel);
        JScrollPane itemScrollPane = new JScrollPane(itemTable);
        itemPanel.add(itemScrollPane, BorderLayout.CENTER);
        itemScrollPane.setPreferredSize(new Dimension(itemScrollPane.getWidth(), 100));

        // Panel thông tin
        JPanel pnInfo = new JPanel(new BorderLayout());
        
        JPanel pnTT = new JPanel(new GridLayout(4, 2, 5, 3));
        pnTT.setBorder(new TitledBorder(border, "Thông tin hóa đơn"));
        
        JLabel lbMaHoaDon = new JLabel("Mã hóa đơn:");
        JLabel lbMaNV = new JLabel("Mã nhân viên:");
        JLabel lbTenNV = new JLabel("Tên nhân viên:");
        JLabel lbNgayThanhToan = new JLabel("Ngày thanh toán:");

        txtMaHoaDon = new JTextField(30);
        txtMaHoaDon.setEditable(false);
        txtMaNV = new JTextField(30);
        txtMaNV.setEditable(false);
        txtTenNV = new JTextField(30);
        txtTenNV.setEditable(false);
        txtNgayThanhToan = new JTextField(30);

        pnTT.add(lbMaHoaDon);
        pnTT.add(txtMaHoaDon);
        pnTT.add(lbMaNV);
        pnTT.add(txtMaNV);
        pnTT.add(lbTenNV);
        pnTT.add(txtTenNV);
        pnTT.add(lbNgayThanhToan);
        pnTT.add(txtNgayThanhToan);

        // Panel item form
        JPanel pnItemForm = new JPanel(new GridLayout(4, 2, 5, 3));
        pnItemForm.setBorder(new TitledBorder(border, "Thêm/Sửa sản phẩm"));
        
        JLabel lbMaSP = new JLabel("Mã sản phẩm:");
        JLabel lbTenSP = new JLabel("Tên sản phẩm:");
        JLabel lbGia = new JLabel("Giá:");
        JLabel lbSoLuong = new JLabel("Số lượng:");

        txtMaSP = new JTextField(30);
        txtTenSP = new JTextField(30);
        txtTenSP.setEditable(false);
        txtGia = new JTextField(30);
        txtGia.setEditable(false);
        txtSoLuong = new JTextField(30);

        pnItemForm.add(lbMaSP);
        pnItemForm.add(txtMaSP);
        pnItemForm.add(lbTenSP);
        pnItemForm.add(txtTenSP);
        pnItemForm.add(lbGia);
        pnItemForm.add(txtGia);
        pnItemForm.add(lbSoLuong);
        pnItemForm.add(txtSoLuong);

        pnInfo.add(pnTT, BorderLayout.NORTH);
        pnInfo.add(pnItemForm, BorderLayout.CENTER);
        
        // Panel tổng tiền
        JPanel pnTongTien = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTongTien = new JLabel("Tổng tiền: 0 VNĐ");
        lblTongTien.setFont(new Font("Arial", Font.BOLD, 16));
        lblTongTien.setForeground(Color.RED);
        pnTongTien.add(lblTongTien);
        pnInfo.add(pnTongTien, BorderLayout.SOUTH);

        itemPanel.add(pnInfo, BorderLayout.SOUTH);
        pnRight.add(itemPanel, BorderLayout.CENTER);

        //Panel Buttons
        JPanel pnBtn2 = new JPanel();
        JButton btTaoHoaDon = new JButton("Tạo hóa đơn");
        JButton btXoaHoaDon = new JButton("Xóa hóa đơn");
        JButton btThemSP = new JButton("Thêm SP");
        JButton btXoaSP = new JButton("Xóa SP");
        JButton btCapNhatSL = new JButton("Cập nhật SL");
        JButton btInHoaDon = new JButton("In hóa đơn");
        JButton btClear = new JButton("Xóa trắng");

        pnBtn2.add(btTaoHoaDon);
        pnBtn2.add(btXoaHoaDon);
        pnBtn2.add(btThemSP);
        pnBtn2.add(btXoaSP);
        pnBtn2.add(btCapNhatSL);
        pnBtn2.add(btInHoaDon);
        pnBtn2.add(btClear);

        pnRight.add(pnBtn2, BorderLayout.PAGE_END);

        //JSplitPane
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnLeft, pnRight);
        add(jSplitPane);

        setSize(1100, 650);
        setLocationRelativeTo(null);

        // Load dữ liệu ban đầu
        loadNhanVien();
        loadAllHoaDon();

        // Event handlers
        btnRefresh.addActionListener(e -> {
            loadNhanVien();
            loadAllHoaDon();
        });
        btnSearch.addActionListener(e -> TimKiem());
        
        btTaoHoaDon.addActionListener(e -> TaoHoaDon());
        btXoaHoaDon.addActionListener(e -> XoaHoaDon());
        btThemSP.addActionListener(e -> ThemSanPham());
        btXoaSP.addActionListener(e -> XoaSanPham());
        btCapNhatSL.addActionListener(e -> CapNhatSoLuong());
        btInHoaDon.addActionListener(e -> InHoaDon());
        btClear.addActionListener(e -> ClearForm());
        
        // Event khi click vào table hóa đơn
        hoaDonTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = hoaDonTable.getSelectedRow();
                if (row >= 0) {
                    txtMaHoaDon.setText(hoaDonTableModel.getValueAt(row, 0).toString());
                    txtMaNV.setText(hoaDonTableModel.getValueAt(row, 1).toString());
                    txtTenNV.setText(hoaDonTableModel.getValueAt(row, 2).toString());
                    txtNgayThanhToan.setText(hoaDonTableModel.getValueAt(row, 3).toString());
                    
                    loadItemsHoaDon(txtMaHoaDon.getText());
                }
            }
        });
        
        // Event khi click vào table items
        itemTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = itemTable.getSelectedRow();
                if (row >= 0) {
                    txtMaSP.setText(itemTableModel.getValueAt(row, 1).toString());
                    txtTenSP.setText(itemTableModel.getValueAt(row, 2).toString());
                    txtGia.setText(itemTableModel.getValueAt(row, 3).toString());
                    txtSoLuong.setText(itemTableModel.getValueAt(row, 4).toString());
                }
            }
        });
        
        // Event khi chọn nhân viên
        listNhanVien.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = listNhanVien.getSelectedValue();
                if (selected != null) {
                    String maNV = selected.split(" - ")[0];
                    loadHoaDonByNhanVien(maNV);
                }
            }
        });
        
        // Event khi nhập mã sản phẩm
        txtMaSP.addActionListener(e -> loadThongTinSanPham());
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

    // Load danh sách nhân viên
    private void loadNhanVien() {
        nhanVienListModel.clear();
        String sql = "SELECT maNV, ten FROM nhanVien";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String item = rs.getInt("maNV") + " - " + rs.getString("ten");
                nhanVienListModel.addElement(item);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi load nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Load tất cả hóa đơn
    private void loadAllHoaDon() {
        hoaDonTableModel.setRowCount(0);
        String sql = "SELECT hd.maHoaDon, hd.maNV, nv.ten, hd.ngayThanhToan " +
                     "FROM hoaDon hd " +
                     "INNER JOIN nhanVien nv ON hd.maNV = nv.maNV " +
                     "ORDER BY hd.maHoaDon DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int maHoaDon = rs.getInt("maHoaDon");
                int tongTien = tinhTongTien(maHoaDon);
                
                Object[] row = {
                    maHoaDon,
                    rs.getInt("maNV"),
                    rs.getString("ten"),
                    rs.getDate("ngayThanhToan"),
                    tongTien + " VNĐ"
                };
                hoaDonTableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi load hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Load hóa đơn theo nhân viên
    private void loadHoaDonByNhanVien(String maNV) {
        hoaDonTableModel.setRowCount(0);
        String sql = "SELECT hd.maHoaDon, hd.maNV, nv.ten, hd.ngayThanhToan " +
                     "FROM hoaDon hd " +
                     "INNER JOIN nhanVien nv ON hd.maNV = nv.maNV " +
                     "WHERE hd.maNV = ? " +
                     "ORDER BY hd.maHoaDon DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(maNV));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int maHoaDon = rs.getInt("maHoaDon");
                int tongTien = tinhTongTien(maHoaDon);
                
                Object[] row = {
                    maHoaDon,
                    rs.getInt("maNV"),
                    rs.getString("ten"),
                    rs.getDate("ngayThanhToan"),
                    tongTien + " VNĐ"
                };
                hoaDonTableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi load hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Load items trong hóa đơn
    private void loadItemsHoaDon(String maHoaDon) {
        itemTableModel.setRowCount(0);
        int tongTien = 0;
        
        String sql = "SELECT ihd.maItemHoaDon, ihd.maSP, sp.tenSP, sp.gia, ihd.soLuong " +
                     "FROM item_hoaDon ihd " +
                     "INNER JOIN sanPham sp ON ihd.maSP = sp.maSP " +
                     "WHERE ihd.maHoaDon = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(maHoaDon));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int gia = rs.getInt("gia");
                int soLuong = rs.getInt("soLuong");
                int thanhTien = gia * soLuong;
                tongTien += thanhTien;
                
                Object[] row = {
                    rs.getInt("maItemHoaDon"),
                    rs.getInt("maSP"),
                    rs.getString("tenSP"),
                    gia + " VNĐ",
                    soLuong,
                    thanhTien + " VNĐ"
                };
                itemTableModel.addRow(row);
            }
            
            lblTongTien.setText("Tổng tiền: " + tongTien + " VNĐ");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi load items hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Tính tổng tiền hóa đơn
    private int tinhTongTien(int maHoaDon) {
        int tongTien = 0;
        String sql = "SELECT SUM(sp.gia * ihd.soLuong) as tongTien " +
                     "FROM item_hoaDon ihd " +
                     "INNER JOIN sanPham sp ON ihd.maSP = sp.maSP " +
                     "WHERE ihd.maHoaDon = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maHoaDon);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                tongTien = rs.getInt("tongTien");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tongTien;
    }

    // Load thông tin sản phẩm
    private void loadThongTinSanPham() {
        String maSP = txtMaSP.getText().trim();
        if (maSP.isEmpty()) return;
        
        String sql = "SELECT tenSP, gia, soLuong FROM sanPham WHERE maSP = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(maSP));
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                txtTenSP.setText(rs.getString("tenSP"));
                txtGia.setText(rs.getString("gia"));
                
                int soLuongKho = rs.getInt("soLuong");
                if (soLuongKho == 0) {
                    JOptionPane.showMessageDialog(this, "Sản phẩm đã hết hàng!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm!");
                txtTenSP.setText("");
                txtGia.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Mã sản phẩm không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Tìm kiếm hóa đơn
    private void TimKiem() {
        String keyword = JOptionPane.showInputDialog(this, "Nhập mã hóa đơn hoặc tên nhân viên:");
        if (keyword != null && !keyword.trim().isEmpty()) {
            hoaDonTableModel.setRowCount(0);
            String sql = "SELECT hd.maHoaDon, hd.maNV, nv.ten, hd.ngayThanhToan " +
                         "FROM hoaDon hd " +
                         "INNER JOIN nhanVien nv ON hd.maNV = nv.maNV " +
                         "WHERE hd.maHoaDon LIKE ? OR nv.ten LIKE ? " +
                         "ORDER BY hd.maHoaDon DESC";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, "%" + keyword + "%");
                pstmt.setString(2, "%" + keyword + "%");
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    int maHoaDon = rs.getInt("maHoaDon");
                    int tongTien = tinhTongTien(maHoaDon);
                    
                    Object[] row = {
                        maHoaDon,
                        rs.getInt("maNV"),
                        rs.getString("ten"),
                        rs.getDate("ngayThanhToan"),
                        tongTien + " VNĐ"
                    };
                    hoaDonTableModel.addRow(row);
                }
                
                if (hoaDonTableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn nào!");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // ============ CRUD HÓA ĐƠN ============
    private void TaoHoaDon() {
        String selected = listNhanVien.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên!");
            return;
        }
        
        String maNV = selected.split(" - ")[0];
        String ngayThanhToan = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        
        String sql = "INSERT INTO hoaDon (maNV, ngayThanhToan) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(maNV));
            pstmt.setDate(2, java.sql.Date.valueOf(ngayThanhToan));
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Tạo hóa đơn thành công!");
                loadAllHoaDon();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tạo hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void XoaHoaDon() {
        if (txtMaHoaDon.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa hóa đơn này?\n(Sẽ xóa cả các sản phẩm trong hóa đơn)", 
            "Xác nhận", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int maHoaDon = Integer.parseInt(txtMaHoaDon.getText().trim());
                String sql = "DELETE FROM hoaDon WHERE maHoaDon = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, maHoaDon);
                    int result = pstmt.executeUpdate();
                    if (result > 0) {
                        JOptionPane.showMessageDialog(this, "Xóa hóa đơn thành công!");
                        loadAllHoaDon();
                        ClearForm();
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Lỗi xóa hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Mã hóa đơn không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Thêm sản phẩm vào hóa đơn
    private void ThemSanPham() {
        if (txtMaHoaDon.getText().trim().isEmpty() ||
            txtMaSP.getText().trim().isEmpty() ||
            txtSoLuong.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        
        try {
            int maHoaDon = Integer.parseInt(txtMaHoaDon.getText().trim());
            int maSP = Integer.parseInt(txtMaSP.getText().trim());
            int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
            
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!");
                return;
            }
            
            // Kiểm tra tồn kho
            String sqlCheck = "SELECT soLuong FROM sanPham WHERE maSP = ?";
            PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck);
            pstmtCheck.setInt(1, maSP);
            ResultSet rs = pstmtCheck.executeQuery();
            
            if (rs.next()) {
                int soLuongKho = rs.getInt("soLuong");
                if (soLuong > soLuongKho) {
                    JOptionPane.showMessageDialog(this, "Số lượng vượt quá tồn kho! (Còn: " + soLuongKho + ")");
                    return;
                }
            }
            
            // Kiểm tra sản phẩm đã có trong hóa đơn chưa
            String sqlCheckExists = "SELECT maItemHoaDon FROM item_hoaDon WHERE maHoaDon = ? AND maSP = ?";
            PreparedStatement pstmtExists = conn.prepareStatement(sqlCheckExists);
            pstmtExists.setInt(1, maHoaDon);
            pstmtExists.setInt(2, maSP);
            ResultSet rsExists = pstmtExists.executeQuery();
            
            if (rsExists.next()) {
                JOptionPane.showMessageDialog(this, "Sản phẩm đã có trong hóa đơn! Vui lòng cập nhật số lượng.");
                return;
            }
            
            // Thêm sản phẩm vào hóa đơn
            String sql = "INSERT INTO item_hoaDon (maHoaDon, maSP, soLuong) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, maHoaDon);
            pstmt.setInt(2, maSP);
            pstmt.setInt(3, soLuong);
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                // Trừ số lượng trong kho
                String sqlUpdate = "UPDATE sanPham SET soLuong = soLuong - ? WHERE maSP = ?";
                PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate);
                pstmtUpdate.setInt(1, soLuong);
                pstmtUpdate.setInt(2, maSP);
                pstmtUpdate.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm vào hóa đơn thành công!");
                loadItemsHoaDon(txtMaHoaDon.getText());
                loadAllHoaDon();
                txtMaSP.setText("");
                txtTenSP.setText("");
                txtGia.setText("");
                txtSoLuong.setText("");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi thêm sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Xóa sản phẩm khỏi hóa đơn
    private void XoaSanPham() {
        int selectedRow = itemTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa sản phẩm này khỏi hóa đơn?", 
            "Xác nhận", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int maItemHoaDon = Integer.parseInt(itemTableModel.getValueAt(selectedRow, 0).toString());
                int maSP = Integer.parseInt(itemTableModel.getValueAt(selectedRow, 1).toString());
                int soLuong = Integer.parseInt(itemTableModel.getValueAt(selectedRow, 4).toString());
                
                String sql = "DELETE FROM item_hoaDon WHERE maItemHoaDon = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, maItemHoaDon);
                
                int result = pstmt.executeUpdate();
                if (result > 0) {
                    // Cộng lại số lượng trong kho
                    String sqlUpdate = "UPDATE sanPham SET soLuong = soLuong + ? WHERE maSP = ?";
                    PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate);
                    pstmtUpdate.setInt(1, soLuong);
                    pstmtUpdate.setInt(2, maSP);
                    pstmtUpdate.executeUpdate();
                    
                    JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
                    loadItemsHoaDon(txtMaHoaDon.getText());
                    loadAllHoaDon();
                    txtMaSP.setText("");
                    txtTenSP.setText("");
                    txtGia.setText("");
                    txtSoLuong.setText("");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi xóa sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // Cập nhật số lượng sản phẩm
    private void CapNhatSoLuong() {
        int selectedRow = itemTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần cập nhật!");
            return;
        }
        
        if (txtSoLuong.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng!");
            return;
        }
        
        try {
            int maItemHoaDon = Integer.parseInt(itemTableModel.getValueAt(selectedRow, 0).toString());
            int maSP = Integer.parseInt(txtMaSP.getText().trim());
            int soLuongCu = Integer.parseInt(itemTableModel.getValueAt(selectedRow, 4).toString());
            int soLuongMoi = Integer.parseInt(txtSoLuong.getText().trim());
            
            if (soLuongMoi <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng phải lớn hơn 0!");
                return;
            }
            
            int chenhLech = soLuongMoi - soLuongCu;
            
            // Kiểm tra tồn kho nếu tăng số lượng
            if (chenhLech > 0) {
                String sqlCheck = "SELECT soLuong FROM sanPham WHERE maSP = ?";
                PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck);
                pstmtCheck.setInt(1, maSP);
                ResultSet rs = pstmtCheck.executeQuery();
                
                if (rs.next()) {
                    int soLuongKho = rs.getInt("soLuong");
                    if (chenhLech > soLuongKho) {
                        JOptionPane.showMessageDialog(this, "Số lượng vượt quá tồn kho! (Còn: " + soLuongKho + ")");
                        return;
                    }
                }
            }
            
            String sql = "UPDATE item_hoaDon SET soLuong = ? WHERE maItemHoaDon = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, soLuongMoi);
            pstmt.setInt(2, maItemHoaDon);
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                // Cập nhật số lượng trong kho
                String sqlUpdate = "UPDATE sanPham SET soLuong = soLuong - ? WHERE maSP = ?";
                PreparedStatement pstmtUpdate = conn.prepareStatement(sqlUpdate);
                pstmtUpdate.setInt(1, chenhLech);
                pstmtUpdate.setInt(2, maSP);
                pstmtUpdate.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "Cập nhật số lượng thành công!");
                loadItemsHoaDon(txtMaHoaDon.getText());
                loadAllHoaDon();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật số lượng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // In hóa đơn
    private void InHoaDon() {
        if (txtMaHoaDon.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần in!");
            return;
        }
        
        StringBuilder hoaDon = new StringBuilder();
        hoaDon.append("=====================================\n");
        hoaDon.append("           HÓA ĐƠN BÁN HÀNG          \n");
        hoaDon.append("=====================================\n\n");
        hoaDon.append("Mã hóa đơn: ").append(txtMaHoaDon.getText()).append("\n");
        hoaDon.append("Nhân viên: ").append(txtTenNV.getText()).append("\n");
        hoaDon.append("Ngày: ").append(txtNgayThanhToan.getText()).append("\n\n");
        hoaDon.append("-------------------------------------\n");
        hoaDon.append(String.format("%-20s %5s %10s\n", "Tên SP", "SL", "Thành tiền"));
        hoaDon.append("-------------------------------------\n");
        
        for (int i = 0; i < itemTableModel.getRowCount(); i++) {
            String tenSP = itemTableModel.getValueAt(i, 2).toString();
            String soLuong = itemTableModel.getValueAt(i, 4).toString();
            String thanhTien = itemTableModel.getValueAt(i, 5).toString();
            
            hoaDon.append(String.format("%-20s %5s %10s\n", 
                tenSP.length() > 20 ? tenSP.substring(0, 20) : tenSP, 
                soLuong, 
                thanhTien));
        }
        
        hoaDon.append("-------------------------------------\n");
        hoaDon.append(String.format("%-26s %10s\n", "TỔNG CỘNG:", lblTongTien.getText().replace("Tổng tiền: ", "")));
        hoaDon.append("=====================================\n");
        hoaDon.append("      Cảm ơn quý khách đã mua hàng!    \n");
        hoaDon.append("=====================================\n");
        
        JTextArea textArea = new JTextArea(hoaDon.toString());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 500));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Hóa đơn", JOptionPane.INFORMATION_MESSAGE);
    }

    private void ClearForm() {
        txtMaHoaDon.setText("");
        txtMaNV.setText("");
        txtTenNV.setText("");
        txtNgayThanhToan.setText("");
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtGia.setText("");
        txtSoLuong.setText("");
        lblTongTien.setText("Tổng tiền: 0 VNĐ");
        hoaDonTable.clearSelection();
        itemTable.clearSelection();
        itemTableModel.setRowCount(0);
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
        frmHoaDon frmHoaDon = new frmHoaDon();
        frmHoaDon.setVisible(true);
    }
}
