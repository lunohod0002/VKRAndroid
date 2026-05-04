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
            price = "Полный/льготный — 400/300 ₽",
            audioUrl = "https://commondatastorage.googleapis.com/codeskulptor-demos/DDR_assets/Kangaroo_MusiQue_-_The_Neverwritten_Role_Playing_Game.mp3",
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            urlRef = "https://bulgakovmuseum.ru/tickets"
        ),
        Attraction(
            name = "Третьяковская галерея",
            phoneNumber = "+7 495 957-07-27",
            email = "info@tretyakov.ru",
            address = "Москва, Лаврушинский пер., 10\n«Третьяковская», «Новокузнецкая»",
            images = listOf(
                "https://picsum.photos/id/1040/800/500",
                "https://picsum.photos/id/1043/800/500",
                "https://picsum.photos/id/1051/800/500",
                "https://picsum.photos/id/1062/800/500"
            ),
            description = "Государственная Третьяковская галерея — крупнейший в мире музей " +
                    "русского изобразительного искусства. Основана купцом Павлом Третьяковым " +
                    "в 1856 году. Коллекция насчитывает более 180 000 произведений: иконы, " +
                    "живопись, графика и скульптура от XI века до наших дней. " +
                    "Здесь можно увидеть «Троицу» Рублёва, «Явление Христа народу» Иванова, " +
                    "работы Репина, Сурикова, Шишкина и Васнецова.",
            workingHours = "ВТ, СР, ВС — 10:00–18:00 (касса до 17:00)\n" +
                    "ЧТ, ПТ, СБ — 10:00–21:00 (касса до 20:00)\n" +
                    "ПН — выходной день",
            price = "Полный/льготный — 700/400 ₽",
            audioUrl = "https://commondatastorage.googleapis.com/codeskulptor-demos/DDR_assets/Kangaroo_MusiQue_-_The_Neverwritten_Role_Playing_Game.mp3",
            videoUrl = null,
            urlRef = "https://www.tretyakovgallery.ru/tickets/"
        ),
        Attraction(
            name = "Парк «Зарядье»",
            phoneNumber = "+7 495 531-05-00",
            email = "info@zaryadyepark.ru",
            address = "Москва, ул. Варварка, 6, стр. 1\n«Китай-город»",
            images = listOf(
                "https://picsum.photos/id/1018/800/500",
                "https://picsum.photos/id/1036/800/500",
                "https://picsum.photos/id/1059/800/500"
            ),
            description = "Парк «Зарядье» — первый ландшафтный парк, построенный в Москве " +
                    "за последние 50 лет. Открыт в 2017 году рядом с Красной площадью. " +
                    "На территории воссозданы четыре природные зоны России: лес, степь, " +
                    "тундра и заливные луга. Главная достопримечательность — парящий мост, " +
                    "уходящий на 70 метров над Москвой-рекой.",
            workingHours = "Парк открыт круглосуточно\nПавильоны: 10:00–22:00",
            price = "Вход в парк бесплатный",
            audioUrl = null,
            videoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4",
            urlRef = "https://www.zaryadyepark.ru/"
        ),
        Attraction(
            name = "Музей космонавтики",
            phoneNumber = "+7 499 750-23-00",
            email = "info@kosmo-museum.ru",
            address = "Москва, просп. Мира, 111\n«ВДНХ»",
            images = listOf(
                "https://picsum.photos/id/1011/800/500",
                "https://picsum.photos/id/1027/800/500"
            ),
            description = "Мемориальный музей космонавтики основан в 1981 году в честь " +
                    "20-летия полёта Юрия Гагарина. Расположен в стилобате монумента " +
                    "«Покорителям космоса». В коллекции — личные вещи космонавтов, " +
                    "оригинальные спускаемые аппараты, скафандры, образцы лунного грунта " +
                    "и легендарные собаки-космонавты Белка и Стрелка.",
            workingHours = "ВТ, СР, ПТ, ВС — 11:00–19:00\n" +
                    "ЧТ, СБ — 11:00–21:00\n" +
                    "ПН — выходной день",
            price = "Полный/льготный — 450/150 ₽",
            audioUrl = "https://commondatastorage.googleapis.com/codeskulptor-demos/DDR_assets/Sevish_-__nbsp_.mp3",
            videoUrl = null,
            urlRef = "https://kosmo-museum.ru/visitors/tickets"
        ),
        Attraction(
            name = "Большой театр",
            phoneNumber = "+7 495 455-55-55",
            email = "info@bolshoi.ru",
            address = "Москва, Театральная пл., 1\n«Театральная», «Охотный Ряд»",
            images = listOf(
                "https://picsum.photos/id/1067/800/500",
                "https://picsum.photos/id/1074/800/500",
                "https://picsum.photos/id/1080/800/500"
            ),
            description = "Государственный академический Большой театр России — один из " +
                    "крупнейших в мире и самый известный театр оперы и балета в стране. " +
                    "Основан в 1776 году. Современное здание построено архитектором " +
                    "Альбертом Кавосом и открыто в 1856 году. Знаменит своим квадратом " +
                    "квадригой Аполлона работы Петра Клодта.",
            workingHours = "Кассы: ежедневно 11:00–20:00\nСпектакли по расписанию",
            price = "От 600 ₽ до 15 000 ₽",
            audioUrl = null,
            videoUrl = null,
            urlRef = "https://www.bolshoi.ru/timetable/"
        )
    )

    fun first(): Attraction = list.first()

    fun byName(name: String): Attraction? = list.find { it.name == name }
}