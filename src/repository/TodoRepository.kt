package com.ujwalthote.repository

import com.ujwalthote.model.ToDo
import com.ujwalthote.model.TodoDraft

interface TodoRepository {

    fun getAllTodos(): List<ToDo>

    fun getTodo(id: Int): ToDo?

    fun addTodo(todoDraft: TodoDraft): ToDo

    fun removeTodo(id: Int): Boolean

    fun updateTodo(id: Int, todoDraft: TodoDraft): Boolean

}