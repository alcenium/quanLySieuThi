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

public class frmGiaoHang extends JFrame {
    private JTable giaoHangTable, itemTable;
    private DefaultTableModel giaoHangTableModel, itemTableModel;
    private JTextField txtMaGiaoHang, txtMaKH, txtTenKH, txtNgayTao;
    private JTextField txtMaSP, txtTenSP, txtGia, txtSoLuong;
    private JComboBox<String> cboTinhTrang;
    private JLabel lblTongTien;
    private JList<String> listTinhTrang;
    private DefaultListModel<String> tinhTrangListModel;
    
    // Database connection
    private Connection conn;
    private DB db = new DB();
    //private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public frmGiaoHang() {
        super("Quản lý giao hàng");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Kết nối database
        connectDatabase();

        JLabel lbTitle = new JLabel("QUẢN LÝ GIAO HÀNG", JLabel.CENTER);
        lbTitle.setForeground(Color.blue);
        lbTitle.setFont(new Font("Arial", Font.BOLD, 25));
        add(lbTitle, BorderLayout.PAGE_START);

        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

        //Panel Left
        JPanel pnLeft = new JPanel(new BorderLayout());
        tinhTrangListModel = new DefaultListModel<>();
        tinhTrangListModel.addElement("Tất cả");
        tinhTrangListModel.addElement("Chờ xử lý");
        tinhTrangListModel.addElement("Đang giao");
        tinhTrangListModel.addElement("Đã giao");
        tinhTrangListModel.addElement("Đã hủy");
        
        listTinhTrang = new JList<>(tinhTrangListModel);
        JScrollPane scrollPane = new JScrollPane(listTinhTrang);
        scrollPane.setBorder(new TitledBorder(border, "Lọc theo tình trạng"));

        JPanel pnBtn1 = new JPanel();
        JButton btnRefresh = new JButton("Làm mới");
        JButton btnSearch = new JButton("Tìm kiếm");
        
        pnBtn1.add(btnRefresh);
        pnBtn1.add(btnSearch);

        pnLeft.add(scrollPane, BorderLayout.CENTER);
        pnLeft.add(pnBtn1, BorderLayout.PAGE_END);

        //Panel Right
        JPanel pnRight = new JPanel(new BorderLayout());

        // Panel giao hàng
        JPanel giaoHangPanel = new JPanel(new BorderLayout());
        giaoHangPanel.setBorder(BorderFactory.createTitledBorder("Đơn giao hàng"));

        String[] giaoHangColumns = {"Mã giao hàng", "Mã KH", "Tên KH", "Ngày tạo", "Tình trạng", "Tổng tiền"};
        giaoHangTableModel = new DefaultTableModel(giaoHangColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        giaoHangTable = new JTable(giaoHangTableModel);
        JScrollPane giaoHangScrollPane = new JScrollPane(giaoHangTable);
        giaoHangPanel.add(giaoHangScrollPane);
        giaoHangScrollPane.setPreferredSize(new Dimension(giaoHangScrollPane.getWidth(), 120));

        pnRight.add(giaoHangPanel, BorderLayout.PAGE_START);

        // Panel items trong giao hàng
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
        
        JPanel pnTT = new JPanel(new GridLayout(5, 2, 5, 3));
        pnTT.setBorder(new TitledBorder(border, "Thông tin giao hàng"));
        
        JLabel lbMaGiaoHang = new JLabel("Mã giao hàng:");
        JLabel lbMaKH = new JLabel("Mã khách hàng:");
        JLabel lbTenKH = new JLabel("Tên khách hàng:");
        JLabel lbNgayTao = new JLabel("Ngày tạo:");
        JLabel lbTinhTrang = new JLabel("Tình trạng:");

        txtMaGiaoHang = new JTextField(30);
        txtMaGiaoHang.setEditable(false);
        txtMaKH = new JTextField(30);
        txtMaKH.setEditable(false);
        txtTenKH = new JTextField(30);
        txtTenKH.setEditable(false);
        txtNgayTao = new JTextField(30);
        
        cboTinhTrang = new JComboBox<>(new String[]{"Chờ xử lý", "Đang giao", "Đã giao", "Đã hủy"});

        pnTT.add(lbMaGiaoHang);
        pnTT.add(txtMaGiaoHang);
        pnTT.add(lbMaKH);
        pnTT.add(txtMaKH);
        pnTT.add(lbTenKH);
        pnTT.add(txtTenKH);
        pnTT.add(lbNgayTao);
        pnTT.add(txtNgayTao);
        pnTT.add(lbTinhTrang);
        pnTT.add(cboTinhTrang);

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
        JButton btTaoGiaoHang = new JButton("Tạo đơn");
        JButton btXoaGiaoHang = new JButton("Xóa đơn");
        JButton btCapNhatTT = new JButton("Cập nhật TT");
        JButton btThemSP = new JButton("Thêm SP");
        JButton btXoaSP = new JButton("Xóa SP");
        JButton btCapNhatSL = new JButton("Cập nhật SL");
        JButton btClear = new JButton("Xóa trắng");

        pnBtn2.add(btTaoGiaoHang);
        pnBtn2.add(btXoaGiaoHang);
        pnBtn2.add(btCapNhatTT);
        pnBtn2.add(btThemSP);
        pnBtn2.add(btXoaSP);
        pnBtn2.add(btCapNhatSL);
        pnBtn2.add(btClear);

        pnRight.add(pnBtn2, BorderLayout.PAGE_END);

        //JSplitPane
        JSplitPane jSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, pnLeft, pnRight);
        add(jSplitPane);

