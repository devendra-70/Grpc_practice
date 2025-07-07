package com.grpc.stock_trading_client;

import com.grpc.stock_trading_client.service.StockClientService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StockTradingClientApplication implements CommandLineRunner {

	private final StockClientService stockClientService;

	// Constructor injection
	public StockTradingClientApplication(StockClientService stockClientService) {
		this.stockClientService = stockClientService;
	}

	public static void main(String[] args) {
		SpringApplication.run(StockTradingClientApplication.class, args);
	}

	@Override
	public void run(String... args) {
		System.out.println("Starting gRPC streaming client...");

		// 🔑 Correct method call:
		stockClientService.subscribeStockPrice("AAPL");

		// ✅ If you also want the unary call, uncomment:
        /*
        StockResponse response = stockClientService.getStockPrice("AAPL");
        System.out.println("Unary gRPC response: " + response);
        */
	}
}
