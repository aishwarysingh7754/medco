package com.medicine.medicineCatalog.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.medicine.medicineCatalog.exception.ResourceNotFoundException;
import com.medicine.medicineCatalog.model.Medicine;
import com.medicine.medicineCatalog.service.MedicineService;

import java.util.List;

@RestController
@RequestMapping("/medicines")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;

    @GetMapping
    public List<Medicine> listMedicines() {
        return medicineService.listAll();
    }

    @PostMapping
    public void addMedicine(@RequestBody Medicine medicine) {
        medicineService.save(medicine);
    }

    @GetMapping("/{id}")
    public Medicine getMedicine(@PathVariable Long id) {
        return medicineService.get(id).orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id " + id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medicine> updateMedicine(@PathVariable Long id, @RequestBody Medicine medicineDetails) {
        Medicine medicine = medicineService.get(id)
            .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));

        medicine.setName(medicineDetails.getName());
        medicine.setDescription(medicineDetails.getDescription());

        Medicine updatedMedicine = medicineService.save(medicine);
        return ResponseEntity.ok(updatedMedicine);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedicine(@PathVariable Long id) {
        Medicine medicine = medicineService.get(id)
            .orElseThrow(() -> new ResourceNotFoundException("Medicine not found with id: " + id));

        medicineService.delete(id);
        return ResponseEntity.ok().build();
    }
}
