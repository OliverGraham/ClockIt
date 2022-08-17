package com.olivergraham.clockit.data
import com.olivergraham.clockit.domain.model.Activity


fun ActivityEntity.toActivity(): Activity {
    return Activity(
        name = name
    )
}

fun Activity.toActivityEntity(): ActivityEntity {
    return ActivityEntity(
        name = name
    )
}
