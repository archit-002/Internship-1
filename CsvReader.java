package internship.csv_to_json_converter_1;

import java.time.format.DateTimeFormatter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

    // Method to read Transactions
    public List<Transaction> readTransactions(String filePath) {
        List<Transaction> transactions = new ArrayList<>();
        try (CSVParser parser = new CSVParser(new FileReader(filePath), CSVFormat.DEFAULT.withHeader())) {
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


  
//        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm:ss a"); // Define the date-time format
            for (CSVRecord record : parser) {
                Transaction transaction = new Transaction();
                transaction.setTransactionId(record.get("transactionId"));
                transaction.setLoyaltyTransactionId(record.get("loyaltyTransactionId"));
                transaction.setAssignedToCustomerDate(LocalDateTime.parse(record.get("assignedToCustomerDate"), formatter));  // Use the formatter
                transaction.setTransactionType(record.get("transactionType"));
                transaction.setTransactionPlace(record.get("transactionPlace"));
                transaction.setCustomerId(record.get("customerId"));
                transaction.setPosId(record.get("posId"));
                transaction.setTransactionValue(Double.parseDouble(record.get("transactionValue")));
                transaction.setStoreCode(record.get("storeCode"));
                String matchedValue = record.get("matched").trim();
                if ("1".equals(matchedValue)) {
                    transaction.setMatched(true);	
                } else if ("0".equals(matchedValue)) {
                    transaction.setMatched(false);
                } else {
                    transaction.setMatched(false); // Default case
                }

                transaction.setUnitsDeducted(Integer.parseInt(record.get("unitsDeducted")));
                transaction.setPointsEarned(Integer.parseInt(record.get("pointsEarned")));
                transaction.setChannelId(record.get("channelId"));
                transaction.setTransactionDate(LocalDateTime.parse(record.get("transactionDate"), formatter));  // Use the formatter
                transactions.add(transaction);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Transactions read: " + transactions.size());
        return transactions;
    }

    // Method to read Products
    public List<Product> readProducts(String filePath) {
        List<Product> products = new ArrayList<>();
        try (CSVParser parser = new CSVParser(new FileReader(filePath), CSVFormat.DEFAULT.withHeader())) {
            for (CSVRecord record : parser) {
                Product product = new Product();
                product.setSku(record.get("sku"));
                product.setCategory(record.get("category"));
                product.setGrossValue(Double.parseDouble(record.get("grossValue")));
                product.setMaker(record.get("maker"));
                product.setName(record.get("name"));
                product.setQuantity(Integer.parseInt(record.get("quantity")));
                product.setHighPrecisionQuantity(Integer.parseInt(record.get("highPrecisionQuantity")));
                product.setBranchName(record.get("branchName"));
                products.add(product);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Products read: " + products.size());
        return products;
    }

    // Method to read TransactionProductLink
    public List<TransactionProductLink> readTransactionProductLinks(String filePath) {
        List<TransactionProductLink> tpLinks = new ArrayList<>();
        try (CSVParser parser = new CSVParser(new FileReader(filePath), CSVFormat.DEFAULT.withHeader())) {
            for (CSVRecord record : parser) {
                TransactionProductLink tpLink = new TransactionProductLink();
                tpLink.setTransactionId(record.get("transactionId"));
                tpLink.setSku(record.get("sku"));
                tpLinks.add(tpLink);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("TransactionProductLinks read: " + tpLinks.size());
        return tpLinks;
    }

 // Method to read CustomAttributes
    public List<CustomAttribute> readCustomAttributes(String filePath) {
        List<CustomAttribute> customAttributes = new ArrayList<>();
        try (CSVParser parser = new CSVParser(new FileReader(filePath), CSVFormat.DEFAULT.withHeader())) {
            for (CSVRecord record : parser) {
                CustomAttribute customAttribute = new CustomAttribute();
                customAttribute.setId(Integer.parseInt(record.get("id"))); // Set the ID
                customAttribute.setKey(record.get("key"));
                customAttribute.setValue(record.get("value"));
                customAttributes.add(customAttribute);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Error parsing ID in CustomAttributes CSV: " + e.getMessage());
        }
        System.out.println("CustomAttributes read: " + customAttributes.size());
        return customAttributes;
    }


 // Method to read Items
    public List<Items> readItems(String filePath) {
        List<Items> items = new ArrayList<>();
        try (CSVParser parser = new CSVParser(new FileReader(filePath), CSVFormat.DEFAULT.withHeader())) {
            for (CSVRecord record : parser) {
                Items item = new Items();
                item.setId(Integer.parseInt(record.get("id"))); // Set the ID
                item.setKey(record.get("key"));
                item.setValue(record.get("value"));
                items.add(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Error parsing ID in Items CSV: " + e.getMessage());
        }
        System.out.println("Items read: " + items.size());
        return items;
    }


    // Method to read TransactionCustomAttributeLink
    public List<TransactionCustomAttributeLink> readTransactionCustomAttributeLinks(String filePath) {
        List<TransactionCustomAttributeLink> links = new ArrayList<>();
        try (CSVParser parser = new CSVParser(new FileReader(filePath), CSVFormat.DEFAULT.withHeader())) {
            for (CSVRecord record : parser) {
                TransactionCustomAttributeLink link = new TransactionCustomAttributeLink();
                link.setTransactionId(record.get("transactionId"));
                link.setCustomAttributeId(Integer.parseInt(record.get("customAttributeId")));
                links.add(link);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("TransactionCustomAttributeLinks read: " + links.size());
        return links;
    }

    // Method to read ProductItemsLink
    public List<ProductItemsLink> readProductItemsLinks(String filePath) {
        List<ProductItemsLink> links = new ArrayList<>();
        try (CSVParser parser = new CSVParser(new FileReader(filePath), CSVFormat.DEFAULT.withHeader())) {
            for (CSVRecord record : parser) {
                ProductItemsLink link = new ProductItemsLink();
                link.setSku(record.get("sku"));
                link.setItemId(Integer.parseInt(record.get("itemId")));
                links.add(link);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("ProductItemsLinks read: " + links.size());
        return links;
    }
}