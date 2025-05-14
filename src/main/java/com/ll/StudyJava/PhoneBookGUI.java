package com.ll.StudyJava;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PhoneBookGUI extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField nameField, phoneField, searchField;

    public PhoneBookGUI() {
        setTitle("전화번호부");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Color bg = Color.BLACK;
        Color lime = new Color(0xCCFF00);

        tableModel = new DefaultTableModel(new Object[]{"이름", "전화번호"}, 0);
        table = new JTable(tableModel);
        table.setShowGrid(true);
        table.setGridColor(lime);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        table.setRowHeight(24);
        table.setBackground(bg); // 빈 셀 배경도 검정
        table.setOpaque(true);

        table.setDefaultRenderer(Object.class, new TableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value == null ? "" : value.toString());
                label.setOpaque(true);
                label.setBackground(isSelected ? Color.DARK_GRAY : bg);
                label.setForeground(lime);
                label.setFont(table.getFont());
                label.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
                return label;
            }
        });

        table.getTableHeader().setBackground(Color.DARK_GRAY);
        table.getTableHeader().setForeground(lime);
        table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(bg));
        scrollPane.getViewport().setBackground(bg);
        scrollPane.setBackground(bg);
        scrollPane.setOpaque(true);

        nameField = new JTextField(10);
        phoneField = new JTextField(10);
        searchField = new JTextField(10);

        JButton addBtn = styledBtn("저장", lime);
        JButton editBtn = styledBtn("수정", lime);
        JButton delBtn = styledBtn("삭제", lime);
        JButton searchBtn = styledBtn("검색", lime);
        JButton clearBtn = styledBtn("전체삭제", lime);
        JButton sortBtn = styledBtn("정렬", lime);
        JButton exitBtn = styledBtn("종료", lime);

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(bg);
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(bg),
                "입력",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("맑은 고딕", Font.BOLD, 12), lime
        ));
        JLabel nameLabel = new JLabel("이름:"); nameLabel.setForeground(lime);
        JLabel phoneLabel = new JLabel("전화번호:"); phoneLabel.setForeground(lime);
        inputPanel.add(nameLabel); inputPanel.add(nameField);
        inputPanel.add(phoneLabel); inputPanel.add(phoneField);
        inputPanel.add(addBtn);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(bg);
        JLabel searchLabel = new JLabel("검색:"); searchLabel.setForeground(lime);
        bottomPanel.add(searchLabel);
        bottomPanel.add(searchField);
        bottomPanel.add(searchBtn);
        bottomPanel.add(clearBtn);
        bottomPanel.add(sortBtn);
        bottomPanel.add(editBtn);
        bottomPanel.add(delBtn);
        bottomPanel.add(exitBtn);

        addBtn.addActionListener(e -> save());
        editBtn.addActionListener(e -> update());
        delBtn.addActionListener(e -> delete());
        clearBtn.addActionListener(e -> tableModel.setRowCount(0));
        searchBtn.addActionListener(e -> search());
        sortBtn.addActionListener(e -> sort());
        exitBtn.addActionListener(e -> System.exit(0));

        getContentPane().setBackground(bg);
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    private JButton styledBtn(String text, Color lime) {
        JButton b = new JButton(text);
        b.setBackground(Color.BLACK);
        b.setForeground(lime);
        b.setBorder(BorderFactory.createLineBorder(lime));
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(true);
        return b;
    }

    private void save() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "이름과 전화번호를 입력하세요");
            return;
        }
        if (!phone.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "전화번호는 숫자만 입력해주세요");
            return;
        }
        String formatted = formatPhone(phone);
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 1).equals(formatted)) {
                JOptionPane.showMessageDialog(this, "이미 등록된 번호입니다");
                return;
            }
        }
        tableModel.addRow(new Object[]{name, formatted});
        nameField.setText("");
        phoneField.setText("");
    }

    private void update() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "수정할 항목을 선택하세요");
            return;
        }
        String newName = JOptionPane.showInputDialog(this, "새 이름", tableModel.getValueAt(row, 0));
        String newPhone = JOptionPane.showInputDialog(this, "새 전화번호(숫자)", tableModel.getValueAt(row, 1));
        if (newName == null || newPhone == null) return;
        if (!newPhone.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "전화번호는 숫자만 입력해주세요");
            return;
        }
        String formatted = formatPhone(newPhone);
        tableModel.setValueAt(newName.trim(), row, 0);
        tableModel.setValueAt(formatted, row, 1);
    }

    private void delete() {
        int row = table.getSelectedRow();
        if (row != -1) tableModel.removeRow(row);
        else JOptionPane.showMessageDialog(this, "삭제할 항목을 선택하세요");
    }

    private void search() {
        String keyword = searchField.getText().trim();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).toString().contains(keyword) ||
                    tableModel.getValueAt(i, 1).toString().contains(keyword)) {
                table.setRowSelectionInterval(i, i);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "일치하는 항목이 없습니다");
    }

    private void sort() {
        List<String[]> data = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            data.add(new String[]{
                    tableModel.getValueAt(i, 0).toString(),
                    tableModel.getValueAt(i, 1).toString()
            });
        }
        data.sort(Comparator.comparing(a -> a[0]));
        tableModel.setRowCount(0);
        for (String[] row : data) tableModel.addRow(row);
    }

    private String formatPhone(String num) {
        num = num.replaceAll("\\D", "");
        if (num.length() == 11) return num.replaceFirst("(\\d{3})(\\d{4})(\\d+)", "$1-$2-$3");
        if (num.length() == 10) return num.replaceFirst("(\\d{3})(\\d{3})(\\d+)", "$1-$2-$3");
        return num;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PhoneBookGUI::new);
    }
}