package com.mtg.Motugame.ranking.service;

import com.mtg.Motugame.entity.RankingEntity;
import com.mtg.Motugame.entity.TotalScoreEntity;
import com.mtg.Motugame.entity.UserEntity;
import com.mtg.Motugame.exception.ExceptionMessage;
import com.mtg.Motugame.ranking.dto.RankRequestDto;
import com.mtg.Motugame.ranking.dto.RankResponseDto;
import com.mtg.Motugame.ranking.dto.RankResponseWrapper;
import com.mtg.Motugame.ranking.repository.RankingRepository;
import com.mtg.Motugame.ranking.repository.TotalScoreRepository;
import com.mtg.Motugame.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    private final TotalScoreRepository totalScoreRepository;
    private final UserRepository userRepository;
    
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

    public RankResponseDto insertRank(RankRequestDto rankRequestDto) {

//        RankingEntity ranking = RankingEntity.builder()
//                .name(rankRequestDto.getName())
//                .rate(rankRequestDto.getRate())
//                .totalCash(rankRequestDto.getTotalCash())
//                .build();
//
//        RankingEntity newRank = rankingRepository.save(ranking);
//
//        List<RankingEntity> users = rankingRepository.findAll(Sort.by(Sort.Direction.DESC, "totalCash"));
//
//        int i=1;
//        for(RankingEntity ranked : users){
//            if(ranked.getId() == newRank.getId()){
//                RankResponseDto rankResponseDto = RankResponseDto.builder()
//                        .id(newRank.getId())
//                        .name(newRank.getName())
//                        .rank(i)
//                        .totalCash(newRank.getTotalCash())
//                        .rate(newRank.getRate())
//                        .build();
//                return rankResponseDto;
//            }
//            i++;
//        }

        return null;
    }
}
