package com.local.dashspybackend.DTO;

import lombok.Data;

@Data
public class SonoffSwitchReqDTO {
   
      private String sequence;
      private String deviceid;
      private String selfApikey;
      private String iv;
      private String data;
      private boolean encrypt;

}
