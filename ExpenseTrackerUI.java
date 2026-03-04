import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Date;
import java.text.SimpleDateFormat;

// --- 3. UI CLASS ---
// Manages the graphical user interface.
class ExpenseTrackerUI extends JFrame {
    private DataManager dataManager;
    private JTable transactionTable;
    private DefaultTableModel tableModel;
    
    // UI Components
    private JLabel lblTotalIncome, lblTotalExpenses, lblBalance;
    private JTextField txtAmount, txtDate, txtDescription;
    private JComboBox<String> comboType, comboCategory;

    public ExpenseTrackerUI() {
        dataManager = new DataManager();
        setupUI();
    }

    private void setupUI() {
        setTitle("Spend Wise Buddy - Personal Expense Tracker");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- COLORS & STYLES ---
        Color primaryBg = new Color(245, 245, 245);
        Color incomeGreen = new Color(46, 204, 113);
        Color expenseRed = new Color(231, 76, 60);
        getContentPane().setBackground(primaryBg);

        // --- DASHBOARD PANEL (Top) ---
        JPanel dashboard = new JPanel(new GridLayout(1, 3, 20, 0));
        dashboard.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        dashboard.setBackground(primaryBg);

        lblTotalIncome = createStatCard("Total Income", "0.00", incomeGreen);
        lblTotalExpenses = createStatCard("Total Expenses", "0.00", expenseRed);
        lblBalance = createStatCard("Balance", "0.00", Color.DARK_GRAY);

        dashboard.add(lblTotalIncome);
        dashboard.add(lblTotalExpenses);
        dashboard.add(lblBalance);
        add(dashboard, BorderLayout.NORTH);

        // --- INPUT PANEL (West) ---
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add Transaction"));
        inputPanel.setPreferredSize(new Dimension(300, 400));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form Fields
        gbc.gridx = 0; gbc.gridy = 0; inputPanel.add(new JLabel("Type:"), gbc);
        comboType = new JComboBox<>(new String[]{"Income", "Expense"});
        gbc.gridx = 1; inputPanel.add(comboType, gbc);

        gbc.gridx = 0; gbc.gridy = 1; inputPanel.add(new JLabel("Category:"), gbc);
        comboCategory = new JComboBox<>(new String[]{"Food", "Travel", "Shopping", "Bills", "Salary", "Other"});
        gbc.gridx = 1; inputPanel.add(comboCategory, gbc);

        gbc.gridx = 0; gbc.gridy = 2; inputPanel.add(new JLabel("Amount:"), gbc);
        txtAmount = new JTextField();
        gbc.gridx = 1; inputPanel.add(txtAmount, gbc);

        gbc.gridx = 0; gbc.gridy = 3; inputPanel.add(new JLabel("Date (YYYY-MM-DD):"), gbc);
        txtDate = new JTextField(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        gbc.gridx = 1; inputPanel.add(txtDate, gbc);

        gbc.gridx = 0; gbc.gridy = 4; inputPanel.add(new JLabel("Description:"), gbc);
        txtDescription = new JTextField();
        gbc.gridx = 1; inputPanel.add(txtDescription, gbc);

        // Buttons
        JButton btnAdd = new JButton("Add Transaction");
        btnAdd.setBackground(new Color(52, 152, 219));
        btnAdd.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        inputPanel.add(btnAdd, gbc);

        JButton btnClear = new JButton("Clear Fields");
        gbc.gridy = 6;
        inputPanel.add(btnClear, gbc);

        JButton btnDelete = new JButton("Delete Selected");
        btnDelete.setBackground(expenseRed);
        btnDelete.setForeground(Color.WHITE);
        gbc.gridy = 7;
        inputPanel.add(btnDelete, gbc);

        add(inputPanel, BorderLayout.WEST);

        // --- TABLE PANEL (Center) ---
        String[] columnNames = {"ID", "Type", "Category", "Amount", "Date", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0);
        transactionTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(transactionTable);
        add(scrollPane, BorderLayout.CENTER);

        // --- ACTIONS ---
        btnAdd.addActionListener(e -> addTransaction());
        btnClear.addActionListener(e -> clearFields());
        btnDelete.addActionListener(e -> deleteTransaction());

        setVisible(true);
    }

    private JLabel createStatCard(String title, String value, Color color) {
        JLabel label = new JLabel("<html><center>" + title + "<br><font size='6' color='" + toHex(color) + "'>$" + value + "</font></center></html>", SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(Color.WHITE);
        label.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
        return label;
    }

    private String toHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    private void addTransaction() {
        try {
            String type = (String) comboType.getSelectedItem();
            String category = (String) comboCategory.getSelectedItem();
            double amount = Double.parseDouble(txtAmount.getText());
            String date = txtDate.getText();
            String description = txtDescription.getText();

            Transaction t = new Transaction(type, category, amount, date, description);
            dataManager.addTransaction(t);
            updateUI();
            clearFields();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTransaction() {
        int selectedRow = transactionTable.getSelectedRow();
        if (selectedRow >= 0) {
            dataManager.deleteTransaction(selectedRow);
            updateUI();
        } else {
            JOptionPane.showMessageDialog(this, "Select a row to delete.");
        }
    }

    private void clearFields() {
        txtAmount.setText("");
        txtDescription.setText("");
    }

    private void updateUI() {
        tableModel.setRowCount(0);
        for (Transaction t : dataManager.getAllTransactions()) {
            tableModel.addRow(new Object[]{t.getId(), t.getType(), t.getCategory(), t.getAmount(), t.getDate(), t.getDescription()});
        }

        lblTotalIncome.setText("<html><center>Total Income<br><font size='6' color='#2ecc71'>$" + String.format("%.2f", dataManager.getTotalIncome()) + "</font></center></html>");
        lblTotalExpenses.setText("<html><center>Total Expenses<br><font size='6' color='#e74c3c'>$" + String.format("%.2f", dataManager.getTotalExpenses()) + "</font></center></html>");
        lblBalance.setText("<html><center>Balance<br><font size='6' color='#333333'>$" + String.format("%.2f", dataManager.getBalance()) + "</font></center></html>");
    }
}