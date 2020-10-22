package com.local.dashspybackend.Entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "local_device_address_info")
@Data
public class LocalDeviceAddressInfoEntity {
    private String mac;
    private String localAddress;
    private String broadcastService;
}
