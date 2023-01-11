package com.mtg.Motugame.stock.controller;

import com.mtg.Motugame.stock.dto.StockDataInfoDto;
import com.mtg.Motugame.stock.service.StockServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class StockController {
    private final StockServiceImpl stockRepository;

    //Request로 받은 random값을 기준으로 주식을 골라 해당 주식의 한 달 종가를 반환
    @GetMapping("/stocks")
    public ResponseEntity<List<StockDataInfoDto>> findRandStockPriceInfo(@RequestParam("index") List<Integer> index) {
        List<StockDataInfoDto> stockDataInfos = stockRepository.getStocksPrices(index);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Total-Count", Integer.toString(stockDataInfos.size()));

        return ResponseEntity.ok().headers(headers).body(stockDataInfos);
    }

    //몇개의 주식 데이터가 존재하는지 헤더에만 담아서 반환하는 메서드
    @RequestMapping(value = "/stocks", method = RequestMethod.HEAD)
    public ResponseEntity<Void> findHeadRandStockPriceInfo() {
        int stockDataInfoSize = stockRepository.getStocksInfo();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Total-Count", Integer.toString(stockDataInfoSize));

        return ResponseEntity.ok().headers(headers).body(null);
    }
}
