// --- 1. TRANSACTION CLASS ---
// Represents a single income or expense entry.
class Transaction {
    private static int idCounter = 1;
    private int id;
    private String type; // Income or Expense
    private String category;
    private double amount;
    private String date;
    private String description;

    public Transaction(String type, String category, double amount, String date, String description) {
        this.id = idCounter++;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.date = date;
        this.description = description;
    }

    // Getters
    public int getId() { return id; }
    public String getType() { return type; }
    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public String getDate() { return date; }
    public String getDescription() { return description; }
}