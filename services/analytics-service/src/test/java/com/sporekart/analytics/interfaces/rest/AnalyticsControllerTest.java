package com.sporekart.analytics.interfaces.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldExposeDashboardReportAndSeoEndpoints() throws Exception {
        mockMvc.perform(get("/analytics/dashboard"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasKey("revenueToday")));

        mockMvc.perform(post("/analytics/widgets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Revenue\",\"metric\":\"sales\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Revenue"));

        mockMvc.perform(post("/reports/export")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"reportType\":\"sales\",\"format\":\"pdf\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.reportType").value("sales"));

        mockMvc.perform(post("/seo/metadata")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"path\":\"/products\",\"title\":\"Products\",\"description\":\"Explore products\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.path").value("/products"));

        mockMvc.perform(get("/seo/sitemap"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/reports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
