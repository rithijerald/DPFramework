package com.server.dp.Service;

import com.server.dp.Model.DataRecord;
import org.apache.commons.csv.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Service
public class HybridService {

    private static final String INPUT_CSV = "/Users/rithiafrajeraldjothi/Downloads/sample_submission.csv";
    private static final String OUTPUT_CSV = "noisy_exponential_submission.csv";

    // Process CSV and apply Exponential noise
    public String processCSV(Double epsilon, Double sensitivity) {
        List<DataRecord> records = new ArrayList<>();

        try (Reader reader = new FileReader(INPUT_CSV);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                Long csvId = Long.parseLong(csvRecord.get("Id"));
                Double salePrice = Double.parseDouble(csvRecord.get("SalePrice"));

                // Apply Exponential Mechanism
                Double noisyExponentialPrice = applyExponentialMechanism(salePrice, epsilon, sensitivity);
                Double noisyLaplacePrice = generateLaplaceNoise(sensitivity / epsilon);
                		
                Double noisyPrice = (noisyExponentialPrice + noisyLaplacePrice)/2;
                // Create DataRecord object
                records.add(new DataRecord(csvId, salePrice, noisyPrice));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return saveToCSV(records);
    }

    private Double generateLaplaceNoise(double scale) {
		// TODO Auto-Random random = new Random();
    	Random random = new Random();
        double u = 0.5 - random.nextDouble();
        return scale * Math.signum(u) * Math.log(1 - 2 * Math.abs(u));
		
	}

	// Apply Exponential Mechanism noise
    private Double applyExponentialMechanism(Double value, Double epsilon, Double sensitivity) {
        Random random = new Random();
        double lambda = epsilon / (2 * sensitivity);  // Scale parameter

        // Generate random noise from Exponential distribution
        double u = random.nextDouble();  // Uniform random value in (0,1)
        double noise = -Math.log(1 - u) / lambda;  // Inverse transform sampling

        // Randomly flip sign for unbiased noise
        noise *= random.nextBoolean() ? 1 : -1;

        return value + noise;
    }

    // Save results to a new CSV file
    private String saveToCSV(List<DataRecord> records) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(OUTPUT_CSV));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("Id", "SalePrice", "NoisyPrice"))) {

            for (DataRecord record : records) {
                csvPrinter.printRecord(record.getCsvId(), record.getSalePrice(), record.getNoisyPrice());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return OUTPUT_CSV;
    }
}
