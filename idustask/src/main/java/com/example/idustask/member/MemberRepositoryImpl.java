package com.example.idustask.member;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static com.example.idustask.order.QOrderInfo.orderInfo;
import static com.example.idustask.member.QMember.member;


public class MemberRepositoryImpl implements MemberRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public MemberRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<Member> findAllByMembers(Pageable pageable, String name, String email) {
        List<OrderSpecifier> ORDERS = getAllOrderSpecifiers(pageable);

        QueryResults<Member> result = (QueryResults<Member>) queryFactory.select(orderInfo.member)
                .from(orderInfo)
                .join(orderInfo.member)
                .where(
                        orderInfo.id.in( JPAExpressions.select(orderInfo.id.max())
                        .from(orderInfo).groupBy(orderInfo.member.id))
                        , eqName(name)
                        , eqEmail(email)
                )
                        .orderBy(ORDERS.stream().toArray(OrderSpecifier[]::new))
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

    private List<OrderSpecifier> getAllOrderSpecifiers(Pageable pageable) {

        List<OrderSpecifier> ORDERS = new ArrayList<>();

        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                switch (order.getProperty()) {
                    case "id":
                        OrderSpecifier<?> orderId = QueryDslUtil.getSortedColumn(direction, orderInfo.member, "id");
                        ORDERS.add(orderId);
                        break;
                    case "email":
                        OrderSpecifier<?> orderEmail = QueryDslUtil.getSortedColumn(direction, orderInfo.member, "email");
                        ORDERS.add(orderEmail);
                        break;
                    case "name":
                        OrderSpecifier<?> orderName = QueryDslUtil.getSortedColumn(direction, orderInfo.member, "name");
                        ORDERS.add(orderName);
                        break;
                    case "nickName":
                        OrderSpecifier<?> orderNickName = QueryDslUtil.getSortedColumn(direction, orderInfo.member, "nickName");
                        ORDERS.add(orderNickName);
                        break;
                    default:
                        break;
                }
            }
        }

        return ORDERS;
    }

}


