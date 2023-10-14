import org.junit.jupiter.api.BeforeEach
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

    @BeforeEach
    fun setUp() {

    }

    @Test
    fun getCategoriesWithFeatures() {
        val resultList = getCategoriesWithFeatures(categories, features)
        // check first element
        resultList[0].run {
            assertNotNull(categoryId); assertNotNull(categoryName)
            assertNull(featureId); assertNull(featureTitle); assertNull(featureValue)
        }
        // // check the order and the contents of all elements
        var currentCategoryId = 0
        for (element in resultList) {
            if (element.categoryName != null) {
                //messages
                assertNotNull(element.categoryId)
                assertNull(element.featureId)
                assertNull(element.featureTitle)
                assertNull(element.featureValue)
                currentCategoryId = element.categoryId
                //check for the end element
            }
            else if (element.featureTitle != null) {
                assertNotNull(element.categoryId)
                assertNotNull(element.featureId)
                assertNotNull(element.featureValue)
                // element.categoryName is always null here
                assertEquals(element.categoryId, currentCategoryId)
            }
            else {
                assertNotNull(element.categoryId)
                // element.categoryName is always null here
                assertNull(element.featureId)
                // element.featureTitle is always null here
                assertNull(element.featureValue)
                assertEquals(element.categoryId, currentCategoryId)
            }
        }

    }

    @Test
    fun getCategoriesWithFeaturesOptimized() {
    }

    @Test
    fun groupCategoriesWithFeatures() {
    }
}

/*
 �� ������� ����� ����� ���, ��� ��� ������ �����������.
 ������� � Sealed Class'�� ���� �� ����������� ������� �����: ����� ���� �� ���������, ��� ������� ��������
 ����������� ������-�� ������ ( assertIs<>() ).
*/
