package internship.csv_to_json_converter;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.time.format.*;
import java.util.*;

public class csv_header_converter {

    public static void main(String[] args) {
        // Provide the path to the input CSV file and the output CSV file
        String inputFilePath = "C:/Users/archi/Documents/workspace-sts/csv_to_json_converter/src/main/java/internship/csv_to_json_converter/conv_tra.csv";
        String outputFilePath = "C:/Users/archi/Documents/workspace-sts/csv_to_json_converter/src/main/java/internship/csv_to_json_converter/tra.csv";

        // Fixed header names with their data types
        Map<String, String> fixedHeaders = new LinkedHashMap<>();
        fixedHeaders.put("transactionId", "String");
        fixedHeaders.put("loyaltyTransactionId", "String");
        fixedHeaders.put("assignedToCustomerDate", "LocalDateTime");
        fixedHeaders.put("transactionType", "String");
        fixedHeaders.put("transactionPlace", "String");
        fixedHeaders.put("customerId", "String");
        fixedHeaders.put("posId", "String");
        fixedHeaders.put("transactionValue", "double");
        fixedHeaders.put("storeCode", "String");
        fixedHeaders.put("matched", "boolean");
        fixedHeaders.put("unitsDeducted", "int");
        fixedHeaders.put("pointsEarned", "int");
        fixedHeaders.put("channelId", "String");
        fixedHeaders.put("transactionDate", "LocalDateTime");
        fixedHeaders.put("id", "int");
        fixedHeaders.put("key", "String");
        fixedHeaders.put("value", "String");
        fixedHeaders.put("sku", "String");
        fixedHeaders.put("category", "String");
        fixedHeaders.put("grossValue", "double");
        fixedHeaders.put("maker", "String");
        fixedHeaders.put("name", "String");
        fixedHeaders.put("quantity", "int");
        fixedHeaders.put("highPrecisionQuantity", "int");
        fixedHeaders.put("branchName", "String");
        fixedHeaders.put("itemId", "int");
        fixedHeaders.put("customAttributeId", "int");

        try {
            // Read the CSV file
            List<String> lines = Files.readAllLines(Paths.get(inputFilePath));

            if (lines.isEmpty()) {
                System.out.println("CSV file is empty.");
                return;
            }

            // Split the first line to identify headers
            String[] originalHeaders = lines.get(0).split(",");

            if (lines.size() < 2) {
                System.out.println("CSV file does not contain any data rows.");
                return;
            }

            // Get the first record (second line in the file)
            String[] firstRecord = lines.get(1).split(",");

            // Determine the data types of each column based on the first record
            Map<String, String> headerDataTypes = new HashMap<>();
            for (int i = 0; i < originalHeaders.length; i++) {
                String value = firstRecord[i];
                String dataType = determineDataType(value);
                headerDataTypes.put(originalHeaders[i], dataType);
            }

            // Display the fixed headers and their data types (3 per line)
            System.out.println("Fixed header names with data types:");
            int index = 1;
            int count = 0;
            for (Map.Entry<String, String> entry : fixedHeaders.entrySet()) {
                System.out.print(index + ") " + entry.getKey() + ": " + entry.getValue() + "    ");
                index++;
                count++;
                if (count % 3 == 0) {
                    System.out.println();  // Move to the next line after every 3 headers
                }
            }

            // Display headers read from the CSV file (3 per line)
            System.out.println("\nHeaders read from the CSV file:");
            count = 0;
            for (int i = 0; i < originalHeaders.length; i++) {
                System.out.print(i + 1 + ") " + originalHeaders[i] + ": " + headerDataTypes.get(originalHeaders[i]) + "    ");
                count++;
                if (count % 3 == 0) {
                    System.out.println();  // Move to the next line after every 3 headers
                }
            }

            // Ask the user to map CSV headers to fixed headers
            Scanner scanner = new Scanner(System.in);
            Map<String, String> renamedHeaders = new HashMap<>();
            Set<String> fixedHeadersUsed = new HashSet<>();

            for (int i = 0; i < originalHeaders.length; i++) {
                String csvHeader = originalHeaders[i];
                String csvDataType = headerDataTypes.get(csvHeader);

                boolean validMapping = false;
                while (!validMapping) {
                    System.out.print("\nEnter the number corresponding to the fixed header you want to map to CSV header '" + csvHeader + "' (" + csvDataType + "): ");
                    int fixedHeaderNumber = Integer.parseInt(scanner.nextLine().trim());

                    // Check if the number is valid and if it's not already used
                    if (fixedHeaderNumber >= 1 && fixedHeaderNumber <= fixedHeaders.size()) {
                        String fixedHeader = (String) fixedHeaders.keySet().toArray()[fixedHeaderNumber - 1];

                        // Check if the fixed header is already used
                        if (!fixedHeadersUsed.contains(fixedHeader)) {
                            // Check data type compatibility
                            if (normalizeDataType(fixedHeaders.get(fixedHeader)).equalsIgnoreCase(normalizeDataType(csvDataType))) {
                                renamedHeaders.put(csvHeader, fixedHeader);
                                fixedHeadersUsed.add(fixedHeader);
                                validMapping = true;
                            } else {
                                System.out.println("Data type mismatch: Fixed header '" + fixedHeader + "' expects " +
                                        fixedHeaders.get(fixedHeader) + ", but CSV header '" + csvHeader + "' has " + csvDataType + ". Please try again.");
                            }

                        } else {
                            System.out.println("This fixed header is already mapped. Please choose another.");
                        }
                    } else {
                        System.out.println("Invalid number. Please choose a valid number between 1 and " + fixedHeaders.size());
                    }
                }
            }

            // Replace the original headers with the new headers (based on user mapping)
            List<String> newHeaders = new ArrayList<>();
            for (String header : originalHeaders) {
                if (renamedHeaders.containsKey(header)) {
                    newHeaders.add(renamedHeaders.get(header));
                } else {
                    newHeaders.add(header);
                }
            }

            // Write the new CSV file with updated headers
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
            writer.write(String.join(",", newHeaders));
            writer.newLine();

            // Write the rest of the lines (data rows) without modification
            for (int i = 1; i < lines.size(); i++) {
                writer.write(lines.get(i));
                writer.newLine();
            }

            writer.close();
            System.out.println("CSV file has been saved with renamed headers.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to normalize the data types for comparison
    private static String normalizeDataType(String dataType) {
        switch (dataType.toLowerCase()) {
            case "integer":
            case "int":
                return "int";  // Normalize to primitive type
            case "double":
            case "float":
                return "double";  // Normalize to primitive type
            case "boolean":
                return "boolean"; // No change needed
            case "string":
                return "String";  // Capitalize for consistency
            case "localdate":
                return "LocalDate";
            case "localdatetime":
                return "LocalDateTime";
            default:
                return dataType;  // If no match, return the type as it is
        }
    }

    // Method to determine the data type of a value
    private static String determineDataType(String value) {
        if (value == null || value.trim().isEmpty()) {
            return "String"; // Default to String for empty values
        }

        // Check for Boolean
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
            return "Boolean";
        }

        // Check for Integer
        try {
            Integer.parseInt(value);
            return "Integer";
        } catch (NumberFormatException ignored) {}

        // Check for Double
        try {
            Double.parseDouble(value);
            return "Double";
        } catch (NumberFormatException ignored) {}

        // List of date and date-time patterns to check against
        String[] datePatterns = {
            "dd-MM-yyyy HH:mm", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy HH:mm", 
            "MM/dd/yyyy HH:mm", "MM-dd-yyyy HH:mm", "yyyy/MM/dd HH:mm:ss", "dd-MM-yyyy HH:mm:ss", 
            "dd/MM/yyyy HH:mm:ss", "MM/dd/yyyy HH:mm:ss", "MM-dd-yyyy HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss", 
            "yyyy-MM-dd'T'HH:mm:ss.SSS", "yyyy-MM-dd", "dd-MM-yyyy", "MM-dd-yyyy", "dd/MM/yyyy", 
            "MM/dd/yyyy", "yyyy/MM/dd", "HH:mm", "HH:mm:ss", "HH:mm:ss.SSS", "yyyy-MM-dd'T'HH:mm:ssXXX", 
            "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", "yyyy-MM-dd HH:mm:ss Z", "yyyy-MM-dd'T'HH:mmXXX", 
            "EEE, MMM dd yyyy HH:mm:ss Z", "EEE, dd MMM yyyy HH:mm:ss zzz", "dd-MMM-yyyy HH:mm:ss", 
            "dd-MMM-yyyy", "MMM dd, yyyy HH:mm:ss", "MMM dd, yyyy"
        };

        // Check for LocalDateTime or LocalDate
        for (String pattern : datePatterns) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                if (pattern.contains("HH:mm")) {
                    LocalDateTime.parse(value, formatter);
                    return "LocalDateTime";
                } else {
                    LocalDate.parse(value, formatter);
                    return "LocalDate";
                }
            } catch (DateTimeParseException ignored) {}
        }

        // If no other match, default to String
        return "String";
    }
}
