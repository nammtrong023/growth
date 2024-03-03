package com.ecommerce.shopapp.services.color;

import com.ecommerce.shopapp.dtos.ColorDTO;
import com.ecommerce.shopapp.exceptions.BadRequestException;
import com.ecommerce.shopapp.exceptions.DataNotFoundException;
import com.ecommerce.shopapp.models.Color;
import com.ecommerce.shopapp.repositories.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {
    private final ColorRepository colorRepository;

    @Override
    public Color createColor(ColorDTO colorDTO) {
        Color color = Color
                .builder()
                .name(colorDTO.getName())
                .value(colorDTO.getValue())
                .build();
        return colorRepository.save(color);
    }

    @Override
    public Color getColorById(Long colorId) {
        return colorRepository.findById(colorId)
                .orElseThrow(() -> new DataNotFoundException("Color not found"));
    }

    @Override
    public List<Color> getAllColors() {
        return colorRepository.findAll();
    }

    @Override
    @Transactional
    public Color updateColor(Long colorId, ColorDTO colorDTO) {
        Color color = colorRepository.findById(colorId)
                .orElseThrow(() -> new DataNotFoundException("Color not found"));

        color.setName(colorDTO.getName());
        color.setValue(colorDTO.getValue());
        colorRepository.save(color);

        return color;
    }


    @Override
    @Transactional
    public void deleteColor(Long colorId) {
        Color color = colorRepository.findById(colorId)
                .orElseThrow(() -> new DataNotFoundException("Color not found"));

        if(!color.getProducts().isEmpty()) {
            throw new BadRequestException(
                    "Ensure that all products linked with this color is removed.");
        }

        try {
            colorRepository.deleteById(colorId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error when [DELETE_COLOR]");
        }
    }

    @Override
    public List<Color> existingColors(List<Long> colorIds) {
        List<Color> existingColors = colorRepository.findAllByIdIn(colorIds);

        if(existingColors.size() != colorIds.size()) {
            throw new DataNotFoundException("Color not found");
        }

        return existingColors;
    }
}
