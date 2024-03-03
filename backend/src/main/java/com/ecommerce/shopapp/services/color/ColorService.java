package com.ecommerce.shopapp.services.color;

import com.ecommerce.shopapp.dtos.ColorDTO;
import com.ecommerce.shopapp.models.Color;

import java.util.List;

public interface ColorService {
    Color createColor(ColorDTO colorDTO);

    Color getColorById(Long colorId);

    List<Color> getAllColors();

    Color updateColor(Long colorId, ColorDTO colorDTO) throws Exception;

    void deleteColor(Long colorId) throws Exception;

    List<Color> existingColors(List<Long> colorIds);
}
