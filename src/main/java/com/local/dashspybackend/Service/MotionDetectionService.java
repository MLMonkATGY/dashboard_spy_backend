package com.local.dashspybackend.Service;

import java.net.URI;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import com.local.dashspybackend.DTO.IpCameraSensorsDTO;
import com.local.dashspybackend.Singleton.MockCacheData;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersSpec;

@Service
public class MotionDetectionService {
    @Autowired
    private LightStateService lightStateService;

    public class MotionActive {
        public String unit;
        public List<List<Object>> data;
    }

    public class Root {
        public MotionActive motion_active;
    }

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private MockCacheData cacheData;

    @Scheduled(fixedRate = 100000)
    public void reportCurrentTime() {
        Timestamp currentTimeStamp = new Timestamp(System.currentTimeMillis());
        long currentAttemptTime = currentTimeStamp.getTime() - 100;
        String absUri = String.format("http://192.168.1.3:8080/sensors.json?from=%s&sense=motion_active",
                currentAttemptTime);
        RequestHeadersSpec<?> requestSpec2 = WebClient.create().get().uri(URI.create(absUri));
        String rawResp = requestSpec2.exchange().block().bodyToMono(String.class).block();
        try {
            JSONObject jsonResp = new JSONObject(rawResp);
            JSONObject a = (JSONObject) jsonResp.get("motion_active");
            JSONArray b = (JSONArray) a.get("data");
            JSONArray c = (JSONArray) b.get(b.length() - 1);
            String stateInStr = ((JSONArray) c.get(1)).get(0).toString();
            boolean latestState = Math.round(Double.parseDouble(stateInStr)) == 1;

            if (latestState) {
                // toggle
                System.out.println(latestState);
                lightStateService.toggleLightSwitchAuto("room_light");
            }

        } catch (JSONException e) {
            // TODO: handle exception
            System.out.println(e.getMessage());

        }

    }
}
