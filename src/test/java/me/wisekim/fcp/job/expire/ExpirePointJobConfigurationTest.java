package me.wisekim.fcp.job.expire;

import me.wisekim.fcp.BatchTestSupport;
import me.wisekim.fcp.point.Point;
import me.wisekim.fcp.point.PointRepository;
import me.wisekim.fcp.point.wallet.PointWallet;
import me.wisekim.fcp.point.wallet.PointWalletRepository;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.BDDAssertions.then;

class ExpirePointJobConfigurationTest extends BatchTestSupport {
    @Autowired
    Job expirePointJob;
    @Autowired
    PointWalletRepository pointWalletRepository;
    @Autowired
    PointRepository pointRepository;
    @Test
    void expirePointJob() throws Exception {
        // given
        LocalDate earnDate = LocalDate.of(2021, 1, 1);
        LocalDate expireDate = LocalDate.of(2021, 1, 3);
        PointWallet pointWallet = pointWalletRepository.save(
                new PointWallet(
                        "user123",
                        BigInteger.valueOf(6000) // 6000원이 든 지갑을 만듬(포인트)
                )
        );
        pointRepository.save(new Point(pointWallet, BigInteger.valueOf(1000), earnDate, expireDate)); // 1000 포인트 적립
        pointRepository.save(new Point(pointWallet, BigInteger.valueOf(1000), earnDate, expireDate)); // 1000 포인트 적립
        pointRepository.save(new Point(pointWallet, BigInteger.valueOf(1000), earnDate, expireDate)); // 1000 포인트 적립

        // when
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("today", "2021-01-04")
                .toJobParameters();
        JobExecution jobExecution = launchJob(expirePointJob, jobParameters);
        // then
        then(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED); // job 정상적으로 종료되었다는 것을 검증함
        List<Point> points = pointRepository.findAll();
        then(points.stream().filter(Point::isExpired)).hasSize(3); // 만료된것이 3개다.
        PointWallet changePointWallet = pointWalletRepository.findById(pointWallet.getId()).orElseGet(null);
        then(changePointWallet).isNotNull();
        then(changePointWallet.getAmount()).isEqualByComparingTo(BigInteger.valueOf(3000)); // 6000포인트에서 3000이 만료되어 3000이 남은
    }
}