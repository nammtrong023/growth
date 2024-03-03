package com.ecommerce.shopapp.services.size;

import com.ecommerce.shopapp.dtos.SizeDTO;
import com.ecommerce.shopapp.exceptions.BadRequestException;
import com.ecommerce.shopapp.exceptions.DataNotFoundException;
import com.ecommerce.shopapp.models.Size;
import com.ecommerce.shopapp.repositories.SizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {
    private final SizeRepository sizeRepository;

    @Override
    public Size createSize(SizeDTO sizeDTO) {
        Size size = Size
                .builder()
                .name(sizeDTO.getName())
                .value(sizeDTO.getValue())
                .build();
        return sizeRepository.save(size);
    }

    @Override
    public Size getSizeById(Long sizeId) {
        return sizeRepository.findById(sizeId)
                .orElseThrow(() -> new DataNotFoundException("Size not found"));
    }

    @Override
    public List<Size> getAllSizes() {
        return sizeRepository.findAll();
    }

    @Override
    @Transactional
    public Size updateSize(Long sizeId, SizeDTO sizeDTO) {
        Size size = sizeRepository.findById(sizeId)
                .orElseThrow(() -> new DataNotFoundException("Size not found"));

        size.setName(sizeDTO.getName());
        size.setValue(sizeDTO.getValue());
        sizeRepository.save(size);

        return size;
    }

    @Override
    @Transactional
    public void deleteSize(Long sizeId) {
        Size size = sizeRepository.findById(sizeId)
                .orElseThrow(() -> new DataNotFoundException("Size not found"));

        if(!size.getProducts().isEmpty()) {
            throw new BadRequestException(
                    "Ensure that all products linked with this size is removed.");
        }

        try {
            sizeRepository.deleteById(sizeId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error when [DELETE_SIZE]");
        }
    }

    @Override
    public List<Size> existingSizes(List<Long> sizeIds) {
        List<Size> existingSizes = sizeRepository.findAllByIdIn(sizeIds);

        if(!(existingSizes.size() == sizeIds.size())) {
            throw new DataNotFoundException("Size not found");
        }

        return existingSizes;
    }
}
