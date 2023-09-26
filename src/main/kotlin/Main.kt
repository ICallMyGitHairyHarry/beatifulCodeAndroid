/* ### ������
���� ������� ������, �������� �������, ���������� ������ ���������(List Category), ������ �������������(List Feature),
 � ������������� �� � ���� List ���������, � ������������ ���.
������� ������������ ��������������� ������:
- ������ ������� ������ � ����������(Category). ������ � ���� ��� ���������� � ���������.
- ����� ���� ��� ��������, ��������� � ���������������(Feature) ����������� � ������ ���������.
- ����� ��������� ��������������, ����������� � �������� ���������, ���� �������,
  ��������������� � ���, ��� ��������� �����������. ������ � ���� ������ CategoryId.
���������� ��������� �� ���������� */

// ### ������� ������
// ���� ��������� Data ������

// ����� ���������
data class Category(
    val categoryId: Int,
    val name: String)

// ����� ��������������
data class Feature(
    val featureId: Int,
    val categoryId: Int,
    val title: String,
    var value: Int)

// ������ ���� ��������� ��������� ����� �������� �������
fun getCategories(): List<Category> = listOf(
    Category(1, "�������"),
    Category(2, "�����"),
    Category(3, "���������"),
    )

// ������ ������������� ��� ����������� ����� �������� �������
fun getFeatures(): List<Feature> = listOf(
    Feature(3, 1, "�����", 1),
    Feature(4, 2, "�����", 2),
    Feature(8, 3, "��������", 50),
    Feature(5, 2, "�������", 1),
    Feature(1, 1, "������", 1000),
    Feature(2, 1, "������", 10000),
    Feature(6, 2, "�������", 1),
    Feature(7, 3, "�����", 25),
)

// ���������� ������� �������� �������� �� �������������

// �������������� � ��������� ������� ����� ����� �����
// val categoryId: Int

// ### �� �������

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

/* ### ������� ����������� �������� ������� �� ����� �������

����� ���������� �������������� (feature) � �������� ������ (categoriesWithFeatures),
��� ��������� �� ������ ������������� (mutableFeatures), ������� ��������� �������� �����
������ �������, ��� ��� � ������ ����� ������ ���������.
������ � ������ ������ ���������� ��������� mutableFeatures � ���������� �������� ������ features. */

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


/* ### ������� �� chatGPT
������� ����������, �� �� ������������� ������������ ������.

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
            // ������� ����� ���������, ����� ������� ��������� ��������� ��� ��� �� ������
            val category = categoryMap[feature.categoryId]
            if (category != null) {
                currentCategory = CategoryWithFeatures(category.categoryId, category.name, mutableListOf())
                result.add(currentCategory)
            }
        }

        // ��������� �������������� � ������� ���������
        currentCategory?.features?.add(feature)
    }

    return result
}

fun main() {
    val categories = getCategories()
    val features = getFeatures()

    val combinedList = combineCategoriesAndFeatures(categories, features)

    // ������� ���������
    for (categoryWithFeatures in combinedList) {
        println("Category: ${categoryWithFeatures.categoryName} (ID: ${categoryWithFeatures.categoryId})")
        for (feature in categoryWithFeatures.features) {
            println(" - Feature: ${feature.title} (ID: ${feature.featureId}), Value: ${feature.value}")
        }
    }
} */


// ### � ����� ������� ��� ������� chatGPT � �������� ��� ��������. ��� ��� ������ ����� �������:

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

/* � ���� ������:
 - ��������� ������ ����� ��.
 - ��������� ����� ��������, �� ������ ��������� ��� groupBy, ��� ������� ������� ����� ����.
 - ���������� ���� ������� ���������. */