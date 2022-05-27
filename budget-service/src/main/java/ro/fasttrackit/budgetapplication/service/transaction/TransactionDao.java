package ro.fasttrackit.budgetapplication.service.transaction;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ro.fasttrackit.budgetapplication.model.entity.Transaction;
import ro.fasttrackit.budgetapplication.utils.Criteria;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
@AllArgsConstructor
public class TransactionDao {

    private final EntityManager entityManager;

    public List<Transaction> find(Criteria criteria) {
        StringBuilder query = new StringBuilder("SELECT t FROM Transaction t ");
        if (criteria.getFilterOptions() != null) {
            query.append("WHERE ");
            criteria.getFilterOptions().forEach(filterOption -> query
                    .append(filterOption.getFieldName())
                    .append(" ").append(filterOption.getOperator())
                    .append(" ").append(filterOption.getValue()).append(" "));
        }
        if (criteria.getSortOptions() != null) {
            query.append("ORDER BY ");
            criteria.getSortOptions().forEach(sortOption -> query
                    .append(sortOption.getProperty()).append(" ")
                    .append(sortOption.getDirection()).append(" "));
        }

        if (criteria.getSize() > 0) {
            query.append("LIMIT ").append(criteria.getSize()).append(" ");
        }

        if (criteria.getPage() > 0) {
            query.append("OFFSET ").append(criteria.getPage() * criteria.getSize()).append(" ");
        }

        return entityManager.createQuery(query.toString(), Transaction.class).getResultList();
    }

    public Page<Transaction> findUsingCriteriaBuilder(Criteria criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<Transaction> criteriaQuery = cb.createQuery(Transaction.class);
        Root<Transaction> transaction = criteriaQuery.from(Transaction.class);

        List<Predicate> predicates = new ArrayList<>();
        if (criteria.getFilterOptions() != null) {
            List<Predicate> filterPredicates = new ArrayList<>();
            criteria.getFilterOptions().forEach(filterOption -> {
                        if (filterOption.getOperator().equals("=")) {
                            predicates.add(cb.equal(transaction.get(filterOption.getFieldName()), filterOption.getValue()));
                        }
                        if (filterOption.getOperator().equals("<")) {
                            predicates.add(cb.lessThan(transaction.get(filterOption.getFieldName()), filterOption.getValue()));
                        }
                        if (filterOption.getOperator().equals(">")) {
                            predicates.add(cb.greaterThan(transaction.get(filterOption.getFieldName()), filterOption.getValue()));
                        }
                        if (filterOption.getOperator().equals("<=")) {
                            predicates.add(cb.lessThanOrEqualTo(transaction.get(filterOption.getFieldName()), filterOption.getValue()));
                        }
                        if (filterOption.getOperator().equals(">=")) {
                            predicates.add(cb.greaterThanOrEqualTo(transaction.get(filterOption.getFieldName()), filterOption.getValue()));
                        }
                        if (filterOption.getOperator().equalsIgnoreCase("LIKE")) {
                            predicates.add(cb.like(transaction.get(filterOption.getFieldName()),
                                    "%" + filterOption.getValue() + "%"));
                        }
                        if (filterOption.getOperator().equalsIgnoreCase("NOT LIKE")) {
                            predicates.add(cb.notLike(transaction.get(filterOption.getFieldName()),
                                    "%" + filterOption.getValue() + "%"));

                        }
                        if (filterOption.getOperator().equalsIgnoreCase("IN")) {
                            predicates.add(transaction.get(filterOption.getFieldName()).in(filterOption.getValue()));
                        }
                    }
            );
        }

        List<Order> orders = new ArrayList<>();
        if (criteria.getSortOptions() != null) {
            criteria.getSortOptions().forEach(sortOption -> {
                if (sortOption.getDirection().equalsIgnoreCase("ASC")) {
                    orders.add(cb.asc(transaction.get(sortOption.getProperty())));
                }
                if (sortOption.getDirection().equalsIgnoreCase("DESC")) {
                    orders.add(cb.desc(transaction.get(sortOption.getProperty())));
                }
            });
        }

        List<Sort.Order> orderList = new ArrayList<>();
        if (criteria.getSortOptions() != null) {
            criteria.getSortOptions().forEach(sortOption -> orderList
                    .add(new Sort.Order(
                            Sort.Direction.fromString(sortOption.getDirection()),
                            sortOption.getProperty())));
        }

        Predicate[] predicatesArray = new Predicate[predicates.size()];
        predicates.toArray(predicatesArray);

        criteriaQuery.where(predicatesArray);
        criteriaQuery.orderBy(orders);

        PageRequest pageRequest = PageRequest.of(criteria.getPage(), criteria.getSize(), Sort.by(orderList));

        List<Transaction> resultList = entityManager.createQuery(criteriaQuery)
                .setFirstResult((int) pageRequest.getOffset())
                .setMaxResults(pageRequest.getPageSize())
                .getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);

        countQuery.where(predicatesArray);
        Root<Transaction> transactionRootCount = countQuery.from(Transaction.class);
        countQuery.select(cb.count(transactionRootCount)).where(predicatesArray);
        Long count = entityManager.createQuery(countQuery).getSingleResult();
        System.out.println("Total count: " + count);

        return new PageImpl<>(resultList, pageRequest, count);
    }
}