package com.example.flight_agency.services;

 import com.example.flight_agency.api.dto.BookingItemDtos;
 import com.example.flight_agency.domine.entities.BookingItem;
 import com.example.flight_agency.domine.entities.Cabin;
 import com.example.flight_agency.domine.entities.Flight;
 import com.example.flight_agency.domine.repositories.BookingItemRepository;
 import com.example.flight_agency.domine.repositories.FlightRepository;
 import com.example.flight_agency.services.mappers.BookingItemMapper;
 import org.junit.jupiter.api.BeforeEach;
 import org.junit.jupiter.api.Test;
 import org.mapstruct.factory.Mappers;

 import java.math.BigDecimal;
 import java.util.Optional;

 import static org.junit.jupiter.api.Assertions.*;
 import static org.mockito.Mockito.*;

class BookingItemServiceImplTest {

    private BookingItemRepository bookingItemRepository;
    private FlightRepository flightRepository;
    private BookingItemMapper mapper;
    private BookingItemService service;

    @BeforeEach
    void setUp() {
        bookingItemRepository = mock(BookingItemRepository.class);
        flightRepository = mock(FlightRepository.class);
        mapper = Mappers.getMapper(BookingItemMapper.class);
        service = new BookingItemServiceImpl(bookingItemRepository, flightRepository, mapper);
    }

    @Test
    void testCreateBookingItem() {
        Flight flight = new Flight();
        flight.setId(1L);

        BookingItemDtos.BookingItemCreateRequest request = new BookingItemDtos.BookingItemCreateRequest(
                Cabin.ECONOMY, new BigDecimal("150.00"), 1, 1L
        );

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(bookingItemRepository.save(any(BookingItem.class)))
                .thenAnswer(invocation -> {
                    BookingItem item = invocation.getArgument(0);
                    item.setId(99L);
                    return item;
                });

        BookingItemDtos.BookingItemResponse response = service.create(request);

        assertNotNull(response);
        assertEquals(Cabin.ECONOMY, response.cabin());
        assertEquals(new BigDecimal("150.00"), response.price());
        verify(bookingItemRepository, times(1)).save(any(BookingItem.class));
    }
}