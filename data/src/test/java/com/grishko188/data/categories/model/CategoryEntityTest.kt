package com.grishko188.data.categories.model

import android.graphics.Color
import com.grishko188.data.features.categories.model.*
import com.grishko188.domain.features.categories.models.CategoryType
import org.junit.Test
import kotlin.test.assertEquals

class CategoryEntityTest {

    @Test
    fun `test CategoryDataModel_toEntity() function returns CategoryEntity model with correct fields`() {
        val categoryDataModel = CategoryDataModel(
            id = 1,
            title = "Title",
            color = Color.WHITE,
            type = "GLOBAL",
            isMutable = false
        )

        val entity = categoryDataModel.toEntity()

        assertEquals(categoryDataModel.id, entity.id)
        assertEquals(categoryDataModel.title, entity.title)
        assertEquals(categoryDataModel.color, entity.color)
        assertEquals(categoryDataModel.type, entity.type)
        assertEquals(categoryDataModel.isMutable, entity.isMutable)
    }

    @Test
    fun `test CategoryEntity_toDataModel() function returns CategoryDataModel model with correct fields`() {
        val entity = CategoryEntity(
            title = "Title",
            color = Color.WHITE,
            type = CategoryType.GLOBAL.name,
            isMutable = false
        ).apply {
            id = 1
        }

        val dataModel = entity.toDataModel()

        assertEquals(entity.id, dataModel.id)
        assertEquals(entity.title, dataModel.title)
        assertEquals(entity.color, dataModel.color)
        assertEquals(entity.type, dataModel.type)
        assertEquals(entity.isMutable, dataModel.isMutable)
    }
}