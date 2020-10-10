package com.local.dashspybackend.Repository;

import com.local.dashspybackend.Entity.LightStateEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ILightStateRepo extends JpaRepository<LightStateEntity, Integer> {
    
}
