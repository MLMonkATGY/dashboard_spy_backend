package com.local.dashspybackend.Entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.Getter;
@Data
@Entity
@Table(name = "room_light_states")
public class LightStateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int seq;
    @Column(name="created_at", columnDefinition = "TIMESTAMP", updatable = false)
    private LocalDateTime createAt;
    private boolean lightState;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "device_id")

    @JsonIgnoreProperties("states")
    private DeviceInfoEntity device;
  
    @PrePersist
    public void setCreationDateTime() {
      this.createAt = LocalDateTime.now();
    }
    public boolean getLightState(){
      return this.lightState;
    }

}
