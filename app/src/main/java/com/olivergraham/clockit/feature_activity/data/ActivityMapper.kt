package com.olivergraham.clockit.feature_activity.data
import com.olivergraham.clockit.feature_activity.domain.model.Activity


fun ActivityEntity.toActivity(): Activity {
    return Activity(
        name = name,
        color = color
    )
}

fun Activity.toActivityEntity(): ActivityEntity {
    return ActivityEntity(
        name = name,
        color = color
    )
}
