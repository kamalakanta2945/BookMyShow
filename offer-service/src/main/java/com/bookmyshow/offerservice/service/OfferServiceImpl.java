package com.bookmyshow.offerservice.service;

import com.bookmyshow.offerservice.dto.OfferDto;
import com.bookmyshow.offerservice.exception.ResourceNotFoundException;
import com.bookmyshow.offerservice.model.Offer;
import com.bookmyshow.offerservice.repository.OfferRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {

    private static final Logger log = LoggerFactory.getLogger(OfferServiceImpl.class);

    @Autowired
    private OfferRepository offerRepository;

    @Override
    @Cacheable(value = "allOffers", key = "{#pageable.pageNumber, #pageable.pageSize, #pageable.sort, #searchTerm}")
    public Page<Offer> getAllOffers(Pageable pageable, String searchTerm) {
        log.info("Fetching all offers for page: {}, size: {}, search: '{}'", pageable.getPageNumber(), pageable.getPageSize(), searchTerm);
        List<Offer> offers = offerRepository.findAll(pageable, searchTerm);
        int total = offerRepository.count(searchTerm);
        return new PageImpl<>(offers, pageable, total);
    }

    @Override
    @Cacheable(value = "offers", key = "#id")
    public Offer getOfferById(Long id) {
        log.info("Fetching offer with id: {}", id);
        return offerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found with id: " + id));
    }

    @Override
    @CacheEvict(value = {"offers", "allOffers"}, allEntries = true)
    public Offer createOffer(OfferDto offerDto) {
        Offer offer = mapToEntity(offerDto);
        Offer savedOffer = offerRepository.save(offer);
        log.info("Created new offer with id: {}", savedOffer.getId());
        return savedOffer;
    }

    @Override
    @CacheEvict(value = {"offers", "allOffers"}, allEntries = true)
    public Offer updateOffer(Long id, OfferDto offerDto) {
        Offer existingOffer = getOfferById(id); // This will throw ResourceNotFoundException if not found
        updateEntityFromDto(existingOffer, offerDto);
        offerRepository.update(existingOffer);
        log.info("Updated offer with id: {}", id);
        return existingOffer;
    }

    @Override
    @CacheEvict(value = {"offers", "allOffers"}, allEntries = true)
    public void deleteOffer(Long id) {
        if (offerRepository.deleteById(id) == 0) {
            throw new ResourceNotFoundException("Offer not found with id: " + id);
        }
        log.info("Deleted offer with id: {}", id);
    }

    @Override
    @CacheEvict(value = {"offers", "allOffers"}, allEntries = true)
    public void importOffersFromExcel(MultipartFile file) throws IOException {
        log.info("Starting Excel import for file: {}", file.getOriginalFilename());
        List<Offer> offers = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            // Skip header row
            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                Offer offer = new Offer();
                offer.setOfferCode(getCellValueAsString(currentRow.getCell(0)));
                offer.setDescription(getCellValueAsString(currentRow.getCell(1)));
                offer.setDiscountPercentage(getCellValueAsDouble(currentRow.getCell(2)));
                offer.setMaxDiscount(getCellValueAsDouble(currentRow.getCell(3)));
                offer.setExpiryDate(currentRow.getCell(4).getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                offer.setActive(currentRow.getCell(5).getBooleanCellValue());
                offers.add(offer);
            }
            offerRepository.saveAll(offers);
            log.info("Successfully imported {} offers from Excel.", offers.size());
        } catch (Exception e) {
            log.error("Failed to import offers from Excel file.", e);
            throw new RuntimeException("Failed to parse and import Excel file: " + e.getMessage());
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            default:
                return "";
        }
    }

    private Double getCellValueAsDouble(Cell cell) {
        if (cell == null) return 0.0;
        if (cell.getCellType() == Cell.CellType.NUMERIC) {
            return cell.getNumericCellValue();
        }
        return 0.0;
    }


    private Offer mapToEntity(OfferDto dto) {
        Offer offer = new Offer();
        offer.setOfferCode(dto.getOfferCode());
        offer.setDescription(dto.getDescription());
        offer.setDiscountPercentage(dto.getDiscountPercentage());
        offer.setMaxDiscount(dto.getMaxDiscount());
        offer.setExpiryDate(dto.getExpiryDate());
        offer.setActive(dto.isActive());
        return offer;
    }

    private void updateEntityFromDto(Offer offer, OfferDto dto) {
        offer.setOfferCode(dto.getOfferCode());
        offer.setDescription(dto.getDescription());
        offer.setDiscountPercentage(dto.getDiscountPercentage());
        offer.setMaxDiscount(dto.getMaxDiscount());
        offer.setExpiryDate(dto.getExpiryDate());
        offer.setActive(dto.isActive());
    }
}