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
        setLocationRelativeTo(null); // 화면 중앙 정렬

        // 테이블 구성
        tableModel = new DefaultTableModel(new Object[]{"이름", "전화번호"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // 입력 필드
        nameField = new JTextField(10);
        phoneField = new JTextField(10);
        searchField = new JTextField(10);

        // 버튼들
        JButton addButton = new JButton("저장");
        JButton deleteButton = new JButton("삭제");
        JButton searchButton = new JButton("검색");
        JButton clearButton = new JButton("전체삭제");
        JButton sortButton = new JButton("정렬");
        JButton exitButton = new JButton("종료");

        // 상단 입력 패널
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("이름:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("전화번호:"));
        inputPanel.add(phoneField);
        inputPanel.add(addButton);

        // 하단 버튼 패널
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("검색:"));
        bottomPanel.add(searchField);
        bottomPanel.add(searchButton);
        bottomPanel.add(clearButton);
        bottomPanel.add(sortButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(exitButton);

        // 이벤트 리스너 등록
        addButton.addActionListener(e -> saveEntry());
        deleteButton.addActionListener(e -> deleteEntry());
        clearButton.addActionListener(e -> clearAll());
        searchButton.addActionListener(e -> search());
        sortButton.addActionListener(e -> sortByName());
        exitButton.addActionListener(e -> System.exit(0));

        // 레이아웃 구성
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    // 저장
    private void saveEntry() {
        String name = nameField.getText().trim();
        String rawPhone = phoneField.getText().trim();

        // 둘 다 비었을 때
        if (name.isEmpty() && rawPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "내용을 입력해주세요.");
            return;
        }

        // 둘 중 하나만 비었을 때
        if (name.isEmpty() || rawPhone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "이름과 전화번호를 모두 입력하세요.");
            return;
        }

        // 숫자 아닌 문자가 포함되어 있을 때
        if (!rawPhone.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "전화번호에는 숫자만 입력할 수 있습니다.");
            return;
        }

        String phone = formatPhone(rawPhone);

        // 중복 체크
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String existingPhone = (String) tableModel.getValueAt(i, 1);
            if (existingPhone.equals(phone)) {
                JOptionPane.showMessageDialog(this, "이미 등록된 전화번호입니다.");
                return;
            }
        }

        // 저장
        tableModel.addRow(new Object[]{name, phone});
        nameField.setText("");
        phoneField.setText("");
    }

    // 삭제
    private void deleteEntry() {
        int selected = table.getSelectedRow();
        if (selected != -1) {
            tableModel.removeRow(selected);
        } else {
            JOptionPane.showMessageDialog(this, "삭제할 항목을 선택하세요.");
        }
    }

    // 전체 삭제
    private void clearAll() {
        int confirm = JOptionPane.showConfirmDialog(this, "정말 모두 삭제할까요?", "전체삭제", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            tableModel.setRowCount(0);
        }
    }

    // 검색
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

    // 정렬 (이름 기준)
    private void sortByName() {
        List<String[]> data = new ArrayList<>();
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            data.add(new String[]{
                    (String) tableModel.getValueAt(i, 0),
                    (String) tableModel.getValueAt(i, 1)
            });
        }

        data.sort(Comparator.comparing(a -> a[0])); // 이름 오름차순 정렬

        tableModel.setRowCount(0); // 테이블 비우고 다시 채우기
        for (String[] row : data) {
            tableModel.addRow(row);
        }

        JOptionPane.showMessageDialog(this, "이름 기준으로 정렬되었습니다.");
    }

    // 전화번호 포맷
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