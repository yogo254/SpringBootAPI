package com.comulynx.wallet.rest.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.comulynx.wallet.rest.api.exception.ResourceNotFoundException;
import com.comulynx.wallet.rest.api.model.Account;
import com.comulynx.wallet.rest.api.repository.AccountRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.comulynx.wallet.rest.api.util.AppUtils;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(AppUtils.BASE_URL + "/accounts")
public class AccountController {
	private Gson gson = new Gson();

	@Autowired
	private AccountRepository accountRepository;

	@GetMapping("/")
	public List<Account> getAllAccount() {
	
		return accountRepository.findAll();
	}

	@GetMapping("/{searchId}")
	public ResponseEntity<?> getAccountByCustomerIdOrAccountNo(
			@PathVariable(value = "searchId") String customerIdOrAccountNo) throws ResourceNotFoundException {
		Account account = accountRepository
				.findAccountByCustomerIdOrAccountNo(customerIdOrAccountNo, customerIdOrAccountNo)
				.orElseThrow(() -> new ResourceNotFoundException(
						"Account not found for this searchId :: " + customerIdOrAccountNo));

		return ResponseEntity.ok().body(account);
	}

	@PostMapping("/balance")
	public ResponseEntity<?> getAccountBalanceByCustomerIdAndAccountNo(@RequestBody Account account)
			throws ResourceNotFoundException {
		try {
			JsonObject response = new JsonObject();

			Optional<Account> userAccount = accountRepository
					.findAccountByCustomerIdAndAccountNo(account.getCustomerId(), account.getAccountNo());
			if (userAccount.isPresent())
				response.addProperty("balance", userAccount.get().getBalance());
			return ResponseEntity.ok().body(gson.toJson(response));
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

		}
	}

	@PostMapping("/create")
	public Account createAccount(@RequestBody Account account) {
		return accountRepository.save(account);
	}

}
