package maxi.med.tableti.exception.handler;

import maxi.med.tableti.dto.ExceptionDto;
import maxi.med.tableti.exception.IncompatibleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(IncompatibleException.class)
    public ResponseEntity<ExceptionDto> handleIncompatibleException(IncompatibleException exception) {
        return buildExceptionDto(exception, HttpStatus.CONFLICT);
    }

    private ResponseEntity<ExceptionDto> buildExceptionDto(Exception exception, HttpStatus status) {
        ExceptionDto exceptionDto = new ExceptionDto(exception.getMessage(), status);
        return new ResponseEntity<>(exceptionDto, status);
    }
}
