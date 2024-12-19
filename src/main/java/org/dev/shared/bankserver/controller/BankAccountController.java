package org.dev.shared.bankserver.controller;

import org.dev.shared.bankserver.exception.AccountNotFound;
import org.dev.shared.bankserver.exception.CustomerNotFound;
import org.dev.shared.bankserver.exception.IllegalAccountOperationException;
import org.dev.shared.bankserver.exception.InsufficientFundsException;
import org.dev.shared.bankserver.pojo.BankAccount;
import org.dev.shared.bankserver.pojo.Customer;
import org.dev.shared.bankserver.pojo.request.TransferRequestDTO;
import org.dev.shared.bankserver.pojo.response.BaseResponse;
import org.dev.shared.bankserver.service.AccountService;
import org.dev.shared.bankserver.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class BankAccountController {

    private final CustomerService customerService;
    private final AccountService accountService;

    public BankAccountController(CustomerService customerService, AccountService accountService) {
        this.customerService = customerService;
        this.accountService = accountService;
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<Customer> getCustomerDetails(@PathVariable Long id) {
        Customer customer = customerService.getCustomerWithAccounts(id);
        return customer != null ? ResponseEntity.ok(customer) : ResponseEntity.notFound().build();
    }

    @GetMapping("/{cusId}/accounts")
    public ResponseEntity<List<BankAccount>> getCustomerWithAccounts(@PathVariable Long cusId) {
       try {
           List<BankAccount> response = accountService.getBankAccountsForCustomer(cusId);
           return response != null ? ResponseEntity.ok(response) : ResponseEntity.notFound().build();
       }catch (AccountNotFound e) {
           return ResponseEntity.notFound().build();
       } catch (Exception e) {
           return ResponseEntity.internalServerError().build();
       }
    }


    @PostMapping("/transfer")
    public ResponseEntity<BaseResponse> postTransferFunds(@RequestBody TransferRequestDTO transferRequest) {
        try {
            accountService.transferMoney(
                    transferRequest.getFromAccountNumber(),
                    transferRequest.getToAccountNumber(),
                    transferRequest.getAmount(),
                    transferRequest.getCustomerId()
            );
            return ResponseEntity.ok(new BaseResponse(200, "Transfer successful"));
        } catch (AccountNotFound | CustomerNotFound | IllegalAccountOperationException | InsufficientFundsException e) {
            return ResponseEntity.badRequest().body(new BaseResponse(400, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse(500, "Internal Server Error"));
        }
    }
}
