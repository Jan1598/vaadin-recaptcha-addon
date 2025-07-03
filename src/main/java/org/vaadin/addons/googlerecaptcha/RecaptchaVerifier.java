package org.vaadin.addons.googlerecaptcha;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.java.Log;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;

@Log
public class RecaptchaVerifier
{
    private static final String VERIFICATION_URL = "https://www.google.com/recaptcha/api/siteverify";

    public Map<String, Object> verifyRecaptcha( String recaptchaResponse, String secretKey )
    {
        try ( HttpClient client = HttpClient.newHttpClient() )
        {
            String params = "secret=" + URLEncoder.encode( secretKey, StandardCharsets.UTF_8 ) +
                    "&response=" + URLEncoder.encode( recaptchaResponse, StandardCharsets.UTF_8 );

            HttpRequest request = HttpRequest.newBuilder()
                    .uri( URI.create( VERIFICATION_URL ) )
                    .header( "Content-Type", "application/x-www-form-urlencoded" )
                    .POST( HttpRequest.BodyPublishers.ofString( params ) )
                    .build();

            HttpResponse<String> response = client.send( request, HttpResponse.BodyHandlers.ofString() );

            String responseBody = response.body();

            return parseJson( responseBody );
        }
        catch ( Exception e )
        {
            LOGGER.log( Level.WARNING, e.getMessage(), e );
        }

        return Collections.emptyMap();
    }

    private Map<String, Object> parseJson( String json )
    {
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();

            return objectMapper.readValue( json, new TypeReference<>()
            {
            } );
        }
        catch ( Exception e )
        {
            LOGGER.log( Level.WARNING, e.getMessage(), e );
        }

        return Collections.emptyMap();
    }

}
