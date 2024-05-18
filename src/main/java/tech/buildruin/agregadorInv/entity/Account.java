package tech.buildruin.agregadorInv.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name= "tb_accounts")
public class Account {
    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID accountId;

    // OK

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL,mappedBy = "account")
    @PrimaryKeyJoinColumn       // Isso promove que a primarykey da entidade account para a tabela de billing address
    private BillingAddress billingAddress;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "account")
    private List<AccountStock> accountStocks;


    public Account(){

    }

    // Depois deleto
    public Account(UUID accountId, String description) {
        this.accountId = accountId;
        this.description = description;
    }

    public Account(UUID accountId, User user, BillingAddress billingAddress, String description, List<AccountStock> accountStocks) {
        this.accountId = accountId;
        this.user = user;
        this.billingAddress = billingAddress;
        this.description = description;
        this.accountStocks = accountStocks;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //user
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
