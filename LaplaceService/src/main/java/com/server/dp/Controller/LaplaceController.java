package com.server.dp.Controller;



import com.server.dp.Service.LaplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/laplace")
public class LaplaceController {

    @Autowired
    private LaplaceService laplaceService;

    // Endpoint to process CSV and return modified file
    @PostMapping("/process-csv")
    public ResponseEntity<Resource> processCSV(@RequestParam Double epsilon, @RequestParam Double sensitivity) {
        String filePath = laplaceService.processCSV(epsilon, sensitivity);
        
        try {
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"noisy_submission.csv\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
