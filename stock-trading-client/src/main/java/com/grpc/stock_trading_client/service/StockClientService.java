package com.grpc.stock_trading_client.service;

import com.stocks.grpc.StockRequest;
import com.stocks.grpc.StockResponse;
import com.stocks.grpc.StockTradingServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class StockClientService {

    // Example for unary request:
    /*
    @GrpcClient("stockService")  // matches `grpc.client.stockService.address` in properties
    private StockTradingServiceGrpc.StockTradingServiceBlockingStub stockTradingStub;

    public StockResponse getStockPrice(String stockSymbol) {
        StockRequest request = StockRequest.newBuilder()
                .setStockSymbol(stockSymbol)
                .build();

        return stockTradingStub.getStockPrice(request);
    }
    */

    @GrpcClient("stockService")  // matches `grpc.client.stockService.address` in properties
    private StockTradingServiceGrpc.StockTradingServiceStub stockTradingServiceStub;

    public void subscribeStockPrice(String symbol) {
        StockRequest request = StockRequest.newBuilder()
                .setStockSymbol(symbol)
                .build();

        stockTradingServiceStub.subscribeStockPrice(request, new StreamObserver<StockResponse>() {
            @Override
            public void onNext(StockResponse response) {
                System.out.println("Stock price update: "
                        + response.getStockSymbol()
                        + " | Price: " + response.getPrice()
                        + " | Time: " + response.getTimestamp());
            }

            @Override
            public void onError(Throwable t) {
                System.err.println("Error receiving stock price update: " + t.getMessage());
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("Stock price subscription completed.");
            }
        });
    }
}
