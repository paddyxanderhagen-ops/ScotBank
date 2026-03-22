package uk.co.asepstrath.bank.unit.service;

import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import kong.unirest.core.GetRequest;
import kong.unirest.core.HttpRequestWithBody;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import uk.co.asepstrath.bank.model.Investor;
import uk.co.asepstrath.bank.model.Price;
import uk.co.asepstrath.bank.model.dto.InvestorDTO;
import uk.co.asepstrath.bank.model.dto.PriceDTO;
import uk.co.asepstrath.bank.model.dto.TransactionDTO;
import uk.co.asepstrath.bank.repository.PriceRepository;
import uk.co.asepstrath.bank.service.APIService;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class APIServiceTest {

    @Test
    void getAllInvestors() {
        try (MockedStatic<Unirest> unirest = mockStatic(Unirest.class)) {
            GetRequest request = mock(GetRequest.class);
            HttpResponse<String> response = mock(HttpResponse.class);

            String json = """
                    {
                        "investors": [
                            {
                                "id": "1",
                                "name": "John",
                                "cashBalance": 1500
                            }
                        ]
                    }
                    """;

            unirest.when(() -> Unirest.get(anyString())).thenReturn(request);
            when(request.accept("application/json")).thenReturn(request);
            when(request.asString()).thenReturn(response);
            when(response.getBody()).thenReturn(json);

            APIService service = new APIService();
            InvestorDTO result = service.getAllInvestor();

            assertNotNull(result);
            assertEquals(1, result.getInvestors().size());
            assertEquals("1", result.getInvestors().getFirst().getId());
            assertEquals("John", result.getInvestors().getFirst().getName());
            assertEquals(1500, result.getInvestors().getFirst().getCashBalance());
        }
    }

    @Test
    void getAllInvestors_isEmpty () {
        try (MockedStatic<Unirest> unirest = mockStatic(Unirest.class)) {
            GetRequest request = mock(GetRequest.class);
            HttpResponse<String> response = mock(HttpResponse.class);

            String json = """
                    {
                        "investors": [
                        ]
                    }
                    """;

            unirest.when(() -> Unirest.get(anyString())).thenReturn(request);
            when(request.accept("application/json")).thenReturn(request);
            when(request.asString()).thenReturn(response);
            when(response.getBody()).thenReturn(json);

            APIService service = new APIService();
            StatusCodeException ex = assertThrows(StatusCodeException.class, () -> service.getAllInvestor());
            assertEquals(StatusCode.SERVER_ERROR, ex.getStatusCode());
        }
    }

    @Test
    void getAllTransactions() {
        try (MockedStatic<Unirest> unirest = mockStatic(Unirest.class)) {
            GetRequest request = mock(GetRequest.class);
            HttpResponse<String> response = mock(HttpResponse.class);

            String json = """
                    {
                        "transactions": [
                            {
                                "id": "1",
                                "timestamp": "10.00",
                                "type": "BUY",
                                "investorId": "1",
                                "ticker": "AAPL",
                                "totalCashAmount": 1500
                            }
                        ],
                        "pagination": {
                            "pageSize": 1,
                            "returnedCount": 1,
                            "nextToken": null
                        }
                    }
                    """;

            unirest.when(() -> Unirest.get(anyString())).thenReturn(request);
            when(request.accept("application/json")).thenReturn(request);
            when(request.asString()).thenReturn(response);
            when(response.getBody()).thenReturn(json);

            APIService service = new APIService();
            TransactionDTO result = service.getAllTransaction();

            assertNotNull(result);
            assertEquals(1, result.getTransactions().size());
            assertEquals("1", result.getTransactions().getFirst().getId());
            assertEquals("10.00", result.getTransactions().getFirst().getTimestamp());
            assertEquals("BUY", result.getTransactions().getFirst().getType());
            assertEquals("1", result.getTransactions().getFirst().getInvestorId());
            assertEquals("AAPL", result.getTransactions().getFirst().getTicker());
            assertEquals(1500, result.getTransactions().getFirst().getTotalCashAmount());
            assertNull(result.getPages().getNextToken());
        }
    }

    @Test
    void getAllTransactions_isEmpty () {
        try (MockedStatic<Unirest> unirest = mockStatic(Unirest.class)) {
            GetRequest request = mock(GetRequest.class);
            HttpResponse<String> response = mock(HttpResponse.class);

            String json = """
                    {
                        "transactions": [
                        ],
                        "pagination": {
                            "nextToken": null
                        }
                    }
                    """;

            unirest.when(() -> Unirest.get(anyString())).thenReturn(request);
            when(request.accept("application/json")).thenReturn(request);
            when(request.asString()).thenReturn(response);
            when(response.getBody()).thenReturn(json);

            APIService service = new APIService();
            StatusCodeException ex = assertThrows(StatusCodeException.class, () -> service.getAllTransaction());
            assertEquals(StatusCode.SERVER_ERROR, ex.getStatusCode());
        }
    }

    @Test
    void getAllPrices () {
        try (MockedStatic<Unirest> unirest = mockStatic(Unirest.class)) {
            GetRequest request = mock(GetRequest.class);
            HttpResponse<String> response = mock(HttpResponse.class);
            PriceRepository repo = mock(PriceRepository.class);


            String page1 = """
                    {
                        "prices": [
                            {
                                "timestamp": "10.00",
                                "price": 100
                            }
                        ],
                        "pagination": {
                            "pageSize": 1,
                            "returnedCount": 1,
                            "nextToken": "1"
                        }
                    }
                    """;

            String page2 = """
                    {
                        "prices": [
                            {
                                "timestamp": "10.15",
                                "price": 101
                            }
                        ],
                        "pagination": {
                            "pageSize": 1,
                            "returnedCount": 1,
                            "nextToken": null
                        }
                    }
                    """;

            unirest.when(() -> Unirest.get(anyString())).thenReturn(request);
            when(request.accept("application/json")).thenReturn(request);
            when(request.asString()).thenReturn(response);
            when(response.getBody()).thenReturn(page1, page2);

            APIService service = new APIService();
            PriceDTO results = service.getAllPrices("AAPL", repo, "0");


            assertNotNull(results);
            assertEquals("10.00", results.getPrices().getFirst().getTimeStamp());
            assertEquals(100, results.getPrices().getFirst().getPrice());
            assertEquals("10.15", results.getPrices().get(1).getTimeStamp());
            assertEquals(101, results.getPrices().get(1).getPrice());
            assertNull(results.getPages().getNextToken());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
