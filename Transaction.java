package internship.csv_to_json_converter_1;

import java.time.LocalDateTime;

public class Transaction {
    private String transactionId;
    private String loyaltyTransactionId;
    private LocalDateTime assignedToCustomerDate;  // Changed to LocalDateTime
    private String transactionType;
    private String transactionPlace;
    private String customerId;
    private String posId;
    private double transactionValue;
    private String storeCode;
    private boolean matched;
    private int unitsDeducted;
    private int pointsEarned;
    private String channelId;
    private LocalDateTime transactionDate;  // Changed to LocalDateTime

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getLoyaltyTransactionId() { return loyaltyTransactionId; }
    public void setLoyaltyTransactionId(String loyaltyTransactionId) { this.loyaltyTransactionId = loyaltyTransactionId; }

    public LocalDateTime getAssignedToCustomerDate() { return assignedToCustomerDate; }
    public void setAssignedToCustomerDate(LocalDateTime assignedToCustomerDate) { this.assignedToCustomerDate = assignedToCustomerDate; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public String getTransactionPlace() { return transactionPlace; }
    public void setTransactionPlace(String transactionPlace) { this.transactionPlace = transactionPlace; }

    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }

    public String getPosId() { return posId; }
    public void setPosId(String posId) { this.posId = posId; }

    public double getTransactionValue() { return transactionValue; }
    public void setTransactionValue(double transactionValue) { this.transactionValue = transactionValue; }

    public String getStoreCode() { return storeCode; }
    public void setStoreCode(String storeCode) { this.storeCode = storeCode; }

    public boolean isMatched() { return matched; }
    public void setMatched(boolean matched) { this.matched = matched; }

    public int getUnitsDeducted() { return unitsDeducted; }
    public void setUnitsDeducted(int unitsDeducted) { this.unitsDeducted = unitsDeducted; }

    public int getPointsEarned() { return pointsEarned; }
    public void setPointsEarned(int pointsEarned) { this.pointsEarned = pointsEarned; }

    public String getChannelId() { return channelId; }
    public void setChannelId(String channelId) { this.channelId = channelId; }

    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }
}
