package com.example.vkr.storage.repositories
import com.example.vkr.storage.models.StationEntity
import androidx.room.withTransaction
import com.example.vkr.logic.repositories.StationRepository
import com.example.vkr.network.api.StationApi
import com.example.vkr.storage.AppDatabase
import com.example.vkr.storage.dao.CellDao
import com.example.vkr.storage.dao.StationDao
import com.example.vkr.storage.models.CellTower

import javax.inject.Inject
import kotlin.collections.first
import kotlin.collections.isNotEmpty

class StationRepositoryImpl @Inject constructor(
    private val api: StationApi,
    private val db: AppDatabase, // Инжектим саму БД для транзакций
    private val stationDao: StationDao,
    private val cellDao: CellDao
) : StationRepository {
    companion object {
        private const val CACHE_DURATION_MS = 24 *  1000L
    }

    override suspend fun getStations(): List<StationEntity> {
        val cachedStations = stationDao.getAllStations()
        val isCacheValid = cachedStations.isNotEmpty() &&
                (System.currentTimeMillis() - cachedStations.first().cachedAt < CACHE_DURATION_MS)

        if (isCacheValid) {
            return cachedStations
        }

        // Идем в сеть
        val response = api.getAllStations()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                val stationEntities = mutableListOf<StationEntity>()
                val cellEntities = mutableListOf<CellTower>()

                // Мапим данные
                body.stations.forEach { stationDto ->
                    stationEntities.add(
                        StationEntity(
                            id = stationDto.id,
                            name = stationDto.name,
                            branch = stationDto.branch,
                            latitude = stationDto.latitude,
                            longitude = stationDto.longitude
                        )
                    )

                    stationDto.cellTowers.forEach { cellDto ->
                        cellEntities.add(
                            CellTower(
                                stationId = stationDto.id, // ПРИВЯЗКА К СТАНЦИИ
                                cid = cellDto.cid,
                                lac = cellDto.lac,
                                mcc = cellDto.mcc,
                                mnc = cellDto.mnc,
                                radio = cellDto.radio
                            )
                        )
                    }
                }

                // Сохраняем в БД внутри транзакции
                db.withTransaction {
                    stationDao.deleteAll() // Каскадно удалит и вышки благодаря ForeignKey
                    stationDao.insertAll(stationEntities)
                    cellDao.insertAll(cellEntities)
                }

                return stationEntities
            }
        }

        return cachedStations
    }

    override suspend fun getStationByCellTower(
        cid: String, lac: String, mcc: String, mnc: String, radio: String
    ): StationEntity? = stationDao.getStationByCellTower(cid, lac, mcc, mnc, radio)



}