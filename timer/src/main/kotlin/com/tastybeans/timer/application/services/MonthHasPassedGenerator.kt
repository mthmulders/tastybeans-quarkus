package com.tastybeans.timer.application.services

import com.tastybeans.timer.api.events.DayHasPassed
import com.tastybeans.timer.api.events.MonthHasPassed
import io.smallrye.mutiny.Multi
import io.smallrye.mutiny.Uni
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Outgoing
import org.slf4j.LoggerFactory
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class MonthHasPassedGenerator {
    val logger = LoggerFactory.getLogger(MonthHasPassedGenerator::class.java)

    @Incoming("dayHasPassed")
    @Outgoing("monthHasPassed")
    fun handle(msg: Multi<DayHasPassed>): Multi<MonthHasPassed> {
        return msg.filter { evt -> evt.date.dayOfMonth == 1 }
            .map { evt ->
                val month = if(evt.date.month.value == 1) 12 else evt.date.month.value - 1
                val year = if (evt.date.month.value == 1) evt.date.year - 1 else evt.date.year

                logger.info("Month has passed {}/{}", year, month)

                MonthHasPassed(year, month)
            }
    }
}