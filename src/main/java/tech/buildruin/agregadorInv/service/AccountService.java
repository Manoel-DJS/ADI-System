package tech.buildruin.agregadorInv.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
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
    private AccountRepository accountRepository;
    private StockRepository stockRepository;
    private AccountStockRepository accountStockRepository;

    public AccountService(AccountRepository accountRepository, StockRepository stockRepository, AccountStockRepository accountStockRepository) {
        this.accountRepository = accountRepository;
        this.stockRepository = stockRepository;
        this.accountStockRepository = accountStockRepository;
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
                        (as.getStock().getStockId(), as.getQuantity(),0.0)).toList();
    }
}
