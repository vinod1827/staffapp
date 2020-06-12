package com.kleverowl.staffapp.utils

object Page {

    const val HOME = 0
    const val SEARCH = 1
    const val ORDERS = 2
    const val FEEDS = 3
    const val ACCOUNTS = 4
}

object RequestCode {
    const val ADD_CATALOGUE = 101
}

object RequestCodes {
    const val PICK_SWITCH_ICON = 104
    const val RC_SIGN_IN = 103
    const val SELECT_IMAGE = 102
    const val PICK_IMAGE_GALLERY = 101
}

object StaffType {
    const val STAFF = 1
    const val DELIVERY = 2
}


object AppConstants {
    const val CUSTOMER_MODEL = "customer_model"
    const val ORDER_MODEL = "order_model"
    const val CATALOGUE_MODEL = "catalogue_model"
    const val CHANNEL_ID = "staff_channel_id"
}

object OrderStatus {
    const val PENDING = "0"
    const val ACCEPTED = "1"
    const val IN_PROCESS = "2"
    const val DISPATCHED = "3"
    const val DELIVERED = "4"
    const val PICKED_UP = "6"
    const val CANCEL_ORDER = "5"
}

object FILE_STORAGE_PATHS {
    const val STORAGE_PATH_UPLOADS = "catalogue_images/"
}