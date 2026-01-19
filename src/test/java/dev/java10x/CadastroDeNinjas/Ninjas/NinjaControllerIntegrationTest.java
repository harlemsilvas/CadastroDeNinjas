package dev.java10x.CadastroDeNinjas.Ninjas;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.java10x.CadastroDeNinjas.Missoes.MissoesModel;
import dev.java10x.CadastroDeNinjas.Missoes.MissoesRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NinjaControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private NinjaRepository ninjaRepository;

    @Autowired
    private MissoesRepository missoesRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void beforeEach() {
        ninjaRepository.deleteAll();
        missoesRepository.deleteAll();
    }

    @AfterEach
    public void afterEach() {
        ninjaRepository.deleteAll();
        missoesRepository.deleteAll();
    }

    private String url(String path) {
        return "http://localhost:" + port + path;
    }

    @Test
    public void testPatchNinja_updateNameAndAge() throws Exception {
        // arrange
        NinjaModel ninja = new NinjaModel(null, "Naruto", "naruto@konoha", 17, null);
        ninja = ninjaRepository.save(ninja);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = objectMapper.writeValueAsString(Map.of("nome", "Kakashi", "idade", 35));
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        // act
        ResponseEntity<String> response = restTemplate.exchange(url("/ninjas/" + ninja.getId()), HttpMethod.PATCH, entity, String.class);

        // assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        NinjaModel updated = objectMapper.readValue(response.getBody(), NinjaModel.class);
        assertEquals("Kakashi", updated.getNome());
        assertEquals(35, updated.getIdade());
    }

    @Test
    public void testPatchNinja_missaoNotFound_shouldReturn400() throws Exception {
        NinjaModel ninja = new NinjaModel(null, "Naruto", "naruto@konoha", 17, null);
        ninja = ninjaRepository.save(ninja);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = objectMapper.writeValueAsString(Map.of("missoes", 9999));
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url("/ninjas/" + ninja.getId()), HttpMethod.PATCH, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Missão com id"));
    }

    @Test
    public void testPatchNinja_unknownField_shouldReturn400() throws Exception {
        NinjaModel ninja = new NinjaModel(null, "Naruto", "naruto@konoha", 17, null);
        ninja = ninjaRepository.save(ninja);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = objectMapper.writeValueAsString(Map.of("apelido", "Nino"));
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url("/ninjas/" + ninja.getId()), HttpMethod.PATCH, entity, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Campo(s) desconhecido(s)"));
    }

    @Test
    public void testPatchNinja_ninjaNotFound_shouldReturn404() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = objectMapper.writeValueAsString(Map.of("nome", "Kakashi"));
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url("/ninjas/99999"), HttpMethod.PATCH, entity, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Ninja não encontrado"));
    }

    @Test
    public void testPatchNinja_updateMissionById() throws Exception {
        MissoesModel missao = new MissoesModel(null, "Recuperar Pergaminho", "Alta", null);
        missao = missoesRepository.save(missao);

        NinjaModel ninja = new NinjaModel(null, "Sasuke", "sasuke@konoha", 19, null);
        ninja = ninjaRepository.save(ninja);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = objectMapper.writeValueAsString(Map.of("missoes", missao.getId()));
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url("/ninjas/" + ninja.getId()), HttpMethod.PATCH, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        NinjaModel updated = objectMapper.readValue(response.getBody(), NinjaModel.class);
        assertNotNull(updated.getMissoes());
        assertEquals(missao.getId(), updated.getMissoes().getId());
    }

    @Test
    public void testPatchNinja_updateMissionByObject() throws Exception {
        // cria missão e ninja
        MissoesModel missao = new MissoesModel(null, "Escoltar Convoy", "Média", null);
        missao = missoesRepository.save(missao);

        NinjaModel ninja = new NinjaModel(null, "Shikamaru", "shikamaru@konoha", 18, null);
        ninja = ninjaRepository.save(ninja);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // payload com objeto { "id": X }
        String body = objectMapper.writeValueAsString(Map.of("missoes", Map.of("id", missao.getId())));
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url("/ninjas/" + ninja.getId()), HttpMethod.PATCH, entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        NinjaModel updated = objectMapper.readValue(response.getBody(), NinjaModel.class);
        assertNotNull(updated.getMissoes());
        assertEquals(missao.getId(), updated.getMissoes().getId());
    }
}
