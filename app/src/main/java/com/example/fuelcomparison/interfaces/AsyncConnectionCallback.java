package com.example.fuelcomparison.interfaces;


import com.example.fuelcomparison.data.Response;


public interface AsyncConnectionCallback {
    void processRequestResponse(Response response);

    void processConnectionTimeout();
}
