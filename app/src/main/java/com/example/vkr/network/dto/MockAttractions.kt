package com.example.vkr.network.dto

import com.example.vkr.logic.models.Attraction

object MockAttractions {

    val list: List<Attraction> = listOf(
        Attraction(
            name = "Музей М.А. Булгакова",
            phoneNumber = "+7 495 695-53-08",
            email = "ex@goslitmuz.ru",
            address = "Москва, ул. Спиридоновка, 2, стр. 1\n«Пушкинская», «Баррикадная», «Арбатская»",
            images = listOf(
                "https://classpic.ru/wp-content/uploads/2016/02/15222/Hitryj-minon.jpg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRnTQ04WdzI8_nx_D7_gGQK5nyjsunQOHNm5g&s",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTnkXX1msb3FcwUKdveOb4VJ_8dlsezqUlqEQ&s",
            ),
            description = "Музей М.А. Булгакова — первый в России музей, посвящённый жизни " +
                    "и творчеству писателя. Расположен в знаменитой «нехорошей квартире» №50 " +
                    "на Большой Садовой, где Михаил Афанасьевич жил с 1921 по 1924 год. " +
                    "Именно здесь происходят события романа «Мастер и Маргарита». " +
                    "В экспозиции представлены личные вещи писателя, рукописи и фотографии.",
            workingHours = "ВТ, ПТ, СБ, ВС — 11:00–18:00 (касса до 17:30)\n" +
                    "СР, ЧТ — 11:00–21:00 (касса до 20:30)\n" +
                    "ПН — выходной день",
            price = 400,
            audios = listOf("https://samplelib.com/mp3/sample-12s.mp3"),
            videos = listOf("https://samplelib.com/preview/mp4/sample-20s.mp4"),
            url = "https://bulgakovmuseum.ru/tickets"
        )
    )

    fun first(): Attraction = list.first()

    fun byName(name: String): Attraction? = list.find { it.name == name }
}