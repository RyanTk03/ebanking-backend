package tech.rayanetoko.ebanking.services;

import tech.rayanetoko.ebanking.dtos.BankAccountDTO;
import tech.rayanetoko.ebanking.dtos.CurrentBankAccountDTO;
import tech.rayanetoko.ebanking.dtos.CustomerDTO;
import tech.rayanetoko.ebanking.dtos.SavingBankAccountDTO;
import tech.rayanetoko.ebanking.exceptions.BalanceNotSufficientException;
import tech.rayanetoko.ebanking.exceptions.BankAccountNotFoundException;
import tech.rayanetoko.ebanking.exceptions.CustomerNotFoundException;

import java.util.List;
public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;

    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

    List<CustomerDTO> listCustomers();

    BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException;

    void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;

    void credit(String accountId, double amount, String description) throws BankAccountNotFoundException;

    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccountDTO> bankAccountList();
}