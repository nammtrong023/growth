package com.ecommerce.shopapp.controllers;

import com.ecommerce.shopapp.dtos.SizeDTO;
import com.ecommerce.shopapp.models.Size;
import com.ecommerce.shopapp.services.size.SizeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("${api.prefix}/sizes")
public class SizeController {
    private final SizeService sizeService;

    @PostMapping
    public ResponseEntity<Size> createSize(@Valid @RequestBody SizeDTO sizeDTO) {
        Size savedSize = sizeService.createSize(sizeDTO);
        return new ResponseEntity<>(savedSize, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Size> getSizeById(@PathVariable("id") Long SizeId) {
        Size Size = sizeService.getSizeById(SizeId);
        return new ResponseEntity<>(Size, HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<List<Size>> getAllSizes() {
        List<Size> sizes = sizeService.getAllSizes();
        return new ResponseEntity<>(sizes, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Size> updateSize(
            @Valid
            @PathVariable("id") Long sizeId,
            @RequestBody SizeDTO sizeDTO
    ) throws Exception {
        Size updatedSize = sizeService.updateSize(sizeId, sizeDTO);
        return new ResponseEntity<>(updatedSize, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteSize(@PathVariable("id") Long sizeId) throws Exception {
            sizeService.deleteSize(sizeId);
            return ResponseEntity.ok("Size successfully deleted!");
    }
}