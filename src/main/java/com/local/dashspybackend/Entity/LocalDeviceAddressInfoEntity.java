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
    @Id
    @Column(name = "mac")
    private String mac;
    @Column(name = "local_address")
    private String localAddress;
    @Column(name = "broadcast_service")
    private String broadcastService;
}
