package ingsw.mobile.dao

import ingsw.mobile.entity.Review
import ingsw.mobile.entity.Structure

interface ReviewDAO {
    fun addNewReview(review: Review): Boolean
    fun getReviewStructure(structure: Structure): MutableList<Review>
}