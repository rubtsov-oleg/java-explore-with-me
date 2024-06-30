package ru.practicum.explorewithme.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.explorewithme.dto.HitDTO;
import ru.practicum.explorewithme.dto.StatsDTO;

import java.time.LocalDateTime;
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

    public List<StatsDTO> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url + "/stats")
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("uris", String.join(",", uris))
                .queryParam("unique", unique);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<List<StatsDTO>> response = restTemplate.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<StatsDTO>>() {
                });

        log.info("Получен ответ GET для статистики с кодом статуса: {}", response.getStatusCode());
        return response.getBody();
    }
}
