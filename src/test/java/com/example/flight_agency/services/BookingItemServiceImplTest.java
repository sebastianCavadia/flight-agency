package com.example.flight_agency.services;

 import com.example.flight_agency.api.dto.BookingItemDtos;
 import com.example.flight_agency.api.dto.FlightDtos;
 import com.example.flight_agency.domine.entities.BookingItem;
 import com.example.flight_agency.domine.entities.Cabin;
 import com.example.flight_agency.domine.entities.Flight;
 import com.example.flight_agency.domine.repositories.BookingItemRepository;
 import com.example.flight_agency.domine.repositories.FlightRepository;
 import com.example.flight_agency.services.mappers.BookingItemMapper;
 import org.junit.jupiter.api.Test;
 import org.junit.jupiter.api.extension.ExtendWith;
 import org.mockito.InjectMocks;
 import org.mockito.Mock;
 import org.mockito.junit.jupiter.MockitoExtension;

 import java.math.BigDecimal;
 import java.time.OffsetDateTime;
 import java.util.Optional;

 import static org.junit.jupiter.api.Assertions.*;
 import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingItemServiceImplTest {
    @Mock
    private BookingItemRepository bookingItemRepository;
    @Mock
    private FlightRepository flightRepository;
    @Mock
    private BookingItemMapper mapper;
    @InjectMocks
    private BookingItemServiceImpl service;
/*
    @BeforeEach
    void setUp() {
        bookingItemRepository = mock(BookingItemRepository.class);
        flightRepository = mock(FlightRepository.class);
        mapper = Mappers.getMapper(BookingItemMapper.class);
        service = new BookingItemServiceImpl(bookingItemRepository, flightRepository, mapper);
    }
*/
    @Test
    void testCreateBookingItem() {
        Flight flight = new Flight();
        flight.setId(1L);

        BookingItem bookingItem = new BookingItem();
        bookingItem.setCabin(Cabin.ECONOMY);
        bookingItem.setPrice(new BigDecimal("150.00"));
        bookingItem.setSegmentOrder(1);
        bookingItem.setFlight(flight);
        BookingItemDtos.BookingItemCreateRequest request =
                new BookingItemDtos.BookingItemCreateRequest(
                Cabin.ECONOMY, new BigDecimal("150.00"), 1, 1L
        );
        FlightDtos.FlightResponseBasic flightResponseBasic =
                new FlightDtos.FlightResponseBasic(
                        1L, "AA100",
                        OffsetDateTime.now(),
                        OffsetDateTime.now().plusHours(5));

        BookingItemDtos.BookingItemResponse response =
                new BookingItemDtos.BookingItemResponse(
                        99L,Cabin.ECONOMY,
                        new BigDecimal("150.00"),
                        1,flightResponseBasic);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(mapper.toEntity(request, flight)).thenReturn(bookingItem);
        when(bookingItemRepository.save(any(BookingItem.class)))
                .thenAnswer(invocation -> {
                    BookingItem item = invocation.getArgument(0);
                    item.setId(99L);
                    return item;
                });


        when(mapper.toResponse(bookingItem)).thenReturn(response);

        BookingItemDtos.BookingItemResponse responseActual = service.create(request);

        assertNotNull(responseActual);
        assertEquals(99L,responseActual.id());
        assertEquals(Cabin.ECONOMY, responseActual.cabin());
        assertEquals(new BigDecimal("150.00"), responseActual.price());
        assertEquals(1L,responseActual.flight().id());

        verify(flightRepository, times(1)).findById(1L);
        verify(mapper, times(1)).toResponse(bookingItem);
    }
}