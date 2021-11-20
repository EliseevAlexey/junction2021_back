package com.team13.junction.http

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient(name="brella", url = "https://aalto-event-endpoint-default-dot-brella-sandbox.appspot.com/api/")
interface BrellaHttpClient {

    @RequestMapping(method = [RequestMethod.GET], value = ["/aalto/events/{slug}"])
    fun getEvent(@PathVariable("slug") slug: String): BrellaMeetupResponse
}

data class BrellaMeetupResponse(
   val id: String
)