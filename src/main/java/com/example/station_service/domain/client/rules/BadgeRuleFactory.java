package com.example.station_service.domain.client.rules;

import com.example.station_service.domain.client.entity.BadgeType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BadgeRuleFactory {

    private final NormalBadgeRule normalBadgeRule;
    private final GoldBadgeRule goldBadgeRule;

    public BadgeRule getRule(BadgeType type) {
        return switch (type) {
            case NORMAL -> normalBadgeRule;
            case GOLD -> goldBadgeRule;
        };
    }
}
