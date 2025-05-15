package com.ll.StudyJava;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PhoneBookGUI extends JFrame {
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField nameField, phoneField, searchField;
    private JLabel countLabel;
    private JLabel signature;
    private boolean isDarkTheme = true;

    public PhoneBookGUI() {
        setTitle("전화번호부");
        setSize(650, 530);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        Color bg = getBgColor();
        Color text = getTextColor();

        tableModel = new DefaultTableModel(new Object[]{"이름", "전화번호"}, 0);
        table = new JTable(tableModel);
        table.setShowGrid(true);
        table.setGridColor(text);
        table.setFillsViewportHeight(true);
        table.setFont(new Font("맑은 고딕", Font.PLAIN, 13));
        table.setRowHeight(24);
        table.setBackground(bg);
        table.setOpaque(true);

        table.setDefaultRenderer(Object.class, getTableCellRenderer());
        table.getTableHeader().setBackground(Color.DARK_GRAY);
        table.getTableHeader().setForeground(text);
        table.getTableHeader().setFont(new Font("맑은 고딕", Font.BOLD, 13));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(bg);
        scrollPane.setBackground(bg);
        scrollPane.setBorder(BorderFactory.createLineBorder(bg));

        nameField = new JTextField(10);
        phoneField = new JTextField(10);
        searchField = new JTextField(10);

        JButton addBtn = styledBtn("저장", text);
        JButton editBtn = styledBtn("수정", text);
        JButton delBtn = styledBtn("삭제", text);
        JButton searchBtn = styledBtn("검색", text);
        JButton clearBtn = styledBtn("전체삭제", text);
        JButton sortBtn = styledBtn("정렬", text);
        JButton themeBtn = styledBtn("테마전환", text);
        JButton exitBtn = styledBtn("종료", text);
        JButton exportBtn = styledBtn("내보내기", text);
        JButton importBtn = styledBtn("불러오기", text);

        JPanel inputPanel = new JPanel();
        inputPanel.setOpaque(true);
        inputPanel.setBackground(bg);
        inputPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(bg),
                "입력",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("맑은 고딕", Font.BOLD, 12), text
        ));
        JLabel nameLabel = new JLabel("이름:"); nameLabel.setForeground(text);
        JLabel phoneLabel = new JLabel("전화번호:"); phoneLabel.setForeground(text);
        inputPanel.add(nameLabel); inputPanel.add(nameField);
        inputPanel.add(phoneLabel); inputPanel.add(phoneField);
        inputPanel.add(addBtn);

        signature = new JLabel("Made by Kingyj00.Git");
        signature.setForeground(text);
        signature.setFont(new Font("Consolas", Font.BOLD, 12));
        JPanel topBanner = new JPanel(new BorderLayout());
        topBanner.setBackground(bg);
        topBanner.add(signature, BorderLayout.EAST);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(bg);
        topPanel.add(topBanner, BorderLayout.NORTH);
        topPanel.add(inputPanel, BorderLayout.SOUTH);

        countLabel = new JLabel();
        countLabel.setForeground(text);
        updateCountLabel();

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(true);
        bottomPanel.setBackground(bg);
        JLabel searchLabel = new JLabel("검색:"); searchLabel.setForeground(text);
        bottomPanel.add(searchLabel);
        bottomPanel.add(searchField);
        bottomPanel.add(searchBtn);
        bottomPanel.add(clearBtn);
        bottomPanel.add(sortBtn);
        bottomPanel.add(importBtn);
        bottomPanel.add(exportBtn);
        bottomPanel.add(editBtn);
        bottomPanel.add(delBtn);
        bottomPanel.add(themeBtn);
        bottomPanel.add(exitBtn);
        bottomPanel.add(Box.createHorizontalStrut(20));
        bottomPanel.add(countLabel);

        addBtn.addActionListener(e -> { save(); updateCountLabel(); });
        editBtn.addActionListener(e -> update());
        delBtn.addActionListener(e -> { delete(); updateCountLabel(); });
        clearBtn.addActionListener(e -> { tableModel.setRowCount(0); updateCountLabel(); });
        searchBtn.addActionListener(e -> search());
        sortBtn.addActionListener(e -> sort());
        themeBtn.addActionListener(e -> toggleTheme());
        exportBtn.addActionListener(e -> exportCSV());
        importBtn.addActionListener(e -> importCSV());
        exitBtn.addActionListener(e -> System.exit(0));

        getContentPane().removeAll();
        getContentPane().setBackground(bg);
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void exportCSV() {
        try (PrintWriter pw = new PrintWriter(new FileWriter("contacts.csv"))) {
            pw.println("이름,전화번호");
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                pw.println(tableModel.getValueAt(i, 0) + "," + tableModel.getValueAt(i, 1));
            }
            JOptionPane.showMessageDialog(this, "CSV 파일로 저장되었습니다.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "저장 중 오류 발생: " + e.getMessage());
        }
    }

    private void importCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader("contacts.csv"))) {
            String line;
            tableModel.setRowCount(0);
            boolean skipHeader = true;
            while ((line = br.readLine()) != null) {
                if (skipHeader) { skipHeader = false; continue; }
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    tableModel.addRow(new Object[]{parts[0], parts[1]});
                }
            }
            updateCountLabel();
            JOptionPane.showMessageDialog(this, "CSV 파일을 불러왔습니다.");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "불러오기 중 오류 발생: " + e.getMessage());
        }
    }

    private Color getBgColor() {
        return isDarkTheme ? Color.BLACK : Color.WHITE;
    }

    private Color getTextColor() {
        return isDarkTheme ? new Color(0xCCFF00) : Color.BLACK;
    }

    private TableCellRenderer getTableCellRenderer() {
        Color bg = getBgColor();
        Color text = getTextColor();
        return (table, value, isSelected, hasFocus, row, column) -> {
            JLabel label = new JLabel(value == null ? "" : value.toString());
            label.setOpaque(true);
            label.setBackground(isSelected ? Color.DARK_GRAY : bg);
            label.setForeground(text);
            label.setFont(table.getFont());
            label.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 6));
            return label;
        };
    }

    private JButton styledBtn(String text, Color fg) {
        JButton b = new JButton(text);
        b.setBackground(getBgColor());
        b.setForeground(fg);
        b.setBorder(BorderFactory.createLineBorder(fg));
        b.setFocusPainted(false);
        b.setContentAreaFilled(true);
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

    private void updateCountLabel() {
        countLabel.setText("총 " + tableModel.getRowCount() + "개 등록됨");
    }

    private void toggleTheme() {
        isDarkTheme = !isDarkTheme;
        initUI(); // 모든 색상 재적용 포함
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