package ru.hse.spb.kazakov.index

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
import ru.hse.spb.kazakov.BookLocationStore
import ru.hse.spb.kazakov.Datastore

private const val LOCATIONS_PER_PAGE = 5

fun main(args: Array<String>) {
    val bookLocStore = BookLocationStore(Datastore.instance)

    val server = embeddedServer(Netty, port = 8080) {
        routing {
            static("static") {
                resources()
            }

            get("/unreviewed_locations") {
                val locations = bookLocStore.getUnreviewedLocations(LOCATIONS_PER_PAGE)
                call.respondText(toJSON(locations))
            }

            post("/quote_review") {
                val parameters = call.receive<ValuesMap>()
                val id = ObjectId(parameters["id"])
                val action = parameters["action"]

                val location = bookLocStore.getById(id)
                if (action == "inc") {
                    location.incUserRating()
                } else if (action == "dec") {
                    location.decUserRating()
                }
                bookLocStore.save(location)
            }

            get("/reviewed_locations") {
                val locations = bookLocStore.getReviewedLocations()
                call.respondText(toJSON(locations))
            }
        }
    }
    server.start(wait = true)
}

