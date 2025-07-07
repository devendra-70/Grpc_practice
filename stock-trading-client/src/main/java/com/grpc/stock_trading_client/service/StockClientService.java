package com.grpc.stock_trading_client.service;

import com.stocks.grpc.StockRequest;
import com.stocks.grpc.StockResponse;
import com.stocks.grpc.StockTradingServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class StockClientService {

    @GrpcClient("stockService")  // matches `grpc.client.stockService.address`
    private StockTradingServiceGrpc.StockTradingServiceBlockingStub stockTradingStub;

    public StockResponse getStockPrice(String stockSymbol) {
        StockRequest request = StockRequest.newBuilder()
                .setStockSymbol(stockSymbol)
                .build();

        return stockTradingStub.getStockPrice(request);
    }
}
