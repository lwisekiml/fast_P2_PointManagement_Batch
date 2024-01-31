package me.wisekim.fcp.point;

import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

public class PointCustomRepositoryImpl extends QuerydslRepositorySupport implements PointCustomRepository {
    public PointCustomRepositoryImpl() {
        super(Point.class);
    }

    /**
     * select
     * w.user_id,
     * sum(p.amount)
     * from point p
     * inner join point_wallet w
     * on p.point_wallet_id = w.id
     * where p.is_expired = 1
     * and p.is_used = 0
     * and p.expire_date = ‘2021-01-01’
     * group by p.point_wallet_id;
     */
    @Override
    public Page<ExpiredPointSummary> sumByExpiredDate(LocalDate alarmCriteriaDate, Pageable pageable) {
        QPoint point = QPoint.point;
        JPQLQuery<ExpiredPointSummary> query = from(point)
                .select(
                        new QExpiredPointSummary(
                                point.pointWallet.userId,
                                point.amount.sum().coalesce(BigInteger.ZERO)
                        )
                )
                .where(point.expired.eq(true))
                .where(point.used.eq(false))
                .where(point.expireDate.eq(alarmCriteriaDate))
                .groupBy(point.pointWallet);
        List<ExpiredPointSummary> expiredPointList = getQuerydsl().applyPagination(pageable, query).fetch();
        long elementCount = query.fetchCount();
        return new PageImpl<>(
                expiredPointList,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),
                elementCount
        );
    }

    @Override
    public Page<ExpiredPointSummary> sumBeforeExpireDate(LocalDate alarmCriteriaDate, Pageable pageable) {
        QPoint point = QPoint.point;
        JPQLQuery<ExpiredPointSummary> query = from(point)
                .select(
                        new QExpiredPointSummary(
                                point.pointWallet.userId,
                                point.amount.sum().coalesce(BigInteger.ZERO)
                        )
                )
                .where(point.expired.eq(false))
                .where(point.used.eq(false))
                .where(point.expireDate.lt(alarmCriteriaDate))
                .groupBy(point.pointWallet);
        List<ExpiredPointSummary> expiredPointList = getQuerydsl().applyPagination(pageable, query).fetch();
        long totalElement = query.fetchCount();
        return new PageImpl<>(
                expiredPointList,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()),
                totalElement
        );
    }
}
