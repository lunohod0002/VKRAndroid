package com.example.vkr.network.dto

object MockAttractions {

    val list: List<Attraction> = listOf(
        Attraction(
            name = "Музей М.А. Булгакова",
            phoneNumber = "+7 495 695-53-08",
            email = "ex@goslitmuz.ru",
            address = "Москва, ул. Спиридоновка, 2, стр. 1\n«Пушкинская», «Баррикадная», «Арбатская»",
            images = listOf(
                "https://picsum.photos/id/1015/800/500",
                "https://picsum.photos/id/1019/800/500",
                "https://picsum.photos/id/1024/800/500"
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
            audios = listOf("https://commondatastorage.googleapis.com/codeskulptor-demos/DDR_assets/Kangaroo_MusiQue_-_The_Neverwritten_Role_Playing_Game.mp3"),
            videos = listOf("https://samplelib.com/preview/mp4/sample-20s.mp4"),
            url = "https://bulgakovmuseum.ru/tickets"
        )
    )

    fun first(): Attraction = list.first()

    fun byName(name: String): Attraction? = list.find { it.name == name }
}