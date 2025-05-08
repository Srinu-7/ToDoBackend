package com.example.ToDo.Controller;

import com.example.ToDo.Exception.UserNotFoundException;
import com.example.ToDo.Helper.MyAnalyzer;
import com.example.ToDo.Model.User;
import com.example.ToDo.ServiceInterface.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/token")
@Tag(name = "API Tokens", description = "API endpoints for retrieving external service tokens")
public class TokenController {

    private static final Logger LOGGER = Logger.getLogger(TokenController.class.getName());
    private static final Dotenv dotenv = Dotenv.load();
    private final UserService userService;
    private final MyAnalyzer myAnalyzer;

    public TokenController(UserService userService, MyAnalyzer myAnalyzer) {
        this.userService = userService;
        this.myAnalyzer = myAnalyzer;
    }

    @GetMapping("/github")
    @Operation(summary = "Retrieve GitHub API token", description = "Returns a GitHub API token for authenticated users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing authentication token"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error - Token not configured")
    })
    public ResponseEntity<Map<String, String>> getGithubToken(@RequestHeader("Authorization") String authToken)
            throws UserNotFoundException {
        LOGGER.info("Received GitHub token request");

        try {

            // Verify the user is authenticated through the JWT token
            User user = userService.findUserProfileByJwt(authToken);
            LOGGER.info("User authenticated: " + user.getEmail());

            // Get the token from environment variables
            String token = dotenv.get("GITHUB_TOKEN");

            if (token == null || token.isEmpty()) {
                LOGGER.severe("GITHUB_TOKEN environment variable not found or empty");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(createErrorResponse("GitHub token not configured on server"));
            }

            // Return the token in a JSON response
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("success", "true");

            LOGGER.info("GitHub token successfully retrieved for user: " + user.getEmail());
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (UserNotFoundException e) {
            LOGGER.warning("User not found with provided JWT: " + e.getMessage());
            throw e; // Let the exception handler deal with this
        } catch (Exception e) {
            LOGGER.severe("Error retrieving GitHub token: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Internal server error: " + e.getMessage()));
        }
    }

    @GetMapping("/external-service")
    @Operation(summary = "Retrieve external service API token", description = "Returns an API token for an external service for authenticated users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token retrieved successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing authentication token"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error - Token not configured")
    })
    public ResponseEntity<Map<String, String>> getExternalServiceToken(
            @RequestHeader("Authorization") String authToken,
            @RequestParam("service") String serviceName) throws UserNotFoundException {

        LOGGER.info("Received token request for service: " + serviceName);

        try {
            // Remove "Bearer " prefix if present
            if (authToken.startsWith("Bearer ")) {
                authToken = authToken.substring(7);
            }

            // Verify the user is authenticated through the JWT token
            User user = userService.findUserProfileByJwt(authToken);

            // Apply stemming to service name if needed
            String processedServiceName = myAnalyzer.stem(serviceName).toUpperCase();

            // Get the token from environment variables
            String envVarName = processedServiceName + "_TOKEN";
            String token = dotenv.get(envVarName);

            if (token == null || token.isEmpty()) {
                LOGGER.warning(envVarName + " environment variable not found or empty");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createErrorResponse("Token not configured for service: " + serviceName));
            }

            // Return the token in a JSON response
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("service", serviceName);
            response.put("success", "true");

            LOGGER.info("Token for " + serviceName + " successfully retrieved for user: " + user.getEmail());
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (UserNotFoundException e) {
            LOGGER.warning("User not found with provided JWT: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.severe("Error retrieving token for " + serviceName + ": " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Internal server error: " + e.getMessage()));
        }
    }

    /**
     * Helper method to create error response maps
     */
    private Map<String, String> createErrorResponse(String errorMessage) {
        Map<String, String> response = new HashMap<>();
        response.put("error", errorMessage);
        response.put("success", "false");
        return response;
    }
}