package com.dailyPT.backend.common.exception;

import com.dailyPT.backend.common.response.CommonResponse;
import com.dailyPT.backend.common.response.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class GlobalExceptionFilterHandler extends OncePerRequestFilter {

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (GradeChangeException e) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, e, ErrorCode.COMMON_CHANGED_USER_GRADE);
        } catch (ExpiredJwtException e) {
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, e, ErrorCode.COMMON_ACCESS_TOKEN_EXPIRE);
        } catch (AuthenticationException | IllegalArgumentException e) {
            setErrorResponse(HttpStatus.FORBIDDEN, response, e, ErrorCode.COMMON_UNAUTHORIZED);
        } catch (BadRequestException | JwtException e) {
            setErrorResponse(HttpStatus.NOT_FOUND, response, e, ErrorCode.COMMON_SYSTEM_ERROR);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex, ErrorCode errorCode){
        response.setStatus(status.value());
        response.setContentType("application/json");
        CommonResponse<Object> fail = CommonResponse.fail(ex.getMessage(),
                errorCode.name(), CommonResponse.ErrorType.ERROR);
        try{
            String result = mapper.writeValueAsString(fail);
            response.getWriter().write(result);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}