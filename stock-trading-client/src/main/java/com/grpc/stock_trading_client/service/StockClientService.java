package com.grpc.stock_trading_client.service;

import com.stocks.grpc.*;
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

    public void placeBulkOrders(){

        StreamObserver<OrderSummary> responseObserver=new StreamObserver<OrderSummary>() {
            @Override
            public void onNext(OrderSummary orderSummary) {
                System.out.println("Order Summary Recieved from Server:");
                System.out.println("Total orders: "+orderSummary.getTotalOrders());
                System.out.println("Successful Orders: "+orderSummary.getSuccessCount());
                System.out.println("Total Amount: "+orderSummary.getTotalAmount());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("Order summary Recieved error from server"+throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Order Summary Recieved from server");
            }

            };

        StreamObserver<StockOrder> requestObserver=stockTradingServiceStub.bulkStockOrder(responseObserver);

        //send multiple stream of stock order message/request

        try{
            requestObserver.onNext(
                    StockOrder.newBuilder()
                            .setOrderId("1")
                            .setStockSymbol("AAPL")
                            .setOrderType("BUY")
                            .setPrice(150.5)
                            .setQuantity(10)
                            .build());

            requestObserver.onNext(
                    StockOrder.newBuilder()
                            .setOrderId("2")
                            .setStockSymbol("GOOGL")
                            .setOrderType("SELL")
                            .setPrice(2700)
                            .setQuantity(5)
                            .build());

            requestObserver.onNext(
                    StockOrder.newBuilder()
                            .setOrderId("3")
                            .setStockSymbol("TSLA")
                            .setOrderType("BUY")
                            .setPrice(700)
                            .setQuantity(8)
                            .build());


            //done sending orders
            requestObserver.onCompleted();
        } catch (Exception e) {
            requestObserver.onError(e);
        }
        }
}
