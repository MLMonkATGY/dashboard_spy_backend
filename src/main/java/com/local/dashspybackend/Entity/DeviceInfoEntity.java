package com.local.dashspybackend.Entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import lombok.Data;
@Data
@Entity
@Table(name = "devices_info_table")
public class DeviceInfoEntity {
    @Id
    @Column(name="device_id")
    private String deviceId;
    @Column(name="device_name")
    private String deviceName;
    @Column(name="created_at", columnDefinition = "TIMESTAMP", updatable = false)
    private LocalDateTime createAt;
    @Column(name="local_address")
    private String localAddress;
    @Column(name="api_key")
    private String apiKey;
    @Column(name="initialization_vector_on")
    private String initializationVectorOn;
    @Column(name="initialization_vector_off")
    private String initializationVectorOff;
    @Column(name="encrypted_payload_on")
    private String encryptedPayloadOn;
    @Column(name="encrypted_payload_off")
    private String encryptedPayloadOff;
    @Column(name="self_api")
    private String selfApi;
    @Column(name="switch_state")
    private boolean switchState;
    @OneToMany(mappedBy = "device", fetch = FetchType.EAGER,
    cascade = CascadeType.ALL)
    @JsonIgnoreProperties("device")
    private List<LightStateEntity> states;
    @PrePersist
    public void setCreationDateTime() {
      this.createAt = LocalDateTime.now();
    }
    public boolean getSwitchState(){
      return this.switchState;
    }
}
