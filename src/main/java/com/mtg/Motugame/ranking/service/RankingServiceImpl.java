package com.mtg.Motugame.ranking.service;

import com.mtg.Motugame.entity.*;
import com.mtg.Motugame.exception.ExceptionMessage;
import com.mtg.Motugame.ranking.dto.RankRequestDto;
import com.mtg.Motugame.ranking.dto.RankResponseDto;
import com.mtg.Motugame.ranking.dto.ScoreInfo;
import com.mtg.Motugame.ranking.repository.ScoreRecordRepository;
import com.mtg.Motugame.ranking.repository.TotalScoreRepository;
import com.mtg.Motugame.stock.repository.StockInfoRepository;
import com.mtg.Motugame.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.mtg.Motugame.entity.TotalScoreEntity;
import com.mtg.Motugame.entity.UserEntity;
import com.mtg.Motugame.ranking.dto.RankResponseWrapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {
    private final ScoreRecordRepository scoreRecordRepository;

    private final TotalScoreRepository totalScoreRepository;

    private final UserRepository userRepository;

    private final StockInfoRepository stockInfoRepository;


    public void saveScore(RankRequestDto rankRequestDto) {
        List<ScoreInfo> scoreInfoList = rankRequestDto.getScoreInfoList();

        UserEntity userEntity = saveUser(rankRequestDto);

        TotalScoreEntity totalScore = totalScoreRepository.save(TotalScoreEntity.builder()
                .profit(rankRequestDto.getTotalProfit())
                .totalYield(rankRequestDto.getTotalYield())
                .user(userEntity)
                .build());

        for (ScoreInfo scoreInfo : scoreInfoList) {
            StockInfoEntity stockInfoEntity = stockInfoRepository.findById(scoreInfo.getStockCode())
                    .orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NO_DATA_ERROR));

            scoreRecordRepository.save(ScoreRecordEntity.builder()
                    .totalScore(totalScore)
                    .stockInfo(stockInfoEntity)
                    .profit(scoreInfo.getProfit())
                    .yield(scoreInfo.getYield())
                    .build());
        }
    }

    @Override
    public RankResponseDto getRank(RankRequestDto rankRequestDto) {
        return RankResponseDto.builder()
                .nickname(rankRequestDto.getNickname())
                .profit(rankRequestDto.getTotalProfit())
                .yield(rankRequestDto.getTotalYield())
                .rank(findRank(rankRequestDto.getNickname(), rankRequestDto.getTotalProfit()))
                .build();
    }

    private UserEntity saveUser(RankRequestDto rankRequestDto) {
        Optional<UserEntity> findData = userRepository.findByNickname(rankRequestDto.getNickname());
        if (findData.isEmpty()) {
            return userRepository.save(UserEntity.builder()
                    .nickname(rankRequestDto.getNickname())
                    .build());
        }
        return findData.get();
    }

    public int findRank(String nickname, BigDecimal profit) {
        List<TotalScoreEntity> rankList = totalScoreRepository.findAllByOrderByProfitDesc();
        int rank = 1;

        for (TotalScoreEntity totalScoreEntity : rankList) {
            if (nickname.equals(totalScoreEntity.getUser().getNickname())
                    && profit.equals(totalScoreEntity.getProfit()))
                break;

            rank++;
        }

        return rank;
    }

    @Override
    public List<RankResponseDto> getSortedRank(int cnt) {
        List<RankResponseWrapper> users = totalScoreRepository.findRank(cnt);
        List<RankResponseDto> list = new ArrayList<>();


        if (users.isEmpty()) {
            throw new IllegalArgumentException(ExceptionMessage.NO_DATA_ERROR);
        }

        for (RankResponseWrapper element : users) {
            Optional<UserEntity> userEntity = userRepository.findById(element.getUserId());
            userEntity.orElseThrow(() -> new IllegalArgumentException(ExceptionMessage.NO_DATA_ERROR));


            RankResponseDto rankResponseDto = RankResponseDto.builder()
                    .rank(element.getNum())
                    .nickname(userEntity.get().getNickname())
                    .profit(element.getProfit())
                    .yield(element.getTotalYield())
                    .build();
            list.add(rankResponseDto);
        }
        return list;
    }

    public Integer getHeadRank() {
        return totalScoreRepository.findAll().size();
    }

}
