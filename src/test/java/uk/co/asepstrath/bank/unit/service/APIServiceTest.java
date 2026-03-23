package uk.co.asepstrath.bank.unit.service;

import io.jooby.StatusCode;
import io.jooby.exception.StatusCodeException;
import kong.unirest.core.GetRequest;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import uk.co.asepstrath.bank.model.dto.*;
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
    void getAllInvestors_whenEmptyResponse() {
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
    void getAllInvestors_whenNoResponse() {
        try (MockedStatic<Unirest> unirest = mockStatic(Unirest.class)) {
            GetRequest request = mock(GetRequest.class);
            HttpResponse<String> response = mock(HttpResponse.class);

            String json = """
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
    void getAllTransactions_whenEmptyResponse() {
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
    void getAllTransactions_whenNoResponse() {
        try (MockedStatic<Unirest> unirest = mockStatic(Unirest.class)) {
            GetRequest request = mock(GetRequest.class);
            HttpResponse<String> response = mock(HttpResponse.class);

            String json = """
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
    void getAllPrices() {
        try (MockedStatic<Unirest> unirest = mockStatic(Unirest.class)) {
            GetRequest request = mock(GetRequest.class);
            HttpResponse<String> response = mock(HttpResponse.class);
            PriceRepository repo = mock(PriceRepository.class);
            APIService service = new APIService();

            String token = service.loadToken();

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
                            "nextToken": "%s"
                        }
                    }
                    """.formatted(token);

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

    @Test
    void getAllPrices_whenEmptyResponse() {
        try (MockedStatic<Unirest> unirest = mockStatic(Unirest.class)) {
            GetRequest request = mock(GetRequest.class);
            HttpResponse<String> response = mock(HttpResponse.class);
            PriceRepository repo = mock(PriceRepository.class);


            String json = """
                    {
                        "prices": [
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

            StatusCodeException ex = assertThrows(StatusCodeException.class, () -> service.getAllPrices("x", repo, "1"));

            assertEquals(StatusCode.SERVER_ERROR, ex.getStatusCode());
        }
    }

    @Test
    void getAllPrices_whenNoResponse() {
        try (MockedStatic<Unirest> unirest = mockStatic(Unirest.class)) {
            GetRequest request = mock(GetRequest.class);
            HttpResponse<String> response = mock(HttpResponse.class);
            PriceRepository repo = mock(PriceRepository.class);


            String json = """
                    """;

            unirest.when(() -> Unirest.get(anyString())).thenReturn(request);
            when(request.accept("application/json")).thenReturn(request);
            when(request.asString()).thenReturn(response);
            when(response.getBody()).thenReturn(json);

            APIService service = new APIService();

            StatusCodeException ex = assertThrows(StatusCodeException.class, () -> service.getAllPrices("x", repo, "1"));

            assertEquals(StatusCode.SERVER_ERROR, ex.getStatusCode());
        }
    }

    @Test
    void getAllPrices_databaseErr() {
        try (MockedStatic<Unirest> unirest = mockStatic(Unirest.class)) {
            GetRequest request = mock(GetRequest.class);
            HttpResponse<String> response = mock(HttpResponse.class);
            PriceRepository repo = mock(PriceRepository.class);

            APIService service = new APIService();

            String token = service.loadToken();


            String json = """
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
                            "nextToken": "%s"
                        }
                    }
                    """.formatted(token);

            unirest.when(() -> Unirest.get(anyString())).thenReturn(request);
            when(request.accept("application/json")).thenReturn(request);
            when(request.asString()).thenReturn(response);
            when(response.getBody()).thenReturn(json);
            doThrow(new SQLException()).when(repo).saveAll(anyString(), anyList());


            StatusCodeException ex = assertThrows(StatusCodeException.class, () -> service.getAllPrices("x", repo, "1"));

            assertEquals(StatusCode.SERVER_ERROR, ex.getStatusCode());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getAllEquities() {
        try (MockedStatic<Unirest> unirest = mockStatic(Unirest.class)) {
            GetRequest request = mock(GetRequest.class);
            HttpResponse<String> response = mock(HttpResponse.class);

            String json = """
                    {
                        "assets": [
                            {
                                "name": "Apple",
                                "ticker": "AAPL",
                                "sector": "Technology"
                            }
                        ]
                    }
                    """;

            unirest.when(() -> Unirest.get(anyString())).thenReturn(request);
            when(request.accept("application/json")).thenReturn(request);
            when(request.asString()).thenReturn(response);
            when(response.getBody()).thenReturn(json);

            APIService service = new APIService();
            EquityDTO result = service.getAllEquities();

            assertNotNull(result);
            assertEquals("Apple", result.getEquities().getFirst().getName());
            assertEquals("AAPL", result.getEquities().getFirst().getTicker());
            assertEquals("Technology", result.getEquities().getFirst().getSector());
        }
    }

    @Test
    void getAllEquities_whenEmptyResponse() {
        try (MockedStatic<Unirest> unirest = mockStatic(Unirest.class)) {
            GetRequest request = mock(GetRequest.class);
            HttpResponse<String> response = mock(HttpResponse.class);

            String json = """
                    {
                        "assets": [
                        ]
                    }
                    """;

            unirest.when(() -> Unirest.get(anyString())).thenReturn(request);
            when(request.accept("application/json")).thenReturn(request);
            when(request.asString()).thenReturn(response);
            when(response.getBody()).thenReturn(json);

            APIService service = new APIService();

            StatusCodeException ex = assertThrows(StatusCodeException.class, () -> service.getAllEquities());
            assertEquals(StatusCode.SERVER_ERROR, ex.getStatusCode());
        }
    }

    @Test
    void getAllEquities_whenNoResponse() {
        try (MockedStatic<Unirest> unirest = mockStatic(Unirest.class)) {
            GetRequest request = mock(GetRequest.class);
            HttpResponse<String> response = mock(HttpResponse.class);

            String json = """
                    """;

            unirest.when(() -> Unirest.get(anyString())).thenReturn(request);
            when(request.accept("application/json")).thenReturn(request);
            when(request.asString()).thenReturn(response);
            when(response.getBody()).thenReturn(json);

            APIService service = new APIService();

            StatusCodeException ex = assertThrows(StatusCodeException.class, () -> service.getAllEquities());
            assertEquals(StatusCode.SERVER_ERROR, ex.getStatusCode());
        }
    }

    @Test
    void getAllEtfs() {
        try (MockedStatic<Unirest> unirest = mockStatic(Unirest.class)) {
            GetRequest request = mock(GetRequest.class);
            HttpResponse<String> response = mock(HttpResponse.class);

            String json = """
                    {
                        "etfs": [
                            {
                                "name": "Small Cap Growth",
                                "ticker": "SMLX",
                                "description": "High-growth small cap fund targeting companies with market cap under $2B",
                                "inceptionDate": "2021-09-01",
                                "constituents": [
                                    {
                                        "ticker": "OPE",
                                        "weight": 0.05
                                    },
                                ]
                            }
                        ]
                    }
                    """;

            unirest.when(() -> Unirest.get(anyString())).thenReturn(request);
            when(request.accept("application/json")).thenReturn(request);
            when(request.asString()).thenReturn(response);
            when(response.getBody()).thenReturn(json);

            APIService service = new APIService();
            EtfDTO result = service.getAllEtf();

            assertNotNull(result);
            assertEquals("Small Cap Growth", result.getEtfs().getFirst().getName());
            assertEquals("SMLX", result.getEtfs().getFirst().getTicker());
            assertEquals("High-growth small cap fund targeting companies with market cap under $2B", result.getEtfs().getFirst().getDescription());
            assertEquals("2021-09-01", result.getEtfs().getFirst().getInceptionDate());
            assertEquals("OPE", result.getEtfs().getFirst().getConstituents().getFirst().getTicker());
            assertEquals(0.05, result.getEtfs().getFirst().getConstituents().getFirst().getWeight());
        }
    }

    @Test
    void getAllEts_whenEmptyResponse() {
        try (MockedStatic<Unirest> unirest = mockStatic(Unirest.class)) {
            GetRequest request = mock(GetRequest.class);
            HttpResponse<String> response = mock(HttpResponse.class);

            String json = """
                    {
                        "etfs": [
                        ]
                    }
                    """;

            unirest.when(() -> Unirest.get(anyString())).thenReturn(request);
            when(request.accept("application/json")).thenReturn(request);
            when(request.asString()).thenReturn(response);
            when(response.getBody()).thenReturn(json);

            APIService service = new APIService();

            StatusCodeException ex = assertThrows(StatusCodeException.class, () -> service.getAllEtf());
            assertEquals(StatusCode.SERVER_ERROR, ex.getStatusCode());
        }
    }

    @Test
    void getAllEts_whenNoResponse() {
        try (MockedStatic<Unirest> unirest = mockStatic(Unirest.class)) {
            GetRequest request = mock(GetRequest.class);
            HttpResponse<String> response = mock(HttpResponse.class);

            String json = """
                    """;

            unirest.when(() -> Unirest.get(anyString())).thenReturn(request);
            when(request.accept("application/json")).thenReturn(request);
            when(request.asString()).thenReturn(response);
            when(response.getBody()).thenReturn(json);

            APIService service = new APIService();

            StatusCodeException ex = assertThrows(StatusCodeException.class, () -> service.getAllEtf());
            assertEquals(StatusCode.SERVER_ERROR, ex.getStatusCode());
        }
    }
}
