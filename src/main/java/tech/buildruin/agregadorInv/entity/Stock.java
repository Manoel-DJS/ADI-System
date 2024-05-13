package tech.buildruin.agregadorInv.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_stocks")
public class Stock {
    @Id
    @Column(name = "stock_id")
    private String stockId; //

    @Column(name = "description")
    private String description;

    public Stock(){

    }
    public Stock(String stockId, String description) {
        this.stockId = stockId;
        this.description = description;
    }

}
