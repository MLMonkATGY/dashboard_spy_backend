package com.local.dashspybackend.Entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "local_device_address_info")
public class LocalDeviceAddressInfoEntity {
    @Column(name = "mac")
    private String mac;
    @Column(name = "local_address")
    private String localAddress;
    @Id
    @Column(name = "broadcast_service")
    private String broadcastService;
}
