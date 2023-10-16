import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class MainKtTest {

    private val categories = listOf(
        Category(4, "телефоны"),
        Category(5, "торты"),
        Category(6, "подарки"),
    )
    private val features = listOf(
        Feature(13, 5, "коржи", 2),
        Feature(14, 4, "размер экрана", 5),
        Feature(18, 5, "сладость", 50),
        Feature(15, 6, "праздник", 4),
        Feature(11, 6, "цена", 1222),
        Feature(12, 5, "масса", 750),
        Feature(16, 4, "ёмкость аккумулятора", 2500),
        Feature(17, 4, "камера", 15),
    )

    private val resultList = getCategoriesWithFeatures(categories, features)
    /* Чтобы протестировать другие функции */
//    private val resultList = getCategoriesWithFeaturesOptimized(categories, features)
//    private val resultList = groupCategoriesWithFeatures(categories, features)


    @Test
    fun first_element_is_category() {
        // categoryId всегда != null
        resultList[0].run {
            assertTrue(
                categoryName != null && featureId == null && featureTitle == null && featureValue == null,
                "Первый элемент не является категорией: $this"
            )
        }
    }

    @Test
    fun category_element_content() {
        for (element in resultList) {
            if (element.categoryName != null) {
                assertTrue(
                    element.featureId == null && element.featureTitle == null && element.featureValue == null,
                    "Одна или несколько категорий имеют неправильное содержание, " +
                            "первая неверная категория: $element",
                )
            }
        }
    }

    @Test
    fun feature_element_content() {
        for (element in resultList) {
            if (element.featureId != null) {
                assertTrue(
                    element.categoryName == null && element.featureTitle != null && element.featureValue != null,
                    "Одна или несколько особенностей имеют неправильное содержание, " +
                            "первая неверная особенность: $element",
                )
            }
        }
    }

    @Test
    fun end_element_content() {
        for (element in resultList) {
            if (element.categoryName == null && element.featureId == null) {
                assertTrue(
                    element.featureTitle == null && element.featureValue == null,
                    "Один или несколько концевиков имеют неправильное содержание, " +
                            "первый неверный концевик: $element",
                )
            }
        }
    }

    @Test
    fun feature_of_current_category() {
        var currentCategoryId = 0
        for (element in resultList) {
            if (element.categoryName != null) {
                currentCategoryId = element.categoryId
            } else if (element.featureId != null) {
                // Эта функция также ловит баг, когда после концевика идёт особенность
                assertEquals(
                    element.categoryId, currentCategoryId,
                    "Неверный порядок особенностей. " +
                            "В категории $currentCategoryId присутствует особенность категории ${element.categoryId}. " +
                            "Первая неверная особенность: $element"
                )
            }
        }
    }

    @Test
    fun end_element_of_current_category() {
        var currentCategoryId = 0
        for (element in resultList) {
            if (element.categoryName != null) {
                currentCategoryId = element.categoryId
            } else if (element.featureId == null) {
                assertEquals(
                    element.categoryId, currentCategoryId,
                    "Концевик не соответствует закрываемой категории, " +
                            "id_категории=$currentCategoryId, id_концевика=${element.categoryId}. "
                )
            }
        }
    }

    @Test
    fun end_element_exists() {
        resultList.run {
            var currentCategoryId = 0
            for (i in this.indices) {
                if (this[i].categoryName != null) {
                    currentCategoryId = this[i].categoryId
                } else if (this[i].featureTitle != null) {
                    try {
                        /* После особенности всегда идёт элемент с той же id категории: другая особенность или концевик.
                        В противном случае либо идёт особеность или концевик с неверным id,
                        либо сразу идёт новая категория, концевик отстутствует.
                        Неверные id ловятся предыдущими тестами, отстутвие концевика - этим тестом. */
                        assertEquals(
                            this[i + 1].categoryId, currentCategoryId,
                            "У категории с id=$currentCategoryId отсутствует концевик. " +
                                    "Последняя особенность категории: ${this[i]}"
                        )
                    } catch (e: ArrayIndexOutOfBoundsException) {
                        fail(
                            "У категории с id=$currentCategoryId отсутствует концевик. " +
                                    "Последняя особенность категории: ${this[i]}"
                        )
                    }

                }
            }
        }
    }

    /* Почти первоначальный вариант тест-функции, породивший все тесты
    @Test
    fun getCategoriesWithFeatures() {
        resultList.run {
            var currentCategoryId = 0
            for (i in this.indices) {
                if (this[i].categoryName != null) {
                    assertNotNull(this[i].categoryId)
                    assertNull(this[i].featureId)
                    assertNull(this[i].featureTitle)
                    assertNull(this[i].featureValue)
                    currentCategoryId = this[i].categoryId
                } else if (this[i].featureTitle != null) {
                    assertNotNull(this[i].categoryId)
                    assertNotNull(this[i].featureId)
                    assertNotNull(this[i].featureValue)
                    // element.categoryName is always null here
                    assertEquals(this[i].categoryId, currentCategoryId)
                    // catch
                    assertEquals(this[i + 1].categoryId, currentCategoryId)
                } else {
                    assertNotNull(this[i].categoryId)
                    // element.categoryName is always null here
                    assertNull(this[i].featureId)
                    // element.featureTitle is always null here
                    assertNull(this[i].featureValue)
                    assertEquals(this[i].categoryId, currentCategoryId)
                }
            }
        }
    }
    */
}

/*
 Моё решение также плохо тем, что его неудобно тестировать.
 Решение с Sealed Class'ом было бы тестировать намного проще: можно было бы проверять, что элемент является
 экземпляром такого-то класса ( assertIs<>() ).
*/
