package com.example.ToDo.Controller;

import com.example.ToDo.Exception.UserNotFoundException;
import com.example.ToDo.Model.User;
import com.example.ToDo.ServiceInterface.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/token")
public class TokenController {

    private static final Logger logger = Logger.getLogger(TokenController.class.getName());
    private static final Dotenv dotenv = Dotenv.load();
    private final UserService userService;

    public TokenController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves an API token for client-side use
     * Verifies user authentication via JWT before returning the token
     * @param authToken JWT authorization token from request header
     * @return ResponseEntity containing the access token in JSON format
     * @throws UserNotFoundException if the user associated with the JWT is not found
     */
    @GetMapping("/")
    public ResponseEntity<Map<String, String>> getAccessToken(@RequestHeader("Authorization") String authToken)
            throws UserNotFoundException {
        try {
            // Verify the user is authenticated through the JWT token
            User user = userService.findUserProfileByJwt(authToken);

            // Get the token from environment variables
            String token = dotenv.get("GITHUB_TOKEN");

            if (token == null || token.isEmpty()) {
                logger.warning("GITHUB_TOKEN environment variable not found or empty");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Return the token in a JSON response
            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            logger.warning("User not found with provided JWT: " + e.getMessage());
            throw e; // Let the exception handler deal with this
        } catch (Exception e) {
            logger.severe("Error retrieving access token: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}