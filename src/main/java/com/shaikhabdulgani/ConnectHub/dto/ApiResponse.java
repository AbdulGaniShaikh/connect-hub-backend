package com.shaikhabdulgani.ConnectHub.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ApiResponse is a generic class designed to encapsulate the response structure
 * for API operations. It consists of a success flag indicating the outcome
 * of the operation and an optional payload containing the result data.
 *
 * @param <T> The type of the payload data.
 */
@Data
@NoArgsConstructor
public class ApiResponse<T> {

    /**
     * A flag indicating the success or failure of the API operation.
     */
    private boolean success;
    /**
     * The payload containing the result data of the API operation.
     */
    private T payload;


    /**
     * Constructs a new ApiResponse with the specified success flag and payload.
     *
     * @param success The success or failure status of the API operation.
     * @param payload The payload containing the result data.
     */
    public ApiResponse(boolean success, T payload){
        this.success = success;
        this.payload = payload;
    }

    /**
     * Creates a new ApiResponse instance indicating a successful API operation
     * with the provided payload.
     *
     * @param payload The payload containing the result data.
     * @param <T> The type of the payload data.
     * @return A new ApiResponse with success set to true and the specified payload.
     */
    public static <T> ApiResponse<T> success(T payload){
        return new ApiResponse<>(true,payload);
    }

}
