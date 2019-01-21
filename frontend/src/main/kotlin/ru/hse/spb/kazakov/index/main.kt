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
import ru.hse.spb.kazakov.mongo.BookLocationStore
import ru.hse.spb.kazakov.mongo.Datastore

private const val LOCATIONS_PER_PAGE = 5

fun main(args: Array<String>) {
    if(args.isEmpty()) {
        println("No IP specified.")
        return
    }

    val bookLocStore = BookLocationStore(Datastore.getInstance(args[0]))

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

            post("/delete") {
                val parameters = call.receive<ValuesMap>()
                val id = ObjectId(parameters["id"])
                bookLocStore. deleteQuote(id)
            }

            get("/positive_rate") {
                val locations = bookLocStore.getPositiveRateLocations()
                call.respondText(toJSON(locations))
            }

            get("/negative_rate") {
                val locations = bookLocStore.getNegativeRateLocations()
                call.respondText(toJSON(locations))
            }

            get("/outgoing_amod") {
                val locations = bookLocStore.getOutgoingAmodLocations()
                call.respondText(toJSON(locations))
            }

            get("/neighbors_adj") {
                val locations = bookLocStore.getNeighborsAdjLocations()
                call.respondText(toJSON(locations))
            }

            get("/sentiment") {
                val locations = bookLocStore.getSentimentLocations()
                call.respondText(toJSON(locations))
            }
        }
    }
    server.start(wait = true)
}

