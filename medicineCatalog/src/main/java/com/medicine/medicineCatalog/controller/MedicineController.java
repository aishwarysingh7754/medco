package com.medicine.medicineCatalog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.medicine.medicineCatalog.exception.ResourceNotFoundException;
import com.medicine.medicineCatalog.model.Medicine;
import com.medicine.medicineCatalog.service.CloudinaryService;
import com.medicine.medicineCatalog.service.MedicineService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/medicines")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @GetMapping("/get-cat-{category}")
    public ResponseEntity<List<Medicine>> getMedicinesByCategory(@PathVariable String category) {
        List<Medicine> medicines = medicineService.getMedicinesByCategory(category);
        return ResponseEntity.ok(medicines);
    }

    @GetMapping("/get-all")
    public List<Medicine> listMedicines() {
        return medicineService.listAll();
    }

    @PostMapping("/add-medicine")
    public void addMedicine(@RequestBody Medicine medicine) {
        medicineService.save(medicine);
    }

    @GetMapping("/get-medicine-{id}")
    public Medicine getMedicine(@PathVariable Long id) {
        return medicineService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id " + id));
    }

    @PutMapping("/update-medicine-{id}")
    public ResponseEntity<Medicine> updateMedicine(@PathVariable Long id, @RequestBody Medicine medicineDetails) {
        Medicine medicine = medicineService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));

        medicine.setName(medicineDetails.getName());
        medicine.setDescription(medicineDetails.getDescription());

        Medicine updatedMedicine = medicineService.save(medicine);
        return ResponseEntity.ok(updatedMedicine);
    }

    @DeleteMapping("/delete-medicine-{id}")
    public ResponseEntity<?> deleteMedicine(@PathVariable Long id) {
        Medicine medicine = medicineService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));

        medicineService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file)
            throws IOException {
        String imageUrl = cloudinaryService.uploadFile(file);

        Optional<Medicine> optionalMedicine = medicineService.findById(id);
        if (optionalMedicine.isPresent()) {
            Medicine medicine = optionalMedicine.get();
            medicine.setImageUrl(imageUrl);
            medicineService.save(medicine);
            return ResponseEntity.ok("Image uploaded successfully and linked to medicine.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
