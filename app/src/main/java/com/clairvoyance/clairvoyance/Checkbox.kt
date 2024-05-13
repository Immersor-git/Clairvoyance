package com.clairvoyance.clairvoyance

data class Checkbox(
    val isCompleted: Boolean,
    val desc: String
) {
    constructor() : this(false, "") {}
    fun imageResource(): Int = if (this.isCompleted) R.drawable.checkmark_complete else R.drawable.checkmark_uncomplete
}
