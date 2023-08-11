package com.kindredgroup.unibetlivetest.endtoend;



import com.kindredgroup.unibetlivetest.dto.BetRequestDTO;
import com.kindredgroup.unibetlivetest.entity.Customer;
import com.kindredgroup.unibetlivetest.entity.Event;
import com.kindredgroup.unibetlivetest.entity.Selection;
import com.kindredgroup.unibetlivetest.repository.CustomerRepository;
import com.kindredgroup.unibetlivetest.repository.EventRepository;
import com.kindredgroup.unibetlivetest.repository.SelectionRepository;
import com.kindredgroup.unibetlivetest.types.SelectionResult;
import com.kindredgroup.unibetlivetest.types.State;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventsEndToEndTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private SelectionRepository  selectionRepository;

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private EventRepository eventRepository;

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    @Test
    public void testGetEventsSuccess() {
        ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort("/api/v1/events"), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testBadRequest() {
        ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort("/api/v1/events?isLive=wrong"), String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void ignoreWrongParams() {
        ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort("/api/v1/events?invalidParam=wrong"), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetSelectionsForEvent_Success() {
        Event event = eventRepository.findById(1L).get();
        ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort("/api/v1/events/"+event.getId()+"/selections"), String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetSelectionsForEvent_NoResults() {
        Long id = 2L;
        ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort("/api/v1/events/"+id+"/selections"), String.class);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testGetSelectionsForEvent_EventNotFound() {
        Long non_existing_id = 99999L;
        ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort("/api/v1/events/"+non_existing_id+"/selections"), String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetSelectionsForEvent_BadRequest() {
        Event event = eventRepository.findRandom();
        ResponseEntity<String> response = restTemplate.getForEntity(createURLWithPort("/api/v1/events/"+event.getId()+"/selections?state=INVALID_STATE"), String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAddBet_Success() {
        Selection selection = selectionRepository.findRandom();
        Customer customer = customerRepository.findRandom();
        BetRequestDTO betRequest = new BetRequestDTO(selection.getId(), selection.getCurrentOdd(), customer.getId(), customer.getBalance());
        ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort("/api/v1/bets/add"), betRequest, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testAddBet_BadRequest() {
        Selection selection = selectionRepository.findRandom();
        Customer customer = customerRepository.findRandom();
        BetRequestDTO betRequest = new BetRequestDTO(selection.getId(), selection.getCurrentOdd(), null, customer.getBalance());
        ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort("/api/v1/bets/add"), betRequest, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void testAddBet_Conflict() {
        Selection selection = selectionRepository.findRandom();
        Customer customer = customerRepository.findRandom();
        BetRequestDTO betRequest = new BetRequestDTO(selection.getId(), selection.getCurrentOdd(), customer.getId(), customer.getBalance().divide(new BigDecimal(5)));
        ResponseEntity<String> firstResponse = restTemplate.postForEntity(createURLWithPort("/api/v1/bets/add"), betRequest, String.class);
        ResponseEntity<String> secondResponse = restTemplate.postForEntity(createURLWithPort("/api/v1/bets/add"), betRequest, String.class);
        assertEquals(HttpStatus.CONFLICT, secondResponse.getStatusCode());
    }

    @Test
    public void testAddBet_InsufficientBalance() {
        Selection selection = selectionRepository.findRandom();
        Customer customer = customerRepository.findRandom();
        BetRequestDTO betRequest = new BetRequestDTO(selection.getId(), selection.getCurrentOdd(), customer.getId(), customer.getBalance().multiply(new BigDecimal(5)));
        ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort("/api/v1/bets/add"), betRequest, String.class);
        assertEquals(600, response.getStatusCodeValue());
    }

    @Test
    public void testAddBet_OddChanged() {
        Selection selection = selectionRepository.findRandom();
        Customer customer = customerRepository.findRandom();
        BetRequestDTO betRequest = new BetRequestDTO(selection.getId(), selection.getCurrentOdd().add(new BigDecimal(0.1)), customer.getId(), customer.getBalance());
        ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort("/api/v1/bets/add"), betRequest, String.class);
        assertEquals(601, response.getStatusCodeValue());
    }

    @Test
    public void testAddBet_SelectionClosed() {
        Selection selection = selectionRepository.findRandom();
        Customer customer = customerRepository.findRandom();
        selection.setState(State.CLOSED);
        selection.setResult(SelectionResult.LOST);
        selectionRepository.save(selection);
        BetRequestDTO betRequest = new BetRequestDTO(selection.getId(), selection.getCurrentOdd(), customer.getId(), customer.getBalance().multiply(new BigDecimal(5)));
        ResponseEntity<String> response = restTemplate.postForEntity(createURLWithPort("/api/v1/bets/add"), betRequest, String.class);
        assertEquals(602, response.getStatusCodeValue());
    }

}
