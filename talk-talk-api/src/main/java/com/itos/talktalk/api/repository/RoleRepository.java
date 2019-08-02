package com.itos.talktalk.api.repository;

import com.itos.talktalk.api.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByRole(String role);
}
