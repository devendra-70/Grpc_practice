package com.grpc.stock_trading_server.service;

import com.grpc.stock_trading_server.entity.Stock;
import com.grpc.stock_trading_server.repository.StockRepository;
import com.stocks.grpc.StockRequest;
import com.stocks.grpc.StockResponse;
import com.stocks.grpc.StockTradingServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;

@GrpcService
public class StockTradingServiceImpl extends StockTradingServiceGrpc.StockTradingServiceImplBase {

    private final StockRepository stockRepository;

    public StockTradingServiceImpl(StockRepository stockRepository){
        this.stockRepository=stockRepository;
    }

    @Override
    public void getStockPrice(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        //find stock name -> DB -> map response -> attach to streamobserver to return

        String stockSymbol=request.getStockSymbol();

        Stock stock= stockRepository.findByStockSymbol(stockSymbol);

        StockResponse stockResponse=StockResponse.newBuilder()
                .setStockSymbol(stock.getStockSymbol())
                .setPrice(stock.getPrice())
                .setTimestamp(stock.getLastUpdated().toString())
                .build();

        responseObserver.onNext(stockResponse);
        responseObserver.onCompleted();



    }
}
