package com.example.gieok_moa

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.Date

@Entity(tableName = "user_table")
data class User (
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0,
    var name: String = ""
)

@Entity(tableName = "snap_table")
data class Snap (
    @PrimaryKey(autoGenerate = true)
    var snapId: Long = 0,
    var createdDate: Date,
    var photoUrl: String = "",
    var comment: String = ""
)

@Entity(tableName = "tag_table")
data class Tag (
    @PrimaryKey(autoGenerate = true)
    var tagID: Long = 0,
    var staus: String = "",
    var color: Color,
    var ownedSnapID: Long = 0
)

//snap : tag = 1 : 1
data class SnapAndTag (
    @Embedded
    val snap: Snap,
    @Relation(
        parentColumn = "snapID",
        entityColumn = "ownedSnapID"
    )
    val tag: Tag
)

//user : snap = 1 : N
data class UserAndSnap (
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "userID",
        entityColumn = "ownedUserID"
    )
    val snaps : List<Snap>
)
enum class Color(
    val colorName: String
) {
    RED("빨강"),
    YELLOW("노랑"),
    GREEN("녹색")
}