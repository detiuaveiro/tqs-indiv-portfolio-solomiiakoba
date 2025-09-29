package tqs;

import org.junit.jupiter.api.Disabled;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)  // For integration of Mockito framework
@MockitoSettings(strictness = Strictness.LENIENT)
public class StockPortfolioTest {

    @Mock
    private IStockmarketService stockMarketService; // Mock do serviço externo
    @InjectMocks
    StocksPortfolio portfolio;
    @Test
    @Disabled
    void getTotalValueAnnotation() {
        when(stockMarketService.lookUpPrice("APPL")).thenReturn(200.0);
        when(stockMarketService.lookUpPrice("ASBI")).thenReturn(100.0);
        when(stockMarketService.lookUpPrice("TSLA")).thenReturn(70.0);
        when(stockMarketService.lookUpPrice("MSFT")).thenReturn(150.0);

        portfolio.addStock(new Stock("APPL", 3));
        portfolio.addStock(new Stock("ASBI", 2));

        double res = portfolio.totalValue();
        //assertEquals(800.0, res);
        assertThat(res).isEqualTo(800.0)
                .isGreaterThan(0.0);
        verify(stockMarketService, times(2)).lookUpPrice(anyString());
    }

    private void createData() {
        when(stockMarketService.lookUpPrice("APPL")).thenReturn(200.0);
        when(stockMarketService.lookUpPrice("ASBI")).thenReturn(100.0);
        when(stockMarketService.lookUpPrice("TSLA")).thenReturn(70.0);
        when(stockMarketService.lookUpPrice("MSFT")).thenReturn(150.0);

        portfolio.addStock(new Stock("APPL", 3)); // 600
        portfolio.addStock(new Stock("ASBI", 2)); // 200
        portfolio.addStock(new Stock("TSLA", 3)); // 210
        portfolio.addStock(new Stock("MSFT", 2)); // 300
    }
    @Test
    void testTopNNormal() {
        createData();
        List<Stock> top3 = portfolio.mostValuableStocks(3);
        assertThat(top3).hasSize(3);
        assertThat(top3).extracting(Stock::getLabel).containsExactly("APPL", "MSFT", "TSLA");
    }
    @Test
    void testTopNHigh() {
        createData();
        List<Stock> top10 = portfolio.mostValuableStocks(10);
        assertThat(top10).hasSize(4);   // retorna todas
        assertThat(top10).extracting(Stock::getLabel).containsExactly("APPL", "MSFT", "TSLA", "ASBI");
    }
    @Test
    void testTopNLow() {
        createData();
        List<Stock> topLow = portfolio.mostValuableStocks(-1);
        assertThat(topLow).isEmpty();
    }
    @Test
    void testTopNZero() {
        createData();
        List<Stock> topZero = portfolio.mostValuableStocks(0);
        assertThat(topZero).isEmpty();
    }
    @Test
    void testEmptyPortfolio() {
        // sem adicionar a data
        List<Stock> empty = portfolio.mostValuableStocks(5);
        assertThat(empty).isEmpty();
    }
    @Test
    void testDrawRes() {
        // empate exato entre duas ações
        portfolio.addStock(new Stock("A", 2)); // total 100
        portfolio.addStock(new Stock("B", 1)); // total 100
        when(stockMarketService.lookUpPrice("A")).thenReturn(50.0);
        when(stockMarketService.lookUpPrice("B")).thenReturn(100.0);

        List<Stock> result = portfolio.mostValuableStocks(2);
        //ordem não crítica em empate
        assertThat(result).extracting(Stock::getLabel)
                .containsExactlyInAnyOrder("A", "B");
    }
}
