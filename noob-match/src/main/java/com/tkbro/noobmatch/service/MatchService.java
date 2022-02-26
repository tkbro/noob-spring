package com.tkbro.noobmatch.service;

import com.tkbro.noobmatch.model.MatchingContext;
import com.tkbro.noobmatch.model.MatchingJoinContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.*;

@Service
public class MatchService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // todo: ThreadFactory 구현체를 만들어서 디버깅을 위해 thread 정보 남기기
    private static ScheduledExecutorService iterateSearchScheduledExecutorService = Executors.newScheduledThreadPool(100);
    private static ScheduledExecutorService iterateStepScheduledExecutorService = Executors.newScheduledThreadPool(100);

    private Map<String, MatchingContext> matchPoolMap = new ConcurrentHashMap<>();

    public void joinMatching(MatchingJoinContext matchingJoinContext) {
        long now = Instant.now().toEpochMilli();
        MatchingContext matchingContext = MatchingContext.builder()
                .userId(matchingJoinContext.getUserId())
                .createdTime(now)
                .tryCount((short) 0)
                .build();

        if (this.matchPoolMap.get(matchingContext.getUserId()) != null) {
            // 어쩌지?
            logger.error("# Matching | already in matching pool. user: {}", matchingContext.getUserId());
            return;
        }

        this.matchPoolMap.put(matchingContext.getUserId(), matchingContext);

        long startDelay = 1000L;
        ScheduledFuture schedule = iterateSearchScheduledExecutorService.schedule(() -> {
                    logger.info("find matching user {}", matchingJoinContext.getUserId());


                    // 검색 로직 임시,
                    // 호스트 매치 유저는 schedule 마다 매칭할 유저를 찾기 위해서 검색을 하고, matchedContexts 에 채우는 것을 목표로 한다.
                    // 이전 컨텍스트와 무관하게 현재 라인부터 호스트 user + 매칭할 타겟을 검색한다.
                    Optional<String> userIdOptional = matchPoolMap.keySet().stream()
                            .filter(key -> key.equals(matchingJoinContext.getUserId())).findFirst();
                    Optional<String> targetIdOptional = matchPoolMap.keySet().stream()
                            .filter(key -> !key.equals(matchingJoinContext.getUserId())).findAny();

                    if (userIdOptional.isPresent() && targetIdOptional.isPresent()) {
                        // 여기 부턴 matching context 의 정보가 라인마다 무결성이 유지 되지 않는것을 고려해야함.
                        // 임시 처리 - 매칭 후보자들을 map 에서 제거. atomic 하게 후보 context 를 매칭 풀에서 제거할 수 있는 구조여야 함.
                        MatchingContext userContext = matchPoolMap.remove(userIdOptional.get());
                        MatchingContext targetContext = matchPoolMap.remove(targetIdOptional.get());

                        List<MatchingContext> matchedContexts = new ArrayList<MatchingContext>();
                        if (userContext != null)
                            matchedContexts.add(userContext);

                        if (targetContext != null)
                            matchedContexts.add(targetContext);

                        if (matchedContexts.isEmpty()) {
                            matchPoolMap.put(userContext.getUserId(), userContext);
                            matchPoolMap.put(targetContext.getUserId(), targetContext);
                        } else if (matchedContexts.size() == 1) {
                            if (userContext != null)
                                matchPoolMap.put(userContext.getUserId(), userContext);

                            if (targetContext != null)
                                matchPoolMap.put(targetContext.getUserId(), targetContext);
                        } else {
                            // set matched
                            for (MatchingContext matched : matchedContexts) {
                                matched.setMatchedTime(now);
                            }

                            // 한번만 호출되는 걸 보장하거나
                            // callback 형태로 host matching user 에게 처리를 넘겨야한다.
                            matchingComplete(matchedContexts);
                        }
                    }
                },
                startDelay,
                TimeUnit.MILLISECONDS);
    }

    private void matchingComplete(List<MatchingContext> matchedContexts) {
        logger.info("Matching success: {}", matchedContexts.toString());
    }

    public int getPoolSize() {
        return this.matchPoolMap.size();
    }
}
