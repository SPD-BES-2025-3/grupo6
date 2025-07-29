package br.com.ufg.odm.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class GsonConfig {

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, context) -> {
                    String dateString = json.getAsString();
                    try {
                        return LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    } catch (Exception e1) {
                        try {
                            return LocalDateTime.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                        } catch (Exception e2) {
                            try {
                                return LocalDateTime.parse(dateString);
                            } catch (Exception e3) {
                                throw new RuntimeException("Não foi possível parsear a data: " + dateString, e3);
                            }
                        }
                    }
                })
                .create();
    }
}
