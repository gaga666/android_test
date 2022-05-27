package com.example.charge;

import static org.junit.Assert.assertEquals;

import com.example.charge.entity.MessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testJsonParser() throws JsonProcessingException {
        String json = "{\"code\": 0, \"message\": \"Success\"}";
        ObjectMapper mapper = new ObjectMapper();
        MessageResponse res = mapper.readValue(json, MessageResponse.class);
        System.out.println(res.getCode());
        System.out.println(res);
    }
}