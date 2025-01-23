package internship.csv_to_json_converter_1;

public class Product {
    private String sku;
    private String category;
    private double grossValue;
    private String maker;
    private String name;
    private int quantity;
    private int highPrecisionQuantity;
    private String branchName;

    // Getters and Setters
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getGrossValue() { return grossValue; }
    public void setGrossValue(double grossValue) { this.grossValue = grossValue; }

    public String getMaker() { return maker; }
    public void setMaker(String maker) { this.maker = maker; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getHighPrecisionQuantity() { return highPrecisionQuantity; }
    public void setHighPrecisionQuantity(int highPrecisionQuantity) { this.highPrecisionQuantity = highPrecisionQuantity; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }
}
