package com.local.dashspybackend.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * BroadcastDiscoveryRespDTO
 */
@Data
@AllArgsConstructor
public class BroadcastDiscoveryRespDTO {

    public String LocalIp;

    public String Mac;

    public String Device;

}