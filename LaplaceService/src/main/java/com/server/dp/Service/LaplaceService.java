package com.server.dp.Service;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.distribution.GammaDistribution;

import com.server.dp.Model.DataRecord;
import com.server.dp.Repository.DataRecordRepository;
import org.apache.commons.csv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@Service
public class LaplaceService {

    @Autowired
    private DataRecordRepository dataRecordRepository;
    
    private final double k; // Decay rate
    private final double n; // Controls sharpness and tail lightness
    private final double A; // Normalization constant

    public LaplaceService() {
    	this.k = 1.0; // Adjust as needed
        this.n = 2.0; // Gaussian-like tails
        this.A = Math.sqrt(k / Math.PI);    }

    // PDF of the distribution
    private double pdf(double x) {
        return A * Math.exp(-k * Math.pow(Math.abs(x), n));
    }

    private static final String INPUT_CSV = "/Users/rithiafrajeraldjothi/Downloads/sample_submission.csv";
    private static final String OUTPUT_CSV = "noisy_submission.csv";

    // Method to process CSV and apply Laplace noise
    public String processCSV(Double epsilon, Double sensitivity) {
        List<DataRecord> records = new ArrayList<>();

        try (Reader reader = new FileReader(INPUT_CSV);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {

            for (CSVRecord csvRecord : csvParser) {
                Long csvId = Long.parseLong(csvRecord.get("Id"));
                Double salePrice = Double.parseDouble(csvRecord.get("SalePrice"));

                // Generate Laplace noise
                //Double noise = generateLaplaceNoise(sensitivity / epsilon);
                Double noise = generateNoise();
                Double noisyPrice = salePrice + noise;

                // ✅ Fix: Use the correct constructor without null
                DataRecord dataRecord = new DataRecord(csvId, salePrice, noisyPrice);
                records.add(dataRecord);
            }

            // Save records to the database
            dataRecordRepository.saveAll(records);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return saveToCSV(records);
    }

    // Method to generate Laplace noise
   /* private Double generateLaplaceNoise(Double scale) {
        Random random = new Random();
        double u = 0.5 - random.nextDouble();
        return scale * Math.signum(u) * Math.log(1 - 2 * Math.abs(u));
    }*/
    
    private double generateNoise() {
        Random random = new Random();
        while (true) {
            double x = random.nextGaussian(); // Proposal distribution (Gaussian)
            double u = random.nextDouble() * A; // Uniform random value
            if (u <= pdf(x)) {
                return x;
            }
        }
    }


    // Method to save processed records to a new CSV file
    private String saveToCSV(List<DataRecord> records) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(OUTPUT_CSV));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                     .withHeader("Id", "SalePrice", "NoisyPrice"))) {

            for (DataRecord record : records) {
                csvPrinter.printRecord(record.getCsvId(), record.getSalePrice(), record.getNoisyPrice());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return OUTPUT_CSV;
    }
}
