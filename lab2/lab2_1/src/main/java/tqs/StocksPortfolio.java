package tqs;

import java.util.*;
import java.util.stream.Collectors;

public class StocksPortfolio {
    private IStockmarketService stock;
    private List<Stock> stocks = new ArrayList<Stock>();
    public StocksPortfolio(IStockmarketService stock) {
        this.stock = stock;
    }
    public void addStock(Stock stock) {
        stocks.add(stock);
    }
    public double totalValue() {
        double total = 0;
        for (Stock s : stocks) {
            total += stock.lookUpPrice(s.getLabel())*s.getQuantity();
        }
        return total;
    }
    /**
     * @param topN the number of most valuable stocks to return
     * @return a list with the topN most valuable stocks in the portfolio
     */
    public List<Stock> mostValuableStocks(int topN) {
        // Ordena as ações de acordo com o valor total (preço de mercado * quantidade),
        // em ordem decrescente, e retorna apenas as topN
        if  (topN <= 0) {
            return Collections.emptyList();
        }
        return stocks.stream()
                .sorted((s1, s2) -> {
                    double value1 = stock.lookUpPrice(s1.getLabel()) * s1.getQuantity();
                    double value2 = stock.lookUpPrice(s2.getLabel()) * s2.getQuantity();
                    return Double.compare(value2, value1); // ordem decrescente
                })
                .limit(topN)
                .collect(Collectors.toList());
    }
}
