package internship.csv_to_json_converter_1;

import org.apache.commons.csv.CSVRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class TransactionJsonMapper {

    // Set the parallelism level for ForkJoinPool
    private static final ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

    public List<Map<String, Object>> mapTransactionsToJson(List<Transaction> transactions,
                                                            List<Product> products,
                                                            List<TransactionProductLink> tpLinks,
                                                            List<CustomAttribute> customAttributes,
                                                            List<TransactionCustomAttributeLink> transactionCustomAttributesLinks,
                                                            List<Items> items,
                                                            List<ProductItemsLink> productItemsLinks) {

        List<Map<String, Object>> transactionJsonList = Collections.synchronizedList(new ArrayList<>());

        try {
            // Create optimized maps for lookups
            Map<String, Product> productDetailsMap = new HashMap<>();
            for (Product product : products) {
                productDetailsMap.put(product.getSku(), product);
            }

            Map<String, List<CustomAttribute>> transactionCustomAttributesMap = new HashMap<>();
            for (TransactionCustomAttributeLink link : transactionCustomAttributesLinks) {
                transactionCustomAttributesMap
                    .computeIfAbsent(link.getTransactionId(), k -> new ArrayList<>())
                    .add(customAttributes.stream()
                        .filter(ca -> ca.getId() == link.getCustomAttributeId())
                        .findFirst().orElse(null));
            }

            Map<String, List<Items>> productItemsMap = new HashMap<>();
            for (ProductItemsLink link : productItemsLinks) {
                productItemsMap
                    .computeIfAbsent(link.getSku(), k -> new ArrayList<>())
                    .addAll(items.stream()
                        .filter(item -> item.getId() == link.getItemId())
                        .collect(Collectors.toList()));
            }

            // Process transactions in parallel
            transactions.parallelStream().forEach(transaction -> {
                try {
                    Map<String, Object> transactionMap = new LinkedHashMap<>();
                    transactionMap.put("loyaltyTransactionId", transaction.getLoyaltyTransactionId());
                    transactionMap.put("transactionId", transaction.getTransactionId());
                    transactionMap.put("assignedToCustomerDate", transaction.getAssignedToCustomerDate());
                    transactionMap.put("transactionType", transaction.getTransactionType());
                    transactionMap.put("transactionPlace", transaction.getTransactionPlace());
                    transactionMap.put("customerId", transaction.getCustomerId());

                    // Adding custom attributes for transaction
                    List<Map<String, Object>> customAttributesForTransaction = new ArrayList<>();
                    if (transactionCustomAttributesMap.containsKey(transaction.getTransactionId())) {
                        for (CustomAttribute ca : transactionCustomAttributesMap.get(transaction.getTransactionId())) {
                            if (ca != null) {
                                Map<String, Object> customAttributeMap = new HashMap<>();
                                customAttributeMap.put("key", ca.getKey());
                                customAttributeMap.put("value", ca.getValue());
                                customAttributesForTransaction.add(customAttributeMap);
                            }
                        }
                    }
                    transactionMap.put("customAttribute", customAttributesForTransaction);

                    // Add transaction's other fields
                    transactionMap.put("posId", transaction.getPosId());
                    transactionMap.put("transactionValue", transaction.getTransactionValue());
                    transactionMap.put("storeCode", transaction.getStoreCode());

                    // Link products to transactions
                    List<Map<String, Object>> productsForTransaction = new ArrayList<>();
                    for (TransactionProductLink link : tpLinks) {
                        if (link.getTransactionId().equals(transaction.getTransactionId())) {
                            Product product = productDetailsMap.get(link.getSku());
                            if (product != null) {
                                Map<String, Object> productMap = new HashMap<>();
                                productMap.put("category", product.getCategory());
                                productMap.put("grossValue", product.getGrossValue());
                                productMap.put("maker", product.getMaker());
                                productMap.put("name", product.getName());
                                productMap.put("quantity", product.getQuantity());
                                productMap.put("highPrecisionQuantity", product.getHighPrecisionQuantity());
                                productMap.put("sku", product.getSku());
                                productMap.put("branchName", product.getBranchName());

                                // Link items to product
                                List<Map<String, Object>> itemsForProduct = new ArrayList<>();
                                for (Items linkedItem : productItemsMap.getOrDefault(product.getSku(), Collections.emptyList())) {
                                    Map<String, Object> itemMap = new HashMap<>();
                                    itemMap.put("key", linkedItem.getKey());
                                    itemMap.put("value", linkedItem.getValue());
                                    itemsForProduct.add(itemMap);
                                }
                                productMap.put("items", itemsForProduct);
                                productsForTransaction.add(productMap);
                            }
                        }
                    }
                    transactionMap.put("products", productsForTransaction);

                    // Determine if transaction is matched
                    transactionMap.put("matched", isTransactionMatched(transaction));

                    // Additional fields
                    transactionMap.put("unitsDeducted", transaction.getUnitsDeducted());
                    transactionMap.put("pointsEarned", transaction.getPointsEarned());
                    transactionMap.put("channelId", transaction.getChannelId());
                    transactionMap.put("transactionDate", formatDate(transaction.getTransactionDate()));
                    transactionMap.put("assignedToCustomerDate", formatDate(transaction.getAssignedToCustomerDate()));

                    // Safely add the map to the synchronized list
                    transactionJsonList.add(transactionMap);
                } catch (Exception e) {
                    System.err.println("Error processing transaction " + transaction.getTransactionId() + ": " + e.getMessage());
                }
            });

        } catch (Exception e) {
            System.err.println("Error mapping transactions to JSON: " + e.getMessage());
        }

        return transactionJsonList;
    }

    private boolean isTransactionMatched(Transaction transaction) {
        return Boolean.parseBoolean(String.valueOf(transaction.isMatched()));
    }

    private String formatDate(java.time.LocalDateTime dateTime) {
        return dateTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"));
    }


    public static void main(String[] args) {
        // Provide the updated file paths
        String transactionsFilePath = "C:/Users/archi/Documents/workspace-sts/csv_to_json_converter/src/main/java/internship/csv_to_json_converter_1/transactions_final_formatted.csv";
        String productsFilePath = "C:/Users/archi/Documents/workspace-sts/csv_to_json_converter/src/main/java/internship/csv_to_json_converter_1/Products_Table.csv";
        String tpFilePath = "C:/Users/archi/Documents/workspace-sts/csv_to_json_converter/src/main/java/internship/csv_to_json_converter_1/transaction_product_link_trial.csv";
        
        String customAttributeFilePath = "C:/Users/archi/Documents/workspace-sts/csv_to_json_converter/src/main/java/internship/csv_to_json_converter_1/custom_attribute_50k.csv";
        String transactionCustomAttributesLinkFilePath = "C:/Users/archi/Documents/workspace-sts/csv_to_json_converter/src/main/java/internship/csv_to_json_converter_1/transaction_customattribute_mapping.csv";
        String itemsFilePath = "C:/Users/archi/Documents/workspace-sts/csv_to_json_converter/src/main/java/internship/csv_to_json_converter_1/items_table.csv";
        String productItemsLinkFilePath = "C:/Users/archi/Documents/workspace-sts/csv_to_json_converter/src/main/java/internship/csv_to_json_converter_1/product_items_corrected_mapping.csv";

        CsvReader csvReader = new CsvReader();

        // Read CSV data into Java objects
        List<Transaction> transactions = null;
        try {
            transactions = csvReader.readTransactions(transactionsFilePath);
        } catch (Exception e) {
            System.err.println("Error reading transactions: " + e.getMessage());
        }

        List<Product> products = null;
        try {
            products = csvReader.readProducts(productsFilePath);
        } catch (Exception e) {
            System.err.println("Error reading products: " + e.getMessage());
        }

        List<TransactionProductLink> tpLinks = null;
        try {
            tpLinks = csvReader.readTransactionProductLinks(tpFilePath);
        } catch (Exception e) {
            System.err.println("Error reading transaction-product links: " + e.getMessage());
        }

        List<CustomAttribute> customAttributes = null;
        try {
            customAttributes = csvReader.readCustomAttributes(customAttributeFilePath);
        } catch (Exception e) {
            System.err.println("Error reading custom attributes: " + e.getMessage());
        }

        List<TransactionCustomAttributeLink> transactionCustomAttributesLinks = null;
        try {
            transactionCustomAttributesLinks = csvReader.readTransactionCustomAttributeLinks(transactionCustomAttributesLinkFilePath);
        } catch (Exception e) {
            System.err.println("Error reading transaction custom attribute links: " + e.getMessage());
        }

        List<Items> items = null;
        try {
            items = csvReader.readItems(itemsFilePath);
        } catch (Exception e) {
            System.err.println("Error reading items: " + e.getMessage());
        }

        List<ProductItemsLink> productItemsLinks = null;
        try {
            productItemsLinks = csvReader.readProductItemsLinks(productItemsLinkFilePath);
        } catch (Exception e) {
            System.err.println("Error reading product-items links: " + e.getMessage());
        }

        // Create an instance of the JSON mapper
        TransactionJsonMapper jsonMapper = new TransactionJsonMapper();

        // Map the data to JSON format
        List<Map<String, Object>> jsonData = jsonMapper.mapTransactionsToJson(
                transactions, products, tpLinks, customAttributes, transactionCustomAttributesLinks, items, productItemsLinks);

        // Create an ObjectMapper instance with custom date format for LocalDateTime
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Ensure JavaTimeModule is registered

        // Write the JSON output to a file using BufferedWriter
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("C:/Users/archi/Documents/workspace-sts/csv_to_json_converter/src/main/java/internship/csv_to_json_converter_1/output_trial.json"))) {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(bufferedWriter, jsonData);
            System.out.println("JSON output written to output_trial.json");
        } catch (IOException e) {
            System.err.println("Error writing JSON output: " + e.getMessage());
        }
    }
}
