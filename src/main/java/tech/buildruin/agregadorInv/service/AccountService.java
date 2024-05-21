package tech.buildruin.agregadorInv.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;
import tech.buildruin.agregadorInv.client.BrapiClient;
import tech.buildruin.agregadorInv.controller.dto.AccountStockResponseDto;
import tech.buildruin.agregadorInv.controller.dto.AssociateAccountStockDto;
import tech.buildruin.agregadorInv.entity.AccountStock;
import tech.buildruin.agregadorInv.entity.AccountStockId;
import tech.buildruin.agregadorInv.repository.AccountRepository;
import tech.buildruin.agregadorInv.repository.AccountStockRepository;
import tech.buildruin.agregadorInv.repository.StockRepository;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    @Value("#{environment.TOKEN}")
    private String TOKEN;
    private AccountRepository accountRepository;
    private StockRepository stockRepository;
    private AccountStockRepository accountStockRepository;

    private BrapiClient brapiClient;

    public AccountService(AccountRepository accountRepository, StockRepository stockRepository, AccountStockRepository accountStockRepository, BrapiClient brapiClient) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
        this.brapiClient = brapiClient;
    }

    public void associateStock(String accountId, AssociateAccountStockDto dto) {

        var account = accountRepository.findById(UUID.fromString(accountId)).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var stock = stockRepository.findById(dto.stockId()).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // DTO -> ENTITY

        var id = new AccountStockId(account.getAccountId(), stock.getStockId());

        var entity = new AccountStock(
                id,
                account,
                stock,
                dto.quantity()
        );
        accountStockRepository.save(entity);
    }

    public List<AccountStockResponseDto> listStocks(String accountId) {

        var account = accountRepository.findById(UUID.fromString(accountId)).
                orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return account.getAccountStocks().stream().
                map(as -> new AccountStockResponseDto
                        (as.getStock().getStockId(), as.getQuantity(),
                                GetTotal(as.getQuantity(), as.getStock().getStockId())
                        )).
                toList();
    }

    private double GetTotal(Integer quantity, String stockId) {
        var response = brapiClient.getQuote(TOKEN, stockId);

        var price = response.results().get(0).regularMarketPrice();
        // var price = response.results().getFirst().regularMarketPrice(); so funfa no java 21

        return quantity * price;
    }
}
