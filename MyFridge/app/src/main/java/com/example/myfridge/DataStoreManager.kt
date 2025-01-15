package com.example.myfridge

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.myfridge.Screens.Product
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//Declaracion del dataStore, el nombre del archivo sera "fridge_items"
val Context.dataStore by preferencesDataStore(name = "fridge_items")

class DataStoreManager(private val context: Context) {

    //Se crea una clave para indicar donde se almacenaran los productos
    val PRODUCTS_KEY = stringPreferencesKey("products")

    val gson = Gson()

    //Funcion para guardar (O en su defecto eliminar) un producto
    suspend fun saveProducts(products: List<Product>) {

        //Se crea una variable gson en la que se almacena en forma de texto los productos
        val json = gson.toJson(products)
        context.dataStore.edit { preferences ->
            preferences[PRODUCTS_KEY] = json
        }
    }

    //Flow que examina constantemente la lista de productos para mostrarla en pantalla
    val productsFlow: Flow<List<Product>> = context.dataStore.data

        //Transforma el archivo json a una lista, si no encuentra nada devuelve una llave nula y una lista nula
        .map { preferences ->
            val json = preferences[PRODUCTS_KEY] ?: "[]"
            val type = object : TypeToken<List<Product>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        }


}