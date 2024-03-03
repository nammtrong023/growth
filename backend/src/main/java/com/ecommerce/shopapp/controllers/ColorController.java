package com.ecommerce.shopapp.controllers;

import com.ecommerce.shopapp.dtos.ColorDTO;
import com.ecommerce.shopapp.models.Color;
import com.ecommerce.shopapp.services.color.ColorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("${api.prefix}/colors")
public class ColorController {
    private final ColorService colorService;

    @PostMapping
    public ResponseEntity<Color> createColor(@Valid @RequestBody ColorDTO colorDTO) {
        Color savedColor = colorService.createColor(colorDTO);
        return new ResponseEntity<>(savedColor, HttpStatus.CREATED);
    }

    @GetMapping("{id}")
    public ResponseEntity<Color> getColorById(@PathVariable("id") Long ColorId) {
        Color Color = colorService.getColorById(ColorId);
        return new ResponseEntity<>(Color, HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<List<Color>> getAllColors() {
        List<Color> colors = colorService.getAllColors();
        return new ResponseEntity<>(colors, HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Color> updateColor(
            @Valid
            @PathVariable("id") Long colorId,
            @RequestBody ColorDTO colorDTO
    ) throws Exception {
        Color updatedColor = colorService.updateColor(colorId, colorDTO);
        return new ResponseEntity<>(updatedColor, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteColor(@PathVariable("id") Long colorId) throws Exception {
            colorService.deleteColor(colorId);
            return ResponseEntity.ok("Color successfully deleted!");
    }
}