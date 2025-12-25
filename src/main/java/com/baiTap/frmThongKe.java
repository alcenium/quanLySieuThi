package com.baiTap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class frmThongKe extends JFrame {
    private JTable statisticsTable;
    private DefaultTableModel tableModel;
    private JTextField txtTuNgay, txtDenNgay, txtTongDT, txtTongDH;
    private JComboBox<String> loaiThongKeComboBox;

    public frmThongKe() {
        super("Thong ke");
        setDefaultCloseOperation(frmTrangChu.DISPOSE_ON_CLOSE);

        JLabel lbTitle = new JLabel("THỐNG KÊ DOANH THU", JLabel.CENTER);
        lbTitle.setForeground(Color.blue);
        lbTitle.setFont(new Font("Arial", Font.BOLD, 25));
        add(lbTitle, BorderLayout.PAGE_START);

        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel filterPanel = new JPanel(new GridLayout(3, 2, 5, 3));
        filterPanel.setBorder(new TitledBorder(border, "Lọc thống kê"));

        JLabel lbLoai = new JLabel("Loại thống kê:");
        JLabel lbTuNgay = new JLabel("Từ ngày:");
        JLabel lbDenNgay = new JLabel("Đến ngày:");

        loaiThongKeComboBox = new JComboBox<>(new String[]{"Theo ngày", "Theo tháng", "Theo năm"});
        txtTuNgay = new JTextField(30);
        txtDenNgay = new JTextField(30);

        filterPanel.add(lbLoai);
        filterPanel.add(loaiThongKeComboBox);
        filterPanel.add(lbTuNgay);
        filterPanel.add(txtTuNgay);
        filterPanel.add(lbDenNgay);
        filterPanel.add(txtDenNgay);

        mainPanel.add(filterPanel, BorderLayout.PAGE_START);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Kết quả thống kê"));

        String[] columns = {"Ngày", "Tổng doanh thu", "Tổng đơn hàng", "Doanh thu trung bình"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        statisticsTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(statisticsTable);
        tableScrollPane.setPreferredSize(new Dimension(0, 250));
        tablePanel.add(tableScrollPane);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel summaryPanel = new JPanel(new GridLayout(2, 2, 5, 3));
        summaryPanel.setBorder(new TitledBorder(border, "Tổng kết"));

        JLabel lbTongDT = new JLabel("Tổng doanh thu:");
        JLabel lbTongDH = new JLabel("Tổng đơn hàng:");

        txtTongDT = new JTextField(30);
        txtTongDH = new JTextField(30);
        txtTongDT.setEditable(false);
        txtTongDH.setEditable(false);

        summaryPanel.add(lbTongDT);
        summaryPanel.add(txtTongDT);
        summaryPanel.add(lbTongDH);
        summaryPanel.add(txtTongDH);

        mainPanel.add(summaryPanel, BorderLayout.CENTER);

        JPanel pnBtn = new JPanel();
        JButton btSearch = new JButton("Tìm kiếm");
        JButton btExport = new JButton("Xuất Excel");
        JButton btClear = new JButton("Xóa trắng");

        pnBtn.add(btSearch);
        pnBtn.add(btExport);
        pnBtn.add(btClear);

        mainPanel.add(pnBtn, BorderLayout.PAGE_END);

        add(mainPanel);

        setSize(900, 600);
        setLocationRelativeTo(null);

        btClear.addActionListener(e -> ClearForm());
    }

    private void ClearForm() {
        txtTuNgay.setText("");
        txtDenNgay.setText("");
        txtTongDT.setText("");
        txtTongDH.setText("");
        tableModel.setRowCount(0);
        statisticsTable.clearSelection();
        txtTuNgay.requestFocus();
    }
}