package com.ecommerce.shopapp.services.role;

import com.ecommerce.shopapp.models.Role;
import com.ecommerce.shopapp.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepo;

    @Override
    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }
}
