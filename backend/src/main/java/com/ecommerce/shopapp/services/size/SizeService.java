package com.ecommerce.shopapp.services.size;

import com.ecommerce.shopapp.dtos.SizeDTO;
import com.ecommerce.shopapp.models.Size;

import java.util.List;

public interface SizeService {
    Size createSize(SizeDTO sizeDTO);

    Size getSizeById(Long sizeId);

    List<Size> getAllSizes();

    Size updateSize(Long sizeId, SizeDTO sizeDTO) throws Exception;

    void deleteSize(Long sizeId) throws Exception;

    List<Size> existingSizes(List<Long> sizeIds);
}
