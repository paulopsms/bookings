package com.paulopsms.controller;

import com.paulopsms.domain.entity.Booking;
import com.paulopsms.service.BookingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class BookingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private BookingService bookingService;

    @Autowired
    private BookingController controller;

    static List<Booking> bookings;

    @BeforeAll
    public static void init() {
        bookings = new ArrayList<>();
    }

    @Test
    void testWithMockMvc() throws Exception {
        when(bookingService.listBookings()).thenReturn(bookings);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/bookings"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
//                .andExpect(content().string(""));

        assertEquals("Bookings in construction...", response.getContentAsString());
    }

    @Test
    public void assertThatControllerIsNotNull() {
        assertNotNull(controller);
    }
}
