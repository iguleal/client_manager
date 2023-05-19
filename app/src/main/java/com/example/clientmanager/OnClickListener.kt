package com.example.clientmanager

import com.example.clientmanager.model.Client

interface OnClickListener {
    fun onClick(id: Int)

    fun onLongClickListener(client: Client, position: Int)
}