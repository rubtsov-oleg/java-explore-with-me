package ru.practicum.explorewithme.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.explorewithme.dto.HitDTO;
import ru.practicum.explorewithme.dto.StatsDTO;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class StatsClient {
    private final String url;
    private final RestTemplate restTemplate;

    public StatsClient(String url) {
        this.url = url;
        this.restTemplate = new RestTemplate();
    }

    public void saveHit(HitDTO hitDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<HitDTO> request = new HttpEntity<>(hitDTO, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url + "/hit", request, String.class);
        log.info("Response: " + response.getStatusCode());
        log.info("Body: " + response.getBody());
    }

    public List<StatsDTO> getStats(String start, String end, List<String> uris, boolean unique) {
        StringBuilder uri = new StringBuilder(String.format("/stats?start=%s&end=%s&unique=%s", start, end, unique));
        for (String findUri : uris) {
            uri.append("&uris=").append(findUri);
        }
        try {
            ResponseEntity<StatsDTO[]> response = restTemplate.getForEntity(url + uri, StatsDTO[].class);

            return (response.getBody() != null) ? Arrays.asList(response.getBody()) : Collections.emptyList();
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException();
        }
    }
}
