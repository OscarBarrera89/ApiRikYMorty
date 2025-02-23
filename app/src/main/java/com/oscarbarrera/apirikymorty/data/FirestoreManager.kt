package com.oscarbarrera.apirikymorty.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.oscarbarrera.apirikymorty.data.FirestoreManager.Companion.COLLECTION_CHARACTERS
import com.oscarbarrera.apirikymorty.data.FirestoreManager.Companion.COLLECTION_PLANETAS
import com.oscarbarrera.apirikymorty.model.Characters
import com.oscarbarrera.apirikymorty.model.CharactersDB
import com.oscarbarrera.apirikymorty.model.PlanetaProcedencia
import com.oscarbarrera.apirikymorty.model.PlanetaProcedenciaDB
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await


class FirestoreManager(auth: AuthManager, context: android.content.Context) {
    private val firestore = FirebaseFirestore.getInstance()
    private val userId = auth.getCurrentUser()?.uid

    companion object{
        const val COLLECTION_CHARACTERS = "personajes"
        const val COLLECTION_PLANETAS = "planetas"
    }

    //    Funciones de los personajes
    fun getCharacter(): Flow<List<Characters>> {
        return firestore.collection(COLLECTION_CHARACTERS)
            .whereEqualTo("userId", userId)
            .snapshots()
            .map { qs ->
                qs.documents.mapNotNull { ds ->
                    ds.toObject(CharactersDB::class.java)?.let { PersonajeDB ->
                        Characters(
                            id = ds.id,
                            userId = PersonajeDB.userId,
                            oficio = PersonajeDB.oficio,
                            gender = PersonajeDB.gender,
                            name = PersonajeDB.name,
                            age = PersonajeDB.age,
                            species = PersonajeDB.species,
                            status = PersonajeDB.status
                        )
                    }
                }
            }
    }

    suspend fun addCharacter(personaje: Characters) {
        firestore.collection(COLLECTION_CHARACTERS).add(personaje).await()
    }

    suspend fun updateCharacter(personaje: Characters) {
        val CharacterRef = personaje.id?.let {
            firestore.collection(COLLECTION_CHARACTERS).document(it)
        }
        CharacterRef?.set(personaje)?.await()
    }

    suspend fun deleteCharacterById(id: String) {
        firestore.collection(COLLECTION_CHARACTERS).document(id).delete().await()
    }

    suspend fun getCharacterById(id: String): Characters? {
        val document = firestore.collection(COLLECTION_CHARACTERS).document(id).get().await()

        return document.toObject(CharactersDB::class.java)?.let {
            Characters(
                id = document.id,
                userId = it.userId,
                oficio = it.oficio,
                gender = it.gender,
                name = it.name,
                species = it.species,
                status = it.status,
                age = it.age
            )
        }
    }

    //    Funciones de los planetas
    fun getPlanetasByPersonajeId(personajeId: String): Flow<List<PlanetaProcedencia>> {
        return firestore.collection(COLLECTION_PLANETAS)
            .whereEqualTo("personajeId", personajeId)
            .snapshots()
            .map { qs ->
                qs.documents.mapNotNull { ds ->
                    ds.toObject(PlanetaProcedenciaDB::class.java)?.let { planeta ->
                        PlanetaProcedencia(
                            id = ds.id,
                            personajeId = planeta.personajeId,
                            userId = planeta.userId,
                            nombre = planeta.nombre,
                            descripcion = planeta.descripcion,
                            temperaturaMedia = planeta.temperaturaMedia
                        )
                    }
                }
            }
    }

    suspend fun addPlaneta(planeta: PlanetaProcedencia) {
        firestore.collection(COLLECTION_PLANETAS).add(planeta).await()
    }

    suspend fun updatePlaneta(planeta: PlanetaProcedencia) {
        val PLanetaRef = planeta.id?.let {
            firestore.collection(COLLECTION_PLANETAS).document(it)
        }
        PLanetaRef?.set(planeta)?.await()
    }

    suspend fun deletePlanetaById(id: String) {
        firestore.collection(COLLECTION_PLANETAS).document(id).delete().await()
    }
}



