package com.ecommerce.userservice.exception;

/**
 * Exception thrown when a resource already exists
 */
public class ResourceAlreadyExistsException extends RuntimeException {
    public ResourceAlreadyExistsException(String message) {
        super(message);
    }
}
