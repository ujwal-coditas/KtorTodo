package com.ujwalthote.repository

import com.ujwalthote.model.ToDo
import com.ujwalthote.model.TodoDraft

class InMemoryTodoRepository : TodoRepository {

    private val todoList = mutableListOf<ToDo>()

    override fun getAllTodos(): List<ToDo> = todoList

    override fun getTodo(id: Int): ToDo? = todoList.firstOrNull { it.id == id }
    override fun addTodo(todoDraft: TodoDraft): ToDo {
        val todo = ToDo(
            id = todoList.size + 1,
            title = todoDraft.title,
            isDone = todoDraft.isDone
        )
        todoList.add(todo)
        return todo
    }

    override fun removeTodo(id: Int): Boolean {
        return todoList.removeIf { it.id == id }
    }

    override fun updateTodo(id: Int, todoDraft: TodoDraft): Boolean {
        val todo = todoList.firstOrNull { it.id == id } ?: return false
        todo.title = todoDraft.title
        todo.isDone = todoDraft.isDone
        return true
    }
}