package com.example.databaselab2;

// Main.java
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        /*
         * Part 1.1 - Metadata:
         * - File name: 2024 QS World University Rankings 1.1 For qs.com 1.csv
         * - Number of records: 1498 universities
         * - Number of columns: 29
         * - Encoding: UTF-8
         * - Contains rankings data for world universities with various metrics
         *
         * Part 1.2 - Data Types:
         * - 2024 RANK: String (contains ranges like "501-510")
         * - 2023 RANK: String
         * - Institution Name: String
         * - Country Code: String
         * - Country: String
         * - SIZE: String (categorical: S, M, L, XL)
         * - FOCUS: String (categorical)
         * - RES.: String (categorical)
         * - AGE: String (categorical)
         * - STATUS: String (categorical)
         * - Academic Reputation Score: Double
         * - Academic Reputation Rank: Integer
         * - Employer Reputation Score: Double
         * - Employer Reputation Rank: Integer
         * - Faculty Student Score: Double
         * - Faculty Student Rank: Integer
         * - Citations per Faculty Score: Double
         * - Citations per Faculty Rank: Integer
         * - International Faculty Score: Double
         * - International Faculty Rank: Integer
         * - International Students Score: Double
         * - International Students Rank: Integer
         * - International Research Network Score: Double
         * - International Research Network Rank: Integer
         * - Employment Outcomes Score: Double
         * - Employment Outcomes Rank: Integer
         * - Sustainability Score: Double
         * - Sustainability Rank: Integer
         * - Overall SCORE: Double
         */

        String inputFile = "C:\\Users\\Admin\\OneDrive\\Desktop\\2024 QS World University Rankings 1.1 (For qs.com).csv";
        String outputFile = "QS-World-University-Rankings-2024_updated.csv";
        List<UniData> universities = new ArrayList<>();
        String header = "";

        // Part 2.1 - Read CSV file
        System.out.println("Reading CSV file...");
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            // Read header
            header = br.readLine();

            // Read data
            String line;
            while ((line = br.readLine()) != null) {
                UniData uni = UniData.fromCSV(line);
                universities.add(uni);
            }
            System.out.println("Successfully read " + universities.size() + " universities");
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }

        // Part 2.2 - Add Chiang Mai University
        System.out.println("\nAdding Chiang Mai University...");
        UniData chiangMaiUni = new UniData(
                "601-610",  // 2024 RANK (estimated based on QS website)
                "601-650",           // 2023 RANK
                "Chiang Mai University",
                "TH",     // Country Code
                "Thailand",
                "L",            // SIZE (Large)
                "CO",                // FOCUS (Comprehensive)
                "HI",                // RES. (High research)
                "3",                 // AGE (founded 1964)
                "A",                 // STATUS
                "",                  // Scores will be empty for new entry
                "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""
        );
        universities.add(chiangMaiUni);

        for (UniData uni : universities) {
            uni.replaceEmptyScoresWithZero();
            uni.roundScores();
        }

        universities.removeIf(uni -> uni.getRank2024().contains("-"));
        universities.removeIf(uni -> uni.getRank2023().contains("-"));

        tolowercase(universities);

        Collections.sort(universities);

        // Write to new file
        System.out.println("Writing to new CSV file...");
        try (PrintWriter pw = new PrintWriter(new FileWriter(outputFile))) {
            // Write header
            pw.println(header);

            // Write all universities
            for (UniData uni : universities) {
                pw.println(uni.toCSV());
            }
            System.out.println("Successfully wrote " + universities.size() + " universities to " + outputFile);
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
            e.printStackTrace();
        }

        /*
         * Part 3 - Problems with traditional database files:
         *
         * 1. Domain Integrity Issues:
         *    - No validation for data types (everything stored as strings)
         *    - Rankings can contain ranges ("501-510") making comparisons difficult
         *    - No constraints on valid values for categorical fields
         *
         * 2. NULL Value Handling:
         *    - Empty strings vs actual NULL values are indistinguishable
         *    - Missing data not consistently represented
         *    - No default values for missing scores
         *
         * 3. Data Consistency:
         *    - No referential integrity (country codes not validated)
         *    - Duplicate entries possible
         *    - No primary key enforcement
         *
         * 4. Concurrent Access:
         *    - File locking issues with multiple users
         *    - No transaction support
         *    - Risk of data corruption
         *
         * 5. Query Limitations:
         *    - Must read entire file to find specific records
         *    - No indexing for fast searches
         *    - Complex queries require custom programming
         *
         * 6. Data Redundancy:
         *    - Country names repeated for each university
         *    - No normalization
         *    - Increased storage space
         */
    }
    public static void tolowercase(List<UniData> universities) {
        for (UniData uni : universities) {
            String lowerCaseName = uni.getInstitutionName().toLowerCase();
            uni.setInstitutionName(lowerCaseName);
        }
    }
}
