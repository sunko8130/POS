package com.example.pos.network;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.example.pos.network.Status.ERROR;
import static com.example.pos.network.Status.LOADING;
import static com.example.pos.network.Status.SUCCESS;

public class ApiResponse<T> {

    @NonNull
    public final Status status;
    @Nullable
    public final T data;
    @Nullable
    public final String message;

    private ApiResponse(@NonNull Status status, @Nullable T data,
                        @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }


    public static <T> ApiResponse<T> success(@NonNull T data) {
        return new ApiResponse<>(SUCCESS, data, null);
    }


    public static <T> ApiResponse<T> error(String msg) {
        return new ApiResponse<>(ERROR, null, msg);
    }

    public static <T> ApiResponse<T> loading() {
        return new ApiResponse<>(LOADING, null, null);
    }
    
}