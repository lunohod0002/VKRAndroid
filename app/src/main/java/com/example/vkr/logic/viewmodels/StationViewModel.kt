package com.example.vkr.logic.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.vkr.network.RetrofitClient
import com.example.vkr.storage.dao.CellDao
import com.example.vkr.network.dto.Station
import com.example.vkr.network.api.StationApi
import com.example.vkr.network.api.StationApiImpl
import com.example.vkr.network.dto.StationAttractionInfo
import com.example.vkr.storage.TokenStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StationViewModel(
    private val stationApi: StationApiImpl,
) : ViewModel() {

    private val resultLiveMutable = MutableLiveData<Station?>()
    val resultLive: LiveData<Station?> = resultLiveMutable

    fun getStationInfo(name:String,branch:String) {

        viewModelScope.launch(Dispatchers.IO) {
//            if (name == "Краснопресненская" && branch == "Кольцевая") {
//                resultLiveMutable.postValue(
//                    Station(
//                        1,
//                        "Краснопресненская",
//                        "Кольцевая",
//                        listOf(
//                            "Банкомат",
//                            "Туалет",
//                            "Цветочный магазин",
//
//                            ),
//                        "Станция Краснопресненская открыта на последнем, третьем, участке Кольцевой линии" +
//                                " «Белорусская — Парк Культуры» 14 марта 1954 года. Станция получила название по улице " +
//                                "Красная Пресня, на которой расположен наземный вестибюль, улице, известной своим " +
//                                "революционным прошлым 1905 и 1917 годов. Именно здесь проходили самые ожесточенные " +
//                                "бои восстания 1905 года. Естественно, что эти революционные события послужили темой " +
//                                "оформления станции. Наземный павильон сооружен в виде огромной ротонды. " +
//                                "Т.А.Ильина, В.И.Алешина. Мощные высокие каннелированные колонны светлого цвета " +
//                                "поддерживают свод. На фризе расположена надпись с названием станции. Подземный зал продолжает революционную " +
//                                "тему наземного вестибюля. Мощные пилоны облицованы темно-красным мрамором, а их " +
//                                "верхняя часть — белым.",
//                        listOf(
//                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRORR-H0sKjh4SyWxqX87z2c_i8DfMtiXsCRA&s",
//                            "https://upload.wikimedia.org/wikipedia/commons/thumb/e/e4/MoscowMetro_Krasnopresnenskaya_HW1_6326.jpg/500px-MoscowMetro_Krasnopresnenskaya_HW1_6326.jpg",
//                            "https://img-fotki.yandex.ru/get/6729/5104826.150/0_9988f_1dbbf376_XXXL.jpg",
//                        ),
//                        listOf("https://videos.pexels.com/video-files/18226432/18226432-hd_1080_1920_60fps.mp4"),
//                        listOf("https://samplelib.com/mp3/sample-12s.mp3"),
//                        listOf(
//                            StationAttractionInfo(
//                                1,
//                                "Московский зоопарк",
//                                300,
//                                "https://moscowzoo.moscow/wp-content/uploads/2022/12/%D0%B2%D1%85%D0%BE%D0%B4-%D0%BC%D0%BE%D1%81%D0%BA%D0%BE%D0%B2%D1%81%D0%BA%D0%B8%D0%B9-%D0%B7%D0%BE%D0%BE%D0%BF%D0%B0%D1%80%D0%BA.jpg",
//                                200
//                            ),
//                            StationAttractionInfo(
//                                2,
//                                "Храм Великомученика Георгия Победоносца в Грузинах",
//                                0,
//                                "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/29/12/a9/03/caption.jpg?w=1100&h=-1&s=1",
//                                250
//                            ),
//                            StationAttractionInfo(
//                                3,
//                                "Московский планетарий",
//                                900,
//                                "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/32/ff/21/e6/caption.jpg?w=1400&h=800&s=1",
//                                450
//                            )
//
//                        )
//                    )
//                )
//            }
//            else  if (name == "Белорусская" && branch == "Кольцевая") {
//                resultLiveMutable.postValue(
//                    Station(
//                        1,
//                        "Белорусская",
//                        "Кольцевая",
//                        listOf(
//                            "Банкомат",
//                            "Магазин электроники",
//                            "Цветочный магазин",
//
//                            ),
//                        "«Белорусская» Кольцевой линии метро приняла своих первых пассажиров 30 января 1952 года." +
//                                "Архитектура подземного зала посвящена теме белорусского народа. Необычное мягкое " +
//                                "освещение залов с помощью оригинальных настенных бра впечатляет, кажется, будто " +
//                                "оказался в храме. Бра установлены не только в среднем, но и в боковых залах, они " +
//                                "выполнены из мрамора и стекла. Красиво оформлены рельефом своды залов, они связывают " +
//                                "восприятие трех залов в единое целое. Рельефы содержат повторяющиеся рисунки ржаных " +
//                                "колосьев в выпуклых квадратах. Пилоны облицованы мрамором кремового цвета. Плафоны " +
//                                "свода среднего зала украшают 12 мраморных панно на тему «Расцвет Советской " +
//                                "Белоруссии».",
//                        listOf(
//                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQd-NJ6bJc4XalYrkohci9A8E-dZhtVZ2IR8w&s",
//                            "https://avatars.mds.yandex.net/get-altay/16114102/2a0000019a54b08637e4dd0f96ed8a471184/orig",
//                            "https://storage.yandexcloud.net/moskvichmag/uploads/2024/04/211203-IDR_5163-scaled-e1713550443889.jpg",
//                        ),
//                        listOf("https://media.istockphoto.com/id/929486240/video/the-flow-of-passengers-on-the-escalators-and-underpass-of-the-metro-station-time-lapse.mp4?s=mp4-640x640-is&k=20&c=PcVnxxIfeoCIU9TOEyTTJOnDp5T9ITc64LVoJxQsqFM="),
//                        listOf("https://samplelib.com/mp3/sample-12s.mp3"),
//                        listOf(
//                            StationAttractionInfo(
//                                1,
//                                "Дом культуры имени Зуева",
//                                800,
//                                "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/0a/2f/01/5f/caption.jpg?w=1400&h=800&s=1",
//                                200
//                            ),
//                            StationAttractionInfo(
//                                2,
//                                "Памятник Прощание славянки",
//                                0,
//                                "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/1d/54/9b/c8/caption.jpg?w=900&h=-1&s=1",
//                                350
//                            ),
//                            StationAttractionInfo(
//                                3,
//                                "Галерея живописного искусства Московского Союза Художников",
//                                1200,
//                                "https://avatars.mds.yandex.net/get-altay/16494614/2a0000019d217d160a2417c8177ab7f7e3ee/L_height",
//                                750
//                            )
//
//                        )
//                    )
//                )
//            } else {
//                resultLiveMutable.postValue(
//                    Station(1,
//                        ",", ",",
//                        listOf(
//                            "1",
//                            "2",
//                        ),
//                        "2",
//                        listOf(
//                            "https://classpic.ru/wp-content/uploads/2016/02/15222/Hitryj-minon.jpg",
//                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRnTQ04WdzI8_nx_D7_gGQK5nyjsunQOHNm5g&s",
//                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTnkXX1msb3FcwUKdveOb4VJ_8dlsezqUlqEQ&s",
//                        ),
//                        listOf("https://samplelib.com/preview/mp4/sample-20s-360p.mp4"), listOf("https://samplelib.com/mp3/sample-12s.mp3"),listOf(
//                            StationAttractionInfo(
//                                1,
//                                "Московский зоопарк и еее кп цв йцвйцв йцв цц",
//                                200,
//                                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR-_HGFkK3BhjnV2sHm3zv11GcRlHEjIq4zGg&s",
//                                200
//                            ),
//                            StationAttractionInfo(1,"Зоопарк", 200,"https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR-_HGFkK3BhjnV2sHm3zv11GcRlHEjIq4zGg&s",500)
//                        )
//                    )
//                )
//            }
            val station = stationApi.getStationByNameAndBranch(name = name, branch = branch)
                resultLiveMutable.postValue(station.body())


        }
    }


    companion object {
        fun Factory(context: Context): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
            ): T {
                val stationApi = StationApiImpl(
                    RetrofitClient.stationApi(TokenStorage(context = context.applicationContext))
                )
                return StationViewModel(
                    stationApi
                ) as T
            }
        }
    }
}