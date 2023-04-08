package com.tastybeans.timer.application.services

import com.tastybeans.timer.api.events.DayHasPassed
import io.smallrye.mutiny.Multi
import org.eclipse.microprofile.reactive.messaging.Outgoing
import org.slf4j.LoggerFactory
import java.time.Duration
import java.time.LocalDate
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DayHasPassedGenerator {
    private val logger = LoggerFactory.getLogger(DayHasPassedGenerator::class.java)

    @Outgoing("dayHasPassed")
    fun generate(): Multi<DayHasPassed> {
        val date = LocalDate.now()

        return Multi.createFrom().ticks().every(Duration.ofSeconds(1)).map {
            logger.info("Day has passed: {}", date.plusDays(it))
            DayHasPassed(date.plusDays(it))
        }
    }
}