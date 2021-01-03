package com.g3.spot_guide.eventBus

import com.g3.spot_guide.models.Spot
import com.g3.spot_guide.models.SpotReview


object EventBus {

    sealed class Event {
        data class SpotReviewAdded(val newReview: SpotReview, val spot: Spot?) : Event()
        data class SpotReviewDismissed(val spot: Spot) : Event()
    }

    private val subscribers = mutableListOf<EventBusListener>()

    fun subscribeOnEvent(listener: EventBusListener) {
        subscribers.add(listener)
    }

    fun unsubscribe(listener: EventBusListener) {
        try {
            subscribers.remove(listener)
        } catch (e: Exception) {
            // TODO maybe add logging function
        }
    }

    fun postEvent(event: Event) {
        subscribers.forEach { subscriber ->
            subscriber.onEventPosted(event)
        }
    }

}

interface EventBusListener {
    fun onEventPosted(event: EventBus.Event)
}