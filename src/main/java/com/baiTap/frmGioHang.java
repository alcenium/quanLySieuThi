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
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class frmGioHang extends JFrame {
    private JTable gioHangTable, itemTable;
    private DefaultTableModel gioHangTableModel, itemTableModel;
    private JTextField txtMaGioHang, txtMaKH, txtTenKH, txtNgayTao;
    private JTextField txtMaSP, txtTenSP, txtGia, txtSoLuong;
    private JLabel lblTongTien;
    private JList<String> listKhachHang;
    private DefaultListModel<String> khachHangListModel;
    
    // Database connection
    private Connection conn;
    private final String DB_URL = "jdbc:mysql://localhost:3306/QLBanHangSieuThi";
    private final String USER = "root";
    private final String PASS = "";
    //private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public frmGioHang() {
        super("Quản lý giỏ hàng");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Kết nối database
        connectDatabase();

        JLabel lbTitle = new JLabel("QUẢN LÝ GIỎ HÀNG", JLabel.CENTER);
        lbTitle.setForeground(Color.blue);
        lbTitle.setFont(new Font("Arial", Font.BOLD, 25));
        add(lbTitle, BorderLayout.PAGE_START);

        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

        //Panel Left
        JPanel pnLeft = new JPanel(new BorderLayout());
        khachHangListModel = new DefaultListModel<>();
        
        listKhachHang = new JList<>(khachHangListModel);
        JScrollPane scrollPane = new JScrollPane(listKhachHang);
        scrollPane.setBorder(new TitledBorder(border, "Danh sách khách hàng"));

        JPanel pnBtn1 = new JPanel();
        JButton btnRefresh = new JButton("Làm mới");
        JButton btnSearch = new JButton("Tìm kiếm");
        
        pnBtn1.add(btnRefresh);
        pnBtn1.add(btnSearch);

        pnLeft.add(scrollPane, BorderLayout.CENTER);
        pnLeft.add(pnBtn1, BorderLayout.PAGE_END);

        //Panel Right
        JPanel pnRight = new JPanel(new BorderLayout());

        // Panel giỏ hàng
        JPanel gioHangPanel = new JPanel(new BorderLayout());
        gioHangPanel.setBorder(BorderFactory.createTitledBorder("Giỏ hàng"));

        String[] gioHangColumns = {"Mã giỏ hàng", "Mã KH", "Tên KH", "Ngày tạo", "Tổng tiền"};
        gioHangTableModel = new DefaultTableModel(gioHangColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        gioHangTable = new JTable(gioHangTableModel);
        JScrollPane gioHangScrollPane = new JScrollPane(gioHangTable);
        gioHangPanel.add(gioHangScrollPane);
        gioHangScrollPane.setPreferredSize(new Dimension(gioHangScrollPane.getWidth(), 120));

        pnRight.add(gioHangPanel, BorderLayout.PAGE_START);

        // Panel items trong giỏ hàng
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setBorder(BorderFactory.createTitledBorder("Chi tiết sản phẩm trong giỏ"));

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
        pnTT.setBorder(new TitledBorder(border, "Thông tin giỏ hàng"));
        
        JLabel lbMaGioHang = new JLabel("Mã giỏ hàng:");
        JLabel lbMaKH = new JLabel("Mã khách hàng:");
        JLabel lbTenKH = new JLabel("Tên khách hàng:");
        JLabel lbNgayTao = new JLabel("Ngày tạo:");

        txtMaGioHang = new JTextField(30);
        txtMaGioHang.setEditable(false);
        txtMaKH = new JTextField(30);
        txtMaKH.setEditable(false);
        txtTenKH = new JTextField(30);
        txtTenKH.setEditable(false);
        txtNgayTao = new JTextField(30);

        pnTT.add(lbMaGioHang);
        pnTT.add(txtMaGioHang);
        pnTT.add(lbMaKH);
        pnTT.add(txtMaKH);
        pnTT.add(lbTenKH);
        pnTT.add(txtTenKH);
        pnTT.add(lbNgayTao);
        pnTT.add(txtNgayTao);

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
        JButton btTaoGioHang = new JButton("Tạo giỏ hàng");
        JButton btXoaGioHang = new JButton("Xóa giỏ hàng");
        JButton btThemSP = new JButton("Thêm SP");
        JButton btXoaSP = new JButton("Xóa SP");
        JButton btCapNhatSL = new JButton("Cập nhật SL");
        JButton btClear = new JButton("Xóa trắng");

        pnBtn2.add(btTaoGioHang);
        pnBtn2.add(btXoaGioHang);
        pnBtn2.add(btThemSP);
        pnBtn2.add(btXoaSP);
        pnBtn2.add(btCapNhatSL);
        pnBtn2.add(btClear);

        pnRight.add(pnBtn2, BorderLayout.PAGE_END);

        //JSplitPane
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnLeft, pnRight);
        add(jSplitPane);

        setSize(1000, 650);
        setLocationRelativeTo(null);

        // Load dữ liệu ban đầu
        loadKhachHang();
        loadAllGioHang();

        // Event handlers
        btnRefresh.addActionListener(e -> {
            loadKhachHang();
            loadAllGioHang();
        });
        btnSearch.addActionListener(e -> TimKiem());
        
        btTaoGioHang.addActionListener(e -> TaoGioHang());
        btXoaGioHang.addActionListener(e -> XoaGioHang());
        btThemSP.addActionListener(e -> ThemSanPham());
        btXoaSP.addActionListener(e -> XoaSanPham());
        btCapNhatSL.addActionListener(e -> CapNhatSoLuong());
        btClear.addActionListener(e -> ClearForm());
        
        // Event khi click vào table giỏ hàng
        gioHangTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = gioHangTable.getSelectedRow();
                if (row >= 0) {
                    txtMaGioHang.setText(gioHangTableModel.getValueAt(row, 0).toString());
                    txtMaKH.setText(gioHangTableModel.getValueAt(row, 1).toString());
                    txtTenKH.setText(gioHangTableModel.getValueAt(row, 2).toString());
                    txtNgayTao.setText(gioHangTableModel.getValueAt(row, 3).toString());
                    
                    loadItemsGioHang(txtMaGioHang.getText());
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
        
        // Event khi chọn khách hàng
        listKhachHang.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = listKhachHang.getSelectedValue();
                if (selected != null) {
                    String maKH = selected.split(" - ")[0];
                    loadGioHangByKhachHang(maKH);
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

    // Load danh sách khách hàng
    private void loadKhachHang() {
        khachHangListModel.clear();
        String sql = "SELECT maKH, ten FROM khachHang";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String item = rs.getInt("maKH") + " - " + rs.getString("ten");
                khachHangListModel.addElement(item);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi load khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Load tất cả giỏ hàng
    private void loadAllGioHang() {
        gioHangTableModel.setRowCount(0);
        String sql = "SELECT gh.maGioHang, gh.maKH, kh.ten, gh.ngayTao " +
                     "FROM gioHang gh " +
                     "INNER JOIN khachHang kh ON gh.maKH = kh.maKH " +
                     "ORDER BY gh.maGioHang DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int maGioHang = rs.getInt("maGioHang");
                int tongTien = tinhTongTien(maGioHang);
                
                Object[] row = {
                    maGioHang,
                    rs.getInt("maKH"),
                    rs.getString("ten"),
                    rs.getDate("ngayTao"),
                    tongTien + " VNĐ"
                };
                gioHangTableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi load giỏ hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Load giỏ hàng theo khách hàng
    private void loadGioHangByKhachHang(String maKH) {
        gioHangTableModel.setRowCount(0);
        String sql = "SELECT gh.maGioHang, gh.maKH, kh.ten, gh.ngayTao " +
                     "FROM gioHang gh " +
                     "INNER JOIN khachHang kh ON gh.maKH = kh.maKH " +
                     "WHERE gh.maKH = ? " +
                     "ORDER BY gh.maGioHang DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(maKH));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int maGioHang = rs.getInt("maGioHang");
                int tongTien = tinhTongTien(maGioHang);
                
                Object[] row = {
                    maGioHang,
                    rs.getInt("maKH"),
                    rs.getString("ten"),
                    rs.getDate("ngayTao"),
                    tongTien + " VNĐ"
                };
                gioHangTableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi load giỏ hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Load items trong giỏ hàng
    private void loadItemsGioHang(String maGioHang) {
        itemTableModel.setRowCount(0);
        int tongTien = 0;
        
        String sql = "SELECT igh.maItemGioHang, igh.maSP, sp.tenSP, sp.gia, igh.soLuong " +
                     "FROM item_gioHang igh " +
                     "INNER JOIN sanPham sp ON igh.maSP = sp.maSP " +
                     "WHERE igh.maGioHang = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(maGioHang));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int gia = rs.getInt("gia");
                int soLuong = rs.getInt("soLuong");
                int thanhTien = gia * soLuong;
                tongTien += thanhTien;
                
                Object[] row = {
                    rs.getInt("maItemGioHang"),
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
            JOptionPane.showMessageDialog(this, "Lỗi load items giỏ hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Tính tổng tiền giỏ hàng
    private int tinhTongTien(int maGioHang) {
        int tongTien = 0;
        String sql = "SELECT SUM(sp.gia * igh.soLuong) as tongTien " +
                     "FROM item_gioHang igh " +
                     "INNER JOIN sanPham sp ON igh.maSP = sp.maSP " +
                     "WHERE igh.maGioHang = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maGioHang);
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

    // Tìm kiếm giỏ hàng
    private void TimKiem() {
        String keyword = JOptionPane.showInputDialog(this, "Nhập tên khách hàng:");
        if (keyword != null && !keyword.trim().isEmpty()) {
            gioHangTableModel.setRowCount(0);
            String sql = "SELECT gh.maGioHang, gh.maKH, kh.ten, gh.ngayTao " +
                         "FROM gioHang gh " +
                         "INNER JOIN khachHang kh ON gh.maKH = kh.maKH " +
                         "WHERE kh.ten LIKE ? " +
                         "ORDER BY gh.maGioHang DESC";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, "%" + keyword + "%");
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    int maGioHang = rs.getInt("maGioHang");
                    int tongTien = tinhTongTien(maGioHang);
                    
                    Object[] row = {
                        maGioHang,
                        rs.getInt("maKH"),
                        rs.getString("ten"),
                        rs.getDate("ngayTao"),
                        tongTien + " VNĐ"
                    };
                    gioHangTableModel.addRow(row);
                }
                
                if (gioHangTableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy giỏ hàng nào!");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // ============ CRUD GIỎ HÀNG ============
    private void TaoGioHang() {
        String selected = listKhachHang.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng!");
            return;
        }
        
        String maKH = selected.split(" - ")[0];
        String ngayTao = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        
        String sql = "INSERT INTO gioHang (maKH, ngayTao) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(maKH));
            pstmt.setDate(2, java.sql.Date.valueOf(ngayTao));
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Tạo giỏ hàng thành công!");
                loadAllGioHang();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tạo giỏ hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void XoaGioHang() {
        if (txtMaGioHang.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn giỏ hàng cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa giỏ hàng này?\n(Sẽ xóa cả các sản phẩm trong giỏ)", 
            "Xác nhận", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int maGioHang = Integer.parseInt(txtMaGioHang.getText().trim());
                String sql = "DELETE FROM gioHang WHERE maGioHang = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, maGioHang);
                    int result = pstmt.executeUpdate();
                    if (result > 0) {
                        JOptionPane.showMessageDialog(this, "Xóa giỏ hàng thành công!");
                        loadAllGioHang();
                        ClearForm();
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Lỗi xóa giỏ hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Mã giỏ hàng không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Thêm sản phẩm vào giỏ hàng
    private void ThemSanPham() {
        if (txtMaGioHang.getText().trim().isEmpty() ||
            txtMaSP.getText().trim().isEmpty() ||
            txtSoLuong.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        
        try {
            int maGioHang = Integer.parseInt(txtMaGioHang.getText().trim());
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
            
            // Kiểm tra sản phẩm đã có trong giỏ chưa
            String sqlCheckExists = "SELECT maItemGioHang, soLuong FROM item_gioHang WHERE maGioHang = ? AND maSP = ?";
            PreparedStatement pstmtExists = conn.prepareStatement(sqlCheckExists);
            pstmtExists.setInt(1, maGioHang);
            pstmtExists.setInt(2, maSP);
            ResultSet rsExists = pstmtExists.executeQuery();
            
            if (rsExists.next()) {
                JOptionPane.showMessageDialog(this, "Sản phẩm đã có trong giỏ! Vui lòng cập nhật số lượng.");
                return;
            }
            
            // Thêm sản phẩm vào giỏ
            String sql = "INSERT INTO item_gioHang (maGioHang, maSP, soLuong) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, maGioHang);
            pstmt.setInt(2, maSP);
            pstmt.setInt(3, soLuong);
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm vào giỏ hàng thành công!");
                loadItemsGioHang(txtMaGioHang.getText());
                loadAllGioHang(); // Cập nhật tổng tiền
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

    // Xóa sản phẩm khỏi giỏ hàng
    private void XoaSanPham() {
        int selectedRow = itemTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa sản phẩm này khỏi giỏ hàng?", 
            "Xác nhận", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int maItemGioHang = Integer.parseInt(itemTableModel.getValueAt(selectedRow, 0).toString());
                String sql = "DELETE FROM item_gioHang WHERE maItemGioHang = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, maItemGioHang);
                
                int result = pstmt.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
                    loadItemsGioHang(txtMaGioHang.getText());
                    loadAllGioHang();
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
            int maItemGioHang = Integer.parseInt(itemTableModel.getValueAt(selectedRow, 0).toString());
            int maSP = Integer.parseInt(txtMaSP.getText().trim());
            int soLuongMoi = Integer.parseInt(txtSoLuong.getText().trim());
            
            if (soLuongMoi <= 0) {
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
                if (soLuongMoi > soLuongKho) {
                    JOptionPane.showMessageDialog(this, "Số lượng vượt quá tồn kho! (Còn: " + soLuongKho + ")");
                    return;
                }
            }
            
            String sql = "UPDATE item_gioHang SET soLuong = ? WHERE maItemGioHang = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, soLuongMoi);
            pstmt.setInt(2, maItemGioHang);
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật số lượng thành công!");
                loadItemsGioHang(txtMaGioHang.getText());
                loadAllGioHang();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật số lượng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ClearForm() {
        txtMaGioHang.setText("");
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtNgayTao.setText("");
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtGia.setText("");
        txtSoLuong.setText("");
        lblTongTien.setText("Tổng tiền: 0 VNĐ");
        gioHangTable.clearSelection();
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
        frmGioHang frmGioHang = new frmGioHang();
        frmGioHang.setVisible(true);
    }
}
