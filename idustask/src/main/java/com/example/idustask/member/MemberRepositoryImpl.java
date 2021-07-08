package com.example.idustask.member;

import com.example.idustask.member.dto.MemberResponseDto;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import static com.example.idustask.order.QOrderInfo.orderInfo;


//Repository명 + Impl >> 명명규칙 해당이름으로 정의하면 jpa에서 매핑해줌
public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Member> findAllByMembers(Pageable pageable, String name, String email) {
                QueryResults<Member> result = (QueryResults<Member>) queryFactory.select(orderInfo.member)
                .from(orderInfo)
                .join(orderInfo.member)
                .where(
                        orderInfo.id.in( JPAExpressions.select(orderInfo.id.max())
                        .from(orderInfo).groupBy(orderInfo.member.id))
                        , eqName(name)
                        , eqEmail(email)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();



        return new PageImpl<Member>(result.getResults(),pageable,result.getTotal());

    }

    private BooleanExpression eqName(String name) {
        if (StringUtils.isEmpty(name)) {
            return null;
        }
        return orderInfo.member.name.eq(name);
    }

    private BooleanExpression eqEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return null;
        }
        return orderInfo.member.email.eq(email);
    }
}




