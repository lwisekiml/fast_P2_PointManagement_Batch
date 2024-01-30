package me.wisekim.fcp.job.reservation;

import me.wisekim.fcp.BatchTestSupport;
import me.wisekim.fcp.point.Point;
import me.wisekim.fcp.point.PointRepository;
import me.wisekim.fcp.point.reservation.PointReservation;
import me.wisekim.fcp.point.reservation.PointReservationRepository;
import me.wisekim.fcp.point.wallet.PointWallet;
import me.wisekim.fcp.point.wallet.PointWalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

class ExecutePointReservationStepConfigurationTest extends BatchTestSupport {
    @Autowired
    PointReservationRepository pointReservationRepository;
    @Autowired
    PointRepository pointRepository;
    @Autowired
    PointWalletRepository pointWalletRepository;
    @Autowired
    Job executePointReservationJob;

    @Test
    void executePointReservationStep() throws Exception {
        // given
        // point reservation이 있어야 합니다.
        LocalDate earnDate = LocalDate.of(2021, 1, 5);
        PointWallet pointWallet1 = pointWalletRepository.save(
                new PointWallet("user1", BigInteger.valueOf(3000))
        );
        pointReservationRepository.save(
                new PointReservation(
                        pointWallet1,
                        BigInteger.valueOf(1000),
                        earnDate,
                        10
                )
        );

        // when
        // executePointReservationJob을 실행시킵니다.
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("today", "2021-01-05")
                .toJobParameters();
        JobExecution jobExecution = launchJob(executePointReservationJob, jobParameters);

        // then
        // point reservation은 완료처리되어야합니다.
        // point 적입이 생겨야합니다.
        // point wallet의 잔액이 증가해야합니다.
        // then point 적립 2개 확인
        then(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);

        List<PointReservation> reservations = pointReservationRepository.findAll();
        then(reservations).hasSize(1); // 위에서 1개 저장 했으므로 1개
        then(reservations.get(0).isExecuted()).isTrue(); // 반영이 되었다는 검증

        List<Point> points = pointRepository.findAll();
        then(points).hasSize(1);
        then(points.get(0).getAmount()).isEqualByComparingTo(BigInteger.valueOf(1000));
        then(points.get(0).getEarnedDate()).isEqualTo(LocalDate.of(2021, 1, 5));
        then(points.get(0).getExpireDate()).isEqualTo(LocalDate.of(2021, 1, 15));

        // PointWallet의 잔액 확인 3000 -> 4000
        List<PointWallet> wallets = pointWalletRepository.findAll();
        then(wallets).hasSize(1);
        then(wallets.get(0).getAmount()).isEqualByComparingTo(BigInteger.valueOf(4000)); // 예약 적립 실행
    }
}