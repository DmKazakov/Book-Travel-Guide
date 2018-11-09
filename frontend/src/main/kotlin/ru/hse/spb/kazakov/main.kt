package ru.hse.spb.kazakov

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.json.simple.JSONArray
import org.json.simple.JSONObject

private const val LOCATIONS_PER_PAGE = 5

fun main(args: Array<String>) {
    val bookLocStore = BookLocationStore("BookTravelGuide", "127.0.0.1", 27017)

    val server = embeddedServer(Netty, port = 8080) {
        routing {
            get("/") {
                val html = this.javaClass.getResource("/index.html").readText()
                call.respondText(html, ContentType.Text.Html)
            }

            get("/index.css") {
                val css = this.javaClass.getResource("/index.css").readText()
                call.respondText(css, ContentType.Text.CSS)
            }

            get("/index.js") {
                val js = this.javaClass.getResource("/index.js").readText()
                call.respondText(js, ContentType.Text.JavaScript)
            }

            get("/locations_set") {
                val jArray = JSONArray()
                val locations = bookLocStore.getUnreviewedLocations(LOCATIONS_PER_PAGE)
                locations.forEach {
                    val jObject = JSONObject()
                    jObject.put("location", it.location.location)
                    jObject.put("quote", it.location.sentence)
                    jObject.put("id", it.morphiaId)
                    jArray.add(jObject)
                }
                val result = JSONObject()
                result.put("locations", jArray)

                call.respondText(result.toJSONString())
            }
        }
    }
    server.start(wait = true)
}
