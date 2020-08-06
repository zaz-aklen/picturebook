package com.example.picturebook

import android.graphics.Bitmap

class globals {
    companion object chosen{
        var image: Bitmap? =null
        fun images(): Bitmap? {
            return image
        }
    }
}