        setSize(1100, 650);
        setLocationRelativeTo(null);

        // Load dữ liệu ban đầu
        loadAllGiaoHang();

        // Event handlers
        btnRefresh.addActionListener(e -> loadAllGiaoHang());
        btnSearch.addActionListener(e -> TimKiem());
        
        btTaoGiaoHang.addActionListener(e -> TaoGiaoHang());
        btXoaGiaoHang.addActionListener(e -> XoaGiaoHang());
        btCapNhatTT.addActionListener(e -> CapNhatTinhTrang());
        btThemSP.addActionListener(e -> ThemSanPham());
        btXoaSP.addActionListener(e -> XoaSanPham());
        btCapNhatSL.addActionListener(e -> CapNhatSoLuong());
        btClear.addActionListener(e -> ClearForm());
        
        // Event khi click vào table giao hàng
        giaoHangTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = giaoHangTable.getSelectedRow();
                if (row >= 0) {
                    txtMaGiaoHang.setText(giaoHangTableModel.getValueAt(row, 0).toString());
                    txtMaKH.setText(giaoHangTableModel.getValueAt(row, 1).toString());
                    txtTenKH.setText(giaoHangTableModel.getValueAt(row, 2).toString());
                    txtNgayTao.setText(giaoHangTableModel.getValueAt(row, 3).toString());
                    cboTinhTrang.setSelectedItem(giaoHangTableModel.getValueAt(row, 4).toString());
                    
                    loadItemsGiaoHang(txtMaGiaoHang.getText());
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
        
        // Event khi chọn tình trạng
        listTinhTrang.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selected = listTinhTrang.getSelectedValue();
                if (selected != null) {
                    if (selected.equals("Tất cả")) {
                        loadAllGiaoHang();
                    } else {
                        loadGiaoHangByTinhTrang(selected);
                    }
                }
            }
        });
        
        // Event khi nhập mã sản phẩm
        txtMaSP.addActionListener(e -> loadThongTinSanPham());
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

    // Load tất cả giao hàng
    private void loadAllGiaoHang() {
        giaoHangTableModel.setRowCount(0);
        String sql = "SELECT gh.maGiaoHang, gh.maKH, kh.ten, gh.ngayTao, gh.tinhTrang " +
                     "FROM giaoHang gh " +
                     "INNER JOIN khachHang kh ON gh.maKH = kh.maKH " +
                     "ORDER BY gh.maGiaoHang DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                int maGiaoHang = rs.getInt("maGiaoHang");
                int tongTien = tinhTongTien(maGiaoHang);
                
                Object[] row = {
                    maGiaoHang,
                    rs.getInt("maKH"),
                    rs.getString("ten"),
                    rs.getDate("ngayTao"),
                    rs.getString("tinhTrang"),
                    tongTien + " VNĐ"
                };
                giaoHangTableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi load giao hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Load giao hàng theo tình trạng
    private void loadGiaoHangByTinhTrang(String tinhTrang) {
        giaoHangTableModel.setRowCount(0);
        String sql = "SELECT gh.maGiaoHang, gh.maKH, kh.ten, gh.ngayTao, gh.tinhTrang " +
                     "FROM giaoHang gh " +
                     "INNER JOIN khachHang kh ON gh.maKH = kh.maKH " +
                     "WHERE gh.tinhTrang = ? " +
                     "ORDER BY gh.maGiaoHang DESC";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tinhTrang);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int maGiaoHang = rs.getInt("maGiaoHang");
                int tongTien = tinhTongTien(maGiaoHang);
                
                Object[] row = {
                    maGiaoHang,
                    rs.getInt("maKH"),
                    rs.getString("ten"),
                    rs.getDate("ngayTao"),
                    rs.getString("tinhTrang"),
                    tongTien + " VNĐ"
                };
                giaoHangTableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi load giao hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Load items trong giao hàng
    private void loadItemsGiaoHang(String maGiaoHang) {
        itemTableModel.setRowCount(0);
        int tongTien = 0;
        
        String sql = "SELECT igh.maItemGiaoHang, igh.maSP, sp.tenSP, sp.gia, igh.soLuong " +
                     "FROM item_giaoHang igh " +
                     "INNER JOIN sanPham sp ON igh.maSP = sp.maSP " +
                     "WHERE igh.maGiaoHang = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(maGiaoHang));
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int gia = rs.getInt("gia");
                int soLuong = rs.getInt("soLuong");
                int thanhTien = gia * soLuong;
                tongTien += thanhTien;
                
                Object[] row = {
                    rs.getInt("maItemGiaoHang"),
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
            JOptionPane.showMessageDialog(this, "Lỗi load items giao hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Tính tổng tiền giao hàng
    private int tinhTongTien(int maGiaoHang) {
        int tongTien = 0;
        String sql = "SELECT SUM(sp.gia * igh.soLuong) as tongTien " +
                     "FROM item_giaoHang igh " +
                     "INNER JOIN sanPham sp ON igh.maSP = sp.maSP " +
                     "WHERE igh.maGiaoHang = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maGiaoHang);
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

    // Tìm kiếm giao hàng
    private void TimKiem() {
        String keyword = JOptionPane.showInputDialog(this, "Nhập tên khách hàng:");
        if (keyword != null && !keyword.trim().isEmpty()) {
            giaoHangTableModel.setRowCount(0);
            String sql = "SELECT gh.maGiaoHang, gh.maKH, kh.ten, gh.ngayTao, gh.tinhTrang " +
                         "FROM giaoHang gh " +
                         "INNER JOIN khachHang kh ON gh.maKH = kh.maKH " +
                         "WHERE kh.ten LIKE ? " +
                         "ORDER BY gh.maGiaoHang DESC";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, "%" + keyword + "%");
                ResultSet rs = pstmt.executeQuery();
                
                while (rs.next()) {
                    int maGiaoHang = rs.getInt("maGiaoHang");
                    int tongTien = tinhTongTien(maGiaoHang);
                    
                    Object[] row = {
                        maGiaoHang,
                        rs.getInt("maKH"),
                        rs.getString("ten"),
                        rs.getDate("ngayTao"),
                        rs.getString("tinhTrang"),
                        tongTien + " VNĐ"
                    };
                    giaoHangTableModel.addRow(row);
                }
                
                if (giaoHangTableModel.getRowCount() == 0) {
                    JOptionPane.showMessageDialog(this, "Không tìm thấy đơn giao hàng nào!");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Lỗi tìm kiếm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // ============ CRUD GIAO HÀNG ============
    private void TaoGiaoHang() {
        String maKH = JOptionPane.showInputDialog(this, "Nhập mã khách hàng:");
        if (maKH == null || maKH.trim().isEmpty()) {
            return;
        }
        
        // Kiểm tra khách hàng tồn tại
        String sqlCheck = "SELECT ten FROM khachHang WHERE maKH = ?";
        try (PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck)) {
            pstmtCheck.setInt(1, Integer.parseInt(maKH));
            ResultSet rs = pstmtCheck.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng!");
                return;
            }
        } catch (SQLException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Mã khách hàng không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String ngayTao = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
        
        String sql = "INSERT INTO giaoHang (maKH, ngayTao, tinhTrang) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, Integer.parseInt(maKH));
            pstmt.setDate(2, java.sql.Date.valueOf(ngayTao));
            pstmt.setString(3, "Chờ xử lý");
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Tạo đơn giao hàng thành công!");
                loadAllGiaoHang();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi tạo đơn giao hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void XoaGiaoHang() {
        if (txtMaGiaoHang.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn giao hàng cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa đơn giao hàng này?\n(Sẽ xóa cả các sản phẩm trong đơn)", 
            "Xác nhận", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int maGiaoHang = Integer.parseInt(txtMaGiaoHang.getText().trim());
                String sql = "DELETE FROM giaoHang WHERE maGiaoHang = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setInt(1, maGiaoHang);
                    int result = pstmt.executeUpdate();
                    if (result > 0) {
                        JOptionPane.showMessageDialog(this, "Xóa đơn giao hàng thành công!");
                        loadAllGiaoHang();
                        ClearForm();
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(this, "Lỗi xóa đơn giao hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Mã giao hàng không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void CapNhatTinhTrang() {
        if (txtMaGiaoHang.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đơn giao hàng!");
            return;
        }
        
        try {
            int maGiaoHang = Integer.parseInt(txtMaGiaoHang.getText().trim());
            String tinhTrang = (String) cboTinhTrang.getSelectedItem();
            
            String sql = "UPDATE giaoHang SET tinhTrang = ? WHERE maGiaoHang = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tinhTrang);
            pstmt.setInt(2, maGiaoHang);
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật tình trạng thành công!");
                loadAllGiaoHang();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật tình trạng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Thêm sản phẩm vào giao hàng
    private void ThemSanPham() {
        if (txtMaGiaoHang.getText().trim().isEmpty() ||
            txtMaSP.getText().trim().isEmpty() ||
            txtSoLuong.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
            return;
        }
        
        try {
            int maGiaoHang = Integer.parseInt(txtMaGiaoHang.getText().trim());
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
            
            // Kiểm tra sản phẩm đã có trong đơn chưa
            String sqlCheckExists = "SELECT maItemGiaoHang FROM item_giaoHang WHERE maGiaoHang = ? AND maSP = ?";
            PreparedStatement pstmtExists = conn.prepareStatement(sqlCheckExists);
            pstmtExists.setInt(1, maGiaoHang);
            pstmtExists.setInt(2, maSP);
            ResultSet rsExists = pstmtExists.executeQuery();
            
            if (rsExists.next()) {
                JOptionPane.showMessageDialog(this, "Sản phẩm đã có trong đơn! Vui lòng cập nhật số lượng.");
                return;
            }
            
            // Thêm sản phẩm vào đơn
            String sql = "INSERT INTO item_giaoHang (maGiaoHang, maSP, soLuong) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, maGiaoHang);
            pstmt.setInt(2, maSP);
            pstmt.setInt(3, soLuong);
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm vào đơn thành công!");
                loadItemsGiaoHang(txtMaGiaoHang.getText());
                loadAllGiaoHang();
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

    // Xóa sản phẩm khỏi giao hàng
    private void XoaSanPham() {
        int selectedRow = itemTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!");
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn xóa sản phẩm này khỏi đơn giao hàng?", 
            "Xác nhận", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int maItemGiaoHang = Integer.parseInt(itemTableModel.getValueAt(selectedRow, 0).toString());
                String sql = "DELETE FROM item_giaoHang WHERE maItemGiaoHang = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, maItemGiaoHang);
                
                int result = pstmt.executeUpdate();
                if (result > 0) {
                    JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
                    loadItemsGiaoHang(txtMaGiaoHang.getText());
                    loadAllGiaoHang();
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
            int maItemGiaoHang = Integer.parseInt(itemTableModel.getValueAt(selectedRow, 0).toString());
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
            
            String sql = "UPDATE item_giaoHang SET soLuong = ? WHERE maItemGiaoHang = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, soLuongMoi);
            pstmt.setInt(2, maItemGiaoHang);
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Cập nhật số lượng thành công!");
                loadItemsGiaoHang(txtMaGiaoHang.getText());
                loadAllGiaoHang();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật số lượng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Dữ liệu không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ClearForm() {
        txtMaGiaoHang.setText("");
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtNgayTao.setText("");
        cboTinhTrang.setSelectedIndex(0);
        txtMaSP.setText("");
        txtTenSP.setText("");
        txtGia.setText("");
        txtSoLuong.setText("");
        lblTongTien.setText("Tổng tiền: 0 VNĐ");
        giaoHangTable.clearSelection();
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
        frmGiaoHang frmGiaoHang = new frmGiaoHang();
        frmGiaoHang.setVisible(true);
    }
}
