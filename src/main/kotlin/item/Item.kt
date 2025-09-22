package org.example.item
// open class Item pour que d'autres classes puissent hériter de cette classe
open class Item (var id : Int,
            var nom : String,
            var description : String) {
    fun afficheDetail() {
        println("Item #${id} : $nom")
        println("Description: $description")
    }
}