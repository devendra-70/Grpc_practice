package com.grpc.stock_trading_server.repository;

import com.grpc.stock_trading_server.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock,Long> {
}
