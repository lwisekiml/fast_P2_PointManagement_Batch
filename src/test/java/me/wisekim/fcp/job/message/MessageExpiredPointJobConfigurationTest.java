package me.wisekim.fcp.job.message;

import me.wisekim.fcp.BatchTestSupport;
import me.wisekim.fcp.message.Message;
import me.wisekim.fcp.message.MessageRepository;
import me.wisekim.fcp.point.Point;
import me.wisekim.fcp.point.wallet.PointWallet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

class MessageExpiredPointJobConfigurationTest extends BatchTestSupport {
    @Autowired
    Job messageExpiredPointJob;
    @Autowired
    MessageRepository messageRepository;

    @Test
    void messageExpiredPointJob() throws Exception {
        // given
        // 포인트 지갑을 생성합니다.
        // 오늘(9/6) 만료시킨 포인트 적립 내역을 생성합니다. (expiredDate = 어제)
        LocalDate earnDate = LocalDate.of(2021, 1, 1);
        LocalDate expireDate = LocalDate.of(2021, 9, 5);
        LocalDate notExpireDate = LocalDate.of(2025, 12, 31);
        PointWallet pointWallet1 = pointWalletRepository.save(
                new PointWallet("user1", BigInteger.valueOf(3000))
        );
        PointWallet pointWallet2 = pointWalletRepository.save(
                new PointWallet("user2", BigInteger.ZERO)
        );

        // 5개 만료
        pointRepository.save(new Point(pointWallet2, BigInteger.valueOf(1000), earnDate, expireDate, false, true));
        pointRepository.save(new Point(pointWallet2, BigInteger.valueOf(1000), earnDate, expireDate, false, true));
        pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, expireDate, false, true));
        pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, expireDate, false, true));
        pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, expireDate, false, true));
        pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, notExpireDate));
        pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, notExpireDate));
        pointRepository.save(new Point(pointWallet1, BigInteger.valueOf(1000), earnDate, notExpireDate));

        // when
        // 아래와 같은 메시지가 생성되었는지 확인합니다.
        // 3000 포인트 만료
        // 2021-09-06 기준 3000포인트가 만료되었습니다.
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("today", "2021-09-06")
                .toJobParameters();
        JobExecution execution = launchJob(messageExpiredPointJob, jobParameters);

        // then
        // user1 : 3천원 포인트 만료 메시지
        // user2 : 2천원 포인트 만료 메시지
        then(execution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        List<Message> messages = messageRepository.findAll();
        then(messages).hasSize(2);
        Message message1 = messages.stream().filter(item -> item.getUserId().equals("user1")).findFirst().orElseGet(null);
        then(message1).isNotNull();
        then(message1.getTitle()).isEqualTo("3000 포인트 만료"); // user1에게 전달되는 메시지 제목
        then(message1.getContent()).isEqualTo("2021-09-06 기준 3000 포인트가 만료되었습니다."); // user1에게 전달되는 메시지 내용
        Message message2 = messages.stream().filter(item -> item.getUserId().equals("user2")).findFirst().orElseGet(null);
        then(message2).isNotNull();
        then(message2.getTitle()).isEqualTo("2000 포인트 만료"); // user2에게 전달되는 메시지 제목
        then(message2.getContent()).isEqualTo("2021-09-06 기준 2000 포인트가 만료되었습니다."); // user1에게 전달되는 메시지 내용
    }

    @Test
    void messageExpiredPointJob_no_parameter() throws Exception {
        // given
        // when & then
        Assertions.assertThrows(
                JobParametersInvalidException.class,
                () -> launchJob(messageExpiredPointJob, null),
                "job parameter today is required"
        );
    }
}