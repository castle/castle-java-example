package io.castle.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CastleExampleApplicationTests {

    // Matches the castle_api_secret injected for the test JVM (see the
    // surefire environmentVariables configuration in pom.xml).
    private static final String API_SECRET = "test_secret";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    @Test
    void homePageRenders() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test
    void webhookRejectsInvalidSignature() throws Exception {
        mockMvc.perform(post("/webhooks/castle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Castle-Signature", "not-a-valid-signature")
                        .content("{\"type\":\"$test\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void webhookAcceptsValidSignature() throws Exception {
        byte[] body = "{\"type\":\"$test\"}".getBytes(StandardCharsets.UTF_8);
        mockMvc.perform(post("/webhooks/castle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Castle-Signature", sign(body))
                        .content(body))
                .andExpect(status().isOk());
    }

    private static String sign(byte[] body) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(API_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        return Base64.getEncoder().encodeToString(mac.doFinal(body));
    }
}
