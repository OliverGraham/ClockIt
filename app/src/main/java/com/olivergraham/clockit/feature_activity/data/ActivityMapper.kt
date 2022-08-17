package com.olivergraham.clockit.feature_activity.data
import com.olivergraham.clockit.feature_activity.domain.model.Activity


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
