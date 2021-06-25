package com.ujwalthote

import com.ujwalthote.model.ToDo
import com.ujwalthote.model.TodoDraft
import com.ujwalthote.repository.InMemoryTodoRepository
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(CallLogging)
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
        }
    }
    routing {

        val respository = InMemoryTodoRepository()

        get("/") {
            call.respondText("Hello todo list")
        }

        get("/todos") {
            call.respond(respository.getAllTodos())
        }

        get("/todos/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            id?.run {
                val todo = respository.getTodo(this)
                todo?.run {
                    call.respond(todo)
                } ?: kotlin.run {
                    call.respond(HttpStatusCode.NotFound, "Todo with id:$id doesn't exist")
                }
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "Invalid id")
            }
        }

        post("/todos") {
            val todoDraft = call.receive<TodoDraft>()
            val todo = respository.addTodo(todoDraft)
            call.respond(todo)
        }

        put("/todos/{id}") {
            val todoDraft = call.receive<TodoDraft>()
            val id = call.parameters["id"]?.toIntOrNull()
            id?.run {
                val isUpdated = respository.updateTodo(id, todoDraft = todoDraft)
                if (isUpdated)
                    call.respond(HttpStatusCode.OK)
                else
                    call.respond(HttpStatusCode.NotFound)
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "bad id")
            }
        }

        delete("/todos/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            id?.run {
                val isRemoved = respository.removeTodo(id)
                if (isRemoved)
                    call.respond(HttpStatusCode.OK)
                else
                    call.respond(HttpStatusCode.NotFound)
            } ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest, "bad id")
            }
        }
    }
}

