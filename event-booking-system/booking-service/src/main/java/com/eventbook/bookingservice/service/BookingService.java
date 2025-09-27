package com.eventbook.bookingservice.service;

import com.eventbook.bookingservice.dto.BookingRequest;
import com.eventbook.bookingservice.model.Booking;
import com.eventbook.bookingservice.model.Ticket;
import com.eventbook.bookingservice.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Booking> findAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> findBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    @Transactional
    public Booking createBooking(BookingRequest bookingRequest, Long userId) {
        // In a real microservices environment, you would first call the
        // event/theatre service to validate the showtimeId and seat availability.
        // For now, we'll assume the request is valid.

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setShowtimeId(bookingRequest.getShowtimeId());
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus("PENDING"); // Status becomes CONFIRMED after successful payment

        BigDecimal totalPrice = bookingRequest.getPricePerSeat().multiply(new BigDecimal(bookingRequest.getSeatNumbers().size()));
        booking.setTotalPrice(totalPrice);

        List<Ticket> tickets = bookingRequest.getSeatNumbers().stream().map(seatNumber -> {
            Ticket ticket = new Ticket();
            ticket.setSeatNumber(seatNumber);
            ticket.setQrCode("QR_CODE_PLACEHOLDER"); // QR code would be generated here
            ticket.setBooking(booking);
            return ticket;
        }).collect(Collectors.toList());

        booking.setTickets(tickets);

        return bookingRepository.save(booking);
    }

    public Booking saveBooking(Booking booking) {
        return bookingRepository.save(booking);
    }
}