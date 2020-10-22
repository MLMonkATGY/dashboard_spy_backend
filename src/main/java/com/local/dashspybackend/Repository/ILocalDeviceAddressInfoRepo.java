package com.local.dashspybackend.Repository;

import com.local.dashspybackend.Entity.LocalDeviceAddressInfoEntity;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ILocalDeviceAddressInfoRepo extends JpaRepository<LocalDeviceAddressInfoEntity, String> {
    public LocalDeviceAddressInfoEntity findByBroadcastService(String broadcastService);

}
