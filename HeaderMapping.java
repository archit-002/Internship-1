package internship.csv_to_json_converter_1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HeaderMapping {
    private final Map<String, String> headerMap = new HashMap<>();

    // Method to display program headers with unique identifiers
    public void displayProgramHeaders() {
        System.out.println("Predefined Program Headers:");
        int index = 1;
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            System.out.println("H" + index + ") " + entry.getValue());
            index++;
        }
    }

    // Method to map CSV headers to program headers
    public void mapHeaders(List<String> csvHeaders) {
        // Display CSV headers with unique identifiers
        System.out.println("CSV Headers:");
        int index = 1;
        for (String header : csvHeaders) {
            System.out.println("T" + index + ") " + header);
            index++;
        }

        // User input for header mapping
        Scanner scanner = new Scanner(System.in);
        for (String csvHeader : csvHeaders) {
            System.out.print("What should the \"" + csvHeader + "\" header from the CSV be mapped to: ");
            String userInput = scanner.nextLine().trim().toUpperCase();
            if (userInput.startsWith("H")) {
                String programHeader = headerMap.get(userInput);
                if (programHeader != null) {
                    System.out.println("Mapping " + csvHeader + " to " + programHeader);
                    headerMap.put(csvHeader, programHeader);  // Store the mapping
                } else {
                    System.out.println("Invalid input! Skipping mapping for " + csvHeader);
                }
            } else {
                System.out.println("Invalid input format. Please use identifiers like 'H1', 'H2', etc.");
            }
        }
    }

    // Method to set predefined program headers
    public void setPredefinedHeaders(Map<String, String> headers) {
        headerMap.putAll(headers);
    }

    // Getter for the header map
    public Map<String, String> getHeaderMap() {
        return headerMap;
    }
}
