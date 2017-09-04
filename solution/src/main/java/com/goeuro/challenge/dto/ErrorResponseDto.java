package com.goeuro.challenge.dto;

/**
 * Created by jawad on 9/4/17.
 */
public class ErrorResponseDto {
    String error;
    String description;

    public ErrorResponseDto(String error, String description) {
        this.error = error;
        this.description = description;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
