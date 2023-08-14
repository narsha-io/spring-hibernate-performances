package io.narsha.spring.jpa.troubleshooting.aop;

import io.narsha.spring.jpa.troubleshooting.entity.Book;
import io.narsha.spring.jpa.troubleshooting.entity.Continent;
import io.narsha.spring.jpa.troubleshooting.entity.Country;
import io.narsha.spring.jpa.troubleshooting.entity.Region;
import io.narsha.spring.jpa.troubleshooting.entity.Review;
import io.narsha.spring.jpa.troubleshooting.entity.ReviewId;
import io.narsha.spring.jpa.troubleshooting.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jooq.lambda.Unchecked;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.LocalTime;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Slf4j
@Aspect
@Configuration
@RequiredArgsConstructor
public class TimerAspect {

    private final SessionFactory factory;

    @Around("@annotation(Timer)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) {
        return loadWithTimer(joinPoint.getSignature().getName(), Unchecked.supplier(joinPoint::proceed));
    }

    private Object loadWithTimer(String name, Supplier<?> supplier) {
        var start = LocalTime.now();
        var res = supplier.get();
        var end = LocalTime.now();
        log.info("{} duration : {}ms", name, Duration.between(start, end).toMillis());

        var stats = factory.getStatistics();

        log.info("{} insert queries", stats.getEntityInsertCount());
        log.info("{} select queries", stats.getPrepareStatementCount());

        Stream.of(stats.getEntityNames()).map(Unchecked.function(Class::forName)).forEach(clazz -> {
            var entityStat = stats.getEntityStatistics(clazz.getName());
            if (entityStat.getLoadCount() > 0) {
                log.info("{} loaded entities for class {}", entityStat.getLoadCount(), clazz.getSimpleName());
            }
        });
 
        stats.clear();

        return res;
    }

}
