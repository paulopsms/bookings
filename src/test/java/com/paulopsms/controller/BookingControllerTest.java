package com.paulopsms.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.paulopsms.domain.model.Booking;
import com.paulopsms.domain.model.DateRange;
import com.paulopsms.domain.model.Property;
import com.paulopsms.dto.CreateBookingDto;
import com.paulopsms.service.BookingService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BookingService bookingService;

    @Autowired
    private BookingController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static List<Booking> bookings;
    private static CreateBookingDto bookingDto;
    private static Booking booking;
    private static Booking createdBooking;
    private static Property property;
    private static Property createdBookingProperty;
    private static DateRange dateRange;

    @BeforeAll
    public static void init() {
        bookings = new ArrayList<>();
        dateRange = new DateRange(LocalDate.parse("2025-04-21"), LocalDate.parse("2025-04-30"));
        bookingDto = CreateBookingDto.builder().build();
        property = Property.builder().id(1L).name("Casa na Praia").numberOfGuests(2).bookings(bookings).basePricePerNight(new BigDecimal("100.00")).build();
        createdBookingProperty = Property.builder().id(1L).name("Casa na Praia").numberOfGuests(2).bookings(new ArrayList<>()).basePricePerNight(new BigDecimal("100.00")).build();
        booking = Booking.builder().id(1L).property(property).numberOfGuests(2).dateRange(dateRange).build();
        createdBooking = Booking.builder().id(1L).property(createdBookingProperty).numberOfGuests(2).dateRange(dateRange).build();
    }

    @Test
    public void assertThatControllerIsNotNull() {
        assertNotNull(controller);
    }

    @Test
    void givenABookingResource_whenListingBookings_thenShouldReturnAnEmptytList() throws Exception {
        when(bookingService.listBookings()).thenReturn(bookings);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/bookings"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        String contentAsString = response.getContentAsString();
        List<Booking> bookings = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });

        assertEquals(200, response.getStatus());
        assertFalse(bookings.isEmpty());
        assertEquals(1, bookings.size());
    }

    @Test
    public void givenABookingResource_whenCreatingNewBooking_thenShouldReturnCreatedBookingBooking() throws Exception {
        when(bookingService.createBooking(Mockito.any(CreateBookingDto.class))).thenReturn(createdBooking);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/bookings")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(bookingDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();

        String contentAsString = response.getContentAsString();
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        Booking createdBooking = objectMapper.readValue(contentAsString, new TypeReference<Booking>() {
        });

        assertEquals(201, response.getStatus());
        assertNotNull(createdBooking);
        assertEquals(1, createdBooking.getId());
        assertEquals(2, createdBooking.getNumberOfGuests());
        assertEquals(LocalDate.parse("2025-04-21"), createdBooking.getDateRange().getStartDate());
        assertEquals(LocalDate.parse("2025-04-30"), createdBooking.getDateRange().getEndDate());
        assertEquals(new BigDecimal("100.00"), createdBooking.getProperty().getBasePricePerNight());
        assertEquals(2, createdBooking.getProperty().getNumberOfGuests());
    }

    @Test
    public void givenBookingId_whenFindingBooking_thenShouldReturnBookingById() throws Exception {
        when(bookingService.findBookingById(1L)).thenReturn(booking);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/bookings/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        String contentAsString = response.getContentAsString();
        Booking foundBooking = objectMapper.readValue(contentAsString, new TypeReference<Booking>() {
        });

        assertEquals(200, response.getStatus());
        assertNotNull(foundBooking);
        assertEquals(1, foundBooking.getId());
        assertEquals(2, foundBooking.getNumberOfGuests());
        assertEquals(LocalDate.parse("2025-04-21"), foundBooking.getDateRange().getStartDate());
        assertEquals(LocalDate.parse("2025-04-30"), foundBooking.getDateRange().getEndDate());
        assertEquals(new BigDecimal("100.00"), foundBooking.getProperty().getBasePricePerNight());
        assertEquals(2, foundBooking.getProperty().getNumberOfGuests());
    }


    @Test
    public void givenBookingId_whenCancellingBooking_thenBookingShouldBeCancelled() throws Exception {
        Mockito.doNothing().when(bookingService).cancelBooking(1L);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put("/bookings/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertEquals(200, response.getStatus());
        Mockito.verify(bookingService, Mockito.times(1)).cancelBooking(1L);
    }
}
