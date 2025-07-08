package com.grpc.stock_trading_server.service;

import com.grpc.stock_trading_server.entity.Stock;
import com.grpc.stock_trading_server.repository.StockRepository;
import com.stocks.grpc.*;
import io.grpc.stub.StreamObserver;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@GrpcService
public class StockTradingServiceImpl extends StockTradingServiceGrpc.StockTradingServiceImplBase {

    private final StockRepository stockRepository;

    public StockTradingServiceImpl(StockRepository stockRepository){
        this.stockRepository=stockRepository;
    }

    // unary server method
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

    @Override
    public void subscribeStockPrice(StockRequest request,StreamObserver<StockResponse> responseObserver){
        String symbol=request.getStockSymbol();


        //simulate price fluctuations
        try {
            for (int i = 0; i <= 10; i++) {
                StockResponse stockResponse = StockResponse.newBuilder()
                        .setStockSymbol(symbol)
                        .setPrice(new Random().nextDouble(200))
                        .setTimestamp(Instant.now().toString())
                        .build();

                responseObserver.onNext(stockResponse);
                TimeUnit.SECONDS.sleep(1);
            }

            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }


    //client straming
    @Override
    public StreamObserver<StockOrder> bulkStockOrder(StreamObserver<OrderSummary> responseObserver){

        return new StreamObserver<StockOrder>() {

            private int totalOrder=0;
            private double totalAmount=0;
            private int successCount=0;

            @Override
            public void onNext(StockOrder stockOrder) {
                totalOrder++;
                totalAmount+= stockOrder.getPrice()* stockOrder.getQuantity();
                successCount++;
                System.out.println("Recieved order : "+stockOrder);

            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Server unable to process request"+throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                OrderSummary orderSummary=OrderSummary.newBuilder()
                        .setTotalOrders(totalOrder)
                        .setSuccessCount(successCount)
                        .setTotalAmount(totalAmount)
                        .build();


                responseObserver.onNext(orderSummary);
                responseObserver.onCompleted();
            }
        };
    }
}
