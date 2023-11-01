package org.oldman.entities.entityUtils;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;

import java.util.function.Consumer;
import java.util.function.Function;

public class ServiceOperationUtils {
    private ServiceOperationUtils() {
        throw new AssertionError();
    }

    public static <T, R> Response applyFunction(T service, Function<T, R> function) {
        try {
            R result = function.apply(service);
            return Response.ok(result).build();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return buildFailedResponseByException(e);
        }
    }

    public static <T> Response consumeOperation(T service, Consumer<T> consumer) {
        return consumeOperation(service, consumer, Response.Status.OK);
    }

    public static <T> Response consumeOperation(T service, Consumer<T> consumer, Response.Status status) {
        return consumeOperation(service, consumer, status, "OK");
    }

    public static <T> Response consumeOperation(T service, Consumer<T> consumer, Response.Status status, String message) {
        try {
            consumer.accept(service);
            return buildSuccessResponse(status, message);
        } catch (RuntimeException e) {
            return buildFailedResponseByException(e);
        }
    }

    private static Response.Status getStatus(RuntimeException e) {
        if (e instanceof NotFoundException) {
            return Response.Status.NOT_FOUND;
        } else if (e instanceof IllegalArgumentException) {
            return Response.Status.CONFLICT;
        }
        return Response.Status.BAD_REQUEST;
    }
    
    private static Response buildSuccessResponse(Response.Status status, String message) {
        return buildResponse(status, message);
    }

    private static Response buildFailedResponseByException(RuntimeException exception) {
        return buildResponse(getStatus(exception), exception.getMessage());
    }

    private static Response buildResponse(Response.Status status, String message) {
        return Response.status(status.getStatusCode(), message).build();
    }
}
