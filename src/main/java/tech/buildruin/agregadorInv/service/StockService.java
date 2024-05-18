package tech.buildruin.agregadorInv.service;

import org.springframework.stereotype.Service;
import tech.buildruin.agregadorInv.controller.dto.CreateStockDto;
import tech.buildruin.agregadorInv.entity.Stock;
import tech.buildruin.agregadorInv.repository.StockRepository;

@Service
public class StockService {

    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void createStock(CreateStockDto createStockDto) {
        // DTO -> Entity

        var stock = new Stock(
                createStockDto.stockId(),
                createStockDto.description()
        );

        stockRepository.save(stock);
    }
}
