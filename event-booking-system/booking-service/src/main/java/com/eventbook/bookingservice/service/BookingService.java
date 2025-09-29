package com.eventbook.bookingservice.service;

import com.eventbook.bookingservice.dto.BookingRequest;
import com.eventbook.bookingservice.model.Booking;
import com.eventbook.bookingservice.model.Ticket;
import com.eventbook.bookingservice.repository.BookingRepository;
import com.eventbook.common.dto.BookingDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Page<Booking> findAllBookings(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }

    public Optional<Booking> findBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Transactional
    public Booking createBooking(BookingRequest bookingRequest, Long userId) {
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setShowtimeId(bookingRequest.getShowtimeId());
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus("PENDING");

        BigDecimal totalPrice = bookingRequest.getPricePerSeat().multiply(new BigDecimal(bookingRequest.getSeatNumbers().size()));
        booking.setTotalPrice(totalPrice);

        List<Ticket> tickets = bookingRequest.getSeatNumbers().stream().map(seatNumber -> {
            Ticket ticket = new Ticket();
            ticket.setSeatNumber(seatNumber);
            ticket.setQrCode("QR_CODE_PLACEHOLDER");
            ticket.setBooking(booking);
            return ticket;
        }).collect(Collectors.toList());

        booking.setTickets(tickets);

        return bookingRepository.save(booking);
    }

    public List<BookingDTO> findAllForReport() {
        return bookingRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private BookingDTO convertToDto(Booking booking) {
        return new BookingDTO(
                booking.getId(),
                booking.getUserId(),
                booking.getShowtimeId(),
                booking.getBookingTime(),
                booking.getTotalPrice(),
                booking.getStatus()
        );
    }
}