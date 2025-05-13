package com.ll.StudyJava;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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

        tableModel = new DefaultTableModel(new Object[]{"이름", "전화번호"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        nameField = new JTextField(10);
        phoneField = new JTextField(10);
        searchField = new JTextField(10);

        JButton addButton = new JButton("저장");
        JButton updateButton = new JButton("수정");
        JButton deleteButton = new JButton("삭제");
        JButton searchButton = new JButton("검색");
        JButton clearButton = new JButton("전체삭제");
        JButton sortButton = new JButton("정렬");
        JButton exitButton = new JButton("종료");

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("이름:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("전화번호:"));
        inputPanel.add(phoneField);
        inputPanel.add(addButton);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("검색:"));
        bottomPanel.add(searchField);
        bottomPanel.add(searchButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(sortButton);
        bottomPanel.add(updateButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(exitButton);

        addButton.addActionListener(e -> saveEntry());
        updateButton.addActionListener(e -> updateEntry());
        deleteButton.addActionListener(e -> deleteEntry());
        clearButton.addActionListener(e -> clearAll());
        searchButton.addActionListener(e -> search());
        sortButton.addActionListener(e -> showSortOptions());
        exitButton.addActionListener(e -> System.exit(0));

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void saveEntry() {
        String name = nameField.getText().trim();
        String rawPhone = phoneField.getText().trim();

        if (name.isEmpty() && rawPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "내용을 입력해주세요.");
            return;
        }

        if (name.isEmpty() || rawPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "이름과 전화번호를 모두 입력하세요.");
            return;
        }

        if (!rawPhone.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "전화번호에는 숫자만 입력할 수 있습니다.");
            return;
        }

        String phone = formatPhone(rawPhone);

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String existingPhone = (String) tableModel.getValueAt(i, 1);
            if (existingPhone.equals(phone)) {
                JOptionPane.showMessageDialog(this, "이미 등록된 전화번호입니다.");
                return;
            }
        }

        tableModel.addRow(new Object[]{name, phone});
        nameField.setText("");
        phoneField.setText("");
    }

    private void updateEntry() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "수정할 항목을 선택하세요.");
            return;
        }

        String currentName = (String) tableModel.getValueAt(selectedRow, 0);
        String currentPhone = (String) tableModel.getValueAt(selectedRow, 1);

        String newName = JOptionPane.showInputDialog(this, "새 이름을 입력하세요:", currentName);
        if (newName == null || newName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "이름을 입력해야 합니다.");
            return;
        }

        String newPhone = JOptionPane.showInputDialog(this, "새 전화번호를 입력하세요 (숫자만):", currentPhone);
        if (newPhone == null || newPhone.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "전화번호를 입력해야 합니다.");
            return;
        }
        newPhone = newPhone.trim();

        if (!newPhone.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "전화번호에는 숫자만 입력할 수 있습니다.");
            return;
        }

        String formattedPhone = formatPhone(newPhone);

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (i == selectedRow) continue;
            String existingPhone = (String) tableModel.getValueAt(i, 1);
            if (existingPhone.equals(formattedPhone)) {
                JOptionPane.showMessageDialog(this, "이미 등록된 전화번호입니다.");
                return;
            }
        }

        tableModel.setValueAt(newName.trim(), selectedRow, 0);
        tableModel.setValueAt(formattedPhone, selectedRow, 1);
        JOptionPane.showMessageDialog(this, "수정 완료!");
    }

    private void deleteEntry() {
        int selected = table.getSelectedRow();
        if (selected != -1) {
            tableModel.removeRow(selected);
        } else {
            JOptionPane.showMessageDialog(this, "삭제할 항목을 선택하세요.");
        }
    }

    private void clearAll() {
        int confirm = JOptionPane.showConfirmDialog(this, "정말 모두 삭제할까요?", "전체삭제", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.setRowCount(0);
        }
    }

    private void search() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) return;

        boolean found = false;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String name = (String) tableModel.getValueAt(i, 0);
            String phone = (String) tableModel.getValueAt(i, 1);
            if (name.contains(keyword) || phone.contains(keyword)) {
                table.setRowSelectionInterval(i, i);
                found = true;
                break;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(this, "일치하는 항목이 없습니다.");
        }
    }

    private void showSortOptions() {
        String[] options = {"이름", "전화번호"};
        String selected = (String) JOptionPane.showInputDialog(
                this,
                "정렬 기준을 선택하세요:",
                "정렬",
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        if (selected == null) return;

        if (selected.equals("이름")) {
            sortByName();
        } else if (selected.equals("전화번호")) {
            sortByPhone();
        }
    }

    private void sortByName() {
        List<String[]> data = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            data.add(new String[]{
                    (String) tableModel.getValueAt(i, 0),
                    (String) tableModel.getValueAt(i, 1)
            });
        }

        data.sort(Comparator.comparing(a -> a[0]));

        tableModel.setRowCount(0);
        for (String[] row : data) {
            tableModel.addRow(row);
        }

        JOptionPane.showMessageDialog(this, "이름 기준으로 정렬되었습니다.");
    }

    private void sortByPhone() {
        List<String[]> data = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            data.add(new String[]{
                    (String) tableModel.getValueAt(i, 0),
                    (String) tableModel.getValueAt(i, 1)
            });
        }

        data.sort(Comparator.comparing(a -> a[1].replaceAll("\\D", "")));

        tableModel.setRowCount(0);
        for (String[] row : data) {
            tableModel.addRow(row);
        }

        JOptionPane.showMessageDialog(this, "전화번호 기준으로 정렬되었습니다.");
    }

    private String formatPhone(String number) {
        number = number.replaceAll("\\D", "");
        if (number.length() == 11) {
            return number.substring(0, 3) + "-" + number.substring(3, 7) + "-" + number.substring(7);
        } else if (number.length() == 10) {
            return number.substring(0, 3) + "-" + number.substring(3, 6) + "-" + number.substring(6);
        }
        return number;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PhoneBookGUI::new);
    }
}
