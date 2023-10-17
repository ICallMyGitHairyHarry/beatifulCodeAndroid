/* ### Задача
Имея вводные данные, написать функцию, получающую список категорий(List Category), список характеристик(List Feature),
 и преобразующую их в один List элементов, и возвращающую его.
Правила формирования результирующего списка:
- Первый элемент связан с категорией(Category). Хранит в себе всю информацию о категории.
- Далее идут все элементы, связанные с характеристикой(Feature) относящиеся к данной категории.
- После последней характеристики, относящийся к открытой категории, идет элемент,
  сигнализирующий о том, что категория закончилась. Хранит в себе только CategoryId.
Количество элементов не ограничено */

// ### Вводные данные
// Есть следующие Data классы

// Класс категории
data class Category(
    val categoryId: Int,
    val name: String)

// Класс характеристики
data class Feature(
    val featureId: Int,
    val categoryId: Int,
    val title: String,
    var value: Int)

// Список всех доступных категорий можем получить методом
fun getCategories(): List<Category> = listOf(
    Category(1, "дерьвья"),
    Category(2, "кошки"),
    Category(3, "шоколадки"),
    )

// Список характеристик для отображения можем получить методом
fun getFeatures(): List<Feature> = listOf(
    Feature(3, 1, "ствол", 1),
    Feature(4, 2, "лапки", 2),
    Feature(8, 3, "сладость", 50),
    Feature(5, 2, "мурчало", 1),
    Feature(1, 1, "листья", 1000),
    Feature(2, 1, "воздух", 10000),
    Feature(6, 2, "мяучело", 1),
    Feature(7, 3, "какао", 25),
)

// Получаемые данными методами элементы не отсортированы

// Характеристика и категория связаны между собой полем
// val categoryId: Int

// ### Моё решение

data class CategoryOrFeatureOrEndElement(
    val categoryId: Int,
    val categoryName: String? = null,
    val featureId: Int? = null,
    val featureTitle: String? = null,
    var featureValue: Int? = null
)

fun getCategoriesWithFeatures(categories: List<Category>, features: List<Feature>):
        MutableList<CategoryOrFeatureOrEndElement> {
    val categoriesWithFeatures = mutableListOf<CategoryOrFeatureOrEndElement>()

    for (category in categories) {
        categoriesWithFeatures.add(
            CategoryOrFeatureOrEndElement(categoryId = category.categoryId, categoryName = category.name)
        )

        for (feature in features) {
            if (feature.categoryId == category.categoryId) {
                categoriesWithFeatures.add(
                    CategoryOrFeatureOrEndElement(
                        featureId = feature.featureId,
                        categoryId = category.categoryId,
                        featureTitle = feature.title,
                        featureValue = feature.value
                    )
                )
            }
        }

        categoriesWithFeatures.add(
            CategoryOrFeatureOrEndElement(categoryId = category.categoryId)
        )
    }

    return categoriesWithFeatures
}

fun main() {

    val categoriesWithFeatures = getCategoriesWithFeatures(getCategories(), getFeatures())
    categoriesWithFeatures.forEach {
        println(it)
    }

}

/* ### Вариант оптимизации основной функции из моего решения

После добавления характеристики (feature) в итоговый список (categoriesWithFeatures),
она удаляется из списка характеристик (mutableFeatures), поэтому следующая итерация цикла
пройдёт быстрее, так как в списке будет меньше элементов.
Однако в данном случае необходимо создавать mutableFeatures — изменяемый дупликат списка features. */

fun getCategoriesWithFeaturesOptimized(categories: List<Category>, features: List<Feature>):
        MutableList<CategoryOrFeatureOrEndElement> {
    val categoriesWithFeatures = mutableListOf<CategoryOrFeatureOrEndElement>()

    val mutableFeatures = features.toMutableList()

    for (category in categories) {
        categoriesWithFeatures.add(
            CategoryOrFeatureOrEndElement(categoryId = category.categoryId, categoryName = category.name)
        )

        val iterator = mutableFeatures.iterator()
        while (iterator.hasNext()) {
            val feature = iterator.next()
            if (feature.categoryId == category.categoryId) {
                categoriesWithFeatures.add(
                    CategoryOrFeatureOrEndElement(
                        featureId = feature.featureId,
                        categoryId = category.categoryId,
                        featureTitle = feature.title,
                        featureValue = feature.value
                    )
                )
                iterator.remove()
            }
        }

        categoriesWithFeatures.add(
            CategoryOrFeatureOrEndElement(categoryId = category.categoryId)
        )
    }

    return categoriesWithFeatures
}


/* ### Решение от chatGPT
Решение интересное, но не соответствует поставленной задаче.

data class CategoryWithFeatures(
    val categoryId: Int,
    val categoryName: String,
    val features: List<Feature>
)

fun combineCategoriesAndFeatures(categories: List<Category>, features: List<Feature>): List<CategoryWithFeatures> {
    val categoryMap = categories.associateBy { it.categoryId }

    val result = mutableListOf<CategoryWithFeatures>()
    var currentCategory: CategoryWithFeatures? = null

    for (feature in features) {
        if (currentCategory == null || feature.categoryId != currentCategory.categoryId) {
            // Создаем новую категорию, когда текущая категория закончена или еще не начата
            val category = categoryMap[feature.categoryId]
            if (category != null) {
                currentCategory = CategoryWithFeatures(category.categoryId, category.name, mutableListOf())
                result.add(currentCategory)
            }
        }

        // Добавляем характеристику к текущей категории
        currentCategory?.features?.add(feature)
    }

    return result
}

fun main() {
    val categories = getCategories()
    val features = getFeatures()

    val combinedList = combineCategoriesAndFeatures(categories, features)

    // Выводим результат
    for (categoryWithFeatures in combinedList) {
        println("Category: ${categoryWithFeatures.categoryName} (ID: ${categoryWithFeatures.categoryId})")
        for (feature in categoryWithFeatures.features) {
            println(" - Feature: ${feature.title} (ID: ${feature.featureId}), Value: ${feature.value}")
        }
    }
} */


// ### Я также показал своё решение chatGPT и попросил его улучшить. Вот его версия моего решения:

fun groupCategoriesWithFeatures(categories: List<Category>, features: List<Feature>):
        List<CategoryOrFeatureOrEndElement> {
    val groupedFeatures = features.groupBy { it.categoryId }

    val result = mutableListOf<CategoryOrFeatureOrEndElement>()

    for (category in categories) {
        result.add(CategoryOrFeatureOrEndElement(category.categoryId, category.name))

        groupedFeatures[category.categoryId]?.forEach { feature ->
            result.add(CategoryOrFeatureOrEndElement(
                featureId = feature.featureId,
                categoryId = category.categoryId,
                featureTitle = feature.title,
                featureValue = feature.value
            ))
        }

        result.add(CategoryOrFeatureOrEndElement(category.categoryId))
    }

    return result
}

/* В этой версии:
 - Результат работы такой же.
 - Вложенные циклы остались, но теперь появилась ещё groupBy, под капотом которой также цикл.
 - Читаемость кода немного снизилась. */