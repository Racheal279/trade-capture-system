package com.technicalchallenge.service;

import com.technicalchallenge.dto.TradeDTO;
import com.technicalchallenge.dto.TradeLegDTO;
import com.technicalchallenge.model.Book;
import com.technicalchallenge.model.Counterparty;
import com.technicalchallenge.model.LegType;
import com.technicalchallenge.model.Schedule;
import com.technicalchallenge.model.Trade;
import com.technicalchallenge.model.TradeLeg;
import com.technicalchallenge.model.TradeStatus;
import com.technicalchallenge.repository.CashflowRepository;
import com.technicalchallenge.repository.CounterpartyRepository;
import com.technicalchallenge.repository.LegTypeRepository;
import com.technicalchallenge.repository.ScheduleRepository;
import com.technicalchallenge.repository.TradeLegRepository;
import com.technicalchallenge.repository.TradeRepository;
import com.technicalchallenge.repository.TradeStatusRepository;
import com.technicalchallenge.mapper.TradeMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private LegTypeRepository legTypeRepository;

    @Mock
    private TradeMapper tradeMapper;

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

        TradeLegDTO dtoLeg1 = new TradeLegDTO();
        dtoLeg1.setNotional(BigDecimal.valueOf(1000000));
        dtoLeg1.setRate(0.05);
        Schedule monthlySchedule3 = new Schedule();
        monthlySchedule3.setSchedule("Monthly"); 
        dtoLeg1.setCalculationPeriodSchedule("Monthly");
        dtoLeg1.setLegType("Fixed"); 
        

        TradeLegDTO dtoLeg2 = new TradeLegDTO();
        dtoLeg2.setNotional(BigDecimal.valueOf(1000000));
        dtoLeg2.setRate(0.0);
        Schedule monthlySchedule4 = new Schedule();
        monthlySchedule4.setSchedule("Monthly"); 
        dtoLeg2.setCalculationPeriodSchedule("Monthly");
        dtoLeg2.setLegType("Floating"); // for cashflow
        

        tradeDTO.setTradeLegs(Arrays.asList(dtoLeg1, dtoLeg2));
        
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
        TradeLeg entityLeg1 = new TradeLeg();
        Schedule monthlySchedule1 = new Schedule();
        monthlySchedule1.setSchedule("Monthly"); 
        entityLeg1.setCashflows(new ArrayList<>()); 
        entityLeg1.setCalculationPeriodSchedule(monthlySchedule1);
        entityLeg1.setNotional(BigDecimal.valueOf(1000000));
        entityLeg1.setRate(0.05);

        TradeLeg entityLeg2 = new TradeLeg();
        Schedule monthlySchedule2 = new Schedule();
        monthlySchedule2.setSchedule("Monthly"); 
        entityLeg2.setCashflows(new ArrayList<>()); 
        entityLeg2 .setCalculationPeriodSchedule(monthlySchedule2);
        entityLeg2 .setNotional(BigDecimal.valueOf(1000000));
        entityLeg2 .setRate(0.0);

        fakeTrade.setTradeLegs(Arrays.asList(entityLeg1, entityLeg2));

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
        Optional <Schedule> newSchedule = Optional.of(new Schedule());
        when(scheduleRepository.findBySchedule(any(String.class))).thenReturn(newSchedule);


        // Given
        when(tradeRepository.save(any(Trade.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(tradeLegRepository.save(any(TradeLeg.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(legTypeRepository.findByType(anyString())).thenReturn(Optional.of(new LegType()));

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
        Optional <Schedule> newSchedule = Optional.of(new Schedule());
        when(scheduleRepository.findBySchedule(any(String.class))).thenReturn(newSchedule);


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
        Schedule schedule1m = new Schedule();
        schedule1m.setId(1L);
        schedule1m.setSchedule("1M");
        when(scheduleRepository.findBySchedule(any(String.class))).thenReturn(Optional.of(schedule1m));
    

        
        // When - method call is missing
        Trade result = tradeService.createTrade(tradeDTO);
        // Then - assertions are wrong/missing
        assertNotNull(result);
        assertNotNull(result.getTradeLegs());
        int numberOfCashflows = 0;
        for (TradeLeg tradeleg : result.getTradeLegs()) {
            if (tradeleg.getCashflows() != null){
                numberOfCashflows += tradeleg.getCashflows().size();
        }
    }
        assertEquals(24, numberOfCashflows); // This will always fail - candidates need to fix
    }

   @Test
    void testsearchTrades(){
    String searchCounterparty = "Counterparty";
    String searchBook = "Book";
    String searchStatus = "NEW";
    LocalDate searchstart = LocalDate.of(2025, 10, 15);
    LocalDate searchExecutionDate= LocalDate.of(2025, 10, 17);

    Trade trade1 = new Trade();
    trade1.setId(1L);
    trade1.setTradeDate(LocalDate.of(2025, 10, 16)); 
    Counterparty cp1 = new Counterparty(); cp1.setName(searchCounterparty); trade1.setCounterparty(cp1);
    Book book1 = new Book(); book1.setBookName(searchBook); trade1.setBook(book1);
    TradeStatus status1 = new TradeStatus(); status1.setTradeStatus(searchStatus); trade1.setTradeStatus(status1);
        //Given 
    List<Trade> tradestoReturn = Arrays.asList(trade1);
    when(tradeRepository.findByActiveTrueOrderByTradeIdDesc()).thenReturn(tradestoReturn);
        //When 
    when(tradeMapper.toDto(any(Trade.class))).thenAnswer(invocation -> {
        Trade source = invocation.getArgument(0);
        TradeDTO dto = new TradeDTO();
        dto.setId(source.getId());
        return dto;
    });
    List<TradeDTO> results = tradeService.searchTrades(
        searchCounterparty, searchBook, searchStatus, searchstart, searchExecutionDate
    );

    //Then    
    assertNotNull(results);
    assertEquals(1, results.size(), "Should find the one matching trade");
    assertEquals(1L, results.get(0).getId(), "The matching trade should be trade1");

    }
    //for example user has not input filter criteria
    @Test
    void testfilterTrades(){
    String counterparty = null;
    LocalDate start = null;
    String tradeStatus = null; 
    String bookName = null; 
    LocalDate executionDate = null; 

  
    Trade trade1 = new Trade();
    trade1.setId(1L);
    trade1.setTradeDate(LocalDate.now()); 
    trade1.setCounterparty(new Counterparty()); 
    trade1.setBook(new Book());
    trade1.setTradeStatus(new TradeStatus());

    Trade trade2 = new Trade();
    trade2.setId(2L);
    trade2.setTradeDate(LocalDate.now().minusDays(1)); 
    trade2.setCounterparty(new Counterparty());
    trade2.setBook(new Book());
    trade2.setTradeStatus(new TradeStatus());
    List<Trade> mockEntityList = Arrays.asList(trade1, trade2);
    
    when(tradeRepository.findByActiveTrueOrderByTradeIdDesc()).thenReturn(mockEntityList);
    when(tradeMapper.toDto(any(Trade.class))).thenAnswer(invocation -> {
        Trade source = invocation.getArgument(0);
        TradeDTO dto = new TradeDTO();
        dto.setId(source.getId());
      
        return dto;
    });

    
    List<TradeDTO> actualResults = tradeService.filterTrades(counterparty, bookName, tradeStatus, start, executionDate);
    
    
    assertNotNull(actualResults);
    assertEquals(2, actualResults.size());
}

@Test
void testSearchByRsqlQuery() { 
   
    String rsqlQuery = "counterparty.name==ABC";

    
    Trade trade1 = new Trade();
    trade1.setId(1L);
    List<Trade> mockTradeList = Arrays.asList(trade1);

    
    TradeDTO dto1 = new TradeDTO();
    dto1.setId(1L);

    
    when(tradeRepository.findAll(any(Specification.class))).thenReturn(mockTradeList);

    
    when(tradeMapper.toDto(trade1)).thenReturn(dto1);

    
    List<TradeDTO> results = tradeService.searchByrsqlQuery(rsqlQuery); 

    // --- Then ---
    assertNotNull(results);
    assertEquals(1, results.size());
    assertEquals(1L, results.get(0).getId());

    
    verify(tradeRepository).findAll(any(Specification.class));
}

@Test
void testCalculateCashflowValue_FixVerified_TRD_2025_001() {
   
    LegType fixedLegType = new LegType();
    fixedLegType.setType("Fixed");
    TradeLeg leg = new TradeLeg();
    leg.setNotional(new BigDecimal("10000000.00")); 
    leg.setRate(3.5); 
    leg.setLegRateType(fixedLegType);
    
   
    int quarterlyInterval = 3; 
   
    BigDecimal expectedCorrectValue = new BigDecimal("87500.00");

    // --- When ---
    BigDecimal result = tradeService.calculateCashflowValue(leg, quarterlyInterval);
    
    // --- Then ---
    
    assertNotNull(result);
   
    assertEquals(0, expectedCorrectValue.compareTo(result),
        "The calculation should produce the correct value of 87,500.00");
}


}
