package com.example.clientmanager.model

import androidx.room.*

@Dao
interface ClientDao {

    @Insert
    fun insert(client: Client)

    @Delete
    fun delete(client: Client): Int

    @Update
    fun update(client: Client)

    @Query("SELECT * FROM Client")
    fun getClients(): List<Client>

    @Query("SELECT * FROM Client WHERE id = :id")
    fun getClientById(id: Int): Client

    @Query("SELECT * FROM Client WHERE isFixed = :isFixed")
    fun getClientByFix(isFixed: Boolean): List<Client>

    @Query("SELECT * FROM Client WHERE isPaid = :isPaid")
    fun getClientByPaid(isPaid: Boolean): List<Client>
}