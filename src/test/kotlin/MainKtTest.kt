import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class MainKtTest {

    private val categories = listOf(
        Category(4, "��������"),
        Category(5, "�����"),
        Category(6, "�������"),
    )
    private val features = listOf(
        Feature(13, 5, "�����", 2),
        Feature(14, 4, "������ ������", 5),
        Feature(18, 5, "��������", 50),
        Feature(15, 6, "��������", 4),
        Feature(11, 6, "����", 1222),
        Feature(12, 5, "�����", 750),
        Feature(16, 4, "������� ������������", 2500),
        Feature(17, 4, "������", 15),
    )

    private val resultList = getCategoriesWithFeatures(categories, features)
    /* ����� �������������� ������ ������� */
//    private val resultList = getCategoriesWithFeaturesOptimized(categories, features)
//    private val resultList = groupCategoriesWithFeatures(categories, features)


    @Test
    fun first_element_is_category() {
        // categoryId ������ != null
        resultList[0].run {
            assertTrue(
                categoryName != null && featureId == null && featureTitle == null && featureValue == null,
                "������ ������� �� �������� ����������: $this"
            )
        }
    }

    @Test
    fun category_element_content() {
        for (element in resultList) {
            if (element.categoryName != null) {
                assertTrue(
                    element.featureId == null && element.featureTitle == null && element.featureValue == null,
                    "���� ��� ��������� ��������� ����� ������������ ����������, " +
                            "������ �������� ���������: $element",
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
                    "���� ��� ��������� ������������ ����� ������������ ����������, " +
                            "������ �������� �����������: $element",
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
                    "���� ��� ��������� ���������� ����� ������������ ����������, " +
                            "������ �������� ��������: $element",
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
                // ��� ������� ����� ����� ���, ����� ����� ��������� ��� �����������
                assertEquals(
                    element.categoryId, currentCategoryId,
                    "�������� ������� ������������. " +
                            "� ��������� $currentCategoryId ������������ ����������� ��������� ${element.categoryId}. " +
                            "������ �������� �����������: $element"
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
                    "�������� �� ������������� ����������� ���������, " +
                            "id_���������=$currentCategoryId, id_���������=${element.categoryId}. "
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
                        /* ����� ����������� ������ ��� ������� � ��� �� id ���������: ������ ����������� ��� ��������.
                        � ��������� ������ ���� ��� ���������� ��� �������� � �������� id,
                        ���� ����� ��� ����� ���������, �������� ������������.
                        �������� id ������� ����������� �������, ��������� ��������� - ���� ������. */
                        assertEquals(
                            this[i + 1].categoryId, currentCategoryId,
                            "� ��������� � id=$currentCategoryId ����������� ��������. " +
                                    "��������� ����������� ���������: ${this[i]}"
                        )
                    } catch (e: ArrayIndexOutOfBoundsException) {
                        fail(
                            "� ��������� � id=$currentCategoryId ����������� ��������. " +
                                    "��������� ����������� ���������: ${this[i]}"
                        )
                    }

                }
            }
        }
    }

    /* ����� �������������� ������� ����-�������, ���������� ��� �����
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
 �� ������� ����� ����� ���, ��� ��� �������� �����������.
 ������� � Sealed Class'�� ���� �� ����������� ������� �����: ����� ���� �� ���������, ��� ������� ��������
 ����������� ������-�� ������ ( assertIs<>() ).
*/
