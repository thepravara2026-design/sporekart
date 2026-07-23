package com.sporekart.marketing.copilot.engine;

import com.sporekart.marketing.copilot.dto.SocialMediaRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SocialMediaEngineTest {

    @InjectMocks
    private SocialMediaEngine socialMediaEngine;

    @Test
    void testGeneratePost() {
        var request = new SocialMediaRequest("instagram", "Mushroom Growing Tips", "IMAGE", "professional",
            List.of("#gardening", "#mushrooms"), "Learn More", null);
        var response = socialMediaEngine.generatePost(request);
        assertNotNull(response);
        assertEquals("instagram", response.platform());
        assertFalse(response.content().isEmpty());
        assertFalse(response.hashtags().isEmpty());
        assertEquals("IMAGE", response.postType());
        assertFalse(response.bestPostingTimes().isEmpty());
    }

    @Test
    void testGeneratePostForLinkedIn() {
        var request = new SocialMediaRequest("linkedin", "Commercial Mushroom Farming", "VIDEO", "professional",
            List.of(), null, "camp-1");
        var response = socialMediaEngine.generatePost(request);
        assertNotNull(response);
        assertEquals("linkedin", response.platform());
    }

    @Test
    void testCreatePost() {
        var post = socialMediaEngine.createPost("instagram", "Mushroom Tips", "camp-1");
        assertNotNull(post);
        assertEquals("instagram", post.platform());
        assertNotNull(post.campaignId());
    }

    @Test
    void testGenerateHashtags() {
        var hashtags = socialMediaEngine.generateHashtags("MushroomFarming", List.of("organic", "sustainable"));
        assertTrue(hashtags.contains("#MushroomFarming"));
        assertTrue(hashtags.contains("#organic"));
        assertTrue(hashtags.contains("#sustainable"));
    }

    @Test
    void testGenerateHashtagsWithoutCustom() {
        var hashtags = socialMediaEngine.generateHashtags("Mushrooms", null);
        assertTrue(hashtags.contains("#mushrooms"));
        assertTrue(hashtags.contains("#SporeKart"));
    }

    @Test
    void testAnalyzeBestTimes() {
        var result = socialMediaEngine.analyzeBestTimes("instagram");
        assertNotNull(result.get("platform"));
        assertNotNull(result.get("bestTimes"));
    }

    @Test
    void testAnalyzeBestTimesUnknown() {
        var result = socialMediaEngine.analyzeBestTimes("unknown");
        assertNotNull(result.get("bestTimes"));
        var times = (List<String>) result.get("bestTimes");
        assertEquals(1, times.size());
    }

    @Test
    void testGenerateContentCalendar() {
        var calendar = socialMediaEngine.generateContentCalendar("camp-1", LocalDateTime.now(), 7);
        assertEquals(7, calendar.size());
        assertTrue(calendar.get(0).contains("Day 1"));
    }
}
