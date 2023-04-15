package com.tastybeans.timer.infrastructure

import com.tastybeans.timer.api.events.DayHasPassed
import com.tastybeans.timer.api.events.MonthHasPassed
import io.smallrye.reactive.messaging.rabbitmq.OutgoingRabbitMQMetadata
import org.eclipse.microprofile.reactive.messaging.Incoming
import org.eclipse.microprofile.reactive.messaging.Message
import org.eclipse.microprofile.reactive.messaging.Metadata
import org.eclipse.microprofile.reactive.messaging.Outgoing

class IntegrationEvents {
    @Incoming("dayHasPassed")
    @Outgoing("dayHasPassedIntegrationEvent")
    fun publishDayHasPassed(evt: DayHasPassed): Message<DayHasPassed> {
        val metadata = OutgoingRabbitMQMetadata.builder().withRoutingKey("timer.day-has-passed.v1").build()
        return Message.of(evt, Metadata.of(metadata))
    }

    @Incoming("monthHasPassed")
    @Outgoing("monthHasPassedIntegrationEvent")
    fun publishMontHasPassed(evt: MonthHasPassed): Message<MonthHasPassed> {
        val metadata = OutgoingRabbitMQMetadata.builder().withRoutingKey("timer.month-has-passed.v1").build()
        return Message.of(evt, Metadata.of(metadata))
    }
}