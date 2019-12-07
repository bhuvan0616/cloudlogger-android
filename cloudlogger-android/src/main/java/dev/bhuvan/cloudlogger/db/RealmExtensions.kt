package dev.bhuvan.cloudlogger.db

import io.realm.Realm
import io.realm.RealmObject
import io.realm.RealmQuery
import io.realm.Sort

typealias Query<T> = RealmQuery<T>

/**
 * Created by Bhuvanesh BS on 07/12/18.
 * Extension functions to perform realm db operations
 */

fun <T : RealmObject> T.saveAndUpdateToDB() {
    getDefaultRealm().transaction { realm ->
        realm.insertOrUpdate(this)
    }
}

inline fun <reified T : RealmObject> updateDbFields(row: T?.() -> Unit) {
    getDefaultRealm().transaction { realm ->
        realm.where(T::class.java)
    }
}

fun <T : RealmObject> T.insertToDb() {
    getDefaultRealm().transaction { realm ->
        realm.insert(this)
    }
}

inline fun <reified T : RealmObject> findFirstFromDB(): T? {
    getDefaultRealm().use { realm ->
        val item: T? = realm.where(T::class.java).findFirst()
        return if (item !== null && RealmObject.isValid(item)) realm.copyFromRealm(item) else null
    }
}

inline fun <reified T : RealmObject> managedFindFirstFromDb(): T? {
    val item: T? = getDefaultRealm().where(T::class.java).findFirst()
    return if (item !== null && RealmObject.isValid(item)) item else null
}

inline fun <reified T : RealmObject> findLastFromDb(query: Query<T>.() -> Query<T>): T? {
    getDefaultRealm().use { realm ->
        val item: T? =
            query(getDefaultRealm().where(T::class.java)).sort("id", Sort.DESCENDING).findFirst()
        return if (item !== null && RealmObject.isValid(item)) realm.copyFromRealm(item) else null
    }
}

inline fun <reified T : RealmObject> findFirstFromDb(query: Query<T>.() -> Query<T>): T? {
    getDefaultRealm().use { realm ->
        val item: T? = query(getDefaultRealm().where(T::class.java)).findFirst()
        return if (item !== null && RealmObject.isValid(item)) realm.copyFromRealm(item) else null
    }
}

/**
 * deletes an item from the db matching given constraint.
 * eg: deleteItemFromDB<RealmObject> { equalTo("fieldId", fieldValue) }
 */
inline fun <reified T : RealmObject> deleteItemFromDB(query: Query<T>.() -> Query<T>): Boolean {
    val item: T? = query(getDefaultRealm().where(T::class.java)).findFirst()
    if (item !== null && RealmObject.isValid(item)) {
        getDefaultRealm().transaction { item.deleteFromRealm() }
        return true
    }
    return false
}


inline fun <reified T : RealmObject> managedFindFirstFromDb(query: Query<T>.() -> Query<T>): T? {
    val item: T? = query(getDefaultRealm().where(T::class.java)).findFirst()
    return if (item !== null && RealmObject.isValid(item)) item else null
}

inline fun <reified T : RealmObject> transactionOnFirstItem(crossinline block: T.() -> Unit) {
    getDefaultRealm().transaction { realm ->
        val item: T? = realm.where(T::class.java).findFirst()
        if (item !== null && RealmObject.isValid(item)) block(item)
    }
}

inline fun <reified T : RealmObject> findAllFromDb(): List<T> {
    getDefaultRealm().use { realm ->
        return realm.copyFromRealm<T>(realm.where(T::class.java).findAll())
    }
}

inline fun <reified T : RealmObject> findAllFromDb(query: Query<T>.() -> Query<T>): List<T> {
    getDefaultRealm().use { realm ->
        return realm.copyFromRealm<T>(query(realm.where(T::class.java)).findAll())
    }
}

fun getDefaultRealm(): Realm = Realm.getDefaultInstance()


fun deleteAllObjectsFromDB() {
    try {
        getDefaultRealm().transaction { realm ->
            realm.removeAllChangeListeners()
            realm.deleteAll()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun <T : RealmObject> deleteObjectFromDB(clazz: Class<T>) {
    getDefaultRealm().transaction { realm ->
        realm.delete(clazz)
    }
}

fun Realm.transaction(realm: (Realm) -> Unit) = use { executeTransaction { realm(this) } }

fun <T : RealmObject> T?.safeClose() {
    try {
        if (this?.isValid == true) {
            if (isManaged) removeAllChangeListeners()
            if (!realm.isClosed) realm.close()
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


