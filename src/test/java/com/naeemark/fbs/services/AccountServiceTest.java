package com.naeemark.fbs.services;

import com.naeemark.fbs.models.Account;
import com.naeemark.fbs.repositories.AccountRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static com.naeemark.fbs.utils.Constants.ERROR_ACCOUNT_NOT_FOUND;
import static com.naeemark.fbs.utils.Constants.ERROR_ACCOUNT_SERVICE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @InjectMocks
    private AccountServiceImpl service;

    @Mock
    private AccountRepository repository;

    private Account testAccount ;

    @BeforeEach
    public void setup(){
        testAccount = new Account(1, 1);
    }

    @Test
    @DisplayName("Create - Success")
    void create_Success() {
        when(repository.save(any())).thenReturn(testAccount);
        Account account = service.create();
        assertEquals(account.getId(), testAccount.getId());
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("Create - Exception")
    void create_Exception() {
        when(repository.save(any())).then(invocation -> {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ERROR_ACCOUNT_SERVICE);
        });
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.create());
        assertNotNull(exception);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, exception.getStatus());
        assertEquals(ERROR_ACCOUNT_SERVICE, exception.getReason());
        verify(repository, times(1)).save(any());
    }

    @Test
    @DisplayName("Get - Success")
    void get() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(testAccount));
        Account account = service.get(testAccount.getId());
        assertEquals(account.getId(), testAccount.getId());
        verify(repository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("Get - Exception due to check fail")
    void get_Exception_Fail() {
        when(repository.findById(anyInt())).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.get(testAccount.getId()));
        assertNotNull(exception);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(ERROR_ACCOUNT_NOT_FOUND, exception.getReason());
        verify(repository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("Get - Exception")
    void get_Exception() {
        when(repository.findById(anyInt())).then(invocation -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ERROR_ACCOUNT_NOT_FOUND);
        });
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.get(anyInt()));
        assertNotNull(exception);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals(ERROR_ACCOUNT_NOT_FOUND, exception.getReason());
        verify(repository, times(1)).findById(anyInt());
    }

    @Test
    @DisplayName("List - Success")
    void list() {
        when(repository.findAll()).thenReturn(Collections.singletonList(testAccount));

        List<Account> list = service.list();
        assertNotNull(list);
        assertEquals(list.get(0).getId(), testAccount.getId());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("Update - Success")
    void update() {
        when(repository.saveAll(any())).thenReturn(Collections.singletonList(testAccount));
        service.update(testAccount, testAccount);
        verify(repository, times(1)).saveAll(any());
    }
}