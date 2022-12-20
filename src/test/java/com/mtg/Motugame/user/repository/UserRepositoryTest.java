package com.mtg.Motugame.user.repository;

import com.mtg.Motugame.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("유저 목록 조회")
    @Test
    void findAllUser(){

    }
    @DisplayName("유저 등록")
    @Test
    void insertUser(){
        //given
        UserEntity user = new UserEntity("qwd5320","jiwon","jiione");

        //when
        UserEntity savedUser = userRepository.save(user);

        // then
        assertThat(savedUser.getId()).isEqualTo(user.getId());
        assertThat(savedUser.getName()).isEqualTo(user.getName());
        assertThat(savedUser.getGameId()).isEqualTo(user.getGameId());
    }

}