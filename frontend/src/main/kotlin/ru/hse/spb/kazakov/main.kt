package ru.hse.spb.kazakov

import io.ktor.application.*
import io.ktor.content.resources
import io.ktor.content.static
import io.ktor.request.receive
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.ValuesMap
import org.bson.types.ObjectId
import org.json.simple.JSONArray
import org.json.simple.JSONObject

private const val LOCATIONS_PER_PAGE = 5

fun main(args: Array<String>) {
    val bookLocStore = BookLocationStore(Datastore.instance)

    val server = embeddedServer(Netty, port = 8080) {
        routing {
            static("static") {
                resources()
            }

            get("/locations_set") {
                val jArray = JSONArray()
                val locations = bookLocStore.getUnreviewedLocations(LOCATIONS_PER_PAGE)

                locations.map {
                    JSONObject().apply {
                        put("location", it.location.location)
                        put("quote", it.location.sentence)
                        put("id", it.morphiaId.toHexString())
                    }
                }.forEach { jArray.add(it) }

                val result = JSONObject()
                result["locations"] = jArray

                call.respondText(result.toJSONString())
            }

            post("/quote_review") {
                val parameters = call.receive<ValuesMap>()
                val id = parameters["id"].let { ObjectId(it) }
                val action = parameters["action"]

                val location = bookLocStore.getById(id)
                if (action == "inc") {
                    location.incUserRating()
                } else if (action == "dec") {
                    location.decUserRating()
                }
                bookLocStore.save(location)
            }
        }
    }
    server.start(wait = true)
}
