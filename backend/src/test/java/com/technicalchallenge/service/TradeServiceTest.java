package com.technicalchallenge.service;

import com.technicalchallenge.dto.TradeDTO;
import com.technicalchallenge.dto.TradeLegDTO;
import com.technicalchallenge.model.Book;
import com.technicalchallenge.model.Counterparty;
import com.technicalchallenge.model.Trade;
import com.technicalchallenge.model.TradeLeg;
import com.technicalchallenge.model.TradeStatus;
import com.technicalchallenge.repository.CashflowRepository;
import com.technicalchallenge.repository.CounterpartyRepository;
import com.technicalchallenge.repository.TradeLegRepository;
import com.technicalchallenge.repository.TradeRepository;
import com.technicalchallenge.repository.TradeStatusRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.technicalchallenge.repository.BookRepository;

@ExtendWith(MockitoExtension.class)
class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @Mock
    private TradeLegRepository tradeLegRepository;

    @Mock
    private CashflowRepository cashflowRepository;

    @Mock
    private TradeStatusRepository tradeStatusRepository;

    @Mock
    private AdditionalInfoService additionalInfoService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CounterpartyRepository counterpartyRepository;

    @InjectMocks
    private TradeService tradeService;

    private TradeDTO tradeDTO;
    private Trade fakeTrade;

    @BeforeEach
    void tradeDTOsetUp() {
        // Set up test data
        tradeDTO = new TradeDTO();
        tradeDTO.setTradeId(100001L);
        tradeDTO.setTradeDate(LocalDate.of(2025, 1, 15));
        tradeDTO.setTradeStartDate(LocalDate.of(2025, 1, 17));
        tradeDTO.setTradeMaturityDate(LocalDate.of(2026, 1, 17));
        tradeDTO.setBookName("NewBook");
        tradeDTO.setCounterpartyName("NewCounterparty");

        TradeLegDTO leg1 = new TradeLegDTO();
        leg1.setNotional(BigDecimal.valueOf(1000000));
        leg1.setRate(0.05);

        TradeLegDTO leg2 = new TradeLegDTO();
        leg2.setNotional(BigDecimal.valueOf(1000000));
        leg2.setRate(0.0);

        tradeDTO.setTradeLegs(Arrays.asList(leg1, leg2));
        
    }
    @BeforeEach
    void fakeTradesetUp() {
        fakeTrade = new Trade();
        fakeTrade.setId(1L);
        fakeTrade.setTradeId(100001L);
        fakeTrade.setVersion(1);
        fakeTrade.setTradeId(100001L);
        fakeTrade.setTradeDate(LocalDate.of(2025, 1, 15));
        fakeTrade.setTradeStartDate(LocalDate.of(2025, 1, 17));
        fakeTrade.setTradeMaturityDate(LocalDate.of(2026, 1, 17));
     /*    fakeTrade.setBookName("NewBook");
        fakeTrade.setCounterpartyName("NewCounterparty");
 */
        TradeLeg leg1 = new TradeLeg();
        leg1.setNotional(BigDecimal.valueOf(1000000));
        leg1.setRate(0.05);

        TradeLeg leg2 = new TradeLeg();
        leg2.setNotional(BigDecimal.valueOf(1000000));
        leg2.setRate(0.0);

        fakeTrade.setTradeLegs(Arrays.asList(leg1, leg2));

    }


    @Test
    void testCreateTrade_Success() {

        //setup mocks        
        Optional <Book> newBook = Optional.of(new Book());
        when(bookRepository.findByBookName(any(String.class))).thenReturn(newBook);
        Optional <Counterparty> newCounterparty = Optional.of(new Counterparty());
        when(counterpartyRepository.findByName(any(String.class))).thenReturn(newCounterparty);
        Optional <TradeStatus> newTradeStatus = Optional.of(new TradeStatus());
        when(tradeStatusRepository.findByTradeStatus(any(String.class))).thenReturn(newTradeStatus);

        // Given
        when(tradeRepository.save(any(Trade.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(tradeLegRepository.save(any(TradeLeg.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        Trade result = tradeService.createTrade(tradeDTO);

        // Then
        assertNotNull(result);
        assertEquals(100001L, result.getTradeId());
        verify(tradeRepository).save(any(Trade.class));
    }

    @Test
    void testCreateTrade_InvalidDates_ShouldFail() {
        // Given - This test is intentionally failing for candidates to fix
        tradeDTO.setTradeStartDate(LocalDate.of(2025, 1, 10)); // Before trade date

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            tradeService.createTrade(tradeDTO);
        });

        // This assertion is intentionally wrong - candidates need to fix it
        // assertEquals("Wrong error message", exception.getMessage());
        assertEquals("Start date cannot be before trade date", exception.getMessage());
    }

    @Test
    void testCreateTrade_InvalidLegCount_ShouldFail() {
        // Given
        tradeDTO.setTradeLegs(Arrays.asList(new TradeLegDTO())); // Only 1 leg

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            tradeService.createTrade(tradeDTO);
        });

        assertTrue(exception.getMessage().contains("exactly 2 legs"));
    }

    @Test
    void testGetTradeById_Found() {
        // Given
        when(tradeRepository.findByTradeIdAndActiveTrue(100001L)).thenReturn(Optional.of(fakeTrade));

        // When
        Optional<Trade> result = tradeService.getTradeById(100001L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(100001L, result.get().getTradeId());
    }

    @Test
    void testGetTradeById_NotFound() {
        // Given
        when(tradeRepository.findByTradeIdAndActiveTrue(999L)).thenReturn(Optional.empty());

        // When
        Optional<Trade> result = tradeService.getTradeById(999L);

        // Then
        assertFalse(result.isPresent());
    }

    @Test
    void testAmendTrade_Success() {
        // Given
        when(tradeRepository.findByTradeIdAndActiveTrue(100001L)).thenReturn(Optional.of(fakeTrade));
        Optional <TradeStatus> newTradeStatus = Optional.of(new TradeStatus());
        when(tradeStatusRepository.findByTradeStatus(any(String.class))).thenReturn(newTradeStatus);
        when(tradeRepository.save(any(Trade.class))).thenReturn(fakeTrade);

        // When
        Trade result = tradeService.amendTrade(100001L, tradeDTO);

        // Then
        assertNotNull(result);
        verify(tradeRepository, times(2)).save(any(Trade.class)); // Save old and new
    }

    @Test
    void testAmendTrade_TradeNotFound() {
        // Given
        when(tradeRepository.findByTradeIdAndActiveTrue(999L)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            tradeService.amendTrade(999L, tradeDTO);
        });

        assertTrue(exception.getMessage().contains("Trade not found"));
    }

    // This test has a deliberate bug for candidates to find and fix
    @Test
    void testCashflowGeneration_MonthlySchedule() {

    //Given     
        Optional <Book> newBook = Optional.of(new Book());
        when(bookRepository.findByBookName(any(String.class))).thenReturn(newBook);
        Optional <Counterparty> newCounterparty = Optional.of(new Counterparty());
        when(counterpartyRepository.findByName(any(String.class))).thenReturn(newCounterparty);
        Optional <TradeStatus> newTradeStatus = Optional.of(new TradeStatus());
        when(tradeStatusRepository.findByTradeStatus(any(String.class))).thenReturn(newTradeStatus);
        when(tradeRepository.save(any(Trade.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(tradeLegRepository.save(any(TradeLeg.class))).thenAnswer(invocation -> invocation.getArgument(0));
        
        // When - method call is missing
        Trade result = tradeService.createTrade(tradeDTO);
        // Then - assertions are wrong/missing
        int numberOfCashflows = 0;
        for (TradeLeg tradeleg : result.getTradeLegs()) {
            numberOfCashflows += tradeleg.getCashflows().size();
        }

        assertEquals(12, numberOfCashflows); // This will always fail - candidates need to fix
    }

}
