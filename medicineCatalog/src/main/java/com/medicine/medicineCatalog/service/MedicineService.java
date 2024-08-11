package com.medicine.medicineCatalog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medicine.medicineCatalog.model.Medicine;
import com.medicine.medicineCatalog.repository.MedicineRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MedicineService {

    @Autowired
    private MedicineRepository medicineRepository;

    public List<Medicine> getMedicinesByCategory(String category) {
        return medicineRepository.findByCategory(category);
    }

    public Optional<Medicine> findById(Long id) {
        return medicineRepository.findById(id);
    }

    public Medicine save(Medicine medicine) {
        return medicineRepository.save(medicine);
    }

    public void delete(Long id) {
        medicineRepository.deleteById(id);
    }

    public List<Medicine> listAll() {
        return medicineRepository.findAll();
    }
}
