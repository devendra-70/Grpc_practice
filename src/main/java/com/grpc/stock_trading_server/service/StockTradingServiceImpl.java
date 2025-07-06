package com.grpc.stock_trading_server.service;

import com.stocks.grpc.StockRequest;
import com.stocks.grpc.StockResponse;
import com.stocks.grpc.StockTradingServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;

@GrpcService
public class StockTradingServiceImpl extends StockTradingServiceGrpc.StockTradingServiceImplBase {

    @Override
    public void getStockPrice(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        //find stock name -> DB -> map response -> attach to streamobserver to return

    }
}
