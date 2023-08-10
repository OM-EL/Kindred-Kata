package com.kindredgroup.unibetlivetest.api;

import com.kindredgroup.unibetlivetest.dto.ApiResponse;
import com.kindredgroup.unibetlivetest.dto.BetRequestDTO;
import com.kindredgroup.unibetlivetest.service.BetsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.kindredgroup.unibetlivetest.utils.ServiceConstants.OK_MESSAGE;

@RestController
@Log4j2
@RequestMapping(Urls.BASE_PATH)
@RequiredArgsConstructor
@CrossOrigin(origins = "*")  // Consider limiting origins in production for security.
public class BetController {

    private final BetsService betsService;

    /**
     * Adds a new bet.
     *
     * @param request DTO containing bet details.
     * @return ApiResponse containing status and added bet details.
     */
    @PostMapping(value = Urls.ADD_BET, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> addNewBet(@RequestBody BetRequestDTO request) {
        BetRequestDTO addedBet = betsService.addBet(request);

        ApiResponse response = new ApiResponse();
        response.setData(addedBet);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage(OK_MESSAGE);

        return ResponseEntity.ok(response);
    }
}
