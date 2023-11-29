package com.example.gieok_moa

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.Date

@Entity(tableName = "user_table")
data class User (
    @PrimaryKey(autoGenerate = true)
    val userId: Long = 0,
    val name: String = ""
)

@Entity(tableName = "snap_table")
data class Snap (
    @PrimaryKey(autoGenerate = true)
    val snapId: Long = 0,
    val createdDate: Date,
    val photoUrl: String = "",
    val comment: String = ""
)

@Entity(tableName = "tag_table")
data class Tag (
    @PrimaryKey(autoGenerate = true)
    val tagID: Long = 0,
    val staus: String = "",
    val color: Color,
    val ownedSnapID: String = ""
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