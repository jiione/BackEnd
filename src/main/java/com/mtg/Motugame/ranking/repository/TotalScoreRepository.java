package com.mtg.Motugame.ranking.repository;

import com.mtg.Motugame.entity.TotalScoreEntity;
import com.mtg.Motugame.ranking.dto.RankResponseWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TotalScoreRepository extends JpaRepository<TotalScoreEntity, Long> { //Long은 id의 타입이다.
    @Query(value = "SELECT *, row_number() over(order by profit desc) as num\n" +
            "FROM total_score limit 30 offset 30 * (:cnt - 1)", nativeQuery = true)
    List<RankResponseWrapper> findRank(@Param(value = "cnt") int cnt);
}